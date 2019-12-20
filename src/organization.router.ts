import { Router } from 'express';
import organizationHandlers from './organization.handlers';
import { enforcePermissions } from './keycloak';
import { apiValidator } from './validator';

const recordsPath = '/records';
const validationPath = `/organizations/{organizationId}${recordsPath}`;

export const createOrganizationRouter = (): Router => {
  const organizationRouter = Router();

  organizationRouter.get(
    '/:organizationId/representatives',
    enforcePermissions,
    organizationHandlers.getRepresentatives
  );
  organizationRouter.patch(
    '/:organizationId/representatives',
    enforcePermissions,
    organizationHandlers.patchRepresentatives
  );

  organizationRouter.get(
    `/:organizationId${recordsPath}`,
    enforcePermissions,
    apiValidator.validate('get', validationPath),
    organizationHandlers.getRecordsByOrganizationId
  );

  organizationRouter.post(
    `/:organizationId${recordsPath}`,
    enforcePermissions,
    apiValidator.validate('post', validationPath),
    organizationHandlers.createRecord
  );

  return organizationRouter;
};
