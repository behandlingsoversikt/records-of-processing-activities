import { RequestHandler } from 'express';
import { RecordStatus } from './enums';

interface ArticleSix {
  legality?: string;
  referenceUrl?: string;
}

interface Article {
  checked?: boolean;
  referenceUrl?: string;
}

export interface ContactPoint {
  name?: string;
  email?: string;
  phone?: string;
}

export interface ContactDetails extends ContactPoint {
  address?: string;
}

export interface Record {
  // set to any because of mongoose type compatibility, in reality it is string
  id?: any;
  status?: RecordStatus;
  organizationId?: string;
  purpose?: string;
  title?: string;
  categories?: Categories[];
  articleSixBasis?: ArticleSix[];
  otherArticles?: {
    articleNine?: Article;
    articleTen?: Article;
  };
  businessAreas?: string[];
  relatedDatasets?: string[];
  dataProcessingAgreements?: [
    {
      dataProcessorName?: string;
      agreementUrl?: string;
    }
  ];
  dataProcessorContactDetails?: [
    {
      name?: string;
      email?: string;
      phone?: string;
    }
  ];
  commonDataControllerContact?: {
    companies?: string;
    distributionOfResponsibilities?: string;
    contactPoints?: ContactPoint[];
  };
  personalDataSubjects?: string;
  securityMeasures?: string;
  plannedDeletion?: string;
  dataProtectionImpactAssessment?: {
    conducted?: boolean;
    assessmentReportUrl?: string;
  };
  privacyProcessingSystems?: string;
  recipientCategories?: string[];
  dataTransfers?: {
    transferred?: boolean;
    thirdCountryRecipients?: string;
    guarantees?: string;
  };
  updatedAt?: Date;
}

export interface Categories {
  dataSubjectCategories?: string;
  personalDataCategories?: string[];
}

export interface Organization {
  id?: string;
  dataControllerRepresentative?: ContactDetails;
  dataControllerRepresentativeInEU?: ContactDetails;
  dataProtectionOfficer?: ContactDetails;
}

export interface OrganizationResourceHandler {
  deleteRecordById: RequestHandler;
  getRecordById: RequestHandler;
  patchRecordById: RequestHandler;
  getRepresentatives: RequestHandler;
  patchRepresentatives: RequestHandler;
  getRecordsByOrganizationId: RequestHandler;
  createRecord: RequestHandler;
}

export interface LivenessResourceHandler {
  ping: RequestHandler;
  ready: RequestHandler;
}
