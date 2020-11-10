import {
  Breadcrumb,
  BreadcrumbItem,
  Grid,
  GridItem,
  PageSection,
  Title,
  Card,
  Bullseye,
  Button,
  Split,
  SplitItem,
  OverflowMenu,
  OverflowMenuContent,
  OverflowMenuGroup,
  ModalVariant,
  Modal,
  TitleSizes,
  Flex,
  FlexItem
} from '@patternfly/react-core';
import {
  ServerErrors,
  ouiaPageTypeAndObjectId,
  ItemDescriptor,
  KogitoSpinner,
  GraphQL,
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/common';
import React, { useState, useEffect } from 'react';
import _ from 'lodash';
import { Link, Redirect, RouteComponentProps } from 'react-router-dom';
import ProcessDetails from '../../Organisms/ProcessDetails/ProcessDetails';
import ProcessDetailsProcessVariables from '../../Organisms/ProcessDetailsProcessVariables/ProcessDetailsProcessVariables';
import ProcessDetailsTimeline from '../../Organisms/ProcessDetailsTimeline/ProcessDetailsTimeline';
import ProcessDetailsMilestones from '../../Organisms/ProcessDetailsMilestones/ProcessDetailsMilestones';
import './ProcessDetailsPage.css';
import PageTitle from '../../Molecules/PageTitle/PageTitle';
import ProcessListModal from '../../Atoms/ProcessListModal/ProcessListModal';
import {
  handleAbort,
  setTitle,
  handleVariableUpdate
} from '../../../utils/Utils';
import ProcessInstanceState = GraphQL.ProcessInstanceState;
import { SyncIcon, InfoCircleIcon } from '@patternfly/react-icons';
import ProcessDetailsJobsPanel from '../../Organisms/ProcessDetailsJobsPanel/ProcessDetailsJobsPanel';
import { StaticContext } from 'react-router';
import * as H from 'history';
import ProcessDetailsNodeTrigger from '../../Organisms/ProcessDetailsNodeTrigger/ProcessDetailsNodeTrigger';

interface MatchProps {
  instanceID: string;
}

enum TitleType {
  SUCCESS = 'success',
  FAILURE = 'failure'
}

const ProcessDetailsPage: React.FC<RouteComponentProps<
  MatchProps,
  StaticContext,
  H.LocationState
> &
  OUIAProps> = ({ ouiaId, ouiaSafe, ...props }) => {
  const id = props.match.params.instanceID;
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [modalTitle, setModalTitle] = useState<string>('');
  const [titleType, setTitleType] = useState<string>('');
  const [modalContent, setModalContent] = useState<string>('');
  const [updateJson, setUpdateJson] = useState<any>({});
  const [displayLabel, setDisplayLabel] = useState(false);
  const [displaySuccess, setDisplaySuccess] = useState(false);
  const [errorModalOpen, setErrorModalOpen] = useState(false);
  const [confirmationModal, setConfirmationModal] = useState(false);
  const [variableError, setVariableError] = useState();
  let currentPage = JSON.parse(window.localStorage.getItem('state'));

  const { loading, error, data } = GraphQL.useGetProcessInstanceByIdQuery({
    variables: { id },
    fetchPolicy: 'network-only'
  });

  const getJobs = GraphQL.useGetJobsByProcessInstanceIdQuery({
    variables: {
      processInstanceId: id
    }
  });

  const handleModalToggle = () => {
    setIsModalOpen(!isModalOpen);
  };

  useEffect(() => {
    window.onpopstate = () => {
      props.history.push({ state: { ...props.location.state } });
    };
  });

  useEffect(() => {
    return ouiaPageTypeAndObjectId('process-instances', id);
  });

  const onShowMessage = (
    title: string,
    content: string,
    type: TitleType
  ): void => {
    setTitleType(type);
    setModalTitle(title);
    setModalContent(content);
    handleModalToggle();
  };
  const onAbortClick = async () => {
    await handleAbort(
      data.ProcessInstances[0],
      () =>
        onShowMessage(
          'Abort operation',
          `The process ${data.ProcessInstances[0].processName} was successfully aborted.`,
          TitleType.SUCCESS
        ),
      (errorMessage: string) =>
        onShowMessage(
          'Abort operation',
          `Failed to abort process ${data.ProcessInstances[0].processName}. Message: ${errorMessage}`,
          TitleType.FAILURE
        )
    );
  };

  useEffect(() => {
    if (data) {
      setUpdateJson(JSON.parse(data.ProcessInstances[0].variables));
    }
  }, [data]);

  useEffect(() => {
    if (variableError && variableError.length > 0) {
      setErrorModalOpen(true);
    }
  }, [variableError]);

  const handleSave = async () => {
    setDisplaySuccess(false);
    await handleVariableUpdate(
      data.ProcessInstances[0],
      updateJson,
      setDisplayLabel,
      setDisplaySuccess,
      setUpdateJson,
      setVariableError
    );
  };

  const handleRefresh = () => {
    if (displayLabel === true) {
      setConfirmationModal(true);
    } else {
      window.location.reload();
    }
  };

  const handleConfirmationModal = () => {
    setConfirmationModal(!confirmationModal);
  };

  const handleConfirm = () => {
    window.location.reload();
    handleConfirmationModal();
  };

  const handleCancel = () => {
    handleConfirmationModal();
  };

  const RenderConfirmationModal = () => {
    return (
      <Modal
        title=""
        header={
          <>
            <Title headingLevel="h1" size={TitleSizes['2xl']}>
              <InfoCircleIcon
                className="pf-u-mr-sm"
                color="var(--pf-global--warning-color--100)"
              />
              Refresh
            </Title>
          </>
        }
        variant={ModalVariant.small}
        isOpen={confirmationModal}
        onClose={handleConfirmationModal}
        actions={[
          <Button
            key="Ok"
            variant="primary"
            id="confirm-button"
            onClick={handleConfirm}
          >
            Ok
          </Button>,
          <Button
            key="Cancel"
            variant="link"
            id="cancel-button"
            onClick={handleCancel}
          >
            Cancel
          </Button>
        ]}
        aria-label="Confirmation modal"
        aria-labelledby="Confirmation modal"
      >
        This action discards changes made on process variables.
      </Modal>
    );
  };

  const handleErrorModal = () => {
    setErrorModalOpen(!errorModalOpen);
  };

  const handleRetry = () => {
    handleErrorModal();
    setVariableError(null);
    // tslint:disable-next-line: no-floating-promises
    handleSave();
  };

  const handleDiscard = () => {
    handleErrorModal();
    handleRefresh();
  };

  const errorModal = () => {
    return (
      <Modal
        title=""
        header={
          <>
            <Title headingLevel="h1" size={TitleSizes['2xl']}>
              <InfoCircleIcon
                className="pf-u-mr-sm"
                color="var(--pf-global--danger-color--100)"
              />
              Error
            </Title>
          </>
        }
        variant={ModalVariant.small}
        isOpen={errorModalOpen}
        onClose={handleErrorModal}
        actions={[
          <Button
            key="Retry"
            variant="primary"
            id="retry-button"
            onClick={handleRetry}
          >
            Retry
          </Button>,
          <Button
            key="Discard"
            variant="link"
            id="discard-button"
            onClick={handleDiscard}
          >
            Discard
          </Button>
        ]}
        aria-label="Error modal"
        aria-labelledby="Error modal"
      >
        {variableError}
      </Modal>
    );
  };

  const updateVariablesButton = () => {
    if (data.ProcessInstances[0].serviceUrl !== null) {
      return (
        <Button
          variant="secondary"
          id="save-button"
          className="kogito-management-console--details__buttonMargin"
          onClick={handleSave}
          isDisabled={!displayLabel}
        >
          Save
        </Button>
      );
    }
  };

  const refreshButton = () => {
    return (
      <Button
        variant="plain"
        onClick={() => {
          handleRefresh();
        }}
        id="refresh-button"
        aria-label={'Refresh list'}
      >
        <SyncIcon />
      </Button>
    );
  };
  const abortButton = () => {
    if (
      (data.ProcessInstances[0].state === ProcessInstanceState.Active ||
        data.ProcessInstances[0].state === ProcessInstanceState.Error ||
        data.ProcessInstances[0].state === ProcessInstanceState.Suspended) &&
      data.ProcessInstances[0].addons.includes('process-management') &&
      data.ProcessInstances[0].serviceUrl !== null
    ) {
      return (
        <Button variant="secondary" id="abort-button" onClick={onAbortClick}>
          Abort
        </Button>
      );
    } else {
      return (
        <Button variant="secondary" isDisabled>
          Abort
        </Button>
      );
    }
  };
  let prevPath;
  const BreadCrumb = [];
  let BreadCrumbRoute = [];
  if (data) {
    const result = data.ProcessInstances;
    /* istanbul ignore else */
    if (currentPage) {
      currentPage = { ...currentPage, ...props.location.state };
      const tempPath = currentPage.prev.split('/');
      prevPath = tempPath.filter(item => item);
      BreadCrumb.push(...prevPath);
      let sum = '';
      BreadCrumbRoute = BreadCrumb.map(elem => (sum = sum + `/${elem}`));
    }
    if (result.length === 0) {
      return (
        <Redirect
          to={{
            pathname: '/NoData',
            state: {
              prev: currentPage ? currentPage.prev : '/ProcessInstances',
              title: 'Process not found',
              description: `Process instance with the id ${id} not found`,
              buttonText: currentPage
                ? `Go to ${prevPath[0]
                    .replace(/([A-Z])/g, ' $1')
                    .trim()
                    .toLowerCase()}`
                : 'Go to process instances',
              rememberedData: { ...props.location.state }
            }
          }}
        />
      );
    }
  }

  return (
    <div {...componentOuiaProps(ouiaId, 'ProcessDetailsPage', ouiaSafe)}>
      {!error ? (
        <>
          <PageSection variant="light">
            <ProcessListModal
              isModalOpen={isModalOpen}
              handleModalToggle={handleModalToggle}
              modalTitle={setTitle(titleType, modalTitle)}
              modalContent={modalContent}
              processName={
                data &&
                data.ProcessInstances &&
                data.ProcessInstances[0].processName
              }
            />
            <PageTitle title="Process Details" />
            {!loading ? (
              <Grid hasGutter md={1} span={12} lg={6} xl={4}>
                <GridItem span={12}>
                  <Breadcrumb>
                    <BreadcrumbItem>
                      <Link to={'/'}>Home</Link>
                    </BreadcrumbItem>
                    {BreadCrumb.map((item, index) => {
                      // checking the url if it contains /ProcessInstances to return the state back
                      if (
                        index === 1 ||
                        (index === 0 && item === 'ProcessInstances')
                      ) {
                        return (
                          <BreadcrumbItem key={index}>
                            <Link
                              to={
                                props.location.state && {
                                  pathname: BreadCrumbRoute[index],
                                  state: { ...props.location.state }
                                }
                              }
                            >
                              {item.replace(/([A-Z])/g, ' $1').trim()}
                            </Link>
                          </BreadcrumbItem>
                        );
                      } else {
                        return (
                          <BreadcrumbItem key={index}>
                            <Link to={BreadCrumbRoute[index]}>
                              {item.replace(/([A-Z])/g, ' $1').trim()}
                            </Link>
                          </BreadcrumbItem>
                        );
                      }
                    })}
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
              <>
                <Grid hasGutter md={1} span={12} lg={6} xl={4}>
                  <GridItem span={12}>
                    <Split
                      hasGutter={true}
                      component={'div'}
                      className="pf-u-align-items-center"
                    >
                      <SplitItem isFilled={true}>
                        <Title
                          headingLevel="h2"
                          size="4xl"
                          className="kogito-management-console--details__title"
                        >
                          <ItemDescriptor
                            itemDescription={{
                              id: data.ProcessInstances[0].id,
                              name: data.ProcessInstances[0].processName,
                              description: data.ProcessInstances[0].businessKey
                            }}
                          />
                        </Title>
                      </SplitItem>
                      <SplitItem>
                        <OverflowMenu breakpoint="lg">
                          <OverflowMenuContent isPersistent>
                            <OverflowMenuGroup groupType="button" isPersistent>
                              <>
                                {updateVariablesButton()}
                                {abortButton()}
                                {refreshButton()}
                              </>
                            </OverflowMenuGroup>
                          </OverflowMenuContent>
                        </OverflowMenu>
                      </SplitItem>
                    </Split>
                  </GridItem>
                </Grid>
                <Flex>
                  <Flex
                    direction={{ default: 'column' }}
                    flex={{ default: 'flex_1' }}
                  >
                    {currentPage && (
                      <FlexItem>
                        <ProcessDetails data={data} from={currentPage} />
                      </FlexItem>
                    )}
                    {data.ProcessInstances[0].milestones.length > 0 && (
                      <FlexItem>
                        <ProcessDetailsMilestones
                          milestones={data.ProcessInstances[0].milestones}
                        />
                      </FlexItem>
                    )}
                  </Flex>
                  <Flex
                    direction={{ default: 'column' }}
                    flex={{ default: 'flex_1' }}
                  >
                    {Object.keys(updateJson).length > 0 && (
                      <FlexItem>
                        <ProcessDetailsProcessVariables
                          displayLabel={displayLabel}
                          displaySuccess={displaySuccess}
                          setUpdateJson={setUpdateJson}
                          setDisplayLabel={setDisplayLabel}
                          updateJson={updateJson}
                        />
                      </FlexItem>
                    )}
                  </Flex>
                  <Flex
                    direction={{ default: 'column' }}
                    flex={{ default: 'flex_1' }}
                  >
                    {!getJobs.loading && (
                      <FlexItem>
                        <ProcessDetailsTimeline
                          data={data.ProcessInstances[0]}
                          jobsResponse={_.pick(getJobs, [
                            'data',
                            'loading',
                            'refetch'
                          ])}
                        />
                      </FlexItem>
                    )}
                    <FlexItem>
                      <ProcessDetailsJobsPanel
                        jobsResponse={_.pick(getJobs, [
                          'data',
                          'loading',
                          'refetch'
                        ])}
                      />
                    </FlexItem>
                    {data.ProcessInstances[0].addons.includes(
                      'process-management'
                    ) &&
                      data.ProcessInstances[0].state !==
                        GraphQL.ProcessInstanceState.Completed &&
                      data.ProcessInstances[0].state !==
                        GraphQL.ProcessInstanceState.Aborted && (
                        <FlexItem>
                          <ProcessDetailsNodeTrigger
                            processInstanceData={data.ProcessInstances[0]}
                          />
                        </FlexItem>
                      )}
                  </Flex>
                  {errorModal()}
                  {RenderConfirmationModal()}
                </Flex>
              </>
            ) : (
              <Card>
                <Bullseye>
                  <KogitoSpinner spinnerText="Loading process details..." />
                </Bullseye>
              </Card>
            )}
          </PageSection>
        </>
      ) : (
        <ServerErrors error={error} variant="large" />
      )}
    </div>
  );
};

export default ProcessDetailsPage;
