import React from 'react';
import Moment from 'react-moment';
import TaskDescription from '../TaskDescription/TaskDescription';
import { DataTableColumn } from '@kogito-apps/components-common/dist/components/DataTable';
import { UserTaskInstance, TaskState } from '@kogito-apps/task-console-shared';

export const getDefaultColumn = (
  columnPath: string,
  columnLabel: string,
  isSortable: boolean
): DataTableColumn => {
  return {
    path: columnPath,
    label: columnLabel,
    isSortable
  };
};

export const getDateColumn = (
  columnPath: string,
  columnLabel: string
): DataTableColumn => {
  return {
    label: columnLabel,
    path: columnPath,
    bodyCellTransformer: (value) => (
      <Moment fromNow>{new Date(`${value}`)}</Moment>
    ),
    isSortable: true
  };
};

export const getTaskDescriptionColumn = (
  selectTask: (task: UserTaskInstance) => void
): DataTableColumn => {
  return {
    label: 'Name',
    path: 'referenceName',
    bodyCellTransformer: (cellValue, rowTask: UserTaskInstance) => {
      return (
        <TaskDescription task={rowTask} onClick={() => selectTask(rowTask)} />
      );
    },
    isSortable: true
  };
};

export const getTaskStateColumn = (): DataTableColumn => {
  return {
    label: 'Status',
    path: 'state',
    bodyCellTransformer: (cellValue, rowTask: UserTaskInstance) => (
      <TaskState task={rowTask} />
    ),
    isSortable: true
  };
};

export const getDefaultTaskStates = (): string[] => {
  return ['Ready', 'Reserved', 'Completed', 'Aborted', 'Skipped'];
};

export const getDefaultActiveTaskStates = (): string[] => {
  return ['Ready', 'Reserved'];
};
