import { Router } from 'express';
import recordHandlers from './record.handlers';
import { enforceAuthority, enforcePermissions } from './keycloak';
import { apiValidator } from './validator';

const recordsPath = '/records';
const validationPath = `${recordsPath}/{recordId}`;

export const createRecordRouter = (): Router => {
  const recordRouter = Router();

  recordRouter.get(
    `${recordsPath}/:recordId`,
    enforceAuthority,
    enforcePermissions,
    apiValidator.validate('get', validationPath),
    recordHandlers.getRecordById
  );

  recordRouter.patch(
    `${recordsPath}/:recordId`,
    enforceAuthority,
    enforcePermissions,
    apiValidator.validate('patch', validationPath),
    recordHandlers.patchRecordById
  );

  recordRouter.delete(
    `${recordsPath}/:recordId`,
    enforceAuthority,
    enforcePermissions,
    apiValidator.validate('delete', validationPath),
    recordHandlers.deleteRecordById
  );

  return recordRouter;
};
