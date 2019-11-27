import request from 'supertest';
import mongoose from 'mongoose';
import { Application } from 'express';
import { MongoMemoryServer } from 'mongodb-memory-server';
import { name, lorem, company, internet, random, date } from 'faker';
import { stub } from 'sinon';

import { createApp } from '../src/app';
import * as db from '../src/db/db';
import keycloak from '../src/keycloak';
import { RecordModel } from '../src/record.model';
import { recordApiValidator, spec } from '../src/record.validator';

import { Record } from '../src/types';

const mongoTestServer = new MongoMemoryServer();

const uuidV4regExp = new RegExp(
  /^[0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12}$/i
);

const generateRecordMock = (): Record => {
  return {
    id: random.uuid(),
    purpose: lorem.sentence(),
    registeredCategories: lorem
      .words(random.number({ min: 1, max: 10 }))
      .split(' '),
    personalDataCategories: lorem
      .words(random.number({ min: 1, max: 10 }))
      .split(' '),
    recipientCategories: [random.uuid()],
    deleteDate: date.future(),
    securityMeasures: {
      organizational: lorem.sentence(),
      technical: lorem.sentence()
    },
    dataProcessorContactDetails: {
      name: name.findName(),
      phone: random.number({ min: 20_00_00_00, max: 99_99_99_99 }).toString(),
      email: internet.email()
    },
    dataTransfers: [
      {
        entityName: `${company.companyName()} AS`,
        entityType: random.boolean
          ? 'GDPR-THIRD-COUNTRY'
          : 'GDPR-INTERNATIONAL-ORGANIZATION'
      }
    ],
    title: lorem.sentence(),
    relatedDatasets: [random.uuid()],
    dataProtectionImpactAssessment: internet.url(),
    dataProcessingAgreement: internet.url()
  };
};

let app: Application;

before(async () => {
  stub(keycloak, 'protect').callsFake(
    () =>
      function middlewareMock(_req, _res, next) {
        return next();
      }
  );

  const connectionUris: string = await mongoTestServer.getConnectionString();
  stub(db, 'getConnectionUris').callsFake(() => connectionUris);

  app = await createApp();
});

afterEach(async () => {
  await RecordModel.deleteMany({});
});

after(async () => {
  await mongoose.disconnect();
  process.exit(0);
});

/* POST /api/records */
describe('/api/records', () => {
  const supportedMethods = spec.paths['/records'];

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
      .post(`/api/records`)
      .send(generateRecordMock())
      .expect(parseInt(code))
      .expect('Location', uuidV4regExp)
      .expect(recordApiValidator.validateResponse('post', '/records'));
  });

  it(getDescription || getSummary, async () => {
    const code = Object.keys(getResponses)[0];

    await request(app)
      .get(`/api/records`)
      .expect(parseInt(code))
      .expect(recordApiValidator.validateResponse('get', '/records'));
  });
});

describe('/api/records/{id}', () => {
  const supportedMethods = spec.paths['/records/{id}'];

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
      .expect(recordApiValidator.validateResponse('delete', '/records/{id}'));
  });

  it(patchDescription || patchSummary, async () => {
    const code = Object.keys(patchResponses)[0];

    await request(app)
      .patch(`/api/records/${recordMock.id}`)
      .send(recordMock)
      .expect(parseInt(code))
      .expect(recordApiValidator.validateResponse('patch', '/records/{id}'));
  });

  it(getDescription || getSummary, async () => {
    const code = Object.keys(getResponses)[0];

    await request(app)
      .get(`/api/records/${recordMock.id}`)
      .expect(parseInt(code))
      .expect(recordApiValidator.validateResponse('get', '/records/{id}'));
  });
});
