/**
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import React from 'react';
import { Popover, Button } from '@patternfly/react-core';
import { ProcessInstanceIconCreator } from '../../../utils/Utils';
import { GraphQL, OUIAProps, componentOuiaProps } from '@kogito-apps/common';
import ProcessInstance = GraphQL.ProcessInstance;

interface IOwnProps {
  processInstanceData: ProcessInstance;
  onSkipClick: () => void;
  onRetryClick: () => void;
}
const ErrorPopover: React.FC<IOwnProps & OUIAProps> = ({
  processInstanceData,
  onSkipClick,
  onRetryClick,
  ouiaId,
  ouiaSafe
}) => {
  return (
    <Popover
      zIndex={300}
      id={processInstanceData.id}
      headerContent={<div>Process error</div>}
      bodyContent={
        <div>
          {processInstanceData.error
            ? processInstanceData.error.message
            : 'No error message found'}
        </div>
      }
      footerContent={
        processInstanceData.addons.includes('process-management') &&
        processInstanceData.serviceUrl && [
          <Button
            key="confirm1"
            id="skip-button"
            variant="secondary"
            onClick={onSkipClick}
            className="pf-u-mr-sm"
          >
            Skip
          </Button>,
          <Button
            key="confirm2"
            variant="secondary"
            id="retry-button"
            onClick={onRetryClick}
            className="pf-u-mr-sm"
          >
            Retry
          </Button>
        ]
      }
      position="auto"
      {...componentOuiaProps(ouiaId, 'error-popover', ouiaSafe)}
    >
      <Button variant="link" isInline>
        {ProcessInstanceIconCreator(processInstanceData.state)}
      </Button>
    </Popover>
  );
};

export default ErrorPopover;
