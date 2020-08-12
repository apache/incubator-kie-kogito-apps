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
  TitleSizes
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
import { Link, Redirect, RouteComponentProps } from 'react-router-dom';
import ProcessDetails from '../../Organisms/ProcessDetails/ProcessDetails';
import ProcessDetailsProcessVariables from '../../Organisms/ProcessDetailsProcessVariables/ProcessDetailsProcessVariables';
import ProcessDetailsTimeline from '../../Organisms/ProcessDetailsTimeline/ProcessDetailsTimeline';
import './ProcessDetailsPage.css';
import PageTitle from '../../Molecules/PageTitle/PageTitle';
import ProcessListModal from '../../Atoms/ProcessListModal/ProcessListModal';
import { handleAbort, setTitle } from '../../../utils/Utils';
import ProcessInstanceState = GraphQL.ProcessInstanceState;
import axios from 'axios';
import { SyncIcon, InfoCircleIcon } from '@patternfly/react-icons';

interface MatchProps {
  instanceID: string;
}

enum TitleType {
  SUCCESS = 'success',
  FAILURE = 'failure'
}

const ProcessDetailsPage: React.FC<RouteComponentProps<MatchProps, {}, {}> &
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
  const [variableError, setVariableError] = useState();
  const currentPage = JSON.parse(window.localStorage.getItem('state'));

  const { loading, error, data } = GraphQL.useGetProcessInstanceByIdQuery({
    variables: { id },
    fetchPolicy: 'network-only'
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
    try {
      await axios
        .post(
          `${data.ProcessInstances[0].endpoint}/${data.ProcessInstances[0].id}`,
          updateJson
        )
        .then(() => {
          setDisplayLabel(false);
          setDisplaySuccess(true);
          setTimeout(() => {
            setDisplaySuccess(false);
          }, 2000);
        });
    } catch (error) {
      setVariableError(error.message);
    }
  };

  const handleRefresh = () => {
    window.location.reload();
  };

  const handleErrorModal = () => {
    setErrorModalOpen(!errorModalOpen);
  };

  const handleRetry = () => {
    setErrorModalOpen(!errorModalOpen);
    setVariableError(null);
    // tslint:disable-next-line: no-floating-promises
    handleSave();
  };

  const handleDiscard = () => {
    setErrorModalOpen(!errorModalOpen);
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

  const updateVariables = () => {
    if (data.ProcessInstances[0].serviceUrl !== null && displayLabel) {
      return (
        <Button
          variant="secondary"
          id="save-button"
          className="kogito-management-console--details__buttonMargin"
          onClick={handleSave}
        >
          Save
        </Button>
      );
    } else {
      return (
        <Button
          variant="secondary"
          id="save-button"
          className="kogito-management-console--details__buttonMargin"
          isDisabled
        >
          Save
        </Button>
      );
    }
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
        <Button variant="secondary" id="abort-button" isDisabled>
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
              description: `Process instance with the id ${id} not found`,
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
                              {updateVariables()}
                              {abortButton()}
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
                            </>
                          </OverflowMenuGroup>
                        </OverflowMenuContent>
                      </OverflowMenu>
                    </SplitItem>
                  </Split>
                </GridItem>
                {currentPage && (
                  <GridItem>
                    <ProcessDetails data={data} from={currentPage} />
                  </GridItem>
                )}
                {Object.keys(updateJson).length > 0 && (
                  <GridItem>
                    <ProcessDetailsProcessVariables
                      displayLabel={displayLabel}
                      displaySuccess={displaySuccess}
                      setUpdateJson={setUpdateJson}
                      setDisplayLabel={setDisplayLabel}
                      updateJson={updateJson}
                    />
                  </GridItem>
                )}
                <GridItem>
                  <ProcessDetailsTimeline data={data.ProcessInstances[0]} />
                </GridItem>
                {errorModal()}
              </Grid>
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
