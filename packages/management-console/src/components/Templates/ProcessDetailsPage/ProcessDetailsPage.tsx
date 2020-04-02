import {
  Breadcrumb,
  BreadcrumbItem,
  Grid,
  GridItem,
  Page,
  PageSection,
  Title,
  Card,
  Bullseye
} from '@patternfly/react-core';
import React,{useState, useEffect} from 'react';
import { Link, Redirect } from 'react-router-dom';
import ProcessDetails from '../../Organisms/ProcessDetails/ProcessDetails';
import ProcessDetailsProcessDiagram from '../../Organisms/ProcessDetailsProcessDiagram/ProcessDetailsProcessDiagram';
import ProcessDetailsProcessVariables from '../../Organisms/ProcessDetailsProcessVariables/ProcessDetailsProcessVariables';
import ProcessDetailsTimeline from '../../Organisms/ProcessDetailsTimeline/ProcessDetailsTimeline';
import './ProcessDetailsPage.css';
import { useGetProcessInstanceByIdQuery } from '../../../graphql/types';
import ProcessDescriptor from '../../Molecules/ProcessDescriptor/ProcessDescriptor';
import SpinnerComponent from '../../Atoms/SpinnerComponent/SpinnerComponent';
import PageTitleComponent from '../../Molecules/PageTitleComponent/PageTitleComponent';
import ServerErrorsComponent from '../../Molecules/ServerErrorsComponent/ServerErrorsComponent';

const ProcessDetailsPage = (props) => {
  const id = props.match.params.instanceID;
  const currentPage = JSON.parse(window.localStorage.getItem('state'))

  const { loading, error, data } = useGetProcessInstanceByIdQuery({
    variables: { id }
  });
  let prevPath;
  const BreadCrumb = []
  let BreadCrumbRoute = []
  if (data) {
    const result = data.ProcessInstances;
    if (currentPage) {
      const tempPath = currentPage.prev.split('/')
      prevPath = tempPath.filter(item => item)
      BreadCrumb.push(...prevPath)
      let sum = '';
      BreadCrumbRoute = BreadCrumb.map(elem => sum = (sum) + `/${elem}`);
    }
    if (result.length === 0) {
      return (
        <Redirect
          to={{
            pathname: '/NoData',
            state: {
              prev: currentPage ? currentPage.prev : '/ProcessInstances',
              title: 'Process not found',
              description: `Process instance with the id ${id} not found`,
              buttonText: currentPage ? `Go to ${prevPath[0].replace(/([A-Z])/g, ' $1').trim().toLowerCase()}` : 'Go to process instances'
            }
          }}
        />
      );
    }
  }

  return (
    <>
      {!error ?
        (<>
          <PageSection variant="light">
            <PageTitleComponent title="Process Details" />
            {!loading ?
              (<Grid gutter="md" span={12} lg={6} xl={4}>
                <GridItem span={12}>
                  <Breadcrumb>
                    <BreadcrumbItem>
                      <Link to={'/'}>Home</Link>
                    </BreadcrumbItem>
                    {BreadCrumb.map((item, index) => {
                        return (
                          <BreadcrumbItem key={index}>
                            <Link to={BreadCrumbRoute[index]}>
                              {item.replace(/([A-Z])/g, ' $1').trim()}
                            </Link>
                          </BreadcrumbItem>
                        );
                      // }
                    })}
                    <BreadcrumbItem isActive>
                      {data.ProcessInstances[0].processName}
                    </BreadcrumbItem>
                  </Breadcrumb>
                </GridItem>
              </Grid>
              ) : ''}
          </PageSection>
          <PageSection>
            {!loading ?
              (
                <Grid gutter="md" span={12} lg={6} xl={4}>
                  <GridItem span={12}>
                    <Title headingLevel="h1" size="4xl">
                      <ProcessDescriptor
                        processInstanceData={data.ProcessInstances[0]}
                      />
                    </Title>
                  </GridItem>
                  {currentPage && <GridItem>
                    <ProcessDetails data={data} from={currentPage} />
                  </GridItem>}
                  <GridItem>
                    <ProcessDetailsProcessVariables data={data} />
                  </GridItem>
                  <GridItem>
                    <ProcessDetailsTimeline
                      data={data.ProcessInstances}
                    />
                  </GridItem>
                </Grid>) : (
                <Card>
                  <Bullseye>
                    <SpinnerComponent spinnerText="Loading process details..." />
                  </Bullseye>
                </Card>
              )}
          </PageSection>
        </>) : <ServerErrorsComponent message={error.message} />}
    </>
  );
};

export default ProcessDetailsPage;
