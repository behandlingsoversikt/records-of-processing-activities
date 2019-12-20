import config from 'config';
import Keycloak, { Token } from 'keycloak-connect';
import { RequestHandler, Request } from 'express';

const { config: keycloakConfig } = config.get('keycloak');

const keycloakInstance = new Keycloak({}, keycloakConfig);

const getAuthorities = ({ authorities }: any): string[] =>
  authorities ? authorities.split(',') : [];

const checkPermissions = (
  { content }: Token,
  { params: { organizationId } }: Request
) =>
  getAuthorities(content).some(authority =>
    authority.includes(`organization:${organizationId}:admin`)
  );

export const enforcePermissions: RequestHandler = keycloakInstance.protect(
  checkPermissions
);

export default keycloakInstance;
