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

import React, { useEffect, useState } from 'react';
import { OUIAProps } from '@kogito-apps/ouia-tools';
import { MonitoringView } from '@kogito-apps/monitoring';
import { ProcessListGatewayApi } from '../../../channel/ProcessList';
import { useProcessListGatewayApi } from '../../../channel/ProcessList/ProcessListContext';
import { ProcessInstanceState } from '@kogito-apps/management-console-shared';
import {
  KogitoEmptyState,
  KogitoEmptyStateType
} from '@kogito-apps/components-common';

const MonitoringContainer: React.FC<OUIAProps> = ({ ouiaId, ouiaSafe }) => {
  const gatewayApi: ProcessListGatewayApi = useProcessListGatewayApi();
  const [hasWorkflow, setHasWorkflow] = useState(false);
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    const intervaId = setInterval(() => {
      if (!hasWorkflow) {
        gatewayApi.initialLoad(
          {
            status: [
              ProcessInstanceState.Aborted,
              ProcessInstanceState.Active,
              ProcessInstanceState.Completed,
              ProcessInstanceState.Error,
              ProcessInstanceState.Suspended
            ],
            businessKey: []
          },
          {}
        );
        gatewayApi.query(0, 1).then(list => {
          setHasWorkflow(list.length > 0);
          setLoading(false);
        });
      }
    }, 500);
    return () => clearInterval(intervaId);
  }, [hasWorkflow, loading]);
  return (
    <>
      {hasWorkflow ? (
        <MonitoringView />
      ) : (
        <KogitoEmptyState
          title={loading ? 'Loading' : 'No Data'}
          body={loading ? 'Loading Data' : 'No workflows were started'}
          type={KogitoEmptyStateType.Info}
        />
      )}
    </>
  );
};

export default MonitoringContainer;
