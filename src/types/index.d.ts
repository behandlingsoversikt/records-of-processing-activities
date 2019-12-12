import { RequestHandler } from 'express';

export interface ContactDetails {
  name?: string;
  address?: string;
  email?: string;
  phone?: string;
}

export interface Record {
  // set to any because of mongoose type compatibility, in reality it is string
  id?: any;
  status?: 'APPROVED' | 'DRAFT' | 'PUBLISHED';
  organizationId?: string;
  purpose?: string;
  title?: string;
  dataProtectionImpactAssessment?: string;
  dataProcessingAgreement?: string;
  registeredCategories?: string[];
  personalDataCategories?: string[];
  recipientCategories?: string[];
  dataTransfers?: any;
  relatedDatasets?: any;
  dataProcessorContactDetails?: any;
  securityMeasures?: any;
  deleteDate?: Date;
}

export interface Organization {
  id?: string;
  dataControllerRepresentative?: ContactDetails;
  dataControllerRepresentativeInEU?: ContactDetails;
  dataProtectionOfficer?: ContactDetails;
}

export interface ResourceHandler {
  getRepresentatives: RequestHandler;
  patchRepresentatives: RequestHandler;
  createRecord: RequestHandler;
  getRecordById: RequestHandler;
  getRecordsByOrganizationId: RequestHandler;
  patchRecordById: RequestHandler;
  deleteRecordById: RequestHandler;
}
