import {
  Card,
  CardBody,
  CardHeader,
  Title,
  Dropdown,
  DropdownToggle,
  DropdownItem,
  Flex,
  FlexItem,
  TextContent,
  Text,
  TextVariants,
  Split,
  SplitItem,
  Button
} from '@patternfly/react-core';
import React, { useState, useEffect } from 'react';
import { CaretDownIcon } from '@patternfly/react-icons';
import ProcessListModal from '../../Atoms/ProcessListModal/ProcessListModal';
import {
  setTitle,
  handleNodeTrigger,
  getTriggerableNodes,
  TriggerableNode
} from '../../../utils/Utils';
import { GraphQL, OUIAProps, componentOuiaProps } from '@kogito-apps/common';
import './ProcessDetailsNodeTrigger.css';

interface IOwnProps {
  processInstanceData: Pick<
    GraphQL.ProcessInstance,
    'id' | 'serviceUrl' | 'processId'
  >;
}

const ProcessDetailsNodeTrigger: React.FC<IOwnProps & OUIAProps> = ({
  processInstanceData,
  ouiaId,
  ouiaSafe
}) => {
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [selectedNode, setSelectedNode] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [modalTitle, setModalTitle] = useState<string>('');
  const [titleType, setTitleType] = useState<string>('');
  const [modalContent, setModalContent] = useState<string>('');
  const [nodes, setNodes] = useState([]);
  /* tslint:disable:no-floating-promises */
  useEffect(() => {
    (async () => {
      const nodesArray = await getTriggerableNodes(processInstanceData);
      setNodes(nodesArray);
    })();
  }, []);

  const handleModalToggle = (): void => {
    setIsModalOpen(!isModalOpen);
  };

  const onSelect = (event: React.SyntheticEvent<HTMLDivElement>): void => {
    setIsOpen(!isOpen);
    getSelectedNode(event.currentTarget.id);
  };

  const onToggle = (isDropDownOpen: boolean): void => {
    setIsOpen(isDropDownOpen);
  };

  const getSelectedNode = (selectedNodeName: string): void => {
    const selectedNodeObject: TriggerableNode[] =
      nodes.length > 0 &&
      nodes.filter((node: TriggerableNode) => node.name === selectedNodeName);

    setSelectedNode(selectedNodeObject[0]);
  };

  const createNodeDropDown = (): JSX.Element[] => {
    const nodeDropDown: JSX.Element[] = [];
    nodes.length > 0 &&
      nodes.forEach((node: TriggerableNode) => {
        node.type !== null &&
          node.type !== 'StartNode' &&
          node.type !== 'Join' &&
          nodeDropDown.push(
            <DropdownItem key={node.uniqueId} id={node.name}>
              {node.name}
            </DropdownItem>
          );
      });
    return nodeDropDown;
  };

  const onTriggerClick = async (): Promise<void> => {
    setModalTitle('Node trigger process');
    await handleNodeTrigger(
      processInstanceData,
      selectedNode,
      () => {
        setTitleType('success');
        setModalContent(
          `The node ${selectedNode.name} was triggered successfully`
        );
      },
      errorMessage => {
        setTitleType('failure');
        setModalContent(
          `The node ${selectedNode.name} trigger failed. ErrorMessage : ${errorMessage}`
        );
      }
    );
    handleModalToggle();
  };

  return (
    <Card {...componentOuiaProps(ouiaId, 'node-trigger', ouiaSafe)}>
      <ProcessListModal
        isModalOpen={isModalOpen}
        handleModalToggle={handleModalToggle}
        modalTitle={setTitle(titleType, modalTitle)}
        modalContent={modalContent}
        processName={selectedNode && selectedNode.name}
      />
      <CardHeader>
        <Title headingLevel="h3" size="xl">
          Node Trigger
        </Title>
      </CardHeader>
      <CardHeader>
        <div>
          Select a node from the process nodes list and click Trigger to launch
          it manually.
        </div>
      </CardHeader>
      <CardBody>
        <div>
          <Dropdown
            direction="up"
            onSelect={onSelect}
            toggle={
              <DropdownToggle
                id="toggle-id"
                onToggle={onToggle}
                toggleIndicator={CaretDownIcon}
              >
                {selectedNode ? selectedNode.name : 'select a node'}
              </DropdownToggle>
            }
            isOpen={isOpen}
            dropdownItems={createNodeDropDown()}
          />
        </div>
        {selectedNode && (
          <>
            <div className="pf-u-mt-md">
              <Flex direction={{ default: 'column' }}>
                <FlexItem>
                  <TextContent>
                    {' '}
                    <Split hasGutter>
                      <SplitItem>
                        <Text component={TextVariants.h6}>Node name : </Text>
                      </SplitItem>
                      <SplitItem>
                        <Text component={TextVariants.p}>
                          {selectedNode.name}
                        </Text>
                      </SplitItem>
                    </Split>
                  </TextContent>
                </FlexItem>
                <FlexItem>
                  <TextContent>
                    {' '}
                    <Split hasGutter>
                      <SplitItem>
                        <Text component={TextVariants.h6}>Node type : </Text>
                      </SplitItem>
                      <SplitItem>
                        <Text component={TextVariants.p}>
                          {selectedNode.type}
                        </Text>
                      </SplitItem>
                    </Split>
                  </TextContent>
                </FlexItem>
                <FlexItem>
                  <TextContent>
                    {' '}
                    <Split hasGutter>
                      <SplitItem>
                        <Text component={TextVariants.h6}>Node id : </Text>
                      </SplitItem>
                      <SplitItem>
                        <Text component={TextVariants.p}>
                          {selectedNode.id}
                        </Text>
                      </SplitItem>
                    </Split>
                  </TextContent>
                </FlexItem>
              </Flex>
            </div>
          </>
        )}
        <div className="pf-u-mt-md">
          <Button
            variant="secondary"
            onClick={onTriggerClick}
            id="trigger"
            isDisabled={!selectedNode}
          >
            Trigger
          </Button>
        </div>
      </CardBody>
    </Card>
  );
};

export default ProcessDetailsNodeTrigger;
