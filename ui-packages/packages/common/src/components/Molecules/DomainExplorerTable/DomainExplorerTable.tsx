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
import React, { useState, useEffect } from 'react';
import {
  Table,
  TableHeader,
  TableBody,
  TableVariant,
  sortable
} from '@patternfly/react-table';
import {
  Title,
  EmptyState,
  EmptyStateIcon,
  EmptyStateBody,
  Bullseye,
  Card,
  CardBody,
  Button
} from '@patternfly/react-core';
import {
  CheckCircleIcon,
  BanIcon,
  OnRunningIcon,
  SearchIcon,
  ErrorCircleOIcon,
  PausedIcon,
  HistoryIcon
} from '@patternfly/react-icons';
import Moment from 'react-moment';
import { Link } from 'react-router-dom';
import '../../styles.css';
import ItemDescriptor from '../ItemDescriptor/ItemDescriptor';
import KogitoSpinner from '../../Atoms/KogitoSpinner/KogitoSpinner';
import EndpointLink from '../../Atoms/EndpointLink/EndpointLink';
import { GraphQL } from '../../../graphql/types';
import ProcessInstanceState = GraphQL.ProcessInstanceState;
import ServerErrors from '../ServerErrors/ServerErrors';
import {
  KogitoEmptyState,
  KogitoEmptyStateType
} from '../../Atoms/KogitoEmptyState/KogitoEmptyState';
import { set } from '../../../utils/Utils';
import { OUIAProps, componentOuiaProps } from '../../../utils/OuiaUtils';

