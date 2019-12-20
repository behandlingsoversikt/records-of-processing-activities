import request from 'supertest';
import mongoose from 'mongoose';
import { Application } from 'express';
import { MongoMemoryServer } from 'mongodb-memory-server';
import { name, lorem, company, internet, random, date, address } from 'faker';
import { stub } from 'sinon';
import { expect } from 'chai';

import { createApp } from '../src/app';
import * as db from '../src/db/db';
import * as keycloak from '../src/keycloak';
import { RecordModel } from '../src/models/record';
import { apiValidator, spec } from '../src/validator';

import { Record, ContactDetails } from '../src/types';
import { OrganizationModel } from '../src/models/organization';

const mongoTestServer = new MongoMemoryServer();

const uuidV4regExp = new RegExp(
  /^[0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12}$/i
);

const mockOrganizationId = random
  .number({ min: 800_000_000, max: 999_999_999 })
  .toString();

const generateContactMock = (): ContactDetails => ({
  name: name.findName(),
  address: address.streetAddress(),
  phone: random.number({ min: 20_00_00_00, max: 99_99_99_99 }).toString(),
  email: internet.email()
});

const generateRecordMock = (): Record => {
  return {
    id: random.uuid(),
    purpose: lorem.sentence(),
    status: 'DRAFT',
    organizationId: mockOrganizationId,
    dataSubjectCategories: lorem
      .words(random.number({ min: 1, max: 10 }))
      .split(' '),
    personalDataCategories: lorem
      .words(random.number({ min: 1, max: 10 }))
      .split(' '),
    articleSixBasis: [
      { legality: lorem.sentence(), referenceUrl: internet.url() },
      { legality: lorem.sentence(), referenceUrl: internet.url() },
      { legality: lorem.sentence(), referenceUrl: internet.url() }
    ],
    otherArticles: {
      articleNine: {
        checked: false
      },
      articleTen: {
        checked: true,
        referenceUrl: internet.url()
      }
    },
    businessAreas: [lorem.word(), lorem.word()],
    recipientCategories: lorem.words(),
    plannedDeletion: `${lorem.paragraph()} Planned deletion set due ${date.future()}`,
    securityMeasures: lorem.sentence(),
    commonDataControllerContact: {
      companies: `${company.companyName()} AS`,
      distributionOfResponsibilities: lorem.paragraph(),
      contactPoints: [
        {
          name: name.findName(),
          phone: random
            .number({ min: 20_00_00_00, max: 99_99_99_99 })
            .toString(),
          email: internet.email()
        },
        {
          name: name.findName(),
          phone: random
            .number({ min: 20_00_00_00, max: 99_99_99_99 })
            .toString(),
          email: internet.email()
        }
      ]
    },
    dataProcessorContactDetails: {
      name: name.findName(),
      phone: random.number({ min: 20_00_00_00, max: 99_99_99_99 }).toString(),
      email: internet.email()
    },
    dataTransfers: {
      transferred: true,
      thirdCountryRecipients: `${company.companyName()} LLC`,
      guarantees: lorem.paragraph()
    },
    title: lorem.sentence(),
    relatedDatasets: [random.uuid()],
    dataProtectionImpactAssessment: {
      conducted: true,
      assessmentReportUrl: internet.url()
    },
    dataProcessingAgreement: [
      {
        dataProcessorName: name.findName(),
        agreementUrl: internet.url()
      }
    ]
  };
};

let app: Application;

before(async () => {
  stub(
    keycloak,
    'enforceAuthority'
  ).callsFake((_req: any, _res: any, next: any) => next());

  stub(
    keycloak,
    'enforcePermissions'
  ).callsFake((_req: any, _res: any, next: any) => next());

  const connectionUris: string = await mongoTestServer.getConnectionString();
  stub(db, 'getConnectionUris').callsFake(() => connectionUris);

  app = await createApp();

  await new OrganizationModel({
    id: mockOrganizationId,
    dataControllerRepresentative: generateContactMock(),
    dataControllerRepresentativeInEU: generateContactMock(),
    dataProtectionOfficer: generateContactMock()
  }).save();
});

afterEach(async () => {
  await RecordModel.deleteMany({});
});

after(async () => {
  await mongoose.disconnect();
});

/* POST /api/organizations/{organizationId}/records */
describe('/api/organizations/{organizationId}/records', () => {
  const supportedMethods =
    spec.paths['/organizations/{organizationId}/records'];

  const {
    post: {
      description: postDescription,
      summary: postSummary,
      responses: postResponses
    } = {} as any,
    get: {
      description: getDescription,
      summary: getSummary,
      responses: getResponses
    } = {} as any
  } = supportedMethods;
  it(postDescription || postSummary, async () => {
    const code = Object.keys(postResponses)[0];

    await request(app)
      .post(`/api/organizations/${mockOrganizationId}/records`)
      .send(generateRecordMock())
      .expect(parseInt(code))
      .expect('Location', uuidV4regExp)
      .expect(
        apiValidator.validateResponse(
          'post',
          '/organizations/{organizationId}/records'
        )
      );
  });

  it(getDescription || getSummary, async () => {
    const code = Object.keys(getResponses)[0];

    await request(app)
      .get(`/api/organizations/${mockOrganizationId}/records`)
      .expect(parseInt(code))
      .expect(
        apiValidator.validateResponse(
          'get',
          '/organizations/{organizationId}/records'
        )
      );
  });
});

describe('/api/records/{recordId}', () => {
  const supportedMethods = spec.paths['/records/{recordId}'];

  let recordMock: any = {};
  beforeEach(async () => {
    recordMock = generateRecordMock();
    await new RecordModel(recordMock).save();
  });

  const {
    get: {
      description: getDescription,
      summary: getSummary,
      responses: getResponses
    } = {} as any,
    patch: {
      description: patchDescription,
      summary: patchSummary,
      responses: patchResponses
    } = {} as any,
    delete: {
      description: deleteDescription,
      summary: deleteSummary,
      responses: deleteResponses
    } = {} as any
  } = supportedMethods;

  it(deleteDescription || deleteSummary, async () => {
    const code = Object.keys(deleteResponses)[0];

    await request(app)
      .delete(`/api/records/${recordMock.id}`)
      .expect(parseInt(code))
      .expect(apiValidator.validateResponse('delete', '/records/{recordId}'));
  });

  it(patchDescription || patchSummary, async () => {
    const code = Object.keys(patchResponses)[0];

    const { body } = await request(app)
      .patch(`/api/records/${recordMock.id}`)
      .send(recordMock)
      .expect(parseInt(code))
      .expect(apiValidator.validateResponse('patch', '/records/{recordId}'));

    expect(body).to.deep.equal(recordMock);
  });

  it(getDescription || getSummary, async () => {
    const code = Object.keys(getResponses)[0];

    await request(app)
      .get(`/api/records/${recordMock.id}`)
      .expect(parseInt(code))
      .expect(apiValidator.validateResponse('get', '/records/{recordId}'));
  });
});
