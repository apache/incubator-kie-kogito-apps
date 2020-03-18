import {
  Breadcrumb,
  BreadcrumbItem,
  Grid,
  GridItem,
  Page,
  PageSection,
  Title,
  OverflowMenu,
  OverflowMenuContent,
  OverflowMenuGroup,
  OverflowMenuItem,
  Button,
  ButtonVariant,
  OverflowMenuControl,
  Dropdown,
  KebabToggle
} from '@patternfly/react-core';
import React, { useState, useCallback } from 'react';
import { Link } from 'react-router-dom';
import ProcessDetails from '../../Organisms/ProcessDetails/ProcessDetails';
import ProcessDetailsProcessDiagram from '../../Organisms/ProcessDetailsProcessDiagram/ProcessDetailsProcessDiagram';
import ProcessDetailsProcessVariables from '../../Organisms/ProcessDetailsProcessVariables/ProcessDetailsProcessVariables';
import ProcessDetailsTimeline from '../../Organisms/ProcessDetailsTimeline/ProcessDetailsTimeline';
import './ProcessDetailsPage.css';
import {
  useGetProcessInstanceByIdQuery,
  ProcessInstanceState
} from '../../../graphql/types';
import ProcessBulkModalComponent from '../../Atoms/ProcessBulkModalComponent/ProcessBulkModalComponent';
import { InfoCircleIcon } from '@patternfly/react-icons';
import axios from 'axios';

