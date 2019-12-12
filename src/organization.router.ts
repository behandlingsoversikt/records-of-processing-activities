import { Router } from 'express';
import organizationHandlers from './organization.handlers';
import { enforceAuthority, enforcePermissions } from './keycloak';
import { apiValidator } from './validator';

const recordsPath = '/records';
const validationPath = `/organizations/{organizationId}${recordsPath}`;

export const createOrganizationRouter = (): Router => {
  const organizationRouter = Router();

  organizationRouter.get(
    '/:organizationId/representatives',
    enforceAuthority,
    enforcePermissions,
    organizationHandlers.getRepresentatives
  );
  organizationRouter.patch(
    '/:organizationId/representatives',
    enforceAuthority,
    enforcePermissions,
    organizationHandlers.patchRepresentatives
  );

  organizationRouter.get(
    `/:organizationId${recordsPath}`,
    enforceAuthority,
    enforcePermissions,
    apiValidator.validate('get', validationPath),
    organizationHandlers.getRecordsByOrganizationId
  );

  organizationRouter.post(
    `/:organizationId${recordsPath}`,
    enforceAuthority,
    enforcePermissions,
    apiValidator.validate('post', validationPath),
    organizationHandlers.createRecord
  );

  return organizationRouter;
};
