import { Document, model, Schema } from 'mongoose';
import pick from 'lodash/pick';
import uuidv4 from 'uuid/v4';

import { Record } from '../types';

export interface RecordDocument extends Document, Record {}

const recordSchemaDefinition = {
  id: {
    default: uuidv4,
    type: String,
    required: true,
    unique: true
  },
  organizationId: {
    type: String,
    ref: 'organization'
  },
  status: { type: String, default: null },
  title: { type: String, default: null },
  purpose: { type: String, default: null },
  dataProtectionImpactAssessment: { type: String, default: null },
  dataProcessingAgreement: { type: String, default: null },
  registeredCategories: [String],
  personalDataCategories: [String],
  recipientCategories: [String],
  relatedDatasets: [String],
  deleteDate: { type: Date, default: null },
  dataTransfers: [
    {
      _id: false,
      entityName: String,
      entityType: String
    }
  ],
  dataProcessorContactDetails: {
    name: String,
    phone: String,
    email: String
  },
  securityMeasures: {
    organizational: String,
    technical: String
  }
};

const recordFromDocument = (document: RecordDocument): Record =>
  pick(document, Object.keys(recordSchemaDefinition));

const RecordSchema = new Schema(recordSchemaDefinition, {
  timestamps: true,
  strict: true,
  toObject: {
    transform: recordFromDocument
  }
});

export const RecordModel = model<RecordDocument>('record', RecordSchema);
