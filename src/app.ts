import express, { Application } from 'express';
import cors from 'cors';
import bodyParser from 'body-parser';

import keycloak from './keycloak';
import { connectDb } from './db/db';
import { createRecordRouter } from './record.router';
import { createOrganizationRouter } from './organization.router';
import { commonErrorHandler } from './lib/common-error-handler';

export async function createApp(): Promise<Application> {
  const app = express();

  app.use(bodyParser.json());

  app.use(cors({ origin: '*', exposedHeaders: 'Location' }));

  app.use(keycloak.middleware());

  app.use('/api', createRecordRouter());
  app.use('/api/organizations', createOrganizationRouter());

  app.use(commonErrorHandler);

  await connectDb();

  return app;
}
