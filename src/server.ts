import config = require('config');

import { createApp } from './app';

const PORT = Number(config.get('server.port')) || 8000;

createApp()
  .then(app => {
    app.listen(PORT, err => {
      if (err) {
        throw err;
      }
      console.log(`server running on: ${PORT}`);
    });
  })
  .catch(err => {
    console.error(err);
    process.exit(1);
  });
