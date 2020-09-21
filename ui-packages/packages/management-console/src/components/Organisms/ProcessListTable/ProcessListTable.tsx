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
import { DataList, Bullseye } from '@patternfly/react-core';
import {
  ServerErrors,
  GraphQL,
  KogitoEmptyState,
  KogitoEmptyStateType,
  KogitoSpinner,
  OUIAProps,
  componentOuiaProps
} from '@kogito-apps/common';
import '../../Templates/ProcessListPage/ProcessListPage.css';
import ProcessListTableItems from '../../Molecules/ProcessListTableItems/ProcessListTableItems';
import '@patternfly/patternfly/patternfly-addons.css';
import './ProcessListTable.css';
import ProcessInstanceState = GraphQL.ProcessInstanceState;
import { ProcessInstanceBulkList } from '../../Molecules/ProcessListToolbar/ProcessListToolbar';
/* tslint:disable:no-string-literal */
type filterType = {
  status: GraphQL.ProcessInstanceState[];
  businessKey: string[];
};
interface IOwnProps {
  setInitData: any;
  setLimit: (limit: number) => void;
  initData: any;
  isLoading: boolean;
  setIsError: (isError: boolean) => void;
  selectedInstances: ProcessInstanceBulkList;
  setSelectedInstances: (selectedInstances: ProcessInstanceBulkList) => void;
  pageSize: number;
  filters: filterType;
  setIsAllChecked: (isAllChecked: boolean) => void;
  selectedNumber: number;
  setSelectedNumber: (selectedNumber: number) => void;
}

const ProcessListTable: React.FC<IOwnProps & OUIAProps> = ({
  initData,
  setInitData,
  setLimit,
  isLoading,
  setIsError,
  selectedInstances,
  setSelectedInstances,
  pageSize,
  filters,
  setIsAllChecked,
  selectedNumber,
  setSelectedNumber,
  ouiaId,
  ouiaSafe
}) => {
  const [checkedArray, setCheckedArray] = useState<ProcessInstanceState[]>(
    filters.status
  );
  useEffect(() => {
    setCheckedArray(filters.status);
  }, [filters]);

  const queryVariables = {
    state: { in: checkedArray },
    parentProcessInstanceId: { isNull: true }
  };

  const searchWordsArray = [];
  if (filters.businessKey.length > 0) {
    filters.businessKey.forEach((word: string) =>
      searchWordsArray.push({ businessKey: { like: word } })
    );
    queryVariables['or'] = searchWordsArray;
  }

  const { loading, error, data } = GraphQL.useGetProcessInstancesQuery({
    variables: {
      where: queryVariables,
      offset: 0,
      limit: pageSize
    },
    fetchPolicy: 'network-only'
  });

  useEffect(() => {
    setIsError(false);
    setSelectedInstances({});
    if (!loading && data !== undefined) {
      data.ProcessInstances.forEach((instance: any) => {
        instance.isChecked = false;
        instance.isOpen = false;
      });
      setLimit(data.ProcessInstances.length);
    }
    setInitData(data);
  }, [data]);

  if (loading || isLoading) {
    return (
      <Bullseye>
        <KogitoSpinner spinnerText="Loading process instances..." />
      </Bullseye>
    );
  }

  if (error) {
    setIsError(true);
    return <ServerErrors error={error} variant="large" />;
  }

  return (
    <DataList
      aria-label="Process instance list"
      {...componentOuiaProps(ouiaId, 'process-list-table', ouiaSafe)}
    >
      {!loading &&
        initData !== undefined &&
        initData.ProcessInstances.map((item, index) => {
          return (
            <ProcessListTableItems
              id={index}
              key={item.id}
              processInstanceData={item}
              initData={initData}
              setInitData={setInitData}
              loadingInitData={loading}
              selectedInstances={selectedInstances}
              setSelectedInstances={setSelectedInstances}
              setIsAllChecked={setIsAllChecked}
              selectedNumber={selectedNumber}
              setSelectedNumber={setSelectedNumber}
              filters={filters}
            />
          );
        })}
      {initData !== undefined &&
        !isLoading &&
        initData.ProcessInstances.length === 0 && (
          <KogitoEmptyState
            type={KogitoEmptyStateType.Search}
            title="No results found"
            body="Try using different filters"
          />
        )}
    </DataList>
  );
};

export default ProcessListTable;
