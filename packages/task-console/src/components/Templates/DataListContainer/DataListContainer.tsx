import {
  Breadcrumb,
  BreadcrumbItem,
  Card,
  Grid,
  GridItem,
  PageSection
} from '@patternfly/react-core';
import _ from 'lodash';
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import PageTitleComponent from '../../Molecules/PageTitleComponent/PageTitleComponent';
import DataToolbarComponent from '../../Molecules/DataToolbarComponent/DataToolbarComponent';
import './DataList.css';
import DataListComponent from '../../Organisms/DataListComponent/DataListComponent';
import EmptyStateComponent from '../../Atoms/EmptyStateComponent/EmptyStateComponent';
import { useGetUserTasksByStatesLazyQuery } from '../../../graphql/types';

const DataListContainer: React.FC<{}> = () => {
  const [initData, setInitData] = useState<any>([]);
  const [checkedArray, setCheckedArray] = useState<any>(['Ready']);
  const [isLoading, setIsLoading] = useState(false);
  const [isError, setIsError] = useState(false);
  const [isStatusSelected, setIsStatusSelected] = useState(true);
  const [filters, setFilters] = useState(checkedArray);

  const [
    getProcessInstances,
    { loading, data }
  ] = useGetUserTasksByStatesLazyQuery({ fetchPolicy: 'network-only' });

  const onFilterClick = async (arr = checkedArray) => {
    setIsLoading(true);
    setIsError(false);
    setIsStatusSelected(true);
    getProcessInstances({ variables: { state: arr } });
  };

  useEffect(() => {
    setIsLoading(loading);
    setInitData(data);
  }, [data]);

  return (
    <React.Fragment>
      <PageSection variant="light">
        <PageTitleComponent title="User Tasks" />
        <Breadcrumb>
          <BreadcrumbItem>
            <Link to={'/'}>Home</Link>
          </BreadcrumbItem>
          <BreadcrumbItem isActive>User Tasks</BreadcrumbItem>
        </Breadcrumb>
      </PageSection>
      <PageSection>
        <Grid gutter="md">
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
                <DataListComponent
                  initData={initData}
                  setInitData={setInitData}
                  isLoading={isLoading}
                  setIsLoading={setIsLoading}
                  setIsError={setIsError}
                />
              ) : (
                <EmptyStateComponent
                  iconType="warningTriangleIcon1"
                  title="No status is selected"
                  body="Try selecting at least one status to see results"
                  filterClick={onFilterClick}
                  setFilters={setFilters}
                  setCheckedArray={setCheckedArray}
                />
              )}
            </Card>
          </GridItem>
        </Grid>
      </PageSection>
    </React.Fragment>
  );
};

export default DataListContainer;
