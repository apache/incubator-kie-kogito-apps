import { CheckCircleIcon, ErrorCircleOIcon } from '@patternfly/react-icons';
import React from 'react';
import './ExecutionStatus.scss';

type ExecutionStatusProps = {
  result: boolean;
};

const ExecutionStatus = (props: ExecutionStatusProps) => {
  let className = 'execution-status-badge execution-status-badge--';
  if (props.result) {
    className += 'success';
    return (
      <>
        <CheckCircleIcon className={className} />
        <span>Completed</span>
      </>
    );
  } else {
    className += 'error';
    return (
      <>
        <ErrorCircleOIcon className={className} />
        <span>Error</span>
      </>
    );
  }
};

export default ExecutionStatus;
