import { RecordDocument, RecordModel } from './models/record';
import { NotFoundHttpError } from './lib/http-error';
import { elseThrow } from './lib/else-throw';

import { RecordResourceHandler } from './types';

export default {
  getRecordById: (req, res, next): void => {
    const { recordId } = req.params;
    RecordModel.findOne({ id: recordId })
      .then(elseThrow<RecordDocument>(() => new NotFoundHttpError()))
      .then(doc => res.status(200).send(doc.toObject()))
      .catch(next);
  },

  deleteRecordById: (req, res, next): void => {
    const { recordId } = req.params;
    RecordModel.deleteOne({ id: recordId })
      .then(({ deletedCount }) => {
        const status = deletedCount ? 204 : 404;
        res.status(status).send();
      })
      .catch(next);
  }
} as RecordResourceHandler;
