import config = require('config');

import { createApp } from './app';
import logger from './logger';

const PORT = Number(config.get('server.port')) || 8000;

createApp()
  .then(app => {
    app.listen(PORT, err => {
      if (err) {
        throw err;
      }
      logger.info(`server running on: ${PORT}`);
    });
  })
  .catch(err => {
    logger.error('error occurred when starting server', { error: err });
    process.exit(1);
  });
