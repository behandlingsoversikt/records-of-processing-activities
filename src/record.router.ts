import { Router } from 'express';
import recordHandlers from './record.handlers';
import { enforcePermissions } from './keycloak';
import { apiValidator } from './validator';

const recordsPath = '/records';
const validationPath = `${recordsPath}/{recordId}`;

export const createRecordRouter = (): Router => {
  const recordRouter = Router();

  recordRouter.delete(
    `${recordsPath}/:recordId`,
    enforcePermissions,
    apiValidator.validate('delete', validationPath),
    recordHandlers.deleteRecordById
  );

  recordRouter.get(
    `${recordsPath}/:recordId`,
    enforcePermissions,
    apiValidator.validate('get', validationPath),
    recordHandlers.getRecordById
  );

  return recordRouter;
};
