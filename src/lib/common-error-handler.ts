import { ErrorRequestHandler } from 'express';
import { HttpError } from './http-error';

export const commonErrorHandler: ErrorRequestHandler = (err, _, res) => {
  const error =
    err instanceof HttpError
      ? err
      : new HttpError(err, err.status || err.statusCode);
  res.status(error.status).json({
    message: err.message,
    errors: err.errors
  });
};
