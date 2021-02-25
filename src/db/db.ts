import mongoose from 'mongoose';
import config from 'config';
import logger from '../logger';

const { host, port, name, username = '', password = '' } = config.get(
  'mongodb'
);

export const getConnectionUris = () =>
  `mongodb://${username}:${password}@${host}:${port}/${name}?authSource=admin&authMechanism=SCRAM-SHA-1`;

export const connectDb = async (): Promise<void> => {
  const options = {
    // fix deprecations
    useUnifiedTopology: true,
    useNewUrlParser: true,
    useFindAndModify: false,
    useCreateIndex: true
  };

  await mongoose.connect(getConnectionUris(), options);
  mongoose.connection.on('error', logger.error);
};
