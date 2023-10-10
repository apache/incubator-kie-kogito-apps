import React, { useMemo } from 'react';
import { ApolloClient } from 'apollo-client';
import ProcessListContext from './ProcessListContext';
import { ProcessListGatewayApiImpl } from './ProcessListGatewayApi';
import { GraphQLProcessListQueries } from './ProcessListQueries';

interface ProcessListContextProviderProps {
  apolloClient: ApolloClient<any>;
  children;
}

const ProcessListContextProvider: React.FC<ProcessListContextProviderProps> = ({
  apolloClient,
  children
}) => {
  const gatewayApiImpl = useMemo(() => {
    return new ProcessListGatewayApiImpl(
      new GraphQLProcessListQueries(apolloClient)
    );
  }, []);

  return (
    <ProcessListContext.Provider value={gatewayApiImpl}>
      {children}
    </ProcessListContext.Provider>
  );
};

export default ProcessListContextProvider;
