import express, { Application } from 'express';
import morgan from 'morgan';
import cors from 'cors';
import bodyParser from 'body-parser';

import keycloak from './keycloak';
import { connectDb } from './db/db';
import { createOrganizationRouter } from './organization.router';
import { commonErrorHandler } from './lib/common-error-handler';
import { createLivenessRouter } from './liveness.router';
import { streamLogger } from './logger';

export async function createApp(): Promise<Application> {
  const app = express();

  app.use(bodyParser.json());

  app.use(cors({ origin: '*', exposedHeaders: 'Location' }));

  app.use(keycloak.middleware());

  app.use('/', createLivenessRouter());
  app.use('/api/organizations', createOrganizationRouter());

  app.use(commonErrorHandler);

  app.use(
    morgan('combined', {
      skip: function(req: any) {
        return req.url === '/ping' || req.url === '/ready';
      },
      stream: streamLogger
    })
  );

  await connectDb();

  return app;
}
