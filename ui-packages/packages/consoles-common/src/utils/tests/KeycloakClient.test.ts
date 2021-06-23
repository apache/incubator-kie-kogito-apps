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

import * as KeycloakClient from '../KeycloakClient';
import axios from 'axios';
import {
  ANONYMOUS_USER,
  KeycloakUserContext,
} from '../../environment/auth';
import Keycloak from 'keycloak-js';

describe('mocked function tests in KeycloakClient', () => {
  it('isAuthEnabled test', () => {
    process.env.KOGITO_ENV_MODE = 'PROD';
    const result = KeycloakClient.isAuthEnabled();
    expect(result).toBeTruthy();
  });
  it('check auth server health - resolved', () => {
    const getMock = jest.spyOn(axios, 'get');
    getMock.mockResolvedValue({ status: 200 });
    KeycloakClient.checkAuthServerHealth().then(response => {
      expect(response).toEqual(200);
    });
    getMock.mockClear();
  });
  it('getKeycloakClient test', async () => {
    const result = KeycloakClient.getKeycloakClient();
    expect(result).toBeInstanceOf(Keycloak);
  });
});

describe('Tests for keycloak client functions', () => {
  const mockUserContext = {
    userName: 'jdoe',
    roles: ['role1'],
    token: 'testToken'
  };

  beforeEach(() => {
    isAuthEnabledMock.mockReturnValue(true);
  });

  const isAuthEnabledMock = jest.spyOn(KeycloakClient, 'isAuthEnabled');
  const getKeyCloakClient: any = jest.spyOn(
    KeycloakClient,
    'getKeycloakClient'
  );
  const checkAuthServerHealthMock = jest.spyOn(
    KeycloakClient,
    'checkAuthServerHealth'
  );

  describe('Wrong API usage tests', () => {
    test('getLoadedSecurityContext called before login - with auth', () => {
      expect(() => KeycloakClient.getLoadedSecurityContext()).toThrowError(
        'Cannot load security context! Please reload screen and log in again.'
      );
    });

    test('getLoadedSecurityContext called before login - without auth anonymous', () => {
      isAuthEnabledMock.mockReturnValue(false);

      const context = KeycloakClient.getLoadedSecurityContext();

      expect(context.getCurrentUser()).toBe(ANONYMOUS_USER);
    });
  });

  it('Test getLoadedSecurityContext - without auth anonymous', async () => {
    isAuthEnabledMock.mockReturnValue(false);

    // eslint-disable-next-line
    await KeycloakClient.loadSecurityContext(
      () => {
        // renders the app
      },
      () => {
        // renders nothing
      }
    );

    const context = KeycloakClient.getLoadedSecurityContext();

    expect(context.getCurrentUser()).toBe(ANONYMOUS_USER);
  });

  it('keycloak with no health check', () => {
    isAuthEnabledMock.mockReturnValue(true);
    window['KOGITO_CONSOLES_KEYCLOAK_DISABLE_HEALTH_CHECK'] = true;
    getKeyCloakClient.mockReturnValue({
      init: () => new Promise((resolve, reject) => resolve(true)),
      logout: () => {
        //logs out the user
      }
    });
    const success = jest.fn();
    const failure = jest.fn();
    KeycloakClient.loadSecurityContext(success, failure);
  });

  it('keycloak with no health check', () => {
    isAuthEnabledMock.mockReturnValue(true);
    window['KOGITO_CONSOLES_KEYCLOAK_DISABLE_HEALTH_CHECK'] = false;
    getKeyCloakClient.mockReturnValue({
      init: () => new Promise((resolve, reject) => resolve(true)),
      logout: () => {
        // logs out the user
      }
    });
    checkAuthServerHealthMock.mockResolvedValue({});
    const success = jest.fn();
    const failure = jest.fn();
    KeycloakClient.loadSecurityContext(success, failure);
  });

  it('Test getLoadedSecurityContext - without auth test user system enabled', async () => {
    isAuthEnabledMock.mockReturnValue(false);
    await KeycloakClient.loadSecurityContext(
      () => {
        //success callback
      },
      () => {
        // failure callback
      }
    );
    const context = KeycloakClient.getLoadedSecurityContext();
    expect(context.getCurrentUser().id).toEqual('Dev User');
    expect(context.getCurrentUser().groups).toHaveLength(0);
  });

  it('Test getLoadedSecurityContext - with auth Not logged', () => {
    KeycloakClient.loadSecurityContext(
      () => {
        expect(() => {
          KeycloakClient.getLoadedSecurityContext();
        }).toThrowError(
          'Cannot load security context! Please reload screen and log in again.'
        );
      },
      () => {
        // do nothing
      }
    );
  });

  it('Test getLoadedSecurityContext - with auth', async () => {
    const getMock = jest.spyOn(axios, 'get');
    getMock.mockResolvedValue({ data: mockUserContext });

    // eslint-disable-next-line
    await KeycloakClient.loadSecurityContext(
      () => {
        // success callback
      },
      () => {
        //failure callback
      }
    );
    expect(KeycloakClient.getLoadedSecurityContext()).toHaveProperty(
      'getCurrentUser'
    );
    const context = KeycloakClient.getLoadedSecurityContext() as KeycloakUserContext;
    expect(context.getCurrentUser().id).toEqual('Dev User');
    expect(context.getCurrentUser().groups).toHaveLength(0);
    expect(context.getCurrentUser().groups).toEqual([]);
  });

  it('Test handleLogout function', () => {
    KeycloakClient.handleLogout();
  });

  it('Test appRenderWithoutAuthenticationEnabled function', () => {
    isAuthEnabledMock.mockReturnValue(false);
    const renderMock = jest.fn();
    KeycloakClient.appRenderWithAxiosInterceptorConfig(renderMock, () => {
      // renders error
    });
    expect(renderMock).toBeCalled();
  });

  it('Test appRenderWithQuarkusAuthenticationEnabled function', () => {
    const renderMock = jest.fn();
    const getTokenMock = jest.spyOn(KeycloakClient, 'getToken');

    getTokenMock.mockReturnValue('testToken');
    KeycloakClient.appRenderWithAxiosInterceptorConfig(renderMock, () => {
      // renders error
    });

    expect(
      // @ts-ignore
      // tslint:disable-next-line:no-floating-promises
      axios.interceptors.response.handlers[0].rejected({
        response: {
          error: {
            status: 401,
            config: 'http://originalRequest'
          }
        }
      })
    ).rejects.toMatchObject({
      response: {
        error: {
          status: 401,
          config: 'http://originalRequest'
        }
      }
    });

    expect(
      // @ts-ignore
      axios.interceptors.request.handlers[0].fulfilled({
        headers: { Authorization: 'Bearer No token' }
      })
    ).toMatchObject({
      headers: { Authorization: 'Bearer testToken' }
    });
    expect(getTokenMock.mock.calls.length).toBe(1);
  });
});
