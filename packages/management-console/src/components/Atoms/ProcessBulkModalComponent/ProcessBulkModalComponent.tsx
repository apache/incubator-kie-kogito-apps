import React from 'react';
import {
  Modal,
  Title,
  TitleLevel,
  BaseSizes,
  Button,
  TextContent,
  Text,
  InjectedOuiaProps,
  withOuiaContext
} from '@patternfly/react-core';
import ProcessBulkListComponent from '../ProcessBulkListComponent/ProcessBulkListComponent';
import { componentOuiaProps } from '@kogito-apps/common';

interface IOwnProps {
  modalTitle: JSX.Element;
  modalContent?: string;
  handleModalToggle: () => void;
  abortedMessageObj?: any;
  completedMessageObj?: any;
  isModalOpen: boolean;
  checkedArray: string[];
  isAbortModalOpen?: boolean;
  isSingleAbort?: any;
}
const Modalbox: React.FC<IOwnProps & InjectedOuiaProps> = ({
  modalContent,
  modalTitle,
  abortedMessageObj,
  completedMessageObj,
  isModalOpen,
  checkedArray,
  handleModalToggle,
  isAbortModalOpen,
  isSingleAbort,
  ouiaContext,
  ouiaId
}) => {
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
        <Button
          key="confirm-selection"
          variant="primary"
          onClick={handleModalToggle}
        >
          OK
        </Button>
      ]}
      isFooterLeftAligned={false}
      {...componentOuiaProps(ouiaContext, ouiaId, 'ProcessBulkModal', true)}
    >
      {abortedMessageObj !== undefined &&
        completedMessageObj !== undefined &&
        isAbortModalOpen && (
          <ProcessBulkListComponent
            abortedMessageObj={abortedMessageObj}
            completedMessageObj={completedMessageObj}
            isSingleAbort={isSingleAbort}
            checkedArray={checkedArray}
            isAbortModalOpen={isAbortModalOpen}
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

const ModalboxWithContext = withOuiaContext(Modalbox);
export default ModalboxWithContext;
