import request from 'supertest';
import mongoose from 'mongoose';
import { Application } from 'express';
import { MongoMemoryServer } from 'mongodb-memory-server';
import { name, lorem, company, internet, random, date, address } from 'faker';
import { stub } from 'sinon';

import { createApp } from '../src/app';
import * as db from '../src/db/db';
import * as keycloak from '../src/keycloak';
import { RecordModel } from '../src/models/record';
import { apiValidator, spec } from '../src/validator';

import { Record, ContactDetails } from '../src/types';
import { OrganizationModel } from '../src/models/organization';
import { RecordStatus } from '../src/types/enums';

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
    status: RecordStatus.DRAFT,
    organizationId: mockOrganizationId,
    categories: [
      {
        personalDataCategories: lorem
          .words(random.number({ min: 1, max: 10 }))
          .split(' '),
        dataSubjectCategories: lorem.word()
      },
      {
        personalDataCategories: lorem
          .words(random.number({ min: 1, max: 10 }))
          .split(' '),
        dataSubjectCategories: lorem.word()
      },
      {
        personalDataCategories: lorem
          .words(random.number({ min: 1, max: 10 }))
          .split(' '),
        dataSubjectCategories: lorem.word()
      }
    ],
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
    recipientCategories: [lorem.words()],
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
    dataProcessingAgreements: [
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
    'enforceWritePermissions'
  ).callsFake((_req: any, _res: any, next: any) => next());

  stub(
    keycloak,
    'enforceReadPermissions'
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
  let recordMock: any = {};
  beforeEach(async () => {
    recordMock = generateRecordMock();
    await new RecordModel(recordMock).save();
  });

  const supportedMethods =
    spec.paths['/api/organizations/{organizationId}/records'];

  const {
    post: {
      description: postDescription,
      summary: postSummary,
      responses: postResponses
    } = {} as any,
    patch: {
      description: patchDescription,
      summary: patchSummary,
      responses: patchResponses
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
          '/api/organizations/{organizationId}/records'
        )
      );
  });

  it(patchDescription || patchSummary, async () => {
    const code = Object.keys(patchResponses)[0];

    await request(app)
      .patch(
        `/api/organizations/${mockOrganizationId}/records/${recordMock.id}`
      )
      .send(recordMock)
      .expect(parseInt(code))
      .expect(
        apiValidator.validateResponse(
          'patch',
          '/api/organizations/{organizationId}/records'
        )
      );
    // expect(body).to.deep.equal(recordMock);
  });

  it(getDescription || getSummary, async () => {
    const code = Object.keys(getResponses)[0];

    await request(app)
      .get(`/api/organizations/${mockOrganizationId}/records`)
      .expect(parseInt(code))
      .expect(
        apiValidator.validateResponse(
          'get',
          '/api/organizations/{organizationId}/records'
        )
      );
  });
});

describe('/api/organizations/{organizationId}/records/{recordId}', () => {
  const supportedMethods =
    spec.paths['/api/organizations/{organizationId}/records/{recordId}'];

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
    delete: {
      description: deleteDescription,
      summary: deleteSummary,
      responses: deleteResponses
    } = {} as any
  } = supportedMethods;

  it(deleteDescription || deleteSummary, async () => {
    const code = Object.keys(deleteResponses)[0];

    await request(app)
      .delete(
        `/api/organizations/${mockOrganizationId}/records/${recordMock.id}`
      )
      .expect(parseInt(code))
      .expect(
        apiValidator.validateResponse(
          'delete',
          '/api/organizations/{organizationId}/records/{recordId}'
        )
      );
  });

  it(getDescription || getSummary, async () => {
    const code = Object.keys(getResponses)[0];

    await request(app)
      .get(`/api/organizations/${mockOrganizationId}/records/${recordMock.id}`)
      .expect(parseInt(code))
      .expect(
        apiValidator.validateResponse(
          'get',
          '/api/organizations/{organizationId}/records/{recordId}'
        )
      );
  });
});
