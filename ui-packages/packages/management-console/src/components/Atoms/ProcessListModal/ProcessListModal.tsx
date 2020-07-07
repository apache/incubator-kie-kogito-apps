import React from 'react';
import {
  Modal,
  Title,
  TitleLevel,
  BaseSizes,
  Button,
  TextContent,
  Text
} from '@patternfly/react-core';
import ProcessListBulkInstances from '../ProcessListBulkInstances/ProcessListBulkInstances';

interface IOwnProps {
  modalTitle: JSX.Element;
  modalContent?: string;
  handleModalToggle: () => void;
  requiredInstances?: any;
  ignoredInstances?: any;
  isModalOpen: boolean;
  checkedArray: string[];
  isSingleAbort?: any;
  resetSelected?: () => void;
  titleString?: string;
}
const ProcessListModal: React.FC<IOwnProps> = ({
  modalContent,
  modalTitle,
  requiredInstances,
  ignoredInstances,
  isModalOpen,
  checkedArray,
  handleModalToggle,
  isSingleAbort,
  resetSelected,
  titleString
}) => {
  const onOKClick = () => {
    handleModalToggle();
    requiredInstances !== undefined &&
      ignoredInstances !== undefined &&
      resetSelected();
  };
  return (
    <Modal
      isSmall={true}
      title=""
      header={
        <Title headingLevel={TitleLevel.h1} size={BaseSizes['2xl']}>
          {modalTitle}
        </Title>
      }
      isOpen={isModalOpen}
      onClose={handleModalToggle}
      actions={[
        <Button key="confirm-selection" variant="primary" onClick={onOKClick}>
          OK
        </Button>
      ]}
      isFooterLeftAligned={false}
    >
      {requiredInstances !== undefined && ignoredInstances !== undefined && (
        <ProcessListBulkInstances
          requiredInstances={requiredInstances}
          ignoredInstances={ignoredInstances}
          isSingleAbort={isSingleAbort}
          checkedArray={checkedArray}
          titleString={titleString}
        />
      )}
      <TextContent>
        <Text>
          <strong>{modalContent}</strong>
        </Text>
      </TextContent>
    </Modal>
  );
};

export default ProcessListModal;
