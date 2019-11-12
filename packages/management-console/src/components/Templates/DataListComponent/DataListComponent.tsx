import { useQuery } from '@apollo/react-hooks';
import { Breadcrumb, BreadcrumbItem, Card, DataList, Grid, GridItem, PageSection, Bullseye } from '@patternfly/react-core';
import gql from 'graphql-tag';
import _ from 'lodash';
import React, { useEffect, useState } from 'react';
import { Link } from "react-router-dom";
import ScrollArea from 'react-scrollbar';
import DataListItemComponent from '../../Molecules/DataListItemComponent/DataListItemComponent';
import DataListTitleComponent from '../../Molecules/DataListTitleComponent/DataListTitleComponent';
import DataListToolbarComponent from '../../Molecules/DataListToolbarComponent/DataListToolbarComponent';
import { Spinner } from '@patternfly/react-core/dist/esm/experimental';
import EmptyStateComponent from '../../Atoms/EmptyStateComponent/EmptyState';

const DataListComponent: React.FC<{}> = () => {
  const [isActiveChecked, setIsActiveChecked] = useState<boolean>(false);
  const [isCompletedChecked, setIsCompletedChecked] = useState<boolean>(false);
  const [isAbortChecked, setisAbortChecked] = useState<boolean>(false);
  const [initData, setInitData] = useState<any>([]);
  const [checkedArray, setCheckedArray] = useState<any>([]);
  const [filterArray, setFilterArray] = useState<any>([]);
  /* tslint:disable:no-string-literal */
  const GET_INSTANCES = gql`
    query getInstances($parentProcessId: [String]) {
      ProcessInstances(filter: { parentProcessInstanceId: $parentProcessId }) {
        id
        processId
        processName
        parentProcessInstanceId
        roles
        state
        start
        childProcessInstanceId
      }
    }
  `;
  const { loading, error, data } = useQuery(GET_INSTANCES, {
    variables: {
      parentProcessId: [null]
    },
    fetchPolicy: 'network-only'
  });

  useEffect(() => {
    setInitData(data);
    setFilterArray(data);
  }, [data]);

  const handleChange = (checked, event) => {
    if (event.target.name === 'isActiveChecked') {
      setIsActiveChecked(isActiveChecked ? false : true);
      if (!isActiveChecked === true) {
        setCheckedArray([...checkedArray, 'ACTIVE']);
      } else if (!isActiveChecked === false) {
        const tempArr = checkedArray.slice();
        _.remove(tempArr, _temp => {
          return _temp === 'ACTIVE';
        });
        setCheckedArray(tempArr);
      }
    }

    if (event.target.name === 'isCompletedChecked') {
      setIsCompletedChecked(isCompletedChecked ? false : true);
      if (!isCompletedChecked === true) {
        setCheckedArray([...checkedArray, 'COMPLETED']);
      } else if (!isCompletedChecked === false) {
        const tempArr = checkedArray.slice();
        _.remove(tempArr, _temp => {
          return _temp === 'COMPLETED';
        });
        setCheckedArray(tempArr);
      }
    }
    if (event.target.name === 'isAbortChecked') {
      setisAbortChecked(isAbortChecked ? false : true);
      if (!isAbortChecked === true) {
        setCheckedArray([...checkedArray, 'ABORTED']);
      } else if (!isAbortChecked === false) {
        const tempArr = checkedArray.slice();
        _.remove(tempArr, _temp => {
          return _temp === 'ABORTED';
        });
        setCheckedArray(tempArr);
      }
    }
  };

  const onFilterClick = () => {
    const tempArr = [];
    checkedArray.map(check => {
      initData['ProcessInstances'].map(_data => {
        if (_data.state.toString().toLowerCase() === check.toString().toLowerCase()) {
          tempArr.push(_data);
        }
      });
    });
    const processInstanceObject = { ProcessInstances: tempArr };
    setFilterArray(processInstanceObject);
  };

  const removeChecked = id => {
    if (id.toString().toLowerCase() === 'active') {
      setIsActiveChecked(false);
    } else if (id.toString().toLowerCase() === 'completed') {
      setIsCompletedChecked(false);
    } else if (id.toString().toLowerCase() === 'aborted') {
      setisAbortChecked(false);
    }
  };

  if (loading) return <Bullseye><Spinner /> &nbsp;&nbsp;&nbsp; Loading</Bullseye>;
  if (error) return <p>oops.. some error</p>;

  return (
    <React.Fragment>
      <PageSection variant="light">
        <DataListTitleComponent />
        <Breadcrumb>
          <BreadcrumbItem><Link to={'/'}>Home</Link></BreadcrumbItem>
          <BreadcrumbItem isActive>Process Instances</BreadcrumbItem>
        </Breadcrumb>
      </PageSection>
      <PageSection>
        <Grid gutter="md">
          <GridItem span={12}>
            <Card className="dataList">
              <DataListToolbarComponent
                isActive={isActiveChecked}
                isComplete={isCompletedChecked}
                isAborted={isAbortChecked}
                handleChange={handleChange}
                checkedArray={checkedArray}
                filterClick={onFilterClick}
                removeCheck={removeChecked}
              />
              {
                data.ProcessInstances.length > 0 && (
                  <DataList aria-label="Expandable data list example">
                    <ScrollArea smoothScrolling={true} className="scrollArea">
                      {
                        !loading &&
                        filterArray !== undefined &&
                        filterArray['ProcessInstances'].map((item, index) => {
                          return (
                            <DataListItemComponent
                              id={index}
                              key={index}
                              instanceState={item.state}
                              instanceID={item.id}
                              processID={item.processId}
                              parentInstanceID={item.parentProcessInstanceId}
                              processName={item.processName}
                              start={item.start}
                              childListArray={item.childProcessInstanceId}
                            />
                          );
                        })
                      }
                      {
                        !loading && filterArray !== undefined && filterArray['ProcessInstances'].length === 0 && <Bullseye><EmptyStateComponent /></Bullseye>
                      }
                      {
                        loading && (<Bullseye><Spinner /> &nbsp;&nbsp;&nbsp; Loading</Bullseye>)
                      }
                    </ScrollArea>
                  </DataList>)
              }
            </Card>
          </GridItem>
        </Grid>
      </PageSection>
    </React.Fragment>
  );
};

export default DataListComponent;
