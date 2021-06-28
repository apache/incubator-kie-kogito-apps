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

import React from 'react';
import { HttpLink } from 'apollo-link-http';
import { onError } from 'apollo-link-error';
import {
  getToken,
  isAuthEnabled,
  ServerUnavailablePage
} from '@kogito-apps/consoles-common';
import { setContext } from 'apollo-link-context';
import { InMemoryCache, NormalizedCacheObject } from 'apollo-cache-inmemory';
import ApolloClient from 'apollo-client';
import ConsolesRoutes from '../ConsolesRoutes/ConsolesRoutes';
import ConsolesLayout from '../ConsolesLayout/ConsolesLayout';
import ReactDOM from 'react-dom';

const RuntimeTools = props => {
  const httpLink = new HttpLink({
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore
    uri: props.dataIndex
  });

  const fallbackUI = onError(({ networkError }: any) => {
    if (networkError && networkError.stack === 'TypeError: Failed to fetch') {
      // eslint-disable-next-line react/no-render-return-value
      return ReactDOM.render(
        <ConsolesLayout apolloClient={client} userContext={props.userContext}>
          <ServerUnavailablePage
            displayName={'Runtime Dev UI'}
            reload={() => window.location.reload()}
          />
        </ConsolesLayout>,
        document.getElementById('envelope-app')
      );
    }
  });

  const setGQLContext = setContext((_, { headers }) => {
    if (isAuthEnabled()) {
      const token = getToken();
      return {
        headers: {
          ...headers,
          authorization: token ? `Bearer ${token}` : ''
        }
      };
    }
  });

  const cache = new InMemoryCache();
  const client: ApolloClient<NormalizedCacheObject> = new ApolloClient({
    cache,
    link: setGQLContext.concat(fallbackUI.concat(httpLink))
  });

  return (
    <ConsolesLayout apolloClient={client} userContext={props.userContext}>
      <ConsolesRoutes />
    </ConsolesLayout>
  );
};

export default RuntimeTools;
