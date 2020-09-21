/**
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import React, { useEffect, useState } from 'react';
import { Bullseye } from '@patternfly/react-core';
import {
  Table,
  TableHeader,
  TableBody,
  IRow,
  ITransform,
  ICell,
  sortable
} from '@patternfly/react-table';
import KogitoSpinner from '../../Atoms/KogitoSpinner/KogitoSpinner';
import {
  KogitoEmptyState,
  KogitoEmptyStateType
} from '../../Atoms/KogitoEmptyState/KogitoEmptyState';
import '@patternfly/patternfly/patternfly-addons.css';
import _ from 'lodash';
import uuidv4 from 'uuid';
import jp from 'jsonpath';
import { OUIAProps, componentOuiaProps } from '../../../utils/OuiaUtils';

export interface DataTableColumn {
  path: string;
  label: string;
  bodyCellTransformer?: (value: any, rowDataObj?: any) => any;
  isSortable?: boolean;
}
interface IOwnProps {
  data: any[];
  isLoading: boolean;
  columns?: DataTableColumn[];
  networkStatus: any;
  error: any;
  refetch: () => void;
  LoadingComponent?: React.ReactNode;
  ErrorComponent?: React.ReactNode;
  sortBy?: object;
  onSorting?: (index: number, direction: string) => void;
}

const getCellData = (dataObj: object, path: string) => {
  if (dataObj && path) {
    return !_.isEmpty(jp.value(dataObj, path))
      ? jp.value(dataObj, path)
      : 'N/A';
  } else {
    return 'N/A';
  }
};

const getColumns = (data: any[], columns: DataTableColumn[]) => {
  let columnList: ICell[] = [];
  if (data) {
    columnList = columns
      ? _.filter(columns, column => !_.isEmpty(column.path)).map(column => {
          return {
            title: column.label,
            data: column.path,
            cellTransforms: column.bodyCellTransformer
              ? [
                  ((value, extra) => {
                    const rowDataObj = data[extra.rowIndex];
                    return {
                      children: column.bodyCellTransformer(
                        value.title,
                        rowDataObj
                      )
                    };
                  }) as ITransform
                ]
              : undefined,
            transforms: column.isSortable ? [sortable] : undefined
          } as ICell;
        })
      : _.filter(_.keys(_.sample(data)), key => key !== '__typename').map(
          key => ({ title: key, data: `$.${key}` } as ICell)
        );
  }
  return columnList;
};

const getRows = (data: any[], columns: ICell[]) => {
  let rowList: IRow[] = [];
  if (data) {
    rowList = data.map(rowData => {
      return {
        cells: _.reduce(
          columns,
          (result, column: ICell) => {
            if (column.data) {
              result.push(getCellData(rowData, column.data));
            }
            return result;
          },
          []
        ),
        rowKey: uuidv4() // This is a walkaround to bypass the "id" cannot be included in "columns" issue
      };
    });
  }
  return rowList;
};

const DataTable: React.FC<IOwnProps & OUIAProps> = ({
  data,
  isLoading,
  columns,
  networkStatus,
  error,
  LoadingComponent,
  ErrorComponent,
  refetch,
  sortBy,
  onSorting,
  ouiaId,
  ouiaSafe
}) => {
  const [rows, setRows] = useState<IRow[]>([]);
  const [columnList, setColumnList] = useState<ICell[]>([]);

  useEffect(() => {
    if (!_.isEmpty(data)) {
      setColumnList(getColumns(data, columns));
    }
  }, [data]);

  useEffect(() => {
    if (!_.isEmpty(data) && !_.isEmpty(columnList)) {
      setRows(getRows(data, columnList));
    }
  }, [columnList]);

  const onSort = (event, index, direction) => {
    if (_.isFunction(onSorting)) {
      onSorting(index, direction);
    }
  };

  if (isLoading) {
    return LoadingComponent ? (
      <React.Fragment>{LoadingComponent}</React.Fragment>
    ) : (
      <Bullseye>
        <KogitoSpinner spinnerText="Loading..." />
      </Bullseye>
    );
  }

  if (networkStatus === 4) {
    return LoadingComponent ? (
      <React.Fragment>{LoadingComponent}</React.Fragment>
    ) : (
      <Bullseye>
        <KogitoSpinner spinnerText="Loading..." />
      </Bullseye>
    );
  }

  if (error) {
    return ErrorComponent ? (
      <React.Fragment>{ErrorComponent}</React.Fragment>
    ) : (
      <div className=".pf-u-my-xl">
        <KogitoEmptyState
          type={KogitoEmptyStateType.Refresh}
          title="Oops... error while loading"
          body="Try using the refresh action to reload user tasks"
          onClick={refetch}
        />
      </div>
    );
  }

  return (
    <React.Fragment>
      {data !== undefined &&
        !isLoading &&
        rows.length > 0 &&
        columnList.length > 0 && (
          <Table
            aria-label="Data Table"
            cells={columnList}
            rows={rows}
            sortBy={sortBy}
            onSort={onSort}
            {...componentOuiaProps(
              ouiaId,
              'data-table',
              ouiaSafe ? ouiaSafe : !isLoading
            )}
          >
            <TableHeader />
            <TableBody rowKey="rowKey" />
          </Table>
        )}
      {data !== undefined && !isLoading && rows.length === 0 && (
        <KogitoEmptyState
          type={KogitoEmptyStateType.Search}
          title="No results found"
          body="Try using different filters"
        />
      )}
    </React.Fragment>
  );
};

export default DataTable;
