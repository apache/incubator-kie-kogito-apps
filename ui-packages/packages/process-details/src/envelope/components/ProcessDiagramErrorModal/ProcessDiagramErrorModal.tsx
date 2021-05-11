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
import {
  Modal,
  ModalVariant,
  ModalBoxBody,
  TextContent,
  Text
} from '@patternfly/react-core';
import { OUIAProps, componentOuiaProps } from '@kogito-apps/components-common';

interface IOwnProps {
  errorString: string;
  errorModalOpen: boolean;
  errorModalAction: JSX.Element[];
  handleErrorModal: () => void;
  label: string;
  title: JSX.Element;
}
const ProcessDiagramErrorModal: React.FC<IOwnProps & OUIAProps> = ({
  errorString,
  errorModalOpen,
  errorModalAction,
  handleErrorModal,
  label,
  title,
  ouiaId,
  ouiaSafe
}) => {
  const errorModalContent = (): JSX.Element => {
    return (
      <ModalBoxBody>
        <TextContent>
          <Text>{errorString}</Text>
        </TextContent>
      </ModalBoxBody>
    );
  };

  return (
    <Modal
      variant={ModalVariant.small}
      aria-labelledby={label}
      aria-label={label}
      title=""
      header={title}
      isOpen={errorModalOpen}
      onClose={handleErrorModal}
      actions={errorModalAction}
      {...componentOuiaProps(ouiaId, 'process-diagram-error-modal', ouiaSafe)}
    >
      {errorModalContent()}
    </Modal>
  );
};

export default ProcessDiagramErrorModal;
