import Moment from 'react-moment';
import React, { useCallback, useState, useEffect } from 'react';
import axios from 'axios';
import {
  Button,
  DataListAction,
  DataListCell,
  DataListCheck,
  DataListContent,
  DataListItem,
  DataListItemCells,
  DataListItemRow,
  DataListToggle,
  Dropdown,
  DropdownItem,
  DropdownPosition,
  Bullseye,
  KebabToggle,
  Tooltip,
  TextContent,
  Text,
  TextVariants
} from '@patternfly/react-core';
import { Link } from 'react-router-dom';
import SpinnerComponent from '../../Atoms/SpinnerComponent/SpinnerComponent';
import {
  useGetChildInstancesLazyQuery,
  ProcessInstance,
  ProcessInstanceState
} from '../../../graphql/types';
import EmptyStateComponent from '../../Atoms/EmptyStateComponent/EmptyStateComponent';
import {
  OnRunningIcon,
  CheckCircleIcon,
  BanIcon,
  PausedIcon,
  ErrorCircleOIcon,
  ExternalLinkAltIcon,
  HistoryIcon,
  InfoCircleIcon
} from '@patternfly/react-icons';
import ErrorPopover from '../../Atoms/ErrorPopoverComponent/ErrorPopoverComponent';
import ProcessBulkModalComponent from '../../Atoms/ProcessBulkModalComponent/ProcessBulkModalComponent';
import ProcessDescriptor from '../ProcessDescriptor/ProcessDescriptor';
import DisablePopup from '../DiablePopup/DisablePopup';
interface IOwnProps {
  id: number;
  processInstanceData: ProcessInstance;
  checkedArray: string[];
  initData: any;
  setInitData: any;
  loadingInitData: boolean;
  abortedObj: any;
  setAbortedObj: any;
  setIsAllChecked: any;
  setSelectedNumber: (selectedNumber: number) => void;
  selectedNumber: number;
}

