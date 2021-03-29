import { ProcessInstance, ProcessInstanceState } from '../../../types/types';
import React from 'react';
import {
  OnRunningIcon,
  CheckCircleIcon,
  BanIcon,
  PausedIcon,
  ErrorCircleOIcon
} from '@patternfly/react-icons';

export const ProcessInstanceIconCreator = (
  state: ProcessInstanceState
): JSX.Element => {
  switch (state) {
    case ProcessInstanceState.Active:
      return (
        <>
          <OnRunningIcon className="pf-u-mr-sm" />
          Active
        </>
      );
    case ProcessInstanceState.Completed:
      return (
        <>
          <CheckCircleIcon
            className="pf-u-mr-sm"
            color="var(--pf-global--success-color--100)"
          />
          Completed
        </>
      );
    case ProcessInstanceState.Aborted:
      return (
        <>
          <BanIcon className="pf-u-mr-sm" />
          Aborted
        </>
      );
    case ProcessInstanceState.Suspended:
      return (
        <>
          <PausedIcon className="pf-u-mr-sm" />
          Suspended
        </>
      );
    case ProcessInstanceState.Error:
      return (
        <>
          <ErrorCircleOIcon
            className="pf-u-mr-sm"
            color="var(--pf-global--danger-color--100)"
          />
          Error
        </>
      );
  }
};

export const getProcessInstanceDescription = (
  processInstance: ProcessInstance
) => {
  return {
    id: processInstance.id,
    name: processInstance.processName,
    description: processInstance.businessKey
  };
};

export const alterOrderByObj = (orderByObj): any => {
  if (orderByObj['id']) {
    orderByObj['processName'] = orderByObj['id'];
    delete orderByObj['id'];
  } else if (orderByObj['status']) {
    orderByObj['state'] = orderByObj['status'];
    delete orderByObj['status'];
  } else if (orderByObj['created']) {
    orderByObj['start'] = orderByObj['created'];
    delete orderByObj['created'];
  }
  return orderByObj;
};
