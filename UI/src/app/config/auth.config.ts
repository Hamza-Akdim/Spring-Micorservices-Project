import { PassedInitialConfig } from 'angular-auth-oidc-client';

const isLocal = window.location.hostname === 'localhost';

export const authConfig: PassedInitialConfig = {
  config: {
    authority: isLocal
      ? 'http://localhost:8181/realms/spring-microservices-security-realm'
      : 'http://keycloak.micros.svc.cluster.local:8080/realms/spring-microservices-security-realm',
    redirectUrl: window.location.origin,
    postLogoutRedirectUri: window.location.origin,
    clientId: 'angular-client',
    scope: 'openid profile offline_access',
    responseType: 'code',
    silentRenew: true,
    useRefreshToken: true,
    renewTimeBeforeTokenExpiresInSeconds: 30,
  }
};

