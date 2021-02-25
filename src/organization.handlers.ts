import omit from 'lodash/omit';
import uuidv4 from 'uuid/v4';

import { RecordDocument, RecordModel } from './models/record';
import { NotFoundHttpError } from './lib/http-error';
import { elseThrow } from './lib/else-throw';
import { toPagedResource } from './lib/paginate';
import { OrganizationDocument, OrganizationModel } from './models/organization';
import logger from './logger';

import { OrganizationResourceHandler } from './types';

export default {
  getRecordById: (req, res, next): void => {
    const { recordId } = req.params;
    RecordModel.findOne({ id: recordId })
      .then(elseThrow<RecordDocument>(() => new NotFoundHttpError()))
      .then(doc => {
        logger.info(`Get record with id ${recordId}`);
        res.status(200).send(doc.toObject());
      })
      .catch(next);
  },

  deleteRecordById: (req, res, next): void => {
    const { recordId } = req.params;
    RecordModel.deleteOne({ id: recordId })
      .then(({ deletedCount }) => {
        logger.info(`Delete record with id ${recordId}`);
        const status = deletedCount ? 204 : 404;
        res.status(status).send();
      })
      .catch(next);
  },

  patchRecordById: (req, res, next): void => {
    const { recordId = uuidv4() } = req.params;
    const data = omit(req.body, 'id');

    RecordModel.findOneAndUpdate({ id: recordId }, data, {
      upsert: true,
      new: true,
      setDefaultsOnInsert: true
    })
      .then(elseThrow<RecordDocument>(() => new NotFoundHttpError()))
      .then(doc => {
        logger.info(`Patch record with id ${recordId}`);
        res.status(200).send(doc.toObject());
      })
      .catch(next);
  },

  getRecordsByOrganizationId: async (req, res, next): Promise<void> => {
    const page = +req.query.page || 1;
    const limit = +req.query.limit || 10;

    const { organizationId } = req.params;

    try {
      const docs = await RecordModel.find({ organizationId })
        .skip(limit * page - limit)
        .limit(limit)
        .sort({ createdAt: 'desc' });

      const total = await RecordModel.estimatedDocumentCount();
      logger.info(`Get ${total} records by organizationId ${organizationId}`);
      res.status(200).send(toPagedResource(docs, page, limit, total));
    } catch (err) {
      next(err);
    }
  },

  createRecord: (req, res, next): void => {
    const { organizationId } = req.params;
    const data = omit(req.body, 'id');

    OrganizationModel.findOne(
      { id: organizationId },
      {
        new: true,
        upsert: true
      }
    )
      .then(elseThrow<OrganizationDocument>(() => new NotFoundHttpError()))
      .then(() => {
        new RecordModel({ ...data, organizationId }).save().then(({ id }) => {
          logger.info(
            `Record with id ${id} created for organizationId ${organizationId}`
          );
          return res
            .location(id)
            .status(201)
            .send();
        });
      })
      .catch(next);
  },

  getRepresentatives: (req, res, next): void => {
    const { organizationId } = req.params;

    OrganizationModel.findOne({ id: organizationId })
      .then(elseThrow<RecordDocument>(() => new NotFoundHttpError()))
      .then(doc => {
        logger.info(`Get representatives for organizationId ${organizationId}`);
        res.status(200).send(doc.toObject());
      })
      .catch(next);
  },

  patchRepresentatives: (req, res, next): void => {
    const { organizationId } = req.params;
    const data = omit(req.body, 'id');
    OrganizationModel.findOneAndUpdate({ id: organizationId }, data, {
      new: true,
      upsert: true,
      setDefaultsOnInsert: true
    })
      .then(doc => {
        logger.info(
          `Patch representatives for organizationId ${organizationId}`
        );
        res.status(200).send(doc.toObject());
      })
      .catch(next);
  }
} as OrganizationResourceHandler;
