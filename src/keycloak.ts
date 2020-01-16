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

const checkReadPermissions = (
  { content }: Token,
  { params: { organizationId } }: Request
) => {
  const readAndWritePermissions = [
    `organization:${organizationId}:read`,
    `organization:${organizationId}:admin`
  ];
  return getAuthorities(content).some(authority =>
    readAndWritePermissions.includes(authority)
  );
};

export const enforceWritePermissions: RequestHandler = keycloakInstance.protect(
  checkWritePermissions
);

export const enforceReadPermissions: RequestHandler = keycloakInstance.protect(
  checkReadPermissions
);

export default keycloakInstance;
