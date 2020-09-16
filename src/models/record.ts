import { Document, model, Schema } from 'mongoose';
import pick from 'lodash/pick';
import uuidv4 from 'uuid/v4';

import { Record } from '../types';
import { RecordStatus } from '../types/enums';

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
  status: { type: String, default: RecordStatus.DRAFT },

  // Behandlingen gjelder
  title: { type: String },
  // Formålet med behandlingen
  purpose: { type: String },
  // Kategorier av personopplysninger og registrerte
  categories: [
    {
      dataSubjectCategories: { type: String },
      personalDataCategories: [String]
    }
  ],
  dataSubjectCategories: String,
  // Behandlingsgrunnlagt artikkel 6
  articleSixBasis: {
    type: [
      {
        _id: false,
        legality: String,
        referenceUrl: String
      }
    ],
    default: null
  },
  // Behandlingsgrunnlag artikkel 9 eller 10
  otherArticles: {
    articleNine: {
      checked: Boolean,
      referenceUrl: String
    },
    articleTen: {
      checked: Boolean,
      referenceUrl: String
    }
  },

  // Funksjonsområde
  businessAreas: [String],
  // Tilhørende datasett
  relatedDatasets: [String],

  // Databehandlere og databehandleravtaler
  dataProcessingAgreements: {
    type: [
      {
        _id: false,
        dataProcessorName: { type: String },
        agreementUrl: { type: String }
      }
    ]
  },
  // Daglig behandlingsansvarlig
  dataProcessorContactDetails: {
    type: [
      {
        _id: false,
        name: { type: String },
        email: { type: String },
        phone: { type: String }
      }
    ]
  },
  // Felles behandlingsansvarlig
  commonDataControllerContact: {
    type: {
      companies: String,
      distributionOfResponsibilities: String,
      contactPoints: {
        type: [
          {
            name: String,
            email: String,
            phone: String
          }
        ]
      }
    }
  },
  // Beskrivelse av tekniske og organisatoriske sikkerhetstiltak
  securityMeasures: String,
  // Planlagte tidsfrister for sletting
  plannedDeletion: String,
  // Høy personvernrisiko
  highPrivacyRisk: Boolean,
  // Risikovurdering/DPIA gjennomført
  dataProtectionImpactAssessment: {
    conducted: Boolean,
    assessmentReportUrl: String
  },
  // Kilder til personopplysninger
  personalDataSubjects: String,
  // sist oppdatert
  updatedAt: Date,
  // Systemer i din virksomhet som behandler personopplysningene
  privacyProcessingSystems: String,
  // Kategorier av mottakere
  recipientCategories: [String],
  // Overføres opplysninger til tredjeland?
  dataTransfers: {
    transferred: Boolean,
    thirdCountryRecipients: String,
    guarantees: String
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
