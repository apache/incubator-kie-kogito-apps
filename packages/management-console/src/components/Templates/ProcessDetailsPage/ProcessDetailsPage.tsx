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
import React, { useState } from 'react';
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
import ProcessBulkModalComponent from '../../Atoms/ProcessBulkModalComponent/ProcessBulkModalComponent';
import axios from 'axios';
import { InfoCircleIcon } from '@patternfly/react-icons';

const ProcessDetailsPage = ({ match }) => {
  const [isSkipModalOpen, setIsSkipModalOpen] = useState<boolean>(false);
  const [isRetryModalOpen, setIsRetryModalOpen] = useState<boolean>(false);
  const [modalTitle, setModalTitle] = useState<string>('');
  const [titleType, setTitleType] = useState<string>('');
  const [modalContent, setModalContent] = useState<string>('');
  const id = match.params.instanceID;

  const { loading, error, data } = useGetProcessInstanceByIdQuery({
    variables: { id }
  });

  const handleSkipModalToggle = () => {
    setIsSkipModalOpen(!isSkipModalOpen);
  };

  const handleRetryModalToggle = () => {
    setIsRetryModalOpen(!isRetryModalOpen);
  };

  const handleSkip = () => {
    setModalTitle('Skip operation');
    axios
      .post(
        `${data.ProcessInstances[0].serviceUrl}/management/processes/${data.ProcessInstances[0].processId}/instances/${data.ProcessInstances[0].id}/skip`
      )
      .then(() => {
        setTitleType('success');
        setModalContent(
          'Process execution has successfully skipped node which was in error state.'
        );
        handleSkipModalToggle();
      })
      .catch(axiosError => {
        setTitleType('failure');
        setModalContent(
          `Process execution failed to skip node which is in error state. Message: ${JSON.stringify(
            axiosError.message
          )}`
        );

        handleSkipModalToggle();
      });
  };

  const handleRetry = () => {
    setModalTitle('Retry operation');
    axios
      .post(
        `${data.ProcessInstances[0].serviceUrl}/management/processes/${data.ProcessInstances[0].processId}/instances/${data.ProcessInstances[0].id}/retrigger`
      )
      .then(() => {
        setTitleType('success');
        setModalContent(
          `Process execution has successfully re-executed node which was in error state.`
        );
        handleRetryModalToggle();
      })
      .catch(axiosError => {
        setTitleType('failure');
        setModalContent(
          `Process execution failed to re-execute node which is in error state. Message: ${JSON.stringify(
            axiosError.message
          )}`
        );
        handleRetryModalToggle();
      });
  };

  const setTitle = (titleStatus, titleText) => {
    switch (titleStatus) {
      case 'success':
        return (
          <>
            <InfoCircleIcon
              className="pf-u-mr-sm"
              color="var(--pf-global--info-color--100)"
            />{' '}
            {titleText}{' '}
          </>
        );
      case 'failure':
        return (
          <>
            <InfoCircleIcon
              className="pf-u-mr-sm"
              color="var(--pf-global--danger-color--100)"
            />{' '}
            {titleText}{' '}
          </>
        );
    }
  };

  if (data) {
    const result = data.ProcessInstances;
    if (result.length === 0) {
      return (
        <Redirect
          to={{
            pathname: '/NoData',
            state: {
              prev: location.pathname,
              title: 'Process not found',
              description: `Process instance with the id ${id} not found`,
              buttonText: 'Go to process instances'
            }
          }}
        />
      );
    }
  }

  return (
    <>
      <PageSection variant="light">
        <ProcessBulkModalComponent
          isModalLarge={false}
          isModalOpen={
            modalTitle === 'Skip operation'
              ? isSkipModalOpen
              : modalTitle === 'Retry operation' && isRetryModalOpen
          }
          handleModalToggle={
            modalTitle === 'Skip operation'
              ? handleSkipModalToggle
              : modalTitle === 'Retry operation'
              ? handleRetryModalToggle
              : null
          }
          checkedArray={data && [data.ProcessInstances[0].state]}
          modalTitle={setTitle(titleType, modalTitle)}
          modalContent={modalContent}
        />
        <PageTitleComponent title="Process Details" />
        {!loading ? (
          <Grid gutter="md" span={12} lg={6} xl={4}>
            <GridItem span={12}>
              <Breadcrumb>
                <BreadcrumbItem>
                  <Link to={'/'}>Home</Link>
                </BreadcrumbItem>
                <BreadcrumbItem>
                  <Link to={'/ProcessInstances/'}>Process Instances</Link>
                </BreadcrumbItem>
                <BreadcrumbItem isActive>
                  {data.ProcessInstances[0].processName}
                </BreadcrumbItem>
              </Breadcrumb>
            </GridItem>
          </Grid>
        ) : (
          ''
        )}
      </PageSection>
      <PageSection>
        {!loading ? (
          <Grid gutter="md" span={12} lg={6} xl={4}>
            <GridItem span={12}>
              <Title headingLevel="h1" size="4xl">
                <ProcessDescriptor
                  processInstanceData={data.ProcessInstances[0]}
                />
              </Title>
            </GridItem>
            <GridItem>
              <ProcessDetails data={data} />
            </GridItem>
            <GridItem>
              <ProcessDetailsProcessVariables data={data} />
            </GridItem>
            <GridItem>
              <ProcessDetailsTimeline
                data={data.ProcessInstances[0]}
                handleSkip={handleSkip}
                handleRetry={handleRetry}
              />
            </GridItem>
          </Grid>
        ) : (
          <Card>
            <Bullseye>
              <SpinnerComponent spinnerText="Loading process details..." />
            </Bullseye>
          </Card>
        )}
      </PageSection>
    </>
  );
};

export default ProcessDetailsPage;
