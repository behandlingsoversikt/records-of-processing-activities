import { Document, model, Schema } from 'mongoose';
import pick from 'lodash/pick';

import { ContactDetails } from '../types';

export interface OrganizationDocument extends Document, ContactDetails {}

const organizationSchemaDefinition = {
  id: {
    type: String,
    required: true,
    unique: true
  },

  dataControllerRepresentative: { type: Map, default: {} },
  dataControllerRepresentativeInEU: { type: Map, default: {} },
  dataProtectionOfficer: { type: Map, default: {} }
};

const organizationFromDocument = (
  document: OrganizationDocument
): ContactDetails => pick(document, Object.keys(organizationSchemaDefinition));

export const OrganizationModel = model<OrganizationDocument>(
  'organization',
  new Schema(organizationSchemaDefinition, {
    timestamps: true,
    strict: true,
    toObject: {
      transform: organizationFromDocument
    }
  })
);
