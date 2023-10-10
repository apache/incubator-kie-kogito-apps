import React from 'react';
import ReactDOM from 'react-dom';
import ApolloClient from 'apollo-client';
import '@patternfly/patternfly/patternfly.css';
import { HttpLink } from 'apollo-link-http';
import { setContext } from 'apollo-link-context';
import { onError } from 'apollo-link-error';
import { InMemoryCache, NormalizedCacheObject } from 'apollo-cache-inmemory';
import { UserContext } from '@kogito-apps/consoles-common/dist/environment/auth';
import {
  appRenderWithAxiosInterceptorConfig,
  getToken,
  isAuthEnabled,
  updateKeycloakToken
} from '@kogito-apps/consoles-common/dist/utils/KeycloakClient';
import { KeycloakUnavailablePage } from '@kogito-apps/consoles-common/dist/components/pages/KeycloakUnavailablePage';
import { TaskConsole, TaskConsoleRoutes } from './components/console';
import { ServerUnavailablePage } from '@kogito-apps/consoles-common/dist/components/pages/ServerUnavailablePage/ServerUnavailablePage';

const onLoadFailure = () => {
  ReactDOM.render(<KeycloakUnavailablePage />, document.getElementById('root'));
};

const appRender = (ctx: UserContext) => {
  const httpLink = new HttpLink({
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore
    uri: window.DATA_INDEX_ENDPOINT || process.env.KOGITO_DATAINDEX_HTTP_URL
  });

  const fallbackUI = onError(({ networkError }: any) => {
    if (networkError && networkError.stack === 'TypeError: Failed to fetch') {
      // eslint-disable-next-line react/no-render-return-value
      return ReactDOM.render(
        <TaskConsole apolloClient={client} userContext={ctx}>
          <ServerUnavailablePage
            displayName={'Task Console'}
            reload={() => window.location.reload()}
          />
        </TaskConsole>,
        document.getElementById('root')
      );
    }
  });

  const setGQLContext = setContext((_, { headers }) => {
    if (!isAuthEnabled()) {
      return {
        headers
      };
    }
    return new Promise((resolve, reject) => {
      updateKeycloakToken()
        .then(() => {
          const token = getToken();
          resolve({
            headers: {
              ...headers,
              authorization: token ? `Bearer ${token}` : ''
            }
          });
        })
        .catch(() => {
          reject();
        });
    });
  });

  const cache = new InMemoryCache();
  const client: ApolloClient<NormalizedCacheObject> = new ApolloClient({
    cache,
    link: setGQLContext.concat(fallbackUI.concat(httpLink))
  });

  ReactDOM.render(
    <TaskConsole apolloClient={client} userContext={ctx}>
      <TaskConsoleRoutes />
    </TaskConsole>,
    document.getElementById('root')
  );
};

appRenderWithAxiosInterceptorConfig(
  (ctx: UserContext) => appRender(ctx),
  onLoadFailure
);
