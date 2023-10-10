import React, { useContext } from 'react';
import { JobsManagementGatewayApi } from './JobsManagementGatewayApi';

const JobsManagementContext =
  React.createContext<JobsManagementGatewayApi>(null);

export const useJobsManagementGatewayApi = (): JobsManagementGatewayApi =>
  useContext<JobsManagementGatewayApi>(JobsManagementContext);

export default JobsManagementContext;
