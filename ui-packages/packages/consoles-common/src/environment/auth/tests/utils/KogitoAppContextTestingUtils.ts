import * as Keycloak from '../../../../utils/KeycloakClient';
import * as KogitoAppContext from '../../../context/KogitoAppContext';
import { KeycloakUserContext } from '../../KeycloakUserContext';
import { TestUserContext } from '../../../context/TestUserContext';

export const testIsAuthEnabledMock = jest.spyOn(Keycloak, 'isAuthEnabled');
testIsAuthEnabledMock.mockReturnValue(true);

export const testHandleLogoutMock = jest.spyOn(Keycloak, 'handleLogout');
testHandleLogoutMock.mockImplementation(jest.fn());

const newContext = (authEnabled: boolean): KogitoAppContext.AppContext => {
  const testUserSystem = authEnabled
    ? new KeycloakUserContext({
        userName: 'jdoe',
        roles: ['user', 'manager'],
        token: 'token',
        tokenMinValidity: 30,
        logout: () => Keycloak.handleLogout()
      })
    : new TestUserContext();

  return new KogitoAppContext.AppContextImpl(testUserSystem);
};

export let testKogitoAppContext: KogitoAppContext.AppContext =
  newContext(false);

jest.spyOn(KogitoAppContext, 'useKogitoAppContext').mockImplementation(() => {
  return testKogitoAppContext;
});

export const resetTestKogitoAppContext = (authEnabled: boolean) => {
  testKogitoAppContext = newContext(authEnabled);
};
