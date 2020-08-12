import {
  Card,
  CardBody,
  CardHeader,
  Title,
  Bullseye,
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
import { setTitle, handleNodeTrigger } from '../../../utils/Utils';

interface IOwnProps {
  processInstanceData: any;
}

const ProcessDetailsNodeRetrigger: React.FC<IOwnProps> = ({
  processInstanceData
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [selectedNodeName, setSelectedNodeName] = useState('');
  const [selectedNode, setSelectedNode] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalTitle, setModalTitle] = useState<string>('');
  const [titleType, setTitleType] = useState<string>('');
  const [modalContent, setModalContent] = useState<string>('');

  useEffect(() => {
    getSelectedNode(selectedNodeName);
  }, [selectedNodeName]);

  const handleModalToggle = () => {
    setIsModalOpen(!isModalOpen);
  };

  const onSelect = event => {
    setIsOpen(!isOpen);
    setSelectedNodeName(event.target.innerHTML);
  };

  const onToggle = isDropDownOpen => {
    setIsOpen(isDropDownOpen);
  };

  const getSelectedNode = _selectedNodeName => {
    const selectedNodeObject = processInstanceData.nodes.filter(
      node => node.name === _selectedNodeName
    );
    setSelectedNode(selectedNodeObject[0]);
  };

  const createNodeDropDown = nodes => {
    const nodeDropDown = [];
    nodes.forEach(node => {
      nodeDropDown.push(<DropdownItem key={node.id}>{node.name}</DropdownItem>);
    });
    return nodeDropDown;
  };

  const onTriggerClick = async () => {
    setModalTitle('Trigger process');
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
          `The node ${selectedNode.name} failed to trigger. ErrorMessage : ${errorMessage}`
        );
      }
    );
    handleModalToggle();
  };
  return (
    <Card>
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
      <CardBody>
        <div>
          <Bullseye>
            <Dropdown
              direction="up"
              onSelect={onSelect}
              toggle={
                <DropdownToggle
                  id="toggle-id"
                  onToggle={onToggle}
                  toggleIndicator={CaretDownIcon}
                >
                  {selectedNodeName ? selectedNodeName : 'select a node'}
                </DropdownToggle>
              }
              isOpen={isOpen}
              dropdownItems={createNodeDropDown(processInstanceData.nodes)}
            />
          </Bullseye>
        </div>
        {selectedNode && (
          <>
            <div className="pf-u-mt-md pf-u-mb-md">
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
                          {selectedNode.nodeId}
                        </Text>
                      </SplitItem>
                    </Split>
                  </TextContent>
                </FlexItem>
              </Flex>
            </div>
            <div>
              <Bullseye>
                <Button variant="secondary" onClick={onTriggerClick}>
                  Trigger
                </Button>
              </Bullseye>
            </div>
          </>
        )}
      </CardBody>
    </Card>
  );
};

export default ProcessDetailsNodeRetrigger;
