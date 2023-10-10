import React from 'react';
import { OUIAProps } from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { EmbeddedJobsManagement } from '@kogito-apps/jobs-management';
import { JobsManagementGatewayApi } from '../../../channel/JobsManagement';
import { useJobsManagementGatewayApi } from '../../../channel/JobsManagement/JobsManagementContext';

const JobsManagementContainer: React.FC<OUIAProps> = () => {
  const gatewayApi: JobsManagementGatewayApi = useJobsManagementGatewayApi();
  return (
    <EmbeddedJobsManagement
      driver={gatewayApi}
      targetOrigin={window.location.origin}
    />
  );
};

export default JobsManagementContainer;
