import axios, {AxiosInstance} from 'axios';

import Keycloak, {KeycloakInstance} from 'keycloak-js';

export interface UserContext {
  userName: string;
  roles: string[];
  token: string;
}

export const isAuthEnabled = (): boolean => {
  // @ts-ignore
  return window.KOGITO_AUTH_ENABLED || process.env.KOGITO_AUTH_ENABLED;
};

export const isReactAuthEnabled = (): boolean => {
  // @ts-ignore
  return process.env.KOGITO_REACT_AUTH_ENABLED;
};

export const createKeycloakInstance = (): KeycloakInstance => {
  const authKeycloakRealm = process.env.KOGITO_KEYCLOAK_REALM;
  const authKeycloakUrl = process.env.KOGITO_KEYCLOAK_URL;
  const authKeycloakClientId = process.env.KOGITO_KEYCLOAK_CLIENT_ID;

  return Keycloak({
    realm: authKeycloakRealm,
    url: authKeycloakUrl + '/auth',
    clientId: authKeycloakClientId
  });
};

const keycloakInstance = createKeycloakInstance();

export const getKeycloakInstance = (): KeycloakInstance => {
  return keycloakInstance;
};

let currentSecurityContext;
export const getLoadedSecurityContext = (): UserContext => {
  if (!currentSecurityContext) {
    return {
      userName: 'Anonymous',
      roles: [],
      token: ''
    };
  }
  return currentSecurityContext;
}

export const loadSecurityContext = async (
  onloadSuccess: () => void
) => {
  if (isAuthEnabled()) {
    if (isReactAuthEnabled()) {
      currentSecurityContext = {
        // @ts-ignore
        userName: getKeycloakInstance().tokenParsed.preferred_username,
        roles: getKeycloakInstance().tokenParsed.realm_access.roles,
        token: getKeycloakInstance().token
      };
      onloadSuccess();
    } else {
      try {
        const response = await axios.get(`/api/user/me`, {
          headers: {'Access-Control-Allow-Origin': '*'}
        });
        currentSecurityContext = response.data;
        onloadSuccess();
      } catch (error) {
        currentSecurityContext = {
          userName: error.message,
          roles: [],
          token: ''
        };
      }
    }
  } else {
    currentSecurityContext = {
      userName: 'Anonymous',
      roles: [],
      token: ''
    };
    onloadSuccess();
  }
};

export const getUserName = (): string => {
  return getLoadedSecurityContext().userName;
};

export const getToken = (): string => {
  return getLoadedSecurityContext().token;
};

export const getRoles = (): string[] => {
  return getLoadedSecurityContext().roles;
};

export const addResponseInterceptor = (client: AxiosInstance, onError: (AxiosRequestConfig) => void) => {
  client.interceptors.response.use(response => response,
    (error) => {
      if (error.response.status === 401) {
        onError(error.config);
      }
      return Promise.reject(error);
    });
};

export const appRenderWithAxiosInterceptorConfig = (
  appRender: () => void
): void => {
  if (isAuthEnabled()) {
    if (isReactAuthEnabled()) {
      getKeycloakInstance()
        .init({onLoad: 'login-required'})
        .success(authenticated => {
          if (authenticated) {
            loadSecurityContext(() => appRender());
          }
        });
      addResponseInterceptor(
        axios,
        ((axiosRequestConfig) => {
          getKeycloakInstance()
            .updateToken(5)
            .success(() => {
              /* tslint:disable:no-string-literal */
              axios.defaults.headers.common['Authorization'] =
                'Bearer ' + getToken();
              /* tslint:enable:no-string-literal */
              return axios(axiosRequestConfig);
            });
        }));
    } else {
      loadSecurityContext(() => appRender());
      addResponseInterceptor(
        axios,
        ((axiosRequestConfig) => {
          loadSecurityContext(() => {
              /* tslint:disable:no-string-literal */
              axios.defaults.headers.common['Authorization'] =
                'Bearer ' + getToken();
              /* tslint:enable:no-string-literal */
              return axios(axiosRequestConfig);
            });
        }));
    }
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
  } else {
    loadSecurityContext(() => appRender());
  }
}

export const handleLogout = (): void => {
  currentSecurityContext = undefined;
  if (isReactAuthEnabled()) {
    getKeycloakInstance().logout()
  } else {
    window.location.replace(`/logout`);
  }
};
