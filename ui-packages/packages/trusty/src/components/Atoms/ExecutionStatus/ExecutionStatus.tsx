import React from 'react';
import { CheckCircleIcon, ErrorCircleOIcon } from '@patternfly/react-icons';
import './ExecutionStatus.scss';

type ExecutionStatusProps = {
  result: boolean;
};

const ExecutionStatus = (props: ExecutionStatusProps) => {
  const { result } = props;
  let className = 'execution-status-badge execution-status-badge--';
  let statusDescription;
  let icon;
  if (result) {
    className += 'success';
    statusDescription = 'Completed';
    icon = <CheckCircleIcon className={className} />;
  } else {
    className += 'error';
    statusDescription = 'Error';
    icon = <ErrorCircleOIcon className={className} />;
  }

  return (
    <>
      {icon}
      <span>{statusDescription}</span>
    </>
  );
};

export default ExecutionStatus;
