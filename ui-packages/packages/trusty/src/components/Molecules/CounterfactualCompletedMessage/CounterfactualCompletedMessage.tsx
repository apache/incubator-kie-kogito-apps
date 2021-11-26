import React, { useEffect, useState } from 'react';
import {
  Alert,
  AlertActionCloseButton,
  StackItem
} from '@patternfly/react-core';
import { CFExecutionStatus, CFStatus } from '../../../types';

type CounterfactualCompletedMessageProps = {
  status: CFStatus;
};

const CounterfactualCompletedMessage = (
  props: CounterfactualCompletedMessageProps
) => {
  const { status } = props;
  const [localStatus, setLocalStatus] = useState<CFStatus>();
  const [isMessageVisible, setIsMessageVisible] = useState(false);

  useEffect(() => {
    if (
      localStatus &&
      localStatus.executionStatus === CFExecutionStatus.RUNNING &&
      (status.executionStatus === CFExecutionStatus.COMPLETED ||
        status.executionStatus === CFExecutionStatus.FAILED ||
        status.executionStatus === CFExecutionStatus.NO_RESULTS)
    ) {
      setIsMessageVisible(true);
    }
    if (status.executionStatus === CFExecutionStatus.NOT_STARTED) {
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
          {status.executionStatus === CFExecutionStatus.COMPLETED && (
            <Alert
              isInline
              variant="success"
              title="Counterfactual analysis completed successfully"
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
          )}
          {status.executionStatus === CFExecutionStatus.FAILED && (
            <Alert
              isInline
              variant="danger"
              title="Counterfactual analysis could not be completed"
              actionClose={
                <AlertActionCloseButton onClose={handleAlertClosing} />
              }
            >
              <p>
                <span>
                  There was an error while elaborating counterfactual results.
                </span>
              </p>
            </Alert>
          )}
          {status.executionStatus === CFExecutionStatus.NO_RESULTS && (
            <Alert
              isInline
              variant="warning"
              title="No counterfactual solutions found"
              actionClose={
                <AlertActionCloseButton onClose={handleAlertClosing} />
              }
            >
              <p>
                <span>
                  The Counterfactual analysis wasn&apos;t able to find
                  counterfactual solutions for the provided combination of
                  outcomes and inputs.
                  <br /> Try changing the input settings and running the
                  analysis again.
                </span>
              </p>
            </Alert>
          )}
        </StackItem>
      )}
    </>
  );
};

export default CounterfactualCompletedMessage;