const ProcessDetailsPage = ({ match }) => {
  const id = match.params.instanceID;
  const [modalTitle, setModalTitle] = useState<string>('');
  const [modalContent, setModalContent] = useState<string>('');
  const [titleType, setTitleType] = useState<string>('');
  const [isSkipModalOpen, setIsSkipModalOpen] = useState<boolean>(false);
  const [isRetryModalOpen, setIsRetryModalOpen] = useState<boolean>(false);
  const [isAbortModalOpen, setIsAbortModalOpen] = useState<boolean>(false);
  const { loading, error, data } = useGetProcessInstanceByIdQuery({
    variables: { id }
  });

  if (loading) {
    return <p>Loading...</p>;
  }

  const handleSkipModalToggle = () => {
    setIsSkipModalOpen(!isSkipModalOpen);
  };
  const handleRetryModalToggle = () => {
    setIsRetryModalOpen(!isRetryModalOpen);
  };
  const handleAbortModalToggle = () => {
    setIsAbortModalOpen(!isAbortModalOpen);
  };

  const handleAbort = () => {
    setModalTitle('Abort operation');
    axios
      .delete(
        `${data.ProcessInstances[0].endpoint}/management/processes/${data.ProcessInstances[0].processId}/instances/${data.ProcessInstances[0].id}`
      )
      .then(() => {
        setModalTitle('Process aborted');
        setModalContent(
          `${data.ProcessInstances[0].processId} - process execution has been aborted.`
        );
        setTitleType('success');
        data.ProcessInstances[0].state = ProcessInstanceState.Aborted;
        handleAbortModalToggle();
      })
      .catch(() => {
        setTitleType('failure');
        handleAbortModalToggle();
      });
  };

  const handleSkip = async () => {
    if (!loading && data) {
      try {
        setModalTitle('Skip operation');
        await axios.post(
          `${data.ProcessInstances[0].endpoint}/management/processes/${data.ProcessInstances[0].processId}/instances/${data.ProcessInstances[0].id}/skip`
        );
        setTitleType('success');
        setModalContent(
          'Process execution has successfully skipped node which was in error state.'
        );
        handleSkipModalToggle();
      } catch (error) {
        setTitleType('failure');
        setModalContent(
          `Process execution failed to skip node which is in error state. Message: ${JSON.stringify(
            error.message
          )}`
        );
        handleSkipModalToggle();
      }
    }
  };

  const handleRetry = async () => {
    if (!loading && data) {
      try {
        setModalTitle('Retry operation');
        await axios.post(
          `${data.ProcessInstances[0].endpoint}/management/processes/${data.ProcessInstances[0].processId}/instances/${data.ProcessInstances[0].id}/retrigger`
        );
        setTitleType('success');
        setModalContent(
          `Process execution has successfully re-executed node which was in error state.`
        );
        handleRetryModalToggle();
      } catch (error) {
        setTitleType('failure');
        setModalContent(
          `Process execution failed to re-execute node which is in error state. Message: ${JSON.stringify(
            error.message
          )}`
        );
        handleRetryModalToggle();
      }
    }
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

  const processManagementButtons = () => {
    if (!loading && data) {
      if (data.ProcessInstances[0].addons.includes('process-management')) {
        if (data.ProcessInstances[0].state === ProcessInstanceState.Error) {
          return [
            <OverflowMenuItem isPersistent key={1}>
              <Button variant={ButtonVariant.secondary} onClick={handleAbort}>
                Abort
              </Button>
            </OverflowMenuItem>,
            <OverflowMenuItem key={2}>
              <Button variant={ButtonVariant.secondary} onClick={handleSkip}>
                Skip
              </Button>
            </OverflowMenuItem>,
            <OverflowMenuItem key={3}>
              <Button variant={ButtonVariant.secondary} onClick={handleRetry}>
                Retry
              </Button>
            </OverflowMenuItem>
          ];
        } else if (
          data.ProcessInstances[0].state === ProcessInstanceState.Active ||
          data.ProcessInstances[0].state === ProcessInstanceState.Suspended
        ) {
          return [
            <OverflowMenuItem isPersistent key={1}>
              <Button variant={ButtonVariant.secondary} onClick={handleAbort}>
                Abort
              </Button>
            </OverflowMenuItem>
          ];
        }
      }
    }
  };

  return (
    <>
      <Page>
        <PageSection isFilled={true}>
          <ProcessBulkModalComponent
            isModalLarge={false}
            isModalOpen={isAbortModalOpen}
            handleModalToggle={handleAbortModalToggle}
            checkedArray={[data.ProcessInstances[0].state]}
            modalTitle={
              titleType === 'success'
                ? setTitle(titleType, modalTitle)
                : setTitle(titleType, modalTitle)
            }
            isSingleAbort={true}
            abortedMessageObj={{
              [data.ProcessInstances[0].id]: data.ProcessInstances[0]
            }}
            completedMessageObj={{}}
            isAbortModalOpen={isAbortModalOpen}
          />
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
            modalTitle={
              titleType === 'success'
                ? setTitle(titleType, modalTitle)
                : setTitle(titleType, modalTitle)
            }
            modalContent={modalContent}
          />
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
            <GridItem span={10}>
              <Title headingLevel="h1" size="4xl">
                {data.ProcessInstances[0].processName}
              </Title>
            </GridItem>
            <GridItem span={2}>
              <React.Fragment>
                <OverflowMenu breakpoint="lg">
                  <OverflowMenuContent isPersistent>
                    <OverflowMenuGroup groupType="button" isPersistent>
                      {processManagementButtons()}
                    </OverflowMenuGroup>
                  </OverflowMenuContent>
                  {/* <OverflowMenuControl hasAdditionalOptions>
                    <Dropdown toggle={<KebabToggle />} isPlain />
                  </OverflowMenuControl> */}
                </OverflowMenu>
              </React.Fragment>
            </GridItem>
            <GridItem>
              <ProcessDetails
                loading={loading}
                data={data}
                handleSkip={handleSkip}
                handleRetry={handleRetry}
              />
            </GridItem>
            <GridItem>
              <ProcessDetailsProcessVariables loading={loading} data={data} />
            </GridItem>
            <GridItem>
              <ProcessDetailsTimeline
                loading={loading}
                data={data.ProcessInstances}
              />
            </GridItem>
          </Grid>
        </PageSection>
      </Page>
    </>
  );
};

export default ProcessDetailsPage;
