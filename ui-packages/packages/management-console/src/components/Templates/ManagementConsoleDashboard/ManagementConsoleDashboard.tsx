import {
  Breadcrumb,
  BreadcrumbItem,
  Card,
  CardHead,
  Grid,
  // GridItem,
  PageSection,
  InjectedOuiaProps,
  withOuiaContext,
  CardHeader,
  Title,
  CardBody,
  Gallery,
  GridItem,
  SplitItem,
  Stack,
  StackItem,
  TextVariants,
  Split,
  Text
} from '@patternfly/react-core';
import {
  ChartDonut
  // ChartGroup,
  // ChartVoronoiContainer,
  // ChartArea,
  // ChartContainer,
  // ChartLabel
} from '@patternfly/react-charts';
import { ouiaPageTypeAndObjectId } from '@kogito-apps/common';
import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import PageTitle from '../../Molecules/PageTitle/PageTitle';
import {
  CheckCircleIcon,
  ErrorCircleOIcon,
  DisconnectedIcon
} from '@patternfly/react-icons';
import './ManagementConsoleDashboard.css';

const ProcessListPage: React.FC<InjectedOuiaProps> = ({ ouiaContext }) => {
  useEffect(() => {
    return ouiaPageTypeAndObjectId(ouiaContext, 'process-instance-dashboard');
  });

  return (
    <React.Fragment>
      <PageSection variant="light">
        <PageTitle title="Dashboard" />
        <Breadcrumb>
          <BreadcrumbItem>
            <Link to={'/'}>Home</Link>
          </BreadcrumbItem>
          <BreadcrumbItem isActive>Dashboard</BreadcrumbItem>
        </Breadcrumb>
      </PageSection>
      <PageSection>
        <Grid gutter="md">
          {/*  status card */}
          <GridItem sm={12} xl2RowSpan={2}>
            <Card>
              <CardHeader>
                <Title headingLevel="h3" size="lg">
                  Status
                </Title>
              </CardHeader>
              <CardBody>
                <Split gutter="lg">
                  <SplitItem>
                    <Split gutter="sm">
                      <SplitItem>
                        <CheckCircleIcon
                          size="md"
                          color="var(--pf-global--success-color--100)"
                        />
                      </SplitItem>
                      <SplitItem>
                        <Stack>
                          <StackItem>
                            <Text component={TextVariants.h3}>Data Index</Text>
                          </StackItem>
                          <StackItem>
                            <Text component={TextVariants.small}>healthy</Text>
                          </StackItem>
                        </Stack>
                      </SplitItem>
                    </Split>
                  </SplitItem>
                  <SplitItem>
                    <Split gutter="sm">
                      <SplitItem>
                        <ErrorCircleOIcon
                          size="md"
                          color="var(--pf-global--danger-color--100)"
                        />
                      </SplitItem>
                      <SplitItem>
                        <Stack>
                          <StackItem>
                            <Text component={TextVariants.h3}>Job Service</Text>
                          </StackItem>
                          <StackItem>
                            <Text component={TextVariants.small}>in error</Text>
                          </StackItem>
                        </Stack>
                      </SplitItem>
                    </Split>
                  </SplitItem>
                  <SplitItem>
                    <Split gutter="sm">
                      <SplitItem>
                        <DisconnectedIcon
                          size="md"
                          color="var(--pf-global--disabled-color--100)"
                        />
                      </SplitItem>
                      <SplitItem>
                        <Stack>
                          <StackItem>
                            <Text component={TextVariants.h3}>
                              Process Manager
                            </Text>
                          </StackItem>
                          <StackItem>
                            <Text component={TextVariants.small}>
                              disconnected
                            </Text>
                          </StackItem>
                        </Stack>
                      </SplitItem>
                    </Split>
                  </SplitItem>
                </Split>
              </CardBody>
            </Card>
          </GridItem>

          {/* Processes card */}
          <GridItem sm={12}>
            <Card>
              <CardHead>
                <Title headingLevel="h3" size="lg">
                  Processes
                </Title>
              </CardHead>
              <CardBody>
                <Gallery className="kogito-management-console--gallery">
                  <div style={{ height: '150px', width: '150px' }}>
                    <ChartDonut
                      ariaDesc="Active processes"
                      ariaTitle="Active processes"
                      constrainToVisibleArea={true}
                      data={[
                        { x: 'travel', y: 35 },
                        { x: 'hotelbooking', y: 55 }
                      ]}
                      labels={({ datum }) => `${datum.x}: ${datum.y}`}
                      // legendData={[
                      //   { name: 'travel: 35' },
                      //   { name: 'hotelbooking: 55' }
                      // ]}
                      // legendOrientation="vertical"
                      // legendPosition="right"
                      padding={{
                        bottom: 10,
                        left: 10,
                        right: 10, // Adjusted to accommodate legend
                        top: 10
                      }}
                      title="90"
                      subTitle="Active"
                      height={150}
                      width={150}
                    />
                  </div>

                  <div style={{ height: '150px', width: '150px' }}>
                    <ChartDonut
                      ariaDesc="Completed processes"
                      ariaTitle="Completed processes"
                      constrainToVisibleArea={true}
                      data={[
                        { x: 'travel', y: 514 },
                        { x: 'visaApplications', y: 10 },
                        { x: 'hotelbooking', y: 238 }
                      ]}
                      labels={({ datum }) => `${datum.x}: ${datum.y}`}
                      // legendData={[
                      //   { name: 'travel: 35' },
                      //   { name: 'visaApplications: 10' },
                      //   { name: 'hotelbooking: 65' }
                      // ]}
                      // legendOrientation="vertical"
                      // legendPosition="right"
                      padding={{
                        bottom: 10,
                        left: 10,
                        right: 10, // Adjusted to accommodate legend
                        top: 10
                      }}
                      title="762"
                      subTitle="Completed"
                      height={150}
                      width={150}
                    />
                  </div>

                  <div style={{ height: '150px', width: '150px' }}>
                    <ChartDonut
                      ariaDesc="Aborted processes"
                      ariaTitle="Aborted processes"
                      constrainToVisibleArea={true}
                      data={[
                        { x: 'travel', y: 4 },
                        { x: 'visaApplications', y: 0 },
                        { x: 'hotelbooking', y: 3 }
                      ]}
                      labels={({ datum }) => `${datum.x}: ${datum.y}`}
                      // legendData={[
                      //   { name: 'travel: 4' },
                      //   { name: 'visaApplications: 0' },
                      //   { name: 'hotelbooking: 3' }
                      // ]}
                      // legendOrientation="vertical"
                      // legendPosition="right"
                      padding={{
                        bottom: 10,
                        left: 10,
                        right: 10, // Adjusted to accommodate legend
                        top: 10
                      }}
                      title="7"
                      subTitle="Aborted"
                      height={150}
                      width={150}
                    />
                  </div>

                  <div style={{ height: '150px', width: '150px' }}>
                    <ChartDonut
                      ariaDesc="Suspended processes"
                      ariaTitle="Suspended processes"
                      constrainToVisibleArea={true}
                      data={[
                        { x: 'travel', y: 14 },
                        { x: 'visaApplications', y: 30 },
                        { x: 'hotelbooking', y: 23 }
                      ]}
                      labels={({ datum }) => `${datum.x}: ${datum.y}`}
                      // legendData={[
                      //   { name: 'travel: 14' },
                      //   { name: 'visaApplications: 30' },
                      //   { name: 'hotelbooking: 23' }
                      // ]}
                      // legendOrientation="vertical"
                      // legendPosition="right"
                      padding={{
                        bottom: 10,
                        left: 10,
                        right: 10, // Adjusted to accommodate legend
                        top: 10
                      }}
                      title="67"
                      subTitle="Suspended"
                      height={150}
                      width={150}
                    />
                  </div>

                  <div style={{ height: '150px', width: '150px' }}>
                    <ChartDonut
                      ariaDesc="Error processes"
                      ariaTitle="Error processes"
                      constrainToVisibleArea={true}
                      data={[
                        { x: 'travel', y: 1 },
                        { x: 'visaApplications', y: 10 },
                        { x: 'hotelbooking', y: 0 }
                      ]}
                      labels={({ datum }) => `${datum.x}: ${datum.y}`}
                      // legendData={[
                      //   { name: 'travel: 1' },
                      //   { name: 'visaApplications: 10' },
                      //   { name: 'hotelbooking: 0' }
                      // ]}
                      // legendOrientation="vertical"
                      // legendPosition="right"
                      padding={{
                        bottom: 10,
                        left: 10,
                        right: 10, // Adjusted to accommodate legend
                        top: 10
                      }}
                      title="11"
                      subTitle="Error"
                      height={150}
                      width={150}
                    />
                  </div>
                </Gallery>
              </CardBody>
            </Card>
          </GridItem>

          {/* Jobs card */}
          <GridItem sm={12}>
            <Card>
              <CardHeader>
                <Title headingLevel="h3" size="lg">
                  Jobs
                </Title>
              </CardHeader>
              <CardBody>
                <Gallery className="kogito-management-console--gallery">
                  <div style={{ height: '150px', width: '150px' }}>
                    <ChartDonut
                      ariaDesc="Jobs executed"
                      ariaTitle="Jobs executed"
                      constrainToVisibleArea={true}
                      data={[
                        { x: 'travel', y: 5 },
                        { x: 'hotelbooking', y: 5 }
                      ]}
                      labels={({ datum }) => `${datum.x}: ${datum.y}`}
                      // legendData={[
                      //   { name: 'travel: 35' },
                      //   { name: 'hotelbooking: 55' }
                      // ]}
                      // legendOrientation="vertical"
                      // legendPosition="right"
                      padding={{
                        bottom: 10,
                        left: 10,
                        right: 10, // Adjusted to accommodate legend
                        top: 10
                      }}
                      title="10"
                      subTitle="Executed"
                      height={150}
                      width={150}
                    />
                  </div>

                  <div style={{ height: '150px', width: '150px' }}>
                    <ChartDonut
                      ariaDesc="Jobs in error"
                      ariaTitle="Jobs in error"
                      constrainToVisibleArea={true}
                      data={[
                        { x: 'travel', y: 2 },
                        { x: 'visaApplications', y: 3 },
                        { x: 'hotelbooking', y: 4 }
                      ]}
                      labels={({ datum }) => `${datum.x}: ${datum.y}`}
                      // legendData={[
                      //   { name: 'travel: 35' },
                      //   { name: 'visaApplications: 10' },
                      //   { name: 'hotelbooking: 65' }
                      // ]}
                      // legendOrientation="vertical"
                      // legendPosition="right"
                      padding={{
                        bottom: 10,
                        left: 10,
                        right: 10, // Adjusted to accommodate legend
                        top: 10
                      }}
                      title="9"
                      subTitle="Error"
                      height={150}
                      width={150}
                    />
                  </div>

                  <div style={{ height: '150px', width: '150px' }}>
                    <ChartDonut
                      ariaDesc="Jobs canceled"
                      ariaTitle="Jobs canceled"
                      constrainToVisibleArea={true}
                      data={[
                        { x: 'travel', y: 2 },
                        { x: 'visaApplications', y: 0 },
                        { x: 'hotelbooking', y: 3 }
                      ]}
                      labels={({ datum }) => `${datum.x}: ${datum.y}`}
                      // legendData={[
                      //   { name: 'travel: 4' },
                      //   { name: 'visaApplications: 0' },
                      //   { name: 'hotelbooking: 3' }
                      // ]}
                      // legendOrientation="vertical"
                      // legendPosition="right"
                      padding={{
                        bottom: 10,
                        left: 10,
                        right: 10, // Adjusted to accommodate legend
                        top: 10
                      }}
                      title="5"
                      subTitle="Canceled"
                      height={150}
                      width={150}
                    />
                  </div>

                  <div style={{ height: '150px', width: '150px' }}>
                    <ChartDonut
                      ariaDesc="Jobs scheduled"
                      ariaTitle="Jobs scheduled"
                      constrainToVisibleArea={true}
                      data={[
                        { x: 'travel', y: 56 },
                        { x: 'visaApplications', y: 1 },
                        { x: 'hotelbooking', y: 9 }
                      ]}
                      labels={({ datum }) => `${datum.x}: ${datum.y}`}
                      // legendData={[
                      //   { name: 'travel: 14' },
                      //   { name: 'visaApplications: 30' },
                      //   { name: 'hotelbooking: 23' }
                      // ]}
                      // legendOrientation="vertical"
                      // legendPosition="right"
                      padding={{
                        bottom: 10,
                        left: 10,
                        right: 10, // Adjusted to accommodate legend
                        top: 10
                      }}
                      title="66"
                      subTitle="Scheduled"
                      height={150}
                      width={150}
                    />
                  </div>

                  <div style={{ height: '150px', width: '150px' }}>
                    <ChartDonut
                      ariaDesc="Jobs retried"
                      ariaTitle="Jobs retried"
                      constrainToVisibleArea={true}
                      data={[
                        { x: 'travel', y: 9 },
                        { x: 'visaApplications', y: 10 },
                        { x: 'hotelbooking', y: 0 }
                      ]}
                      labels={({ datum }) => `${datum.x}: ${datum.y}`}
                      // legendData={[
                      //   { name: 'travel: 1' },
                      //   { name: 'visaApplications: 10' },
                      //   { name: 'hotelbooking: 0' }
                      // ]}
                      // legendOrientation="vertical"
                      // legendPosition="right"
                      padding={{
                        bottom: 10,
                        left: 10,
                        right: 10, // Adjusted to accommodate legend
                        top: 10
                      }}
                      title="19"
                      subTitle="Retry"
                      height={150}
                      width={150}
                    />
                  </div>
                </Gallery>
              </CardBody>
            </Card>
          </GridItem>
          {/* <GridItem sm={12} md={4}>
          <Card>
            <div
              style={{ marginLeft: '50px', marginTop: '50px', height: '135px' }}
            >
              <div style={{ height: '100px', width: '400px' }}>
                <ChartGroup
                  ariaDesc="Average number of pets"
                  ariaTitle="Sparkline chart example"
                  containerComponent={
                    <ChartVoronoiContainer
                      labels={({ datum }) => `${datum.name}: ${datum.y}`}
                      constrainToVisibleArea
                    />
                  }
                  height={100}
                  maxDomain={{ y: 50 }}
                  padding={0}
                  width={400}
                >
                  <ChartArea
                    data={[
                      { name: '2001', x: '2001', y: 0 },
                      { name: '2002', x: '2002', y: 0 },
                      { name: '2003', x: '2003', y: 0 },
                      { name: '2006', x: '2006', y: 3 },
                      { name: '2007', x: '2007', y: 4 },
                      { name: 'Cats', x: '2008', y: 8 },
                      { name: 'Cats', x: '2009', y: 50 },
                      { name: 'Cats', x: '2010', y: 4 },
                      { name: 'Cats', x: '2011', y: 8 },
                      { name: 'Cats', x: '2012', y: 3 },
                      { name: 'Cats', x: '2013', y: 4 },
                      { name: 'Cats', x: '2014', y: 8 },
                      { name: 'Cats', x: '2015', y: 13 },
                      { name: 'Cats', x: '2016', y: 4 },
                      { name: 'Cats', x: '2017', y: 8 },
                      { name: 'Cats', x: '2018', y: 6 }
                    ]}
                  />
                </ChartGroup>
              </div>
              <ChartContainer>
                <ChartLabel text="Executed over time" dy={15} />
              </ChartContainer>
            </div>
          </Card>
</GridItem> */}

          {/* Activity stream card */}
          <GridItem sm={12}>
            <Card>
              <CardHeader>
                <Title headingLevel="h3" size="lg">
                  Activity Stream
                </Title>
              </CardHeader>
              {/* <ProcessDetailsTimeline
                      data={data.ProcessInstances[0]}
                      setModalContent={setModalContent}
                      setModalTitle={setModalTitle}
                      setTitleType={setTitleType}
                      handleSkipModalToggle={handleSkipModalToggle}
                      handleRetryModalToggle={handleRetryModalToggle}
                    /> */}

              <CardBody>
                <div className="pf-l-stack pf-m-gutter kogito-management-console--timeline">
                  <div className="pf-l-split pf-m-gutter kogito-management-console--timeline-item">
                    <div className="pf-l-split__item">
                      <svg
                        fill="var(--pf-global--success-color--100)"
                        height="1em"
                        width="1em"
                        viewBox="0 0 512 512"
                        aria-hidden="true"
                        role="img"
                        className="kogito-management-console--timeline-status"
                      >
                        <path
                          d="M504 256c0 136.967-111.033 248-248 248S8 392.967 8 256 119.033 8 256 8s248 111.033 248 248zM227.314 387.314l184-184c6.248-6.248 6.248-16.379 0-22.627l-22.627-22.627c-6.248-6.249-16.379-6.249-22.628 0L216 308.118l-70.059-70.059c-6.248-6.248-16.379-6.248-22.628 0l-22.627 22.627c-6.248 6.248-6.248 16.379 0 22.627l104 104c6.249 6.249 16.379 6.249 22.628.001z"
                          transform=""
                        />
                      </svg>
                    </div>
                    <div className="pf-l-split__item pf-m-fill">
                      <div className="pf-c-content">
                        <p data-pf-content="true">
                          Process travels completed
                          <small data-pf-content="true">
                            <span>1 minute ago</span>
                          </small>
                        </p>
                      </div>
                    </div>
                    <div className="pf-l-split__item">
                      <div className="pf-c-dropdown">
                        <button
                          aria-label="Actions"
                          id="timeline-kebab-toggle-disabled-0"
                          className="pf-c-dropdown__toggle pf-m-plain"
                          type="button"
                          aria-expanded="false"
                          aria-haspopup="false"
                        >
                          <svg
                            fill="currentColor"
                            height="1em"
                            width="1em"
                            viewBox="0 0 192 512"
                            aria-hidden="true"
                            role="img"
                          >
                            <path
                              d="M96 184c39.8 0 72 32.2 72 72s-32.2 72-72 72-72-32.2-72-72 32.2-72 72-72zM24 80c0 39.8 32.2 72 72 72s72-32.2 72-72S135.8 8 96 8 24 40.2 24 80zm0 352c0 39.8 32.2 72 72 72s72-32.2 72-72-32.2-72-72-72-72 32.2-72 72z"
                              transform=""
                            />
                          </svg>
                        </button>
                      </div>
                    </div>{' '}
                  </div>
                  <div className="pf-l-split pf-m-gutter kogito-management-console--timeline-item">
                    <div className="pf-l-split__item">
                      <svg
                        fill="currentColor"
                        height="1em"
                        width="1em"
                        viewBox="0 64 1024 1024"
                        aria-hidden="true"
                        role="img"
                        className="kogito-management-console--timeline-status"
                      >
                        <path
                          d="M511.781-73.143c-70.656 0-136.923 13.312-198.875 40.009s-116.078 63.269-162.45 109.934c-46.299 46.665-82.944 100.937-109.934 162.962s-40.448 128.293-40.448 198.875 13.458 136.923 40.448 198.875 63.634 116.078 109.934 162.45c46.299 46.299 100.425 82.944 162.45 109.934s128.293 40.448 198.875 40.448 136.923-13.458 198.875-40.448 116.078-63.634 162.45-109.934c46.299-46.299 82.944-100.425 109.934-162.45s40.448-128.293 40.448-198.875-13.458-136.923-40.448-198.875-63.634-116.297-109.934-162.962-100.425-83.31-162.45-109.934-128.293-40.009-198.875-40.009v0zM361.326 795.94c-46.958-20.334-88.137-48.128-123.465-83.456s-63.122-76.434-83.456-123.465-30.501-97.134-30.501-150.455 10.167-103.643 30.501-150.894c20.334-47.323 48.128-88.43 83.456-123.465s76.434-62.61 123.465-82.944 97.134-30.501 150.455-30.501 103.424 10.167 150.455 30.501c46.958 20.334 88.137 47.982 123.465 82.944s63.122 76.142 83.456 123.465 30.501 97.646 30.501 150.894-10.167 103.424-30.501 150.455c-20.334 46.958-48.128 88.137-83.456 123.465s-76.434 63.122-123.465 83.456-97.134 30.501-150.455 30.501c-53.321 0-103.424-10.167-150.455-30.501v0zM730.697 498.395c1.975-8.119 2.414-19.383-1.17-26.331-7.387-14.263-16.457-26.99-26.185-40.96-83.017-119.296-158.135-200.485-249.71-305.371-17.993-20.626-34.889-13.751-42.057-9.070-15.579 10.24-15.799 29.769-7.314 52.297 9.801 25.966 20.48 57.783 29.842 79.799 22.455 52.736 20.187 58.075 41.911 114.907-52.297-0.1024-127.781-0.951-180.078-1.463-15.067-0.146-33.499 1.902-39.497 16.311-5.778 13.751-0.146 26.039 8.558 38.107 64.146 89.381 172.617 212.626 248.174 294.546 6.51 7.022 30.062 23.333 43.739 21.358 10.825-1.536 21.87 1.243 26.843-9.289 5.851-12.434 2.706-25.746-2.56-37.23-22.016-48.347-45.714-118.638-71.095-174.519 50.615-0.073 144.53-0.073 195.145-0.146 8.558 0 18.432-0.731 23.406-8.046 1.024-1.536 1.755-3.145 2.121-4.901v0z"
                          transform="rotate(180 0 512) scale(-1 1)"
                        />
                      </svg>
                    </div>
                    <div className="pf-l-split__item pf-m-fill">
                      <div className="pf-c-content">
                        <p data-pf-content="true" className="">
                          Process visas completed
                          <span>
                            <svg
                              fill="var(--pf-global--icon--Color--light)"
                              height="1em"
                              width="1em"
                              viewBox="0 0 448 512"
                              aria-hidden="true"
                              role="img"
                              className="pf-u-ml-sm"
                            >
                              <path
                                d="M224 256c70.7 0 128-57.3 128-128S294.7 0 224 0 96 57.3 96 128s57.3 128 128 128zm89.6 32h-16.7c-22.2 10.2-46.9 16-72.9 16s-50.6-5.8-72.9-16h-16.7C60.2 288 0 348.2 0 422.4V464c0 26.5 21.5 48 48 48h352c26.5 0 48-21.5 48-48v-41.6c0-74.2-60.2-134.4-134.4-134.4z"
                                transform=""
                              />
                            </svg>
                          </span>
                          <small data-pf-content="true" className="">
                            1 minute ago
                          </small>
                        </p>
                      </div>
                    </div>
                    <div className="pf-l-split__item">
                      <div className="pf-c-dropdown">
                        <button
                          aria-label="Actions"
                          id="timeline-kebab-toggle-disabled-1"
                          className="pf-c-dropdown__toggle pf-m-plain"
                          type="button"
                          aria-expanded="false"
                          aria-haspopup="false"
                        >
                          <svg
                            fill="currentColor"
                            height="1em"
                            width="1em"
                            viewBox="0 0 192 512"
                            aria-hidden="true"
                            role="img"
                          >
                            <path
                              d="M96 184c39.8 0 72 32.2 72 72s-32.2 72-72 72-72-32.2-72-72 32.2-72 72-72zM24 80c0 39.8 32.2 72 72 72s72-32.2 72-72S135.8 8 96 8 24 40.2 24 80zm0 352c0 39.8 32.2 72 72 72s72-32.2 72-72-32.2-72-72-72-72 32.2-72 72z"
                              transform=""
                            />
                          </svg>
                        </button>
                      </div>
                    </div>{' '}
                  </div>
                  <div className="pf-l-split pf-m-gutter kogito-management-console--timeline-item">
                    <div className="pf-l-split__item">
                      <svg
                        fill="var(--pf-global--success-color--100)"
                        height="1em"
                        width="1em"
                        viewBox="0 0 512 512"
                        aria-hidden="true"
                        role="img"
                        className="kogito-management-console--timeline-status"
                      >
                        <path
                          d="M504 256c0 136.967-111.033 248-248 248S8 392.967 8 256 119.033 8 256 8s248 111.033 248 248zM227.314 387.314l184-184c6.248-6.248 6.248-16.379 0-22.627l-22.627-22.627c-6.248-6.249-16.379-6.249-22.628 0L216 308.118l-70.059-70.059c-6.248-6.248-16.379-6.248-22.628 0l-22.627 22.627c-6.248 6.248-6.248 16.379 0 22.627l104 104c6.249 6.249 16.379 6.249 22.628.001z"
                          transform=""
                        />
                      </svg>
                    </div>
                    <div className="pf-l-split__item pf-m-fill">
                      <div className="pf-c-content">
                        <p data-pf-content="true" className="">
                          Process visas started
                          <small data-pf-content="true" className="">
                            <span>1 minutes ago</span>
                          </small>
                        </p>
                      </div>
                    </div>
                    <div className="pf-l-split__item">
                      <div className="pf-c-dropdown">
                        <button
                          aria-label="Actions"
                          id="timeline-kebab-toggle-disabled-2"
                          className="pf-c-dropdown__toggle pf-m-plain"
                          type="button"
                          aria-expanded="false"
                          aria-haspopup="false"
                        >
                          <svg
                            fill="currentColor"
                            height="1em"
                            width="1em"
                            viewBox="0 0 192 512"
                            aria-hidden="true"
                            role="img"
                          >
                            <path
                              d="M96 184c39.8 0 72 32.2 72 72s-32.2 72-72 72-72-32.2-72-72 32.2-72 72-72zM24 80c0 39.8 32.2 72 72 72s72-32.2 72-72S135.8 8 96 8 24 40.2 24 80zm0 352c0 39.8 32.2 72 72 72s72-32.2 72-72-32.2-72-72-72-72 32.2-72 72z"
                              transform=""
                            />
                          </svg>
                        </button>
                      </div>
                    </div>{' '}
                  </div>
                  <div className="pf-l-split pf-m-gutter kogito-management-console--timeline-item">
                    <div className="pf-l-split__item">
                      <svg
                        fill="var(--pf-global--success-color--100)"
                        height="1em"
                        width="1em"
                        viewBox="0 0 512 512"
                        aria-hidden="true"
                        role="img"
                        className="kogito-management-console--timeline-status"
                      >
                        <path
                          d="M504 256c0 136.967-111.033 248-248 248S8 392.967 8 256 119.033 8 256 8s248 111.033 248 248zM227.314 387.314l184-184c6.248-6.248 6.248-16.379 0-22.627l-22.627-22.627c-6.248-6.249-16.379-6.249-22.628 0L216 308.118l-70.059-70.059c-6.248-6.248-16.379-6.248-22.628 0l-22.627 22.627c-6.248 6.248-6.248 16.379 0 22.627l104 104c6.249 6.249 16.379 6.249 22.628.001z"
                          transform=""
                        />
                      </svg>
                    </div>
                    <div className="pf-l-split__item pf-m-fill">
                      <div className="pf-c-content">
                        <p data-pf-content="true" className="">
                          Process travels updated
                          <small data-pf-content="true" className="">
                            <span>1 minute ago</span>
                          </small>
                        </p>
                      </div>
                    </div>
                    <div className="pf-l-split__item">
                      <div className="pf-c-dropdown">
                        <button
                          aria-label="Actions"
                          id="timeline-kebab-toggle-disabled-3"
                          className="pf-c-dropdown__toggle pf-m-plain"
                          type="button"
                          aria-expanded="false"
                          aria-haspopup="false"
                        >
                          <svg
                            fill="currentColor"
                            height="1em"
                            width="1em"
                            viewBox="0 0 192 512"
                            aria-hidden="true"
                            role="img"
                          >
                            <path
                              d="M96 184c39.8 0 72 32.2 72 72s-32.2 72-72 72-72-32.2-72-72 32.2-72 72-72zM24 80c0 39.8 32.2 72 72 72s72-32.2 72-72S135.8 8 96 8 24 40.2 24 80zm0 352c0 39.8 32.2 72 72 72s72-32.2 72-72-32.2-72-72-72-72 32.2-72 72z"
                              transform=""
                            />
                          </svg>
                        </button>
                      </div>
                    </div>{' '}
                  </div>
                  <div className="pf-l-split pf-m-gutter kogito-management-console--timeline-item">
                    <div className="pf-l-split__item">
                      <svg
                        fill="var(--pf-global--success-color--100)"
                        height="1em"
                        width="1em"
                        viewBox="0 0 512 512"
                        aria-hidden="true"
                        role="img"
                        className="kogito-management-console--timeline-status"
                      >
                        <path
                          d="M504 256c0 136.967-111.033 248-248 248S8 392.967 8 256 119.033 8 256 8s248 111.033 248 248zM227.314 387.314l184-184c6.248-6.248 6.248-16.379 0-22.627l-22.627-22.627c-6.248-6.249-16.379-6.249-22.628 0L216 308.118l-70.059-70.059c-6.248-6.248-16.379-6.248-22.628 0l-22.627 22.627c-6.248 6.248-6.248 16.379 0 22.627l104 104c6.249 6.249 16.379 6.249 22.628.001z"
                          transform=""
                        />
                      </svg>
                    </div>
                    <div className="pf-l-split__item pf-m-fill">
                      <div className="pf-c-content">
                        <p data-pf-content="true" className="">
                          Process travels started
                          <small data-pf-content="true" className="">
                            <span>9 months ago</span>
                          </small>
                        </p>
                      </div>
                    </div>
                    <div className="pf-l-split__item">
                      <div className="pf-c-dropdown">
                        <button
                          aria-label="Actions"
                          id="timeline-kebab-toggle-disabled-4"
                          className="pf-c-dropdown__toggle pf-m-plain"
                          type="button"
                          aria-expanded="false"
                          aria-haspopup="false"
                        >
                          <svg
                            fill="currentColor"
                            height="1em"
                            width="1em"
                            viewBox="0 0 192 512"
                            aria-hidden="true"
                            role="img"
                          >
                            <path
                              d="M96 184c39.8 0 72 32.2 72 72s-32.2 72-72 72-72-32.2-72-72 32.2-72 72-72zM24 80c0 39.8 32.2 72 72 72s72-32.2 72-72S135.8 8 96 8 24 40.2 24 80zm0 352c0 39.8 32.2 72 72 72s72-32.2 72-72-32.2-72-72-72-72 32.2-72 72z"
                              transform=""
                            />
                          </svg>
                        </button>
                      </div>
                    </div>{' '}
                  </div>
                  <div className="pf-l-split pf-m-gutter kogito-management-console--timeline-item">
                    <div className="pf-l-split__item">
                      <svg
                        fill="var(--pf-global--success-color--100)"
                        height="1em"
                        width="1em"
                        viewBox="0 0 512 512"
                        aria-hidden="true"
                        role="img"
                        className="kogito-management-console--timeline-status"
                      >
                        <path
                          d="M504 256c0 136.967-111.033 248-248 248S8 392.967 8 256 119.033 8 256 8s248 111.033 248 248zM227.314 387.314l184-184c6.248-6.248 6.248-16.379 0-22.627l-22.627-22.627c-6.248-6.249-16.379-6.249-22.628 0L216 308.118l-70.059-70.059c-6.248-6.248-16.379-6.248-22.628 0l-22.627 22.627c-6.248 6.248-6.248 16.379 0 22.627l104 104c6.249 6.249 16.379 6.249 22.628.001z"
                          transform=""
                        />
                      </svg>
                    </div>
                    <div className="pf-l-split__item pf-m-fill">
                      <div className="pf-c-content">
                        <p data-pf-content="true" className="">
                          Join
                          <small data-pf-content="true" className="">
                            <span>9 months ago</span>
                          </small>
                        </p>
                      </div>
                    </div>
                    <div className="pf-l-split__item">
                      <div className="pf-c-dropdown">
                        <button
                          aria-label="Actions"
                          id="timeline-kebab-toggle-disabled-5"
                          className="pf-c-dropdown__toggle pf-m-plain"
                          type="button"
                          aria-expanded="false"
                          aria-haspopup="false"
                        >
                          <svg
                            fill="currentColor"
                            height="1em"
                            width="1em"
                            viewBox="0 0 192 512"
                            aria-hidden="true"
                            role="img"
                          >
                            <path
                              d="M96 184c39.8 0 72 32.2 72 72s-32.2 72-72 72-72-32.2-72-72 32.2-72 72-72zM24 80c0 39.8 32.2 72 72 72s72-32.2 72-72S135.8 8 96 8 24 40.2 24 80zm0 352c0 39.8 32.2 72 72 72s72-32.2 72-72-32.2-72-72-72-72 32.2-72 72z"
                              transform=""
                            />
                          </svg>
                        </button>
                      </div>
                    </div>{' '}
                  </div>
                  <div className="pf-l-split pf-m-gutter kogito-management-console--timeline-item">
                    <div className="pf-l-split__item">
                      <svg
                        fill="var(--pf-global--success-color--100)"
                        height="1em"
                        width="1em"
                        viewBox="0 0 512 512"
                        aria-hidden="true"
                        role="img"
                        className="kogito-management-console--timeline-status"
                      >
                        <path
                          d="M504 256c0 136.967-111.033 248-248 248S8 392.967 8 256 119.033 8 256 8s248 111.033 248 248zM227.314 387.314l184-184c6.248-6.248 6.248-16.379 0-22.627l-22.627-22.627c-6.248-6.249-16.379-6.249-22.628 0L216 308.118l-70.059-70.059c-6.248-6.248-16.379-6.248-22.628 0l-22.627 22.627c-6.248 6.248-6.248 16.379 0 22.627l104 104c6.249 6.249 16.379 6.249 22.628.001z"
                          transform=""
                        />
                      </svg>
                    </div>
                    <div className="pf-l-split__item pf-m-fill">
                      <div className="pf-c-content">
                        <p data-pf-content="true" className="">
                          is visa required
                          <small data-pf-content="true" className="">
                            <span>9 months ago</span>
                          </small>
                        </p>
                      </div>
                    </div>
                    <div className="pf-l-split__item">
                      <div className="pf-c-dropdown">
                        <button
                          aria-label="Actions"
                          id="timeline-kebab-toggle-disabled-6"
                          className="pf-c-dropdown__toggle pf-m-plain"
                          type="button"
                          aria-expanded="false"
                          aria-haspopup="false"
                        >
                          <svg
                            fill="currentColor"
                            height="1em"
                            width="1em"
                            viewBox="0 0 192 512"
                            aria-hidden="true"
                            role="img"
                          >
                            <path
                              d="M96 184c39.8 0 72 32.2 72 72s-32.2 72-72 72-72-32.2-72-72 32.2-72 72-72zM24 80c0 39.8 32.2 72 72 72s72-32.2 72-72S135.8 8 96 8 24 40.2 24 80zm0 352c0 39.8 32.2 72 72 72s72-32.2 72-72-32.2-72-72-72-72 32.2-72 72z"
                              transform=""
                            />
                          </svg>
                        </button>
                      </div>
                    </div>{' '}
                  </div>
                  <div className="pf-l-split pf-m-gutter kogito-management-console--timeline-item">
                    <div className="pf-l-split__item">
                      <svg
                        fill="var(--pf-global--success-color--100)"
                        height="1em"
                        width="1em"
                        viewBox="0 0 512 512"
                        aria-hidden="true"
                        role="img"
                        className="kogito-management-console--timeline-status"
                      >
                        <path
                          d="M504 256c0 136.967-111.033 248-248 248S8 392.967 8 256 119.033 8 256 8s248 111.033 248 248zM227.314 387.314l184-184c6.248-6.248 6.248-16.379 0-22.627l-22.627-22.627c-6.248-6.249-16.379-6.249-22.628 0L216 308.118l-70.059-70.059c-6.248-6.248-16.379-6.248-22.628 0l-22.627 22.627c-6.248 6.248-6.248 16.379 0 22.627l104 104c6.249 6.249 16.379 6.249 22.628.001z"
                          transform=""
                        />
                      </svg>
                    </div>
                    <div className="pf-l-split__item pf-m-fill">
                      <div className="pf-c-content">
                        <p data-pf-content="true" className="">
                          Visa check
                          <small data-pf-content="true" className="">
                            <span>9 months ago</span>
                          </small>
                        </p>
                      </div>
                    </div>
                    <div className="pf-l-split__item">
                      <div className="pf-c-dropdown">
                        <button
                          aria-label="Actions"
                          id="timeline-kebab-toggle-disabled-7"
                          className="pf-c-dropdown__toggle pf-m-plain"
                          type="button"
                          aria-expanded="false"
                          aria-haspopup="false"
                        >
                          <svg
                            fill="currentColor"
                            height="1em"
                            width="1em"
                            viewBox="0 0 192 512"
                            aria-hidden="true"
                            role="img"
                          >
                            <path
                              d="M96 184c39.8 0 72 32.2 72 72s-32.2 72-72 72-72-32.2-72-72 32.2-72 72-72zM24 80c0 39.8 32.2 72 72 72s72-32.2 72-72S135.8 8 96 8 24 40.2 24 80zm0 352c0 39.8 32.2 72 72 72s72-32.2 72-72-32.2-72-72-72-72 32.2-72 72z"
                              transform=""
                            />
                          </svg>
                        </button>
                      </div>
                    </div>{' '}
                  </div>
                  <div className="pf-l-split pf-m-gutter kogito-management-console--timeline-item">
                    <div className="pf-l-split__item">
                      <svg
                        fill="var(--pf-global--success-color--100)"
                        height="1em"
                        width="1em"
                        viewBox="0 0 512 512"
                        aria-hidden="true"
                        role="img"
                        className="kogito-management-console--timeline-status"
                      >
                        <path
                          d="M504 256c0 136.967-111.033 248-248 248S8 392.967 8 256 119.033 8 256 8s248 111.033 248 248zM227.314 387.314l184-184c6.248-6.248 6.248-16.379 0-22.627l-22.627-22.627c-6.248-6.249-16.379-6.249-22.628 0L216 308.118l-70.059-70.059c-6.248-6.248-16.379-6.248-22.628 0l-22.627 22.627c-6.248 6.248-6.248 16.379 0 22.627l104 104c6.249 6.249 16.379 6.249 22.628.001z"
                          transform=""
                        />
                      </svg>
                    </div>
                    <div className="pf-l-split__item pf-m-fill">
                      <div className="pf-c-content">
                        <p data-pf-content="true" className="">
                          StartProcess
                          <small data-pf-content="true" className="">
                            <span>9 months ago</span>
                          </small>
                        </p>
                      </div>
                    </div>
                    <div className="pf-l-split__item">
                      <div className="pf-c-dropdown">
                        <button
                          aria-label="Actions"
                          id="timeline-kebab-toggle-disabled-8"
                          className="pf-c-dropdown__toggle pf-m-plain"
                          type="button"
                          aria-expanded="false"
                          aria-haspopup="false"
                        >
                          <svg
                            fill="currentColor"
                            height="1em"
                            width="1em"
                            viewBox="0 0 192 512"
                            aria-hidden="true"
                            role="img"
                          >
                            <path
                              d="M96 184c39.8 0 72 32.2 72 72s-32.2 72-72 72-72-32.2-72-72 32.2-72 72-72zM24 80c0 39.8 32.2 72 72 72s72-32.2 72-72S135.8 8 96 8 24 40.2 24 80zm0 352c0 39.8 32.2 72 72 72s72-32.2 72-72-32.2-72-72-72-72 32.2-72 72z"
                              transform=""
                            />
                          </svg>
                        </button>
                      </div>
                    </div>{' '}
                  </div>
                </div>
              </CardBody>
            </Card>
          </GridItem>
        </Grid>
      </PageSection>
    </React.Fragment>
  );
};

export default withOuiaContext(ProcessListPage);
