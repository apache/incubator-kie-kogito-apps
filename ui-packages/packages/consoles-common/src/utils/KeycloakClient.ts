/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* eslint-disable @typescript-eslint/ban-ts-comment */
import axios from 'axios';
import {
  ANONYMOUS_USER,
  User,
  UserContext,
  KeycloakUserContext
} from '../environment/auth';
import Keycloak from 'keycloak-js';

export const isAuthEnabled = (): boolean => {
  return process.env.KOGITO_ENV_MODE !== 'DEV';
};

let currentSecurityContext: UserContext;
let keycloak: Keycloak.KeycloakInstance;
export const getLoadedSecurityContext = (): UserContext => {
  if (!currentSecurityContext) {
    if (isAuthEnabled()) {
      throw Error(
        'Cannot load security context! Please reload screen and log in again.'
      );
    }
    currentSecurityContext = getNonAuthUserContext();
  }
  return currentSecurityContext;
};

export const checkAuthServerHealth = () => {
  return new Promise((resolve, reject) => {
    axios
      .get(window['KOGITO_CONSOLES_KEYCLOAK_HEALTH_CHECK_URL'])
      .then(response => {
        if (response.status === 200) {
          resolve();
        }
      })
      .catch(() => {
        reject();
      });
  });
};

export const getKeycloakClient = (): Keycloak.KeycloakInstance => {
  return Keycloak({
    realm: window['KOGITO_CONSOLES_KEYCLOAK_REALM'],
    url: window['KOGITO_CONSOLES_KEYCLOAK_URL'],
    clientId: window['KOGITO_CONSOLES_KEYCLOAK_CLIENT_ID']
  });
};

const initializeKeycloak = (onloadSuccess: () => void) => {
  keycloak = getKeycloakClient();
  keycloak
    .init({
      onLoad: 'login-required'
    })
    .then(authenticated => {
      if (authenticated) {
        currentSecurityContext = new KeycloakUserContext({
          userName: keycloak.tokenParsed['preferred_username'],
          roles: keycloak.tokenParsed['groups'],
          token: keycloak.token
        });
        onloadSuccess();
      } else {
        currentSecurityContext = new KeycloakUserContext({
          userName: 'invalid user',
          roles: [],
          token: ''
        });
      }
    });
};

export const loadSecurityContext = (
  onloadSuccess: () => void,
  onLoadFailure: () => void
) => {
  if (isAuthEnabled()) {
    if (window['KOGITO_CONSOLES_KEYCLOAK_DISABLE_HEALTH_CHECK']) {
      initializeKeycloak(onloadSuccess);
    } else {
      checkAuthServerHealth()
        .then(() => {
          initializeKeycloak(onloadSuccess);
        })
        .catch(() => {
          onLoadFailure();
        });
    }
  } else {
    currentSecurityContext = getNonAuthUserContext();
    onloadSuccess();
  }
};

const getNonAuthUserContext = (): UserContext => {
  return {
    getCurrentUser(): User {
      return ANONYMOUS_USER;
    }
  };
};
export const getToken = (): string => {
  if (isAuthEnabled()) {
    const ctx = getLoadedSecurityContext() as KeycloakUserContext;
    return ctx.getToken();
  }
};

export const appRenderWithAxiosInterceptorConfig = (
  appRender: (ctx: UserContext) => void,
  onLoadFailure: () => void
): void => {
  loadSecurityContext(() => {
    appRender(getLoadedSecurityContext());
  }, onLoadFailure);
  if (isAuthEnabled()) {
    axios.interceptors.response.use(
      response => response,
      error => {
        if (error.response.status === 401) {
          loadSecurityContext(() => {
            /* tslint:disable:no-string-literal */
            axios.defaults.headers.common['Authorization'] =
              'Bearer ' + getToken();
            /* tslint:enable:no-string-literal */
            return axios(error.config);
          }, onLoadFailure);
        }
        return Promise.reject(error);
      }
    );
    axios.interceptors.request.use(
      config => {
        if (currentSecurityContext) {
          const t = getToken();
          /* tslint:disable:no-string-literal */
          config.headers['Authorization'] = 'Bearer ' + t;
          /* tslint:enable:no-string-literal */
          return config;
        }
      },
      error => {
        /* tslint:disable:no-floating-promises */
        Promise.reject(error);
        /* tslint:enable:no-floating-promises */
      }
    );
  }
};

export const handleLogout = (): void => {
  currentSecurityContext = undefined;
  if (keycloak) {
    keycloak.logout();
  }
};
