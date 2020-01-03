import express, { Application } from 'express';
import cors from 'cors';
import bodyParser from 'body-parser';

import keycloak from './keycloak';
import { connectDb } from './db/db';
import { createOrganizationRouter } from './organization.router';
import { commonErrorHandler } from './lib/common-error-handler';
import { createLivenessRouter } from './liveness.router';

export async function createApp(): Promise<Application> {
  const app = express();

  app.use(bodyParser.json());

  app.use(cors({ origin: '*', exposedHeaders: 'Location' }));

  app.use(keycloak.middleware());

  app.use('/', createLivenessRouter());
  app.use('/api/organizations', createOrganizationRouter());

  app.use(commonErrorHandler);

  await connectDb();

  return app;
}
