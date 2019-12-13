import { LivenessResourceHandler } from './types';

export default {
  ping: (_, res): void => {
    res.sendStatus(200);
  },

  ready: (_, res): void => {
    res.sendStatus(200);
  }
} as LivenessResourceHandler;
