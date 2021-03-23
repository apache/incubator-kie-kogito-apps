/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from 'react';
import { Button } from '@patternfly/react-core';
import {
  componentOuiaProps,
  OUIAProps,
  ItemDescriptor
} from '@kogito-apps/components-common';
import { UserTaskInstance } from '../../../types';

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
    <Button
      variant={'link'}
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
    </Button>
  );
};

export default TaskDescription;
