import { Router } from 'express';
import livenessHandlers from './liveness.handlers';

export const createLivenessRouter = (): Router => {
  const livenessRouter = Router();

  livenessRouter.get('/ping', livenessHandlers.ping);
  livenessRouter.get('/ready', livenessHandlers.ready);

  return livenessRouter;
};
