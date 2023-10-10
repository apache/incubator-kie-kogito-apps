import React from 'react';
import { ItemDescriptor } from '@kogito-apps/components-common/dist/components/ItemDescriptor';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { UserTaskInstance } from '@kogito-apps/task-console-shared';

interface IOwnProps {
  task: UserTaskInstance;
  onClick: () => void;
}

const TaskDescription: React.FC<IOwnProps & OUIAProps> = ({
  task,
  onClick,
  ouiaId,
  ouiaSafe
}) => {
  return (
    <a
      onClick={onClick}
      {...componentOuiaProps(ouiaId, 'task-description', ouiaSafe)}
    >
      <strong>
        <ItemDescriptor
          itemDescription={{
            id: task.id,
            name: task.referenceName
          }}
        />
      </strong>
    </a>
  );
};

export default TaskDescription;
