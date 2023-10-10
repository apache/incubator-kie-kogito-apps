import React from 'react';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { EmbeddedJobsManagement } from '@kogito-apps/jobs-management';
import { JobsManagementGatewayApi } from '../../../channel/JobsManagement';
import { useJobsManagementGatewayApi } from '../../../channel/JobsManagement/JobsManagementContext';
import { useDevUIAppContext } from '../../contexts/DevUIAppContext';

const JobsManagementContainer: React.FC<OUIAProps> = ({ ouiaId, ouiaSafe }) => {
  const gatewayApi: JobsManagementGatewayApi = useJobsManagementGatewayApi();
  const appContext = useDevUIAppContext();
  return (
    <EmbeddedJobsManagement
      driver={gatewayApi}
      targetOrigin={appContext.getDevUIUrl()}
      {...componentOuiaProps(ouiaId, 'jobs-management-container', ouiaSafe)}
    />
  );
};

export default JobsManagementContainer;
