import React from 'react';
import { ApolloClient } from 'apollo-client';
import JobsManagementContext from './JobsManagementContext';
import { JobsManagementGatewayApiImpl } from './JobsManagementGatewayApi';
import { GraphQLJobsManagementQueries } from './JobsManagementQueries';

interface IOwnProps {
  apolloClient: ApolloClient<any>;
  children;
}

const JobsManagementContextProvider: React.FC<IOwnProps> = ({
  apolloClient,
  children
}) => {
  return (
    <JobsManagementContext.Provider
      value={
        new JobsManagementGatewayApiImpl(
          new GraphQLJobsManagementQueries(apolloClient)
        )
      }
    >
      {children}
    </JobsManagementContext.Provider>
  );
};

export default JobsManagementContextProvider;
