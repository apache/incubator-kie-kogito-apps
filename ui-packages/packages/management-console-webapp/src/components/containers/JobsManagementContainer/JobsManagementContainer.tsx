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
import { OUIAProps } from '@kogito-apps/components-common';
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
