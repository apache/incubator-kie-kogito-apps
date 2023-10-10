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
