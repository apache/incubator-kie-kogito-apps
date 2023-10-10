import React from 'react';

import { Label } from '@patternfly/react-core/dist/js/components/Label';
import { BanIcon } from '@patternfly/react-icons/dist/js/icons/ban-icon';
import { CheckCircleIcon } from '@patternfly/react-icons/dist/js/icons/check-circle-icon';
import { OnRunningIcon } from '@patternfly/react-icons/dist/js/icons/on-running-icon';
import {
  OUIAProps,
  componentOuiaProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { UserTaskInstance } from '../../types';

interface Props {
  task: UserTaskInstance;
  variant?: 'default' | 'label';
}

const TaskState: React.FC<Props & OUIAProps> = ({
  task,
  variant,
  ouiaId,
  ouiaSafe
}) => {
  const icon: JSX.Element = resolveTaskStateIcon(task);

  if (variant === 'label') {
    const color = resolveTaskStateLabelColor(task);
    return (
      <Label
        color={color}
        icon={icon}
        {...componentOuiaProps(ouiaId, 'task-state', ouiaSafe)}
      >
        {task.state}
      </Label>
    );
  }

  return (
    <React.Fragment>
      {icon}{' '}
      <span {...componentOuiaProps(ouiaId, 'task-state', ouiaSafe)}>
        {task.state}
      </span>
    </React.Fragment>
  );
};

function resolveTaskStateIcon(task: UserTaskInstance): JSX.Element {
  if (task.state === 'Aborted') {
    return <BanIcon className="pf-u-mr-sm" />;
  } else if (task.completed) {
    return (
      <CheckCircleIcon
        className="pf-u-mr-sm"
        color="var(--pf-global--success-color--100)"
      />
    );
  } else {
    return <OnRunningIcon className="pf-u-mr-sm" />;
  }
}

function resolveTaskStateLabelColor(task: UserTaskInstance) {
  if (task.state === 'Aborted') {
    return 'red';
  } else if (task.completed) {
    return 'green';
  } else {
    return 'blue';
  }
}

export default TaskState;