const DataListItemComponent: React.FC<IOwnProps> = ({
  processInstanceData,
  checkedArray,
  initData,
  setInitData,
  loadingInitData,
  abortedObj,
  setAbortedObj,
  setIsAllChecked,
  selectedNumber,
  setSelectedNumber
}) => {
  const [expanded, setexpanded] = useState([]);
  const [isOpen, setisOpen] = useState(false);
  const [isLoaded, setisLoaded] = useState(false);
  const [isPopoverOpen, setIsPopoverOpen] = useState<boolean>(false);
  const [modalTitle, setModalTitle] = useState('');
  const [modalContent, setModalContent] = useState('');
  const [isErrorModalOpen, setIsErrorModalOpen] = useState(false);
  const [isSkipModalOpen, setIsSkipModalOpen] = useState(false);
  const [isRetryModalOpen, setIsRetryModalOpen] = useState(false);
  const [isAbortModalOpen, setIsAbortModalOpen] = useState(false);
  const [titleType, setTitleType] = useState('');
  const isChecked = 'isChecked';
  const [getChildInstances, { loading, data }] = useGetChildInstancesLazyQuery({
    fetchPolicy: 'network-only'
  });

  const currentPage = { prev: location.pathname}
  window.localStorage.setItem('state', JSON.stringify(currentPage))
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

  const stateIconCreator = state => {
    switch (state) {
      case ProcessInstanceState.Active:
        return (
          <>
            <OnRunningIcon className="pf-u-mr-sm" />
            Active
          </>
        );
      case ProcessInstanceState.Completed:
        return (
          <>
            <CheckCircleIcon
              className="pf-u-mr-sm"
              color="var(--pf-global--success-color--100)"
            />
            Completed
          </>
        );
      case ProcessInstanceState.Aborted:
        return (
          <>
            <BanIcon className="pf-u-mr-sm" />
            Aborted
          </>
        );
      case ProcessInstanceState.Suspended:
        return (
          <>
            <PausedIcon className="pf-u-mr-sm" />
            Suspended
          </>
        );
      case ProcessInstanceState.Error:
        return (
          <>
            <ErrorCircleOIcon
              className="pf-u-mr-sm"
              color="var(--pf-global--danger-color--100)"
            />
            Error
          </>
        );
    }
  };

  const handleSkipModalToggle = () => {
    setIsSkipModalOpen(!isSkipModalOpen);
    setIsErrorModalOpen(false);
  };

  const handleRetryModalToggle = () => {
    setIsRetryModalOpen(!isRetryModalOpen);
    setIsErrorModalOpen(false);
  };

  const handleAbortModalToggle = () => {
    setIsAbortModalOpen(!isAbortModalOpen);
  };

  const handleErrorModalToggle = () => {
    setModalTitle('Error');
    setTitleType('failure');
    setModalContent(
      processInstanceData.error
        ? processInstanceData.error.message
        : 'No error message found'
    );
    setIsErrorModalOpen(!isErrorModalOpen);
  };

  const handleSkip = useCallback(async (_processID, _instanceID, _endpoint) => {
    const processInstanceId = processInstanceData.id;
    const processId = processInstanceData.processId;
    try {
      setModalTitle('Skip operation');
      await axios.post(
        `${processInstanceData.serviceUrl}/management/processes/${processId}/instances/${processInstanceId}/skip`
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
  }, []);

  const handleRetry = useCallback(
    async (_processID, _instanceID, _endpoint) => {
      const processInstanceId = processInstanceData.id;
      const processId = processInstanceData.processId;
      try {
        setModalTitle('Retry operation');
        await axios.post(
          `${processInstanceData.serviceUrl}/management/processes/${processId}/instances/${processInstanceId}/retrigger`
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
    },
    []
  );

  const handleAbortActiveInstances = useCallback(
    (processInstanceId, processId) => {
      setModalTitle('Abort operation');
      axios
        .delete(
          `${processInstanceData.serviceUrl}/management/processes/${processId}/instances/${processInstanceId}`
        )
        .then(() => {
          setModalTitle('Process aborted');
          setModalContent(`${processId} - process execution has been aborted.`);
          setTitleType('success');
          processInstanceData.state = ProcessInstanceState.Aborted;
          handleAbortModalToggle();
        })
        .catch(() => {
          setTitleType('failure');
          handleAbortModalToggle();
        });
    },
    []
  );

  const onSelect = event => {
    setisOpen(isOpen ? false : true);
  };
  const onToggle = _isOpen => {
    setisOpen(_isOpen);
  };

  const toggle = async _id => {
    const copyOfInitData = { ...initData };
    copyOfInitData.ProcessInstances.map(instance => {
      if (instance.id === processInstanceData.id) {
        if (instance.isOpen) {
          instance.isOpen = false;
        } else {
          instance.isOpen = true;
        }
      }
    });
    const index = expanded.indexOf(_id);
    const newExpanded =
      index >= 0
        ? [
            ...expanded.slice(0, index),
            ...expanded.slice(index + 1, expanded.length)
          ]
        : [...expanded, _id];
    setexpanded(newExpanded);

    if (!isLoaded) {
      getChildInstances({
        variables: {
          rootProcessInstanceId: processInstanceData.id
        }
      });
    }
  };

  const onCheckBoxClick = () => {
    const copyOfInitData = { ...initData };
    let copyOfAbortedObject = { ...abortedObj };
    copyOfInitData.ProcessInstances.map(instanceData => {
      if (instanceData.id === processInstanceData.id) {
        if (instanceData.isChecked) {
          if (abortedObj[instanceData.id] !== undefined) {
            delete copyOfAbortedObject[instanceData.id];
            setSelectedNumber(selectedNumber > 0 && selectedNumber - 1);
          }
          instanceData.isChecked = false;
        } else {
          const tempObj = {};
          tempObj[instanceData.id] = instanceData;
          copyOfAbortedObject = { ...copyOfAbortedObject, ...tempObj };
          instanceData.isChecked = true;
          setSelectedNumber(selectedNumber + 1);
        }
      }
      if (instanceData.childDataList !== undefined) {
        instanceData.childDataList.map(child => {
          if (child.id === processInstanceData.id) {
            if (child.isChecked) {
              if (copyOfAbortedObject[child.id] !== undefined) {
                delete copyOfAbortedObject[child.id];
                setSelectedNumber(selectedNumber > 0 && selectedNumber - 1);
              }
              child.isChecked = false;
            } else {
              const tempObj = {};
              tempObj[child.id] = child;
              copyOfAbortedObject = { ...copyOfAbortedObject, ...tempObj };
              setSelectedNumber(selectedNumber + 1);
              child.isChecked = true;
            }
          }
        });
      }
    });
    lengthChecker(copyOfInitData);
    setInitData(copyOfInitData);
    setAbortedObj(copyOfAbortedObject);
  };
  const lengthChecker = copyOfData => {
    let totalLength = 0;
    let isCheckedLength = 0;
    copyOfData.ProcessInstances.map(instance => {
      if (
        instance.addons.includes('process-management') &&
        instance.serviceUrl !== null
      ) {
        totalLength += 1;
        if (instance.isChecked) {
          isCheckedLength += 1;
        }
      }

      if (instance.childDataList !== undefined) {
        instance.childDataList.map(child => {
          if (
            child.addons.includes('process-management') &&
            instance.serviceUrl !== null
          ) {
            totalLength += 1;
            if (child.isChecked) {
              isCheckedLength += 1;
            }
          }
        });
      }
    });
    if (isCheckedLength === totalLength) {
      setIsAllChecked(true);
    } else {
      setIsAllChecked(false);
    }
  };
  useEffect(() => {
    if (data !== undefined && !loading && !loadingInitData) {
      data.ProcessInstances.map((instance: any) => {
        instance.isChecked = false;
      });
      const copyOfInitData = { ...initData };
      copyOfInitData.ProcessInstances.map(instanceData => {
        if (instanceData.id === processInstanceData.id) {
          instanceData.childDataList = data.ProcessInstances;
        }
      });
      setInitData(copyOfInitData);
      setisLoaded(true);
    }
  }, [data]);

  const dropDownList = () => {
    if (
      processInstanceData.addons.includes('process-management') &&
      processInstanceData.serviceUrl !== null
    ) {
      if (processInstanceData.state === 'ERROR') {
        return [
          <DropdownItem
            key={1}
            onClick={() =>
              handleRetry(
                processInstanceData.processId,
                processInstanceData.id,
                processInstanceData.endpoint
              )
            }
          >
            Retry
          </DropdownItem>,
          <DropdownItem
            key={2}
            onClick={() =>
              handleSkip(
                processInstanceData.processId,
                processInstanceData.id,
                processInstanceData.endpoint
              )
            }
          >
            Skip
          </DropdownItem>,
          <DropdownItem
            key={4}
            onClick={() =>
              handleAbortActiveInstances(
                processInstanceData.id,
                processInstanceData.processId
              )
            }
          >
            Abort
          </DropdownItem>
        ];
      } else {
        return [
          <DropdownItem
            key={4}
            onClick={() =>
              handleAbortActiveInstances(
                processInstanceData.id,
                processInstanceData.processId
              )
            }
          >
            Abort
          </DropdownItem>
        ];
      }
    } else {
      return [];
    }
  };

  return (
    <React.Fragment>
      <ProcessBulkModalComponent
        isModalLarge={false}
        isModalOpen={isAbortModalOpen}
        handleModalToggle={handleAbortModalToggle}
        checkedArray={checkedArray}
        modalTitle={
          titleType === 'success'
            ? setTitle(titleType, modalTitle)
            : setTitle(titleType, modalTitle)
        }
        isSingleAbort={true}
        abortedMessageObj={{
          [processInstanceData.id]: processInstanceData
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
        checkedArray={checkedArray}
        modalTitle={
          titleType === 'success'
            ? setTitle(titleType, modalTitle)
            : setTitle(titleType, modalTitle)
        }
        modalContent={modalContent}
      />
      <DataListItem
        aria-labelledby="kie-datalist-item"
        isExpanded={expanded.includes('kie-datalist-toggle')}
      >
        <DataListItemRow>
          {processInstanceData.parentProcessInstanceId === null && (
            <DataListToggle
              onClick={() => toggle('kie-datalist-toggle')}
              isExpanded={expanded.includes('kie-datalist-toggle')}
              id="kie-datalist-toggle"
              aria-controls="kie-datalist-expand"
            />
          )}
          {processInstanceData.addons.includes('process-management') &&
          processInstanceData.serviceUrl !== null ? (
            <DataListCheck
              aria-labelledby="width-kie-datalist-item"
              name="width-kie-datalist-item"
              checked={processInstanceData[isChecked]}
              onChange={() => {
                onCheckBoxClick();
              }}
            />
          ) : (
            <DisablePopup
              processInstanceData={processInstanceData}
              component={
                <DataListCheck
                  aria-labelledby="width-kie-datalist-item"
                  name="width-kie-datalist-item"
                  isDisabled={true}
                />
              }
            />
          )}
          <DataListItemCells
            dataListCells={[
              <DataListCell key={1}>
                <Link to={'/Process/' + processInstanceData.id}>
                  <div>
                    <strong>
                      <ProcessDescriptor
                        processInstanceData={processInstanceData}
                      />
                    </strong>
                  </div>
                </Link>
                {!processInstanceData.rootProcessInstanceId &&
                processInstanceData.serviceUrl !== null ? (
                  <Button
                    component={'a'}
                    variant={'link'}
                    target={'_blank'}
                    href={`${processInstanceData.serviceUrl}`}
                    isInline={true}
                  >
                    Endpoint{<ExternalLinkAltIcon className="pf-u-ml-xs" />}
                  </Button>
                ) : (
                  <Tooltip content="This Kogito runtime is missing the kogito.service.url property. Contact your administrator to set up.">
                    <>
                      <span
                        style={{
                          color: 'var(--pf-global--disabled-color--100)'
                        }}
                      >
                        {' '}
                        Endpoint
                      </span>
                      {
                        <ExternalLinkAltIcon
                          className="pf-u-ml-xs"
                          color="var(--pf-global--disabled-color--100)"
                        />
                      }
                    </>
                  </Tooltip>
                )}
              </DataListCell>,
              <DataListCell key={4}>
                {processInstanceData.state === 'ERROR' ? (
                  <ErrorPopover
                    isPopoverOpen={isPopoverOpen}
                    handleRetry={handleRetry}
                    handleSkip={handleSkip}
                    setIsPopoverOpen={setIsPopoverOpen}
                    processInstanceData={processInstanceData}
                    stateIconCreator={stateIconCreator}
                  />
                ) : (
                  stateIconCreator(processInstanceData.state)
                )}
              </DataListCell>,
              <DataListCell key={2}>
                {processInstanceData.start ? (
                  <Moment fromNow>
                    {new Date(`${processInstanceData.start}`)}
                  </Moment>
                ) : (
                  ''
                )}
              </DataListCell>,
              <DataListCell key={3}>
                {processInstanceData.lastUpdate ? (
                  <span>
                    {' '}
                    <HistoryIcon className="pf-u-mr-sm" /> Updated{' '}
                    <Moment fromNow>
                      {new Date(`${processInstanceData.lastUpdate}`)}
                    </Moment>
                  </span>
                ) : (
                  ''
                )}
              </DataListCell>
            ]}
          />
          <DataListAction
            aria-labelledby="kie-datalist-item kie-datalist-action"
            id="kie-datalist-action"
            aria-label="Actions"
          >
            {processInstanceData.state === 'ERROR' ||
            processInstanceData.state === 'ACTIVE' ||
            processInstanceData.state === 'SUSPENDED' ? (
              <Dropdown
                isPlain
                position={DropdownPosition.right}
                isOpen={isOpen}
                onSelect={onSelect}
                toggle={
                  <KebabToggle
                    isDisabled={dropDownList().length === 0}
                    onToggle={onToggle}
                  />
                }
                dropdownItems={dropDownList()}
              />
            ) : (
              <Dropdown
                isPlain
                position={DropdownPosition.right}
                isOpen={isOpen}
                onSelect={onSelect}
                toggle={<KebabToggle isDisabled onToggle={onToggle} />}
                dropdownItems={[]}
              />
            )}
            <ProcessBulkModalComponent
              modalTitle={setTitle(titleType, modalTitle)}
              isModalLarge={true}
              isModalOpen={isErrorModalOpen}
              handleModalToggle={handleErrorModalToggle}
              modalContent={modalContent}
              handleSkip={handleSkip}
              handleRetry={handleRetry}
              isAddonPresent={
                processInstanceData &&
                processInstanceData.addons.includes('process-management')
              }
              checkedArray={checkedArray}
              handleSkipModalToggle={handleSkipModalToggle}
              handleRetryModalToggle={handleRetryModalToggle}
            />
          </DataListAction>
        </DataListItemRow>
        <DataListContent
          aria-label="Primary Content Details"
          id="kie-datalist-expand1"
          isHidden={!expanded.includes('kie-datalist-toggle')}
          className="kogito-management-console__embedded-list pf-m-compact"
        >
          {isLoaded &&
            !loading &&
            !loadingInitData &&
            initData.ProcessInstances.map((instance, idx) => {
              if (instance.id === processInstanceData.id) {
                if (instance.childDataList.length === 0) {
                  return (
                    <EmptyStateComponent
                      key={idx}
                      iconType="infoCircleIcon"
                      title="No child process instances"
                      body="This process has no related sub processes"
                    />
                  );
                } else {
                  return instance.childDataList.map((child, index) => {
                    return (
                      <DataListItemComponent
                        id={index}
                        key={child.id}
                        processInstanceData={child}
                        checkedArray={checkedArray}
                        initData={initData}
                        setInitData={setInitData}
                        loadingInitData={loading}
                        abortedObj={abortedObj}
                        setAbortedObj={setAbortedObj}
                        setIsAllChecked={setIsAllChecked}
                        selectedNumber={selectedNumber}
                        setSelectedNumber={setSelectedNumber}
                      />
                    );
                  });
                }
              }
            })}
          {!isLoaded && (
            <Bullseye>
              <SpinnerComponent spinnerText="Loading process instances..." />
            </Bullseye>
          )}
        </DataListContent>
      </DataListItem>
    </React.Fragment>
  );
};

export default DataListItemComponent;
