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
import { Card, Grid, GridItem, PageSection } from '@patternfly/react-core';
import React, { useState, useEffect } from 'react';
import UserTaskPageHeader from '../../Molecules/UserTaskPageHeader/UserTaskPageHeader';
import DataToolbarComponent from '../../Molecules/DataListToolbar/DataListToolbar';
import './DataList.css';
import TaskList from '../../Organisms/TaskList/TaskList';
import {
  ouiaPageTypeAndObjectId,
  KogitoEmptyState,
  KogitoEmptyStateType,
  GraphQL,
  OUIAProps,
  componentOuiaProps
} from '@kogito-apps/common';

const DataListContainer: React.FC<OUIAProps> = ({ ouiaId, ouiaSafe }) => {
  const [initData, setInitData] = useState<any>([]);
  const [checkedArray, setCheckedArray] = useState<any>(['Ready']);
  const [isLoading, setIsLoading] = useState(false);
  const [isError, setIsError] = useState(false);
  const [isStatusSelected, setIsStatusSelected] = useState(true);
  const [filters, setFilters] = useState(checkedArray);

  const [
    getProcessInstances,
    { loading, data }
  ] = GraphQL.useGetUserTasksByStatesLazyQuery({ fetchPolicy: 'network-only' });

  const onFilterClick = (arr = checkedArray) => {
    setIsLoading(true);
    setIsError(false);
    setIsStatusSelected(true);
    getProcessInstances({ variables: { state: arr } });
  };

  useEffect(() => {
    setIsLoading(loading);
    setInitData(data);
  }, [data]);

  useEffect(() => {
    return ouiaPageTypeAndObjectId('user-tasks', 'true');
  });

  const resetClick = () => {
    setCheckedArray(['Ready']);
    setFilters({ ...filters, status: ['Ready'] });
    onFilterClick(['Ready']);
  };

  return (
    <React.Fragment>
      <div {...componentOuiaProps(ouiaId, 'DataListContainer', ouiaSafe)}>
        <UserTaskPageHeader />
        <PageSection>
          <Grid hasGutter md={1}>
            <GridItem span={12}>
              <Card className="dataList">
                {!isError && (
                  <DataToolbarComponent
                    checkedArray={checkedArray}
                    filterClick={onFilterClick}
                    setCheckedArray={setCheckedArray}
                    setIsStatusSelected={setIsStatusSelected}
                    filters={filters}
                    setFilters={setFilters}
                  />
                )}
                {isStatusSelected ? (
                  <TaskList
                    initData={initData}
                    setInitData={setInitData}
                    isLoading={isLoading}
                    setIsLoading={setIsLoading}
                    setIsError={setIsError}
                  />
                ) : (
                  <KogitoEmptyState
                    type={KogitoEmptyStateType.Reset}
                    title="No status is selected"
                    body="Try selecting at least one status to see results"
                    onClick={resetClick}
                  />
                )}
              </Card>
            </GridItem>
          </Grid>
        </PageSection>
      </div>
    </React.Fragment>
  );
};

export default DataListContainer;
