import { useApolloClient } from '@apollo/react-hooks';
import {
  Breadcrumb,
  BreadcrumbItem,
  Card,
  Grid,
  GridItem,
  PageSection,
  Button
} from '@patternfly/react-core';
import gql from 'graphql-tag';
import _ from 'lodash';
import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import DataListTitleComponent from '../../Molecules/DataListTitleComponent/DataListTitleComponent';
import DataToolbarComponent from '../../Molecules/DataToolbarComponent/DataToolbarComponent';
import './DataList.css';
import DataListComponent from '../../Organisms/DataListComponent/DataListComponent';
import EmptyStateComponent from '../../Atoms/EmptyStateComponent/EmptyStateComponent';
import axios from 'axios';

const DataListContainer: React.FC<{}> = () => {
  const [initData, setInitData] = useState<any>([]);
  const [checkedArray, setCheckedArray] = useState<any>(['ACTIVE']);
  const [isLoading, setIsLoading] = useState(false);
  const [isError, setIsError] = useState(false);
  const [isStatusSelected, setIsStatusSelected] = useState(true);
  const [filters, setFilters] = useState(checkedArray);
  const [abortedArray, setAbortedArray] = useState([]);
  const [completedAndAbortedArray, setCompletedAndAbortedArray] = useState([]);
  const client = useApolloClient();

  /* tslint:disable:no-string-literal */
  const GET_INSTANCES = gql`
    query getInstances($state: [ProcessInstanceState!]) {
      ProcessInstances(
        where: {
          parentProcessInstanceId: { isNull: true }
          state: { in: $state }
        }
      ) {
        id
        processId
        processName
        parentProcessInstanceId
        rootProcessInstanceId
        roles
        state
        start
        lastUpdate
        addons
        endpoint
        error {
          nodeDefinitionId
          message
        }
      }
    }
  `;

  const onFilterClick = async (arr = checkedArray) => {
    setIsLoading(true);
    setIsError(false);
    setIsStatusSelected(true);
    setAbortedArray([]);
    await client
      .query({
        query: GET_INSTANCES,
        variables: {
          state: arr
        },
        fetchPolicy: 'network-only'
      })
      .then(result => {
        setIsLoading(result.loading);
        setAbortedArray([]);
        if (!result.loading) {
          result.data.ProcessInstances.map(instance => {
            instance.isChecked = false;
          });
        }
        setInitData(result.data);
      });
  };

  const handleAbortAll = () => {
    const completedAndAborted = [];
    const tempAbortedArray = [...abortedArray];
    tempAbortedArray.map(abortedInstances => {
      initData.ProcessInstances.map(instance => {
        if (instance.id === abortedInstances) {
          if (instance.state === 'COMPLETED' || instance.state === 'ABORTED') {
            completedAndAborted.push(abortedInstances);
          }
        }
        if (instance.childDataList !== undefined) {
          instance.childDataList.map(child => {
            if (child.id === abortedInstances) {
              if (child.state === 'COMPLETED' || child.state === 'ABORTED') {
                completedAndAborted.push(abortedInstances);
              }
            }
          });
        }
      });
    });
    if (completedAndAborted.length > 0) {
      alert(
        `sorry the following proccesses are skiped since they are either completed or aborted ${[
          ...completedAndAborted
        ]}`
      );
    }

    const filteredAbortedArray = tempAbortedArray.filter(
      val => !completedAndAborted.includes(val)
    );
    const endpoint = initData.ProcessInstances[0].endpoint;
    axios
      .post(
        `${endpoint}/management/processes/instances/${filteredAbortedArray}/abortAll`
      )
      .then(result => {
        result = result;
      })
      .catch(err => {
        err = err;
      });
  };

  const BreadcrumbStyle = {
    paddingBottom: '20px'
  };

  return (
    <React.Fragment>
      <PageSection variant="light">
        <DataListTitleComponent />
        <Breadcrumb>
          <BreadcrumbItem>
            <Link to={'/'}>Home</Link>
          </BreadcrumbItem>
          <BreadcrumbItem isActive>Process instances</BreadcrumbItem>
        </Breadcrumb>
      </PageSection>
      <PageSection>
        <Grid gutter="md">
          <GridItem span={12}>
            <Card className="dataList">
              {!isError && (
                <>
                  <DataToolbarComponent
                    checkedArray={checkedArray}
                    filterClick={onFilterClick}
                    setCheckedArray={setCheckedArray}
                    setIsStatusSelected={setIsStatusSelected}
                    filters={filters}
                    setFilters={setFilters}
                    setSelectedInstances={setIsStatusSelected}
                  />
                  {abortedArray.length !== 0 ? (
                    <div className="pf-u-pr-sm pf-u-pb-sm">
                      <Button
                        className="pf-u-float-right"
                        onClick={handleAbortAll}
                      >
                        Abort all
                      </Button>
                    </div>
                  ) : (
                    <div className="pf-u-pr-sm pf-u-pb-sm">
                      <Button
                        variant="primary"
                        className="pf-u-float-right"
                        isDisabled
                      >
                        Abort all
                      </Button>
                    </div>
                  )}
                </>
              )}
              {isStatusSelected ? (
                <DataListComponent
                  initData={initData}
                  setInitData={setInitData}
                  isLoading={isLoading}
                  setIsLoading={setIsLoading}
                  setIsError={setIsError}
                  abortedArray={abortedArray}
                  setAbortedArray={setAbortedArray}
                  completedAndAbortedArray={completedAndAbortedArray}
                  setCompletedAndAbortedArray={setCompletedAndAbortedArray}
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
