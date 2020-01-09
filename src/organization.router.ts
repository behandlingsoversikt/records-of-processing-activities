import { Router } from 'express';
import organizationHandlers from './organization.handlers';
import { enforcePermissions } from './keycloak';
import { apiValidator } from './validator';

const recordsPath = '/records';
const validationPath = `/api/organizations/{organizationId}${recordsPath}`;

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

  organizationRouter.patch(
    `/:organizationId${recordsPath}/:recordId?`,
    enforcePermissions,
    apiValidator.validate('patch', validationPath),
    organizationHandlers.patchRecordById
  );

  organizationRouter.delete(
    `/:organizationId${recordsPath}/:recordId`,
    enforcePermissions,
    apiValidator.validate('delete', `${validationPath}/{recordId}`),
    organizationHandlers.deleteRecordById
  );

  organizationRouter.get(
    `/:organizationId${recordsPath}/:recordId`,
    enforcePermissions,
    apiValidator.validate('get', `${validationPath}/{recordId}`),
    organizationHandlers.getRecordById
  );

  return organizationRouter;
};
