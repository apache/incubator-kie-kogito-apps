import { useQuery } from '@apollo/react-hooks';
import {
  Breadcrumb,
  BreadcrumbItem,
  Card,
  DataList,
  Grid,
  GridItem,
  PageSection,
  TextContent,
  TextVariants,
  Text
} from '@patternfly/react-core';
import gql from 'graphql-tag';
import _ from 'lodash';
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import ScrollArea from 'react-scrollbar';
import DataListItemComponent from '../../Molecules/DataListItemComponent/DataListItemComponent';
import DataListTitleComponent from '../../Molecules/DataListTitleComponent/DataListTitleComponent';
import DataToolbarComponent from '../../Molecules/DataToolbarComponent/DataToolbarComponent';
import SpinnerComponent from '../../Atoms/SpinnerComponent/SpinnerComponent';
import './DataList.css';

const DataListComponent: React.FC<{}> = () => {
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

  if (loading) return <SpinnerComponent />;
  if (error) return <p>oops.. some error</p>;

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
                      <BreadcrumbItem isActive>Process Instances</BreadcrumbItem>
                    
        </Breadcrumb>
                
      </PageSection>
              
      <PageSection>
                  
        <Grid gutter="md">
                      
          <GridItem span={12}>
                          
            <Card className="dataList">
                              
              {data.ProcessInstances.length > 0 ? (
                <>
                   
                  <div className="align-toolbar">
                    <DataToolbarComponent
                      checkedArray={checkedArray}
                      filterClick={onFilterClick}
                      setCheckedArray={setCheckedArray}
                    />
                  </div>
                                                  
                  <DataList aria-label="Expandable data list example">
                                      
                    <ScrollArea smoothScrolling={true} className="scrollArea">
                                          
                      {!loading &&
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
                            />
                          );
                        })}
                                          
                      {loading && (
                        <div className="spinner-center">
                          <SpinnerComponent />
                        </div>
                      )}
                                        
                    </ScrollArea>
                                    
                  </DataList>
                   
                </>
              ) : (
                <div className="error-text">
                                    
                  <TextContent>
                                        <Text component={TextVariants.h6}>No data to display</Text>
                                      
                  </TextContent>
                                    
                </div>
              )}
                            
            </Card>
                        
          </GridItem>
                    
        </Grid>
                
      </PageSection>
            
    </React.Fragment>
  );
};

export default DataListComponent;
