import omit from 'lodash/omit';

import { RecordDocument, RecordModel } from './record.model';
import { NotFoundHttpError } from './lib/http-error';
import { elseThrow } from './lib/else-throw';
import { toPagedResource } from './lib/paginate';

import { ResourceHandler } from './types';

export default {
  createRecord: (req, res, next): void => {
    const data = omit(req.body, 'id');
    new RecordModel(data)
      .save()
      .then(doc => {
        res
          .location(doc.id)
          .status(201)
          .send();
      })
      .catch(next);
  },

  patchRecordById: (req, res, next): void => {
    const { id } = req.params;
    const data = omit(req.body, 'id');
    RecordModel.findOneAndUpdate({ id }, data, { new: true })
      .then(elseThrow<RecordDocument>(() => new NotFoundHttpError()))
      .then(doc => {
        res.status(200).send(doc.toObject());
      })
      .catch(next);
  },

  getRecordById: (req, res, next): void => {
    const { id } = req.params;
    RecordModel.findOne({ id })
      .then(elseThrow<RecordDocument>(() => new NotFoundHttpError()))
      .then(doc => res.status(200).send(doc.toObject()))
      .catch(next);
  },

  getRecords: async (req, res, next): Promise<void> => {
    const page = +req.query.page || 1;
    const limit = +req.query.limit || 10;

    try {
      const docs = await RecordModel.find()
        .skip(limit * page - limit)
        .limit(limit);

      const total = await RecordModel.estimatedDocumentCount();
      res.status(200).send(toPagedResource(docs, page, limit, total));
    } catch (err) {
      next(err);
    }
  },

  deleteRecordById: (req, res, next): void => {
    const { id } = req.params;
    RecordModel.deleteOne({ id })
      .then(() => res.status(204).send())
      .catch(next);
  }
} as ResourceHandler;
