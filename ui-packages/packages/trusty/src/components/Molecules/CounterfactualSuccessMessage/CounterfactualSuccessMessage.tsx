import React, { useEffect, useState } from 'react';
import {
  Alert,
  AlertActionCloseButton,
  StackItem
} from '@patternfly/react-core';
import { CFStatus } from '../../Templates/Counterfactual/Counterfactual';

type CounterfactualSuccessMessageProps = {
  status: CFStatus;
};

const CounterfactualSuccessMessage = (
  props: CounterfactualSuccessMessageProps
) => {
  const { status } = props;
  const [localStatus, setLocalStatus] = useState<CFStatus>();
  const [isMessageVisible, setIsMessageVisible] = useState(false);

  useEffect(() => {
    if (
      localStatus &&
      localStatus.executionStatus === 'RUNNING' &&
      status.executionStatus === 'RUN'
    ) {
      setIsMessageVisible(true);
    }
    if (status.executionStatus === 'NOT_STARTED') {
      setIsMessageVisible(false);
    }
    setLocalStatus(status);
  }, [status, localStatus]);

  const handleAlertClosing = () => {
    setIsMessageVisible(false);
  };

  return (
    <>
      {isMessageVisible && (
        <StackItem>
          <Alert
            isInline
            variant="success"
            title="Counterfactual Analysis completed successfully"
            actionClose={
              <AlertActionCloseButton onClose={handleAlertClosing} />
            }
          >
            <p>
              To run another analysis, either create a new counterfactual or
              edit the existing counterfactual.
              <br />
              Note: the current results will be cleared when another
              counterfactual has been initiated.
            </p>
          </Alert>
        </StackItem>
      )}
    </>
  );
};

export default CounterfactualSuccessMessage;
