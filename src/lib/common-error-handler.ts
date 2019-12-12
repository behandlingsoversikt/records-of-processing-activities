/* eslint-disable @typescript-eslint/no-unused-vars */
import { ErrorRequestHandler } from 'express';
import { HttpError } from './http-error';

export const commonErrorHandler: ErrorRequestHandler = (
  err,
  _req,
  res,
  _next
) => {
  const error =
    err instanceof HttpError
      ? err
      : new HttpError(err, err.status || err.statusCode);
  res.status(error.status || 500).json({
    message: err.message,
    errors: err.errors
  });
};
