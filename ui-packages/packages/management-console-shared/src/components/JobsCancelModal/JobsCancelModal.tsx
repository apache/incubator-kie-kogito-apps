/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from 'react';
import { Modal, Button, TextContent, Text } from '@patternfly/react-core';
import { OUIAProps, componentOuiaProps } from '@kogito-apps/components-common';
import BulkList, { IOperation } from '../BulkList/BulkList';

interface IOwnProps {
  actionType: string;
  modalTitle: JSX.Element;
  modalContent: string;
  handleModalToggle: () => void;
  isModalOpen: boolean;
  jobOperations?: IOperation;
}
const JobsCancelModal: React.FC<IOwnProps & OUIAProps> = ({
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
export default JobsCancelModal;
