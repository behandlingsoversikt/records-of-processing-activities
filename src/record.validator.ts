import { OpenApiDocument, OpenApiValidator } from 'express-openapi-validate';
import { readSyncYaml } from './lib/read-sync-yaml';

const specPath = require('app-root-path').resolve('/spec/specification.yaml');
export const spec: OpenApiDocument = readSyncYaml(specPath) as OpenApiDocument;

export const recordApiValidator = new OpenApiValidator(spec);
