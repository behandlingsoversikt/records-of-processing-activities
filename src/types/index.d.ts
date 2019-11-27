import { RequestHandler } from 'express';

export interface Record {
  // set to any because of mongoose type compatibility, in reality it is string
  id?: any;
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

export interface ResourceHandler {
  createRecord: RequestHandler;
  getRecordById: RequestHandler;
  getRecords: RequestHandler;
  patchRecordById: RequestHandler;
  deleteRecordById: RequestHandler;
}
