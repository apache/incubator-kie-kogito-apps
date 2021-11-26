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
import { ApolloClient } from 'apollo-client';
import ProcessDetailsContext from './ProcessDetailsContext';
import { ProcessDetailsGatewayApiImpl } from './ProcessDetailsGatewayApi';
import { GraphQLProcessDetailsQueries } from './ProcessDetailsQueries';

interface IOwnProps {
  apolloClient: ApolloClient<any>;
  children;
}

const ProcessDetailsContextProvider: React.FC<IOwnProps> = ({
  apolloClient,
  children
}) => {
  return (
    <ProcessDetailsContext.Provider
      value={
        new ProcessDetailsGatewayApiImpl(
          new GraphQLProcessDetailsQueries(apolloClient)
        )
      }
    >
      {children}
    </ProcessDetailsContext.Provider>
  );
};

export default ProcessDetailsContextProvider;
