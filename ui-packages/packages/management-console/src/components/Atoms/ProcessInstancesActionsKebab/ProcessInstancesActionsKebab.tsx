import React, { useState } from 'react';
import { DropdownItem, Dropdown, KebabToggle } from '@patternfly/react-core';
import { OUIAProps, componentOuiaProps, GraphQL } from '@kogito-apps/common';
import {
  checkProcessInstanceState,
  handleAbort,
  handleRetry,
  handleSkip,
  setTitle
} from '../../../utils/Utils';
import ProcessListModal from '../ProcessListModal/ProcessListModal';
interface IOwnProps {
  processInstance: GraphQL.ProcessInstance;
}
export enum TitleType {
  SUCCESS = 'success',
  FAILURE = 'failure'
}
const ProcessInstancesActionsKebab: React.FC<IOwnProps & OUIAProps> = ({
  processInstance,
  ouiaId,
  ouiaSafe
}) => {
  const [isKebabOpen, setIsKebabOpen] = useState<boolean>(false);
  const [modalTitle, setModalTitle] = useState('');
  const [modalContent, setModalContent] = useState('');
  const [titleType, setTitleType] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);

  const onSelect = (): void => {
    setIsKebabOpen(!isKebabOpen);
  };

  const handleModalToggle = () => {
    setIsModalOpen(!isModalOpen);
  };

  const onToggle = (isOpen): void => {
    setIsKebabOpen(isOpen);
  };

  const onShowMessage = (
    title: string,
    content: string,
    type: TitleType
  ): void => {
    setTitleType(type);
    setModalTitle(title);
    setModalContent(content);
    handleModalToggle();
  };

  const onSkipClick = async (): Promise<void> => {
    await handleSkip(
      processInstance,
      () =>
        onShowMessage(
          'Skip operation',
          `The process ${processInstance.processName} was successfully skipped.`,
          TitleType.SUCCESS
        ),
      (errorMessage: string) =>
        onShowMessage(
          'Skip operation',
          `The process ${processInstance.processName} failed to skip. Message: ${errorMessage}`,
          TitleType.FAILURE
        )
    );
  };

  const onRetryClick = async (): Promise<void> => {
    await handleRetry(
      processInstance,
      () =>
        onShowMessage(
          'Retry operation',
          `The process ${processInstance.processName} was successfully re-executed.`,
          TitleType.SUCCESS
        ),
      (errorMessage: string) =>
        onShowMessage(
          'Retry operation',
          `The process ${processInstance.processName} failed to re-execute. Message: ${errorMessage}`,
          TitleType.FAILURE
        )
    );
  };

  const onAbortClick = async (): Promise<void> => {
    await handleAbort(
      processInstance,
      () =>
        onShowMessage(
          'Abort operation',
          `The process ${processInstance.processName} was successfully aborted.`,
          TitleType.SUCCESS
        ),
      (errorMessage: string) =>
        onShowMessage(
          'Abort operation',
          `Failed to abort process ${processInstance.processName}. Message: ${errorMessage}`,
          TitleType.FAILURE
        )
    );
  };

  const dropDownList = (): JSX.Element[] => {
    if (processInstance.state === 'ERROR') {
      return [
        <DropdownItem key={1} onClick={onRetryClick}>
          Retry
        </DropdownItem>,
        <DropdownItem key={2} onClick={onSkipClick}>
          Skip
        </DropdownItem>,
        <DropdownItem key={4} onClick={onAbortClick}>
          Abort
        </DropdownItem>
      ];
    } else {
      return [
        <DropdownItem key={4} onClick={onAbortClick}>
          Abort
        </DropdownItem>
      ];
    }
  };

  return (
    <>
      <ProcessListModal
        isModalOpen={isModalOpen}
        handleModalToggle={handleModalToggle}
        modalTitle={setTitle(titleType, modalTitle)}
        modalContent={modalContent}
        processName={processInstance && processInstance.processName}
        ouiaId={'process-' + processInstance.id}
      />
      <Dropdown
        onSelect={onSelect}
        toggle={
          <KebabToggle
            isDisabled={checkProcessInstanceState(processInstance)}
            onToggle={onToggle}
            id="kebab-toggle"
          />
        }
        isOpen={isKebabOpen}
        isPlain
        position="right"
        aria-label="process instance actions dropdown"
        aria-labelledby="process instance actions dropdown"
        dropdownItems={dropDownList()}
        {...componentOuiaProps(
          ouiaId,
          'process-instance-actions-kebab',
          ouiaSafe
        )}
      />
    </>
  );
};

export default ProcessInstancesActionsKebab;
