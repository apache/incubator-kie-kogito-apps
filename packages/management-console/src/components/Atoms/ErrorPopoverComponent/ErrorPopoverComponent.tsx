import React, { useRef, useEffect } from 'react';
import { Popover, Button } from '@patternfly/react-core';

interface IOwnProps {
  processInstanceData: any;
  handleSkip: any;
  handleRetry: any;
  stateIconCreator: any;
}
const ErrorPopover: React.FC<IOwnProps> = ({
  processInstanceData,
  handleRetry,
  handleSkip,
  stateIconCreator
}) => {
  return (
    <Popover
      zIndex={300}
      headerContent={<div>Process error</div>}
      bodyContent={
        <div>
          {processInstanceData.error
            ? processInstanceData.error.message
            : 'No error message found'}
        </div>
      }
      footerContent={
        processInstanceData.addons.includes('process-management') && [
          <Button
            key="confirm1"
            variant="secondary"
            onClick={() => {
              handleSkip(
                processInstanceData.processId,
                processInstanceData.id,
                processInstanceData.endpoint
              );
            }}
            className="pf-u-mr-sm"
          >
            Skip
          </Button>,
          <Button
            key="confirm2"
            variant="secondary"
            onClick={() => {
              handleRetry(
                processInstanceData.processId,
                processInstanceData.id,
                processInstanceData.endpoint
              );
            }}
            className="pf-u-mr-sm"
          >
            Retry
          </Button>
        ]
      }
      position="auto"
    >
      <Button variant="link" isInline>
        {stateIconCreator(processInstanceData.state)}
      </Button>
    </Popover>
  );
};

export default ErrorPopover;
