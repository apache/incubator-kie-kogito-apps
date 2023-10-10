import React, { useMemo } from 'react';
import { ApolloClient } from 'apollo-client';
import ProcessListContext from '../../channel/ProcessList/ProcessListContext';
import { ProcessListGatewayApiImpl } from '../../channel/ProcessList/ProcessListGatewayApi';
import { GraphQLProcessListQueries } from '../../channel/ProcessList/ProcessListQueries';

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
