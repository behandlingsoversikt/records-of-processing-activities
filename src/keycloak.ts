import config from 'config';
import Keycloak, { Token } from 'keycloak-connect';
import { RequestHandler, Request } from 'express';

const { config: keycloakConfig } = config.get('keycloak');

const keycloakInstance = new Keycloak({}, keycloakConfig);

const getAuthorities = ({ authorities }: any): string[] =>
  authorities ? authorities.split(',') : [];

const checkWritePermissions = (
  { content }: Token,
  { params: { organizationId } }: Request
) =>
  getAuthorities(content).some(authority =>
    authority.includes(`organization:${organizationId}:admin`)
  );

export const enforceWritePermissions: RequestHandler = keycloakInstance.protect(
  checkWritePermissions
);

export default keycloakInstance;
