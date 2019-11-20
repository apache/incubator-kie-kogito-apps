import { TimeAgo } from '@n1ru4l/react-time-ago';
import React, { useState, useCallback } from 'react';
import gql from 'graphql-tag';
import axios from 'axios';
import {
  Alert, 
  AlertActionCloseButton, 
  DataListItem,
  DataListItemRow,
  DataListToggle,
  DataListItemCells,
  DataListCell,
  DataListCheck,
  Button,
  DataListAction,
  Dropdown,
  KebabToggle,
  DropdownItem,
  DataListContent,
  DropdownPosition,
  Modal
} from '@patternfly/react-core';
import { Link } from 'react-router-dom';
import { useApolloClient } from 'react-apollo';
/* tslint:disable:no-string-literal */
export interface IOwnProps {
  id: number;
  instanceID: string;
  instanceState: string;
  processID: string;
  parentInstanceID: string | null;
  processName: string;
  start:string;
  state:string;
  managementEnabled: boolean;
  endpoint: string;
}

const DataListItemComponent: React.FC<IOwnProps> = ({ id, instanceID, instanceState, state, managementEnabled, processID, endpoint, parentInstanceID, processName,start }) => {
  const [expanded, setexpanded] = useState(['kie-datalist-toggle']);
  const [isOpen, setisOpen] = useState(false);
  const [isLoaded, setisLoaded] = useState(false);
  const [isChecked, setisChecked] = useState(false);
  const [childList, setchildList] = useState([]);
  const [openModal, setOpenModal] = useState(false);
  const [alertVisible, setAlertVisible] = useState(false);
  const [error, setError] = useState('');
  const [alertTitle, setAlertTitle] = useState('');
  const [alertType, setAlertType] = useState(null);
  const [alertMessage, setAlertMessage] = useState('');

  const client = useApolloClient();

  const GET_CHILD_INSTANCES = gql`
    query getChildInstances($instanceId: [String!]) {
      ProcessInstances(filter: { parentProcessInstanceId: $instanceId }) {
        id
        processId
        processName
        parentProcessInstanceId
        roles
        state
        start
        endpoint
      }
    }
  `;
  const handleViewError = useCallback(async (_processID, _instanceID, _endpoint) => {
    const processInstanceId = instanceID;
    const processId = processID;
    setOpenModal(true);
    const result = await axios.get(`${endpoint}/management/process/${processId}/instances/${processInstanceId}/error`);
    setError(result.data);
  },[])

  const handleSkip = useCallback(async (_processID, _instanceID, _endpoint) => {
    const processInstanceId = instanceID;
    const processId = processID;
    
    try {
      const result = await axios.get(`${endpoint}/management/process/${processId}/instances/${processInstanceId}/skip`);
      setAlertTitle('Skip operation');
      setAlertType('success');
      setAlertMessage('Process execution has successfully skipped node which was in error state.');
      setAlertVisible(true);
    }
    catch(error) {
      setAlertTitle('Skip operation');
      setAlertType('danger');
      setAlertMessage('Process execution failed to skip node which in error state. Message: ' + JSON.stringify(error.message));
      setAlertVisible(true);
    }
  },[])
  
  const handleRetry = useCallback(async (_processID, _instanceID, _endpoint) => {
    const processInstanceId = instanceID;
    const processId = processID;
    try {
      const result = await axios.get(`${endpoint}/management/process/${processId}/instances/${processInstanceId}/retrigger`);
      setAlertTitle('Retry operation');
      setAlertType('success');
      setAlertMessage('Process execution has successfully re executed node which was in error state.');
      setAlertVisible(true);
    }
    catch(error) {
      setAlertTitle('Retry operation');
      setAlertType('danger');
      setAlertMessage('Process execution failed to re executed node which is error state. Message: ' + JSON.stringify(error.message));
      setAlertVisible(true);
    }
    
  },[])
  const onSelect = event => {
    setisOpen(isOpen ? false : true);
  };
  const onCheckBoxClick = () => {
    setisChecked(isChecked ? false : true);
  };

  const onToggle = _isOpen => {
    setisOpen(_isOpen);
  };

  const handleModalToggle = () => {
    setOpenModal(!openModal)
  }

  const closeAlert = () => {
    setAlertVisible(false);
  }
  
  const toggle = async _id => {
    const index = expanded.indexOf(_id);
    const newExpanded =
      index >= 0 ? [...expanded.slice(0, index), ...expanded.slice(index + 1, expanded.length)] : [...expanded, _id];
    setexpanded(newExpanded);
    if (!isLoaded){
      const data = await client.query({
        query: GET_CHILD_INSTANCES,
        variables: {
          instanceId: [instanceID]
        },
        fetchPolicy:'network-only'
      });
      setchildList(data['data']);
      setisLoaded(true);
    }
  };
  const handleSkipButton = async () => {
    setOpenModal(!openModal);
    await handleSkip(processID, instanceID, endpoint);
  }

  const handleRetryButton = async () => {
    setOpenModal(!openModal);
    await handleRetry(processID, instanceID, endpoint);
  }
  
  return (
    <React.Fragment>
     {alertVisible && (
        <Alert
            variant={alertType}
            title={alertTitle}
            action={<AlertActionCloseButton onClose={() => closeAlert()} />}
          >
            {alertMessage}
          </Alert>)}
      <DataListItem aria-labelledby="kie-datalist-item" isExpanded={expanded.includes('kie-datalist-toggle')}>
        <DataListItemRow>
          <DataListToggle
            onClick={() => toggle('kie-datalist-toggle')}
            isExpanded={expanded.includes('kie-datalist-toggle')}
            id="kie-datalist-toggle"
            aria-controls="kie-datalist-expand"
          />
          <DataListCheck
            aria-labelledby="width-kie-datalist-item"
            name="width-kie-datalist-item"
            checked={isChecked}
            onChange={() => {
              onCheckBoxClick();
            }}
          />
          <DataListItemCells
            dataListCells={[
              <DataListCell key={1}>{processName}</DataListCell>,
              <DataListCell key={2}>
                {start? 
                  <TimeAgo date={new Date(`${start}`)} render={({ _error, value }) => <span>{value}</span>} />
                : ''}
              </DataListCell>,
              <DataListCell key={3}>{instanceState}</DataListCell>
            ]}
          />

          <DataListAction
            aria-labelledby="kie-datalist-item kie-datalist-action"
            id="kie-datalist-action"
            aria-label="Actions"
          >
            <Link to={'/ProcessInstances/' + instanceID}>
              <Button variant="secondary">Details</Button>
            </Link>
          </DataListAction>
          <DataListAction
            aria-labelledby="kie-datalist-item kie-datalist-action"
            id="kie-datalist-action"
            aria-label="Actions"
          >
            {state === "ERROR" && managementEnabled ? 
              <Dropdown
              isPlain
              position={DropdownPosition.right}
              isOpen={isOpen}
              onSelect={onSelect}
              toggle={<KebabToggle onToggle={onToggle} />}
              dropdownItems={[
                <DropdownItem key={1} onClick = {() => handleRetry(processID, instanceID, endpoint)}>Retry</DropdownItem>,
                <DropdownItem key={2} onClick = {() => handleSkip(processID, instanceID, endpoint)}>Skip</DropdownItem>,
                <DropdownItem key={3} onClick = {() => handleViewError(processID, instanceID, endpoint) }>View Error</DropdownItem>,
              ]}
            />:  
            <Dropdown
            isPlain
            position={DropdownPosition.right}
            isOpen={isOpen}
            onSelect={onSelect}
            toggle={<KebabToggle isDisabled onToggle={onToggle} />}
            dropdownItems={[]}
          />
          }
          <Modal
          isLarge
          title="Error"
          isOpen={openModal}
          onClose={handleModalToggle}
          actions={[
              <Button key="confirm1" variant="secondary" onClick={handleSkipButton}>
                Skip
              </Button>,
              <Button key="confirm2" variant="secondary" onClick={handleRetryButton}>
                Retry
              </Button>,
              <Button key="confirm3" variant="primary" onClick={handleModalToggle}>
                Close
              </Button>
          ]}
          >
            {error}
          </Modal>

          </DataListAction>
        </DataListItemRow>
        <DataListContent
          aria-label="Primary Content Details"
          id="kie-datalist-expand1"
          isHidden={expanded.includes('kie-datalist-toggle')}
        >
          {isLoaded &&
            childList['ProcessInstances'] !== undefined &&
            childList['ProcessInstances'].map((child, index) => {
              return (
                <DataListItemComponent
                  id={index}
                  key={child.id}
                  instanceState={child.state}
                  instanceID={child.id}
                  processID={child.processId}
                  parentInstanceID={child.parentProcessInstanceId}
                  processName={child.processName}
                  start={child.start}
                  state={child.state}
                  managementEnabled={child.state}
                  endpoint={child.endpoint}
                />
              );
            })}
        </DataListContent>
      </DataListItem>
    </React.Fragment>
  );
};

export default DataListItemComponent;