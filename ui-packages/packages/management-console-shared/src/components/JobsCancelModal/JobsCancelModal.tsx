import React from 'react';
import {
  TextContent,
  Text
} from '@patternfly/react-core/dist/js/components/Text';
import { Modal } from '@patternfly/react-core/dist/js/components/Modal';
import { Button } from '@patternfly/react-core/dist/js/components/Button';
import {
  OUIAProps,
  componentOuiaProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { BulkList, IOperation } from '../BulkList';

interface IOwnProps {
  actionType: string;
  modalTitle: JSX.Element;
  modalContent: string;
  handleModalToggle: () => void;
  isModalOpen: boolean;
  jobOperations?: IOperation;
}
export const JobsCancelModal: React.FC<IOwnProps & OUIAProps> = ({
  actionType,
  modalContent,
  modalTitle,
  isModalOpen,
  handleModalToggle,
  jobOperations,
  ouiaId,
  ouiaSafe
}) => {
  return (
    <Modal
      variant="small"
      title=""
      header={modalTitle}
      isOpen={isModalOpen}
      onClose={handleModalToggle}
      aria-label={`${actionType} Modal`}
      aria-labelledby={`${actionType} Modal`}
      actions={[
        <Button
          key="confirm-selection"
          variant="primary"
          onClick={handleModalToggle}
        >
          OK
        </Button>
      ]}
      {...componentOuiaProps(ouiaId, 'jobs-cancel-modal', ouiaSafe)}
    >
      {modalContent.length > 0 ? (
        <TextContent>
          <Text>{modalContent}</Text>
        </TextContent>
      ) : (
        <BulkList operationResult={jobOperations} />
      )}
    </Modal>
  );
};
