import React from 'react';
import { Popover } from '@patternfly/react-core/dist/js/components/Popover';
import { Button } from '@patternfly/react-core/dist/js/components/Button';
import { ProcessInstanceIconCreator } from '../utils/ProcessListUtils';
import { ProcessInstance } from '@kogito-apps/management-console-shared/dist/types';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';

interface ErrorPopoverProps {
  processInstanceData: ProcessInstance;
  onSkipClick: (processInstance: ProcessInstance) => Promise<void>;
  onRetryClick: (processInstance: ProcessInstance) => Promise<void>;
}
const ErrorPopover: React.FC<ErrorPopoverProps & OUIAProps> = ({
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
            data-testid="skip-button"
            variant="secondary"
            onClick={() => onSkipClick(processInstanceData)}
            className="pf-u-mr-sm"
          >
            Skip
          </Button>,
          <Button
            key="confirm2"
            variant="secondary"
            id="retry-button"
            data-testid="retry-button"
            onClick={() => onRetryClick(processInstanceData)}
            className="pf-u-mr-sm"
          >
            Retry
          </Button>
        ]
      }
      position="auto"
      {...componentOuiaProps(ouiaId, 'error-popover', ouiaSafe)}
    >
      <Button variant="link" isInline data-testid="error-state">
        {ProcessInstanceIconCreator(processInstanceData.state)}
      </Button>
    </Popover>
  );
};

export default ErrorPopover;
