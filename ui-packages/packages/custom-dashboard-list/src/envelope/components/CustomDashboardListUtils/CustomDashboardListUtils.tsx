import React from 'react';
import Moment from 'react-moment';
import { DataTableColumn } from '@kogito-apps/components-common/dist/components/DataTable';
import { CustomDashboardInfo } from '../../../api/CustomDashboardListEnvelopeApi';

export const getDashboardNameColumn = (
  selectDashboard: (customDashboardInfo: CustomDashboardInfo) => void
): DataTableColumn => {
  return {
    label: 'Name',
    path: 'name',
    bodyCellTransformer: (cellValue, rowDashboard: CustomDashboardInfo) => {
      return (
        <a onClick={() => selectDashboard(rowDashboard)}>
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
