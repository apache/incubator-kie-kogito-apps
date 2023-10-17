/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
  sortable,
  ISortBy
} from '@patternfly/react-table';
import KogitoSpinner from '../../Atoms/KogitoSpinner/KogitoSpinner';
import {
  KogitoEmptyState,
  KogitoEmptyStateType
} from '../../Atoms/KogitoEmptyState/KogitoEmptyState';
import '@patternfly/patternfly/patternfly-addons.css';
import isEmpty from 'lodash/isEmpty';
import filter from 'lodash/filter';
import sample from 'lodash/sample';
import keys from 'lodash/keys';
import get from 'lodash/get';
import reduce from 'lodash/reduce';
import isFunction from 'lodash/isFunction';
import uuidv4 from 'uuid';
import { OUIAProps, componentOuiaProps } from '@kogito-apps/ouia-tools';

export interface DataTableColumn {
  path: string;
  label: string;
  bodyCellTransformer?: (
    value: any,
    rowDataObj: Record<string, unknown>
  ) => any;
  isSortable?: boolean;
}
interface IOwnProps {
  data: any[];
  isLoading: boolean;
  columns: DataTableColumn[];
  networkStatus: any;
  error: any;
  refetch: () => void;
  LoadingComponent?: React.ReactNode;
  ErrorComponent?: React.ReactNode;
  sortBy?: ISortBy;
  onSorting?: (index: number, direction: string) => void;
}

const getCellData = (dataObj: Record<string, unknown>, path: string) => {
  if (dataObj && path) {
    return get(dataObj, path) ?? 'N/A';
  } else {
    return 'N/A';
  }
};

const getColumns = (data: any[], columns: DataTableColumn[]) => {
  let columnList: ICell[] = [];
  if (data) {
    columnList = columns
      ? filter(columns, (column) => !isEmpty(column.path)).map((column) => {
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
      : filter(keys(sample(data)), (key) => key !== '__typename').map(
          (key) => ({ title: key, data: key } as ICell)
        );
  }
  return columnList;
};

const getRows = (data: any[], columns: ICell[]) => {
  let rowList: IRow[] = [];
  if (data) {
    rowList = data.map((rowData) => {
      return {
        cells: reduce(
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
    if (!isEmpty(data)) {
      const cols = getColumns(data, columns);
      if (!isEmpty(cols)) {
        setColumnList(cols);
        setRows(getRows(data, cols));
      }
    }
  }, [data]);

  const onSort = (event, index, direction) => {
    if (isFunction(onSorting)) {
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
      {data !== undefined && !isLoading && data.length === 0 && (
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
