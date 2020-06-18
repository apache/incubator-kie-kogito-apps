import Moment from 'react-moment';
import {
  Card,
  CardBody,
  CardHeader,
  Title,
  Text,
  TextContent,
  TextVariants,
  Split,
  SplitItem,
  Stack,
  Dropdown,
  KebabToggle,
  DropdownItem,
  Tooltip
} from '@patternfly/react-core';
import {
  UserIcon,
  CheckCircleIcon,
  ErrorCircleOIcon,
  OnRunningIcon
} from '@patternfly/react-icons';
import React, { useState } from 'react';
import './ProcessDetailsTimeline.css';
import { GraphQL } from '@kogito-apps/common';
import {
  handleRetry,
  handleSkip,
  handleNodeInstanceCancel,
  setTitle
} from '../../../utils/Utils';
import ProcessInstance = GraphQL.ProcessInstance;
import ProcessListModal from '../../Atoms/ProcessListModal/ProcessListModal';

export interface IOwnProps {
  data: Pick<
    ProcessInstance,
    'id' | 'nodes' | 'addons' | 'error' | 'serviceUrl' | 'processId' | 'state'
  >;
}

const ProcessDetailsTimeline: React.FC<IOwnProps> = ({ data }) => {
  const [nodeObject, setNodeObject] = useState({});
  const [kebabOpenArray, setKebabOpenArray] = useState<string[]>([]);
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [modalTitle, setModalTitle] = useState<string>('');
  const [titleType, setTitleType] = useState<string>('');
  const [modalContent, setModalContent] = useState<string>('');
  const ignoredNodeTypes = ['Join', 'Split', 'EndNode'];
  const dropdownItems = [
    <DropdownItem
      key="retry"
      component="button"
      onClick={() =>
        handleRetry(
          data,
          setModalTitle,
          setTitleType,
          setModalContent,
          handleModalToggle
        )
      }
    >
      Retry
    </DropdownItem>,
    <DropdownItem
      key="skip"
      component="button"
      onClick={() =>
        handleSkip(
          data,
          setModalTitle,
          setTitleType,
          setModalContent,
          handleModalToggle
        )
      }
    >
      Skip
    </DropdownItem>
  ];

  const nodeManagementDropdownItems = [
    <DropdownItem
      key="cancel"
      component="button"
      onClick={() =>
        handleNodeInstanceCancel(
          data,
          nodeObject,
          setModalTitle,
          setTitleType,
          setModalContent,
          handleModalToggle
        )
      }
    >
      Cancel node
    </DropdownItem>
  ];

  const onKebabToggle = (isOpen: boolean, node, id) => {
    setNodeObject(node);
    if (isOpen) {
      setKebabOpenArray([...kebabOpenArray, id]);
    } else {
      const tempKebabArray = [...kebabOpenArray];
      const index = tempKebabArray.indexOf(id);
      tempKebabArray.splice(index, 1);
      setKebabOpenArray(tempKebabArray);
    }
  };

  const onDropdownSelect = id => {
    const tempKebabArray = [...kebabOpenArray];
    const index = tempKebabArray.indexOf(id);
    tempKebabArray.splice(index, 1);
    setKebabOpenArray(tempKebabArray);
  };

  const handleModalToggle = () => {
    setIsModalOpen(!isModalOpen);
  };

  const processManagementKebabButtons = (content, index) => {
    if (
      data.addons.includes('process-management') &&
      data.serviceUrl !== null &&
      data.error &&
      content.definitionId === data.error.nodeDefinitionId &&
      data.state !== GraphQL.ProcessInstanceState.Completed
    ) {
      return (
        <Dropdown
          onSelect={() => onDropdownSelect('timeline-kebab-toggle-' + index)}
          toggle={
            <KebabToggle
              onToggle={isOpen =>
                onKebabToggle(isOpen, content, 'timeline-kebab-toggle-' + index)
              }
              id={'timeline-kebab-toggle-' + index}
            />
          }
          position="right"
          isOpen={kebabOpenArray.includes('timeline-kebab-toggle-' + index)}
          isPlain
          dropdownItems={dropdownItems}
        />
      );
    } else if (
      data.addons.includes('process-management') &&
      data.serviceUrl !== null &&
      content.exit === null &&
      !ignoredNodeTypes.includes(content.type) &&
      data.state !== GraphQL.ProcessInstanceState.Completed
    ) {
      return (
        <Dropdown
          onSelect={() =>
            onDropdownSelect('timeline-kebab-toggle-nodemanagement-' + index)
          }
          toggle={
            <KebabToggle
              onToggle={isOpen =>
                onKebabToggle(
                  isOpen,
                  content,
                  'timeline-kebab-toggle-nodemanagement-' + index
                )
              }
              id={'timeline-kebab-toggle-nodemanagement-' + index}
            />
          }
          position="right"
          isOpen={kebabOpenArray.includes(
            'timeline-kebab-toggle-nodemanagement-' + index
          )}
          isPlain
          dropdownItems={nodeManagementDropdownItems}
        />
      );
    } else if (data.state === GraphQL.ProcessInstanceState.Completed) {
      return <></>;
    } else {
      return (
        <Dropdown
          toggle={
            <KebabToggle
              isDisabled
              id={'timeline-kebab-toggle-disabled-' + index}
            />
          }
          isPlain
        />
      );
    }
  };

  return (
    <Card>
      <ProcessListModal
        isModalOpen={isModalOpen}
        handleModalToggle={handleModalToggle}
        checkedArray={data && [data.state]}
        modalTitle={setTitle(titleType, modalTitle)}
        modalContent={modalContent}
      />
      <CardHeader>
        <Title headingLevel="h3" size="xl">
          Timeline
        </Title>
      </CardHeader>
      <CardBody>
        <Stack gutter="md" className="kogito-management-console--timeline">
          {data.nodes &&
            data.nodes.map((content, idx) => {
              return (
                <Split
                  gutter={'sm'}
                  className={'kogito-management-console--timeline-item'}
                  key={content.id}
                >
                  <SplitItem>
                    {
                      <>
                        {data.error &&
                        content.definitionId === data.error.nodeDefinitionId ? (
                          <Tooltip content={data.error.message}>
                            <ErrorCircleOIcon
                              color="var(--pf-global--danger-color--100)"
                              className="kogito-management-console--timeline-status"
                            />
                          </Tooltip>
                        ) : content.exit === null ? (
                          <Tooltip content={'Active'}>
                            <OnRunningIcon className="kogito-management-console--timeline-status" />
                          </Tooltip>
                        ) : (
                          <Tooltip content={'Completed'}>
                            <CheckCircleIcon
                              color="var(--pf-global--success-color--100)"
                              className="kogito-management-console--timeline-status"
                            />
                          </Tooltip>
                        )}
                      </>
                    }
                  </SplitItem>
                  <SplitItem isFilled>
                    <TextContent>
                      <Text component={TextVariants.p}>
                        {content.name}
                        <span>
                          {content.type === 'HumanTaskNode' && (
                            <Tooltip content={'Human task'}>
                              <UserIcon
                                className="pf-u-ml-sm"
                                color="var(--pf-global--icon--Color--light)"
                              />
                            </Tooltip>
                          )}
                        </span>
                        <Text component={TextVariants.small}>
                          {content.exit === null ? (
                            'Active'
                          ) : (
                            <Moment fromNow>
                              {new Date(`${content.exit}`)}
                            </Moment>
                          )}
                        </Text>
                      </Text>
                    </TextContent>
                  </SplitItem>
                  <SplitItem>
                    {processManagementKebabButtons(content, idx)}
                  </SplitItem>
                </Split>
              );
            })}
        </Stack>
      </CardBody>
    </Card>
  );
};

export default ProcessDetailsTimeline;
