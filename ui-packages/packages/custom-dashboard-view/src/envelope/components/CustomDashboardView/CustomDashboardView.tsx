/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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

import React, { useRef, useState } from 'react';
import { OUIAProps, componentOuiaProps } from '@kogito-apps/ouia-tools';
import { CustomDashboardViewDriver } from '../../../api/CustomDashboardViewDriver';
import { ServerErrors } from '@kogito-apps/common';
import { Bullseye, Card } from '@patternfly/react-core';

export interface CustomDashboardViewProps {
  isEnvelopeConnectedToChannel: boolean;
  driver: CustomDashboardViewDriver;
  customDashboardName: string;
}

const CustomDashboardView: React.FC<CustomDashboardViewProps & OUIAProps> = ({
  isEnvelopeConnectedToChannel,
  driver,
  ouiaId,
  customDashboardName,
  ouiaSafe
}) => {
  const ref = useRef(null);
  const [dashbaordContent, setDashbaordContent] = useState<string>();
  const [errorMessage, setErrorMessage] = useState<string>();
  const [isError, setError] = useState<boolean>(false);

  driver
    .getCustomDashboardContent(customDashboardName)
    .then(value => setDashbaordContent(value))
    .catch(error => {
      setError(true);
      setErrorMessage(error);
    });

  window.addEventListener('message', e => {
    if (e.data == 'ready') {
      ref.current.contentWindow.postMessage(dashbaordContent, null);
    }
  });

  return (
    <>
      {isError ? (
        <>
          {isEnvelopeConnectedToChannel && (
            <Card className="kogito-process-details__card-size">
              <Bullseye>
                <ServerErrors error={errorMessage} variant="large" />
              </Bullseye>
            </Card>
          )}
        </>
      ) : (
        <iframe
          ref={ref}
          id="db"
          src="resources/webapp/custom-dashboard-view/dashbuilder"
          style={{ width: '100%', height: '100%', padding: '10px' }}
          {...componentOuiaProps(ouiaId, 'customDashboard-view', ouiaSafe)}
        />
      )}
    </>
  );
};

export default CustomDashboardView;
