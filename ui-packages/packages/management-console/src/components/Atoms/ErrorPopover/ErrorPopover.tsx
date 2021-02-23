import React, { useState } from 'react';
import { Popover, Button } from '@patternfly/react-core';
import {
  handleRetry,
  handleSkip,
  ProcessInstanceIconCreator,
  setTitle
} from '../../../utils/Utils';
import { GraphQL, OUIAProps, componentOuiaProps } from '@kogito-apps/common';
import ProcessInstance = GraphQL.ProcessInstance;
import { TitleType } from '../ProcessInstancesActionsKebab/ProcessInstancesActionsKebab';
import ProcessListModal from '../ProcessListModal/ProcessListModal';

interface IOwnProps {
  processInstanceData: ProcessInstance;
}
const ErrorPopover: React.FC<IOwnProps & OUIAProps> = ({
  processInstanceData,
  ouiaId,
  ouiaSafe
}) => {
  const [modalTitle, setModalTitle] = useState('');
  const [modalContent, setModalContent] = useState('');
  const [titleType, setTitleType] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleModalToggle = () => {
    setIsModalOpen(!isModalOpen);
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
      processInstanceData,
      () =>
        onShowMessage(
          'Skip operation',
          `The process ${processInstanceData.processName} was successfully skipped.`,
          TitleType.SUCCESS
        ),
      (errorMessage: string) =>
        onShowMessage(
          'Skip operation',
          `The process ${processInstanceData.processName} failed to skip. Message: ${errorMessage}`,
          TitleType.FAILURE
        )
    );
  };

  const onRetryClick = async (): Promise<void> => {
    await handleRetry(
      processInstanceData,
      () =>
        onShowMessage(
          'Retry operation',
          `The process ${processInstanceData.processName} was successfully re-executed.`,
          TitleType.SUCCESS
        ),
      (errorMessage: string) =>
        onShowMessage(
          'Retry operation',
          `The process ${processInstanceData.processName} failed to re-execute. Message: ${errorMessage}`,
          TitleType.FAILURE
        )
    );
  };
  return (
    <>
      <ProcessListModal
        isModalOpen={isModalOpen}
        handleModalToggle={handleModalToggle}
        modalTitle={setTitle(titleType, modalTitle)}
        modalContent={modalContent}
        processName={processInstanceData && processInstanceData.processName}
        ouiaId={'process-' + processInstanceData.id}
      />
      <Popover
        zIndex={300}
        id={processInstanceData.id}
        headerContent={<div>Process error</div>}
        bodyContent={
          <div>
            {processInstanceData.error
              ? processInstanceData.error.message
              : 'No error message found'}
          </div>
        }
        footerContent={
          processInstanceData.addons.includes('process-management') &&
          processInstanceData.serviceUrl && [
            <Button
              key="confirm1"
              id="skip-button"
              variant="secondary"
              onClick={onSkipClick}
              className="pf-u-mr-sm"
            >
              Skip
            </Button>,
            <Button
              key="confirm2"
              variant="secondary"
              id="retry-button"
              onClick={onRetryClick}
              className="pf-u-mr-sm"
            >
              Retry
            </Button>
          ]
        }
        position="auto"
        {...componentOuiaProps(ouiaId, 'error-popover', ouiaSafe)}
      >
        <Button variant="link" isInline>
          {ProcessInstanceIconCreator(processInstanceData.state)}
        </Button>
      </Popover>
    </>
  );
};

export default ErrorPopover;
