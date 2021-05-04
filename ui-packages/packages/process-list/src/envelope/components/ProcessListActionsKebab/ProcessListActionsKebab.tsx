import React, { useState } from 'react';
import { DropdownItem, Dropdown, KebabToggle } from '@patternfly/react-core';
import {
  ProcessInstance,
  ProcessInstanceState
} from '@kogito-apps/management-console-shared';
import { componentOuiaProps, OUIAProps } from '@kogito-apps/components-common';
import { checkProcessInstanceState } from '../utils/ProcessListUtils';

interface ProcessListActionsKebabProps {
  processInstance: ProcessInstance;
  onSkipClick: (processInstance: ProcessInstance) => Promise<void>;
  onRetryClick: (processInstance: ProcessInstance) => Promise<void>;
  onAbortClick: (processInstance: ProcessInstance) => Promise<void>;
}

const ProcessListActionsKebab: React.FC<ProcessListActionsKebabProps &
  OUIAProps> = ({
  processInstance,
  onSkipClick,
  onRetryClick,
  onAbortClick,
  ouiaId,
  ouiaSafe
}) => {
  const [isKebabOpen, setIsKebabOpen] = useState<boolean>(false);

  const onSelect = (): void => {
    setIsKebabOpen(!isKebabOpen);
  };

  const onToggle = (isOpen: boolean): void => {
    setIsKebabOpen(isOpen);
  };

  const dropDownList: JSX.Element[] =
    processInstance.state === ProcessInstanceState.Error
      ? [
          <DropdownItem key={1} onClick={() => onRetryClick(processInstance)}>
            Retry
          </DropdownItem>,
          <DropdownItem key={2} onClick={() => onSkipClick(processInstance)}>
            Skip
          </DropdownItem>,
          <DropdownItem key={4} onClick={() => onAbortClick(processInstance)}>
            Abort
          </DropdownItem>
        ]
      : [
          <DropdownItem key={4} onClick={() => onAbortClick(processInstance)}>
            Abort
          </DropdownItem>
        ];

  return (
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
      dropdownItems={dropDownList}
      {...componentOuiaProps(ouiaId, 'process-list-actions-kebab', ouiaSafe)}
    />
  );
};

export default ProcessListActionsKebab;
