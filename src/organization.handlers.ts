import omit from 'lodash/omit';

import { RecordDocument, RecordModel } from './models/record';
import { NotFoundHttpError } from './lib/http-error';
import { elseThrow } from './lib/else-throw';
import { toPagedResource } from './lib/paginate';
import { OrganizationDocument, OrganizationModel } from './models/organization';

import { OrganizationResourceHandler } from './types';

export default {
  getRecordsByOrganizationId: async (req, res, next): Promise<void> => {
    const page = +req.query.page || 1;
    const limit = +req.query.limit || 10;

    const { organizationId } = req.params;

    try {
      const docs = await RecordModel.find({ organizationId })
        .select('id title status dataProcessorContactDetails.name')
        .skip(limit * page - limit)
        .limit(limit);

      const total = await RecordModel.estimatedDocumentCount();
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
        new RecordModel({ ...data, organizationId }).save().then(({ id }) =>
          res
            .location(id)
            .status(201)
            .send()
        );
      })
      .catch(next);
  },

  getRepresentatives: (req, res, next): void => {
    const { organizationId } = req.params;

    OrganizationModel.findOne({ id: organizationId })
      .then(elseThrow<RecordDocument>(() => new NotFoundHttpError()))
      .then(doc => res.status(200).send(doc.toObject()))
      .catch(next);
  },

  patchRepresentatives: (req, res, next): void => {
    const { organizationId } = req.params;
    const data = omit(req.body, 'id');
    OrganizationModel.findOneAndUpdate({ id: organizationId }, data, {
      new: true,
      upsert: true
    })
      .then(doc => res.status(200).send(doc.toObject()))
      .catch(next);
  }
} as OrganizationResourceHandler;
