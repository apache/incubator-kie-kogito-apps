/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import * as React from 'react';
import { RuntimeToolsDevUIEnvelopeViewApi } from './RuntimeToolsDevUIEnvelopeViewApi';
import { useImperativeHandle } from 'react';
import { HttpLink } from 'apollo-link-http';
import { onError } from 'apollo-link-error';
import ConsolesLayout from '../components/console/ConsolesLayout/ConsolesLayout';
import {
  getToken,
  isAuthEnabled,
  ServerUnavailablePage,
  UserContext
} from '../../../consoles-common';
import { setContext } from 'apollo-link-context';
import { InMemoryCache, NormalizedCacheObject } from 'apollo-cache-inmemory';
import ApolloClient from 'apollo-client';
import ConsolesRoutes from '../components/console/ConsolesRoutes/ConsolesRoutes';
import ReactDOM from 'react-dom';

export interface Props {
  userContext: UserContext;
}

export const RuntimeToolsDevUIEnvelopeViewRef: React.ForwardRefRenderFunction<
  RuntimeToolsDevUIEnvelopeViewApi,
  Props
> = (props: Props, forwardingRef) => {
  const httpLink = new HttpLink({
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore
    uri: window.DATA_INDEX_ENDPOINT || process.env.KOGITO_DATAINDEX_HTTP_URL
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

  useImperativeHandle(
    forwardingRef,
    () => {
      return {
        setDataIndexUrl: dataIndexUrl => {
          /**/
        }
      };
    },
    []
  );

  return (
    <>
      <ConsolesLayout apolloClient={client} userContext={props.userContext}>
        <ConsolesRoutes />
      </ConsolesLayout>
    </>
  );
};

export const RuntimeToolsDevUIEnvelopeView = React.forwardRef(
  RuntimeToolsDevUIEnvelopeViewRef
);
