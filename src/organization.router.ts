import { Router } from 'express';
import organizationHandlers from './organization.handlers';
import { enforceWritePermissions } from './keycloak';
import { apiValidator } from './validator';

const recordsPath = '/records';
const validationPath = `/api/organizations/{organizationId}${recordsPath}`;

export const createOrganizationRouter = (): Router => {
  const organizationRouter = Router();

  organizationRouter.get(
    '/:organizationId/representatives',
    enforceWritePermissions,
    organizationHandlers.getRepresentatives
  );
  organizationRouter.patch(
    '/:organizationId/representatives',
    enforceWritePermissions,
    organizationHandlers.patchRepresentatives
  );

  organizationRouter.get(
    `/:organizationId${recordsPath}`,
    enforceWritePermissions,
    apiValidator.validate('get', validationPath),
    organizationHandlers.getRecordsByOrganizationId
  );

  organizationRouter.post(
    `/:organizationId${recordsPath}`,
    enforceWritePermissions,
    apiValidator.validate('post', validationPath),
    organizationHandlers.createRecord
  );

  organizationRouter.patch(
    `/:organizationId${recordsPath}/:recordId?`,
    enforceWritePermissions,
    apiValidator.validate('patch', validationPath),
    organizationHandlers.patchRecordById
  );

  organizationRouter.delete(
    `/:organizationId${recordsPath}/:recordId`,
    enforceWritePermissions,
    apiValidator.validate('delete', `${validationPath}/{recordId}`),
    organizationHandlers.deleteRecordById
  );

  organizationRouter.get(
    `/:organizationId${recordsPath}/:recordId`,
    enforceWritePermissions,
    apiValidator.validate('get', `${validationPath}/{recordId}`),
    organizationHandlers.getRecordById
  );

  return organizationRouter;
};
