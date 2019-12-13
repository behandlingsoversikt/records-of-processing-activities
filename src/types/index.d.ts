import { RequestHandler } from 'express';

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
  status?: 'APPROVED' | 'DRAFT' | 'PUBLISHED';
  organizationId?: string;
  purpose?: string;
  title?: string;
  dataSubjectCategories?: string[];
  articleSixBasis?: ArticleSix[];
  otherArticles?: {
    articleNine?: Article;
    articleTen?: Article;
  };
  businessArea?: string[];
  relatedDatasets?: string[];
  dataProcessingAgreement?: [
    {
      dataProcessorName?: string;
      agreementUrl?: string;
    }
  ];
  dataProcessorContactDetails?: {
    name?: string;
    email?: string;
    phone?: string;
  };
  commonDataControllerContact?: {
    companies?: string;
    distributionOfResponsibilities?: string;
    contactPoints?: ContactPoint[];
  };
  personalDataCategories?: string[];
  personalDataSubjects?: string;
  securityMeasures?: string;
  plannedDeletion?: string;
  highPrivacyRisk?: boolean;
  dataProtectionImpactAssessment?: {
    conducted?: boolean;
    assessmentReportUrl?: string;
  };
  privacyProcessingSystems?: string;
  recipientCategories?: string;
  dataTransfers?: {
    transfered?: boolean;
    thirdCountryRecipients?: string;
    guarantees?: string;
    internationalOrganizations?: string;
  };
}

export interface Organization {
  id?: string;
  dataControllerRepresentative?: ContactDetails;
  dataControllerRepresentativeInEU?: ContactDetails;
  dataProtectionOfficer?: ContactDetails;
}

export interface RecordResourceHandler {
  patchRecordById: RequestHandler;
  deleteRecordById: RequestHandler;
  getRecordById: RequestHandler;
}

export interface OrganizationResourceHandler {
  getRepresentatives: RequestHandler;
  patchRepresentatives: RequestHandler;
  getRecordsByOrganizationId: RequestHandler;
  createRecord: RequestHandler;
}

export interface LivenessResourceHandler {
  ping: RequestHandler;
  ready: RequestHandler;
}
