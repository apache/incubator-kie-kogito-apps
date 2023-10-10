import React from 'react';
import Moment from 'react-moment';
import { DataTableColumn } from '@kogito-apps/components-common/dist/components/DataTable';
import { FormInfo } from '../../../api/FormsListEnvelopeApi';
import { Label } from '@patternfly/react-core';

export const getFormTypeLabel = (value) => {
  switch (value) {
    case 'HTML':
      return <Label variant="outline">HTML</Label>;
    case 'TSX':
      return <Label variant="outline">REACT</Label>;
    /* istanbul ignore next */
    default:
      return value;
  }
};

export const getFormNameColumn = (
  selectForm: (formData: FormInfo) => void
): DataTableColumn => {
  return {
    label: 'Name',
    path: 'name',
    bodyCellTransformer: (cellValue, rowForm: FormInfo) => {
      return (
        <a onClick={() => selectForm(rowForm)}>
          <strong>{cellValue}</strong>
        </a>
      );
    },
    isSortable: true
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

export const getFormTypeColumn = (): DataTableColumn => {
  return {
    label: 'Type',
    path: 'type',
    bodyCellTransformer: (cellValue) => getFormTypeLabel(cellValue),
    isSortable: true
  };
};