interface RowContent {
  cells: string[] | object[];
  parent?: number;
  isOpen?: boolean;
  rowKey: string;
}
interface IOwnProps {
  columnFilters: any[];
  displayTable: boolean;
  displayEmptyState: boolean;
  filterError: string;
  finalFilters: object;
  filterChips: string[];
  handleRetry: () => void;
  isLoadingMore: boolean;
  offset: number;
  onDeleteChip: (type: string) => void;
  parameters: object[];
  rows: RowContent[];
  selected: string[];
  setOrderByObj: (orderObj: object) => void;
  setRows: (rows: ((row: object[]) => object[]) | object[]) => void;
  setRunQuery: (runQuery: boolean) => void;
  setSortBy: (sortBy: object) => void;
  sortBy: object;
  tableLoading: boolean;
}
const DomainExplorerTable: React.FC<IOwnProps & OUIAProps> = ({
  columnFilters,
  displayTable,
  displayEmptyState,
  filterError,
  finalFilters,
  filterChips,
  handleRetry,
  isLoadingMore,
  offset,
  onDeleteChip,
  parameters,
  rows,
  selected,
  setRows,
  setOrderByObj,
  setRunQuery,
  setSortBy,
  sortBy,
  tableLoading,
  ouiaId,
  ouiaSafe
}) => {
  // tslint:disable: forin
  const [columns, setColumns] = useState([]);
  const currentPage = { prev: location.pathname };
  window.localStorage.setItem('state', JSON.stringify(currentPage));

  const stateIcon = state => {
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
      case ProcessInstanceState.Pending:
        return (
          <>
            <PausedIcon className="pf-u-mr-sm" />
            Pending
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

  const getKeys = object => {
    const iter = (data, k = '') => {
      for (const i in data) {
        const rest = k.length ? ' / ' + i : i;
        if (data[i] === null) {
          !tempKeys.includes(k + rest) &&
            tempKeys.push({ title: k + rest, transforms: [sortable] });
          if (rest.hasOwnProperty) {
            tempValue.push(data[i]);
          }
        }
        if (typeof data[i] === 'object') {
          if (!Array.isArray(data[i])) {
            iter(data[i], k + rest);
          }
        } else {
          if (rest !== '__typename' && !rest.match('/ __typename')) {
            !tempKeys.includes(k + rest) &&
              tempKeys.push({ title: k + rest, transforms: [sortable] });
            if (rest.hasOwnProperty) {
              tempValue.push(data[i].toString());
            }
          }
        }
      }
    };
    const tempKeys = [];
    const tempValue = [];
    iter(object);
    return { tempKeys, tempValue };
  };

  const getChildKeys = object => {
    const iter = (data, k = '') => {
      for (const i in data) {
        const rest = k.length ? ' / ' + i : i;
        if (data[i] === null) {
          !tempKeys.includes(k + rest) && tempKeys.push(k + rest);
          if (rest.hasOwnProperty) {
            tempValue.push(data[i]);
          }
        }
        if (typeof data[i] === 'object') {
          if (!Array.isArray(data[i])) {
            iter(data[i], k + rest);
          }
        } else {
          if (rest !== '__typename' && !rest.match('/ __typename')) {
            !tempKeys.includes(k + rest) && tempKeys.push(k + rest);
            if (rest.hasOwnProperty) {
              if (rest === 'processName') {
                const tempObj = {
                  id: data.id,
                  processName: data.processName,
                  businessKey: data.businessKey,
                  serviceUrl: data.serviceUrl
                };
                const ele = {
                  title: (
                    <>
                      <Link
                        to={{
                          pathname: '/Process/' + tempObj.id,
                          state: {
                            parameters,
                            selected,
                            finalFilters,
                            filterChips
                          }
                        }}
                      >
                        <strong>
                          <ItemDescriptor
                            itemDescription={{
                              id: tempObj.id,
                              name: tempObj.processName,
                              description: tempObj.businessKey
                            }}
                          />
                        </strong>
                      </Link>
                      <div>
                        <EndpointLink
                          serviceUrl={tempObj.serviceUrl}
                          isLinkShown={false}
                        />
                      </div>
                    </>
                  )
                };
                tempValue.push(ele);
              } else if (rest === 'state') {
                const ele = {
                  title: stateIcon(data[i].toString())
                };
                tempValue.push(ele);
              } else if (rest === 'start') {
                const ele = {
                  title: <Moment fromNow>{data[i].toString()}</Moment>
                };
                tempValue.push(ele);
              } else if (rest === 'lastUpdate') {
                const ele = {
                  title: (
                    <>
                      <HistoryIcon className="pf-u-mr-sm" /> Updated{' '}
                      <Moment fromNow>{data[i].toString()}</Moment>
                    </>
                  )
                };
                tempValue.push(ele);
              }
            }
          }
        }
      }
    };
    const tempKeys = [];
    let tempValue = [];
    iter(object);
    tempValue = tempValue.filter(value => value !== null);
    return { tempKeys, tempValue };
  };
  const tableContent = columnFilters;

  const parentkeys = [];
  let values = [];
  let parentIndex;

  const initLoad = () => {
    if (columnFilters.length > 0) {
      columnFilters.map(item => {
        let metaArray = [];
        const metaKeys = [];
        const metaValues = [];
        if (item.metadata.hasOwnProperty('processInstances')) {
          metaArray = item.metadata.processInstances;
        }
        const tempParents = getKeys(item);
        parentkeys.push(tempParents.tempKeys);
        values.push({
          isOpen: false,
          cells: tempParents.tempValue,
          rowKey: Math.random().toString()
        });
        metaArray.map(data => {
          const tempMeta = getChildKeys(data);
          metaKeys.push(tempMeta.tempKeys);
          metaValues.push({
            cells: tempMeta.tempValue,
            rowKey: Math.random().toString()
          });
        });
        const finalMetaKeys = ['Process name', 'State', 'Start', 'Last update'];
        const innerTable = [
          {
            parent: parentIndex,
            rowKey: Math.random().toString(),
            cells: [
              {
                title: (
                  <Table
                    aria-label="Process Instances"
                    variant={TableVariant.compact}
                    cells={finalMetaKeys}
                    rows={metaValues}
                    className="kogito-common__embedded-table"
                  >
                    <TableHeader />
                    <TableBody />
                  </Table>
                )
              }
            ]
          }
        ];
        values = values.concat(innerTable);
        parentIndex = parentIndex + 2;
      });
      const rowObject: any = {};
      if (tableLoading && !isLoadingMore) {
        rowObject.cells = [
          {
            props: { colSpan: 8 },
            title: (
              <Bullseye>
                <KogitoSpinner spinnerText="Loading domain explorer" />
              </Bullseye>
            )
          }
        ];
        values.push(rowObject);
      }
    }
    const finalKeys = parentkeys[0];
    finalKeys && setColumns([...finalKeys]);
    if (offset > 0) {
      setRows(prev => [...prev, ...values]);
    } else {
      setRows([...values]);
    }
  };

  useEffect(() => {
    if (offset === 0) {
      parentIndex = 0;
    } else {
      const lastObj = rows[rows.length - 1];
      parentIndex = lastObj.parent + 2;
    }
    initLoad();
  }, [tableContent]);

  const onCollapse = (event, rowKey, isOpen) => {
    rows[rowKey].isOpen = isOpen;
    setRows([...rows]);
  };

  const onSort = (event, index, direction) => {
    setSortBy({ index, direction });
    const sortingColumn = event.target.innerText.replace(' / ', ',');
    const obj = {};
    set(obj, sortingColumn, direction.toUpperCase());
    setOrderByObj(obj);
    setRunQuery(true);
  };
  return (
    <React.Fragment>
      {displayTable && !displayEmptyState && columns.length && (
        <Table
          cells={columns}
          rows={rows}
          sortBy={sortBy}
          onSort={onSort}
          aria-label="Domain Explorer Table"
          className="kogito-common--domain-explorer__table"
          onCollapse={onCollapse}
          {...componentOuiaProps(
            ouiaId,
            'domain-explorer-table',
            ouiaSafe ? ouiaSafe : !tableLoading && !isLoadingMore
          )}
        >
          <TableHeader />
          <TableBody rowKey="rowKey" />
        </Table>
      )}
      {!displayTable && (
        <Card component={'div'}>
          <CardBody>
            {!displayEmptyState &&
              !filterError &&
              filterChips.length > 0 &&
              selected.length === 0 && (
                <Bullseye>
                  <EmptyState>
                    <EmptyStateIcon icon={SearchIcon} />
                    <Title headingLevel="h5" size="lg">
                      No columns selected
                    </Title>
                    <EmptyStateBody>
                      <Button
                        variant="link"
                        id="retry-columns-button"
                        onClick={handleRetry}
                        isInline
                      >
                        Manage columns
                      </Button>{' '}
                      to see content
                    </EmptyStateBody>
                  </EmptyState>
                </Bullseye>
              )}
            {displayEmptyState && (
              <Bullseye>
                <EmptyState>
                  <EmptyStateIcon icon={SearchIcon} />
                  <Title headingLevel="h5" size="lg">
                    No data available
                  </Title>
                  <EmptyStateBody>
                    Selected filters have no data to display. Try other filters.
                  </EmptyStateBody>
                </EmptyState>
              </Bullseye>
            )}
            {!displayEmptyState && !displayTable && filterError && (
              <ServerErrors error={filterError} variant="small" />
            )}
            {filterChips.length === 0 && (
              <KogitoEmptyState
                type={KogitoEmptyStateType.Reset}
                title="No filter applied."
                body="Try applying at least one filter to see results"
                onClick={() => onDeleteChip('')}
              />
            )}
          </CardBody>
        </Card>
      )}
    </React.Fragment>
  );
};

export default DomainExplorerTable;
