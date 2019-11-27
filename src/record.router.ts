import { Router } from 'express';
import recordHandlers from './record.handlers';
import { recordApiValidator } from './record.validator';
import { enforceAuthority } from './keycloak';

export const recordsPath = '/records';

export const createRecordRouter = (): Router => {
  const recordRouter = Router();

  recordRouter.get(
    recordsPath,
    recordApiValidator.validate('get', recordsPath),
    recordHandlers.getRecords
  );

  recordRouter.post(
    recordsPath,
    enforceAuthority(),
    recordApiValidator.validate('post', recordsPath),
    recordHandlers.createRecord
  );

  recordRouter.get(
    `${recordsPath}/:id`,
    recordApiValidator.validate('get', `${recordsPath}/{id}`),
    recordHandlers.getRecordById
  );

  recordRouter.patch(
    `${recordsPath}/:id`,
    enforceAuthority(),
    recordApiValidator.validate('patch', `${recordsPath}/{id}`),
    recordHandlers.patchRecordById
  );

  recordRouter.delete(
    `${recordsPath}/:id`,
    enforceAuthority(),
    recordApiValidator.validate('delete', `${recordsPath}/{id}`),
    recordHandlers.deleteRecordById
  );

  return recordRouter;
};
