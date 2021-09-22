import React from 'react';
import { DataTableColumn } from '@kogito-apps/components-common';
import { PlayIcon } from '@patternfly/react-icons';
import { Tooltip } from '@patternfly/react-core';

export const getColumn = (
  columnPath: string,
  columnLabel: string
): DataTableColumn => {
  return {
    label: columnLabel,
    path: columnPath,
    bodyCellTransformer: value => <span>{value}</span>
  };
};

export const getActionColumn = (
  startProcess: (processDefinition: any) => void
): DataTableColumn => {
  return {
    label: 'Actions',
    path: 'actions',
    bodyCellTransformer: (value, rowData) => (
      <div onClick={() => startProcess(rowData)}>
        <Tooltip content="start process">
          <PlayIcon />
        </Tooltip>
      </div>
    )
  };
};
