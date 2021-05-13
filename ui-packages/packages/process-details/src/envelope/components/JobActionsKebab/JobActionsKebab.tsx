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

import React, { useState } from 'react';
import {
  DropdownItem,
  Dropdown,
  KebabToggle,
  Button
} from '@patternfly/react-core';
import {
  JobsDetailsModal,
  JobsRescheduleModal,
  JobsCancelModal,
  Job,
  setTitle
} from '@kogito-apps/management-console-shared';
import { OUIAProps, componentOuiaProps } from '@kogito-apps/components-common';
import { ProcessDetailsDriver } from '../../../api';

interface IOwnProps {
  job: Job;
  driver: ProcessDetailsDriver;
}

const JobActionsKebab: React.FC<IOwnProps & OUIAProps> = ({
  job,
  driver,
  ouiaId,
  ouiaSafe
}) => {
  const [isKebabOpen, setIsKebabOpen] = useState<boolean>(false);
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [rescheduleError, setRescheduleError] = useState<string>('');
  const [isCancelModalOpen, setIsCancelModalOpen] = useState<boolean>(false);
  const [isRescheduleModalOpen, setIsRescheduleModalOpen] = useState<boolean>(
    false
  );
  const [modalTitle, setModalTitle] = useState<JSX.Element>(null);
  const [modalContent, setModalContent] = useState<string>('');
  const RescheduleJobs: string[] = ['SCHEDULED', 'ERROR'];

  const handleModalToggle = (): void => {
    setIsModalOpen(!isModalOpen);
  };

  const handleCancelModalToggle = (): void => {
    setIsCancelModalOpen(!isCancelModalOpen);
  };

  const onSelect = (): void => {
    setIsKebabOpen(!isKebabOpen);
  };

  const onToggle = (isOpen): void => {
    setIsKebabOpen(isOpen);
  };

  const onDetailsClick = (): void => {
    handleModalToggle();
  };

  const handleRescheduleAction = (): void => {
    setIsRescheduleModalOpen(!isRescheduleModalOpen);
  };

  const handleJobReschedule = async (
    repeatInterval,
    repeatLimit,
    scheduleDate
  ): Promise<void> => {
    const response = await driver.rescheduleJob(
      job,
      repeatInterval,
      repeatLimit,
      scheduleDate
    );
    if (response && response.modalTitle === 'success') {
      handleRescheduleAction();
    } else if (response && response.modalTitle === 'failure') {
      handleRescheduleAction();
      setRescheduleError(response.modalContent);
    }
  };

  const handleCancelAction = async (): Promise<void> => {
    const cancelResponse = await driver.cancelJob(job);
    const title: JSX.Element = setTitle(
      cancelResponse.modalTitle,
      'Job cancel'
    );
    setModalTitle(title);
    setModalContent(cancelResponse.modalContent);
    handleCancelModalToggle();
  };

  const rescheduleActions: JSX.Element[] = [
    <Button
      key="cancel-reschedule"
      variant="secondary"
      onClick={handleRescheduleAction}
    >
      Cancel
    </Button>
  ];

  const detailsAction: JSX.Element[] = [
    <Button
      key="confirm-selection"
      variant="primary"
      onClick={handleModalToggle}
    >
      OK
    </Button>
  ];

  const dropdownItems = (): JSX.Element[] => {
    if (job.endpoint !== null && RescheduleJobs.includes(job.status)) {
      return [
        <DropdownItem key="details" component="button" onClick={onDetailsClick}>
          Details
        </DropdownItem>,
        <DropdownItem
          key="reschedule"
          component="button"
          id="reschedule-option"
          onClick={handleRescheduleAction}
        >
          Reschedule
        </DropdownItem>,
        <DropdownItem
          key="cancel"
          component="button"
          id="cancel-option"
          onClick={handleCancelAction}
        >
          Cancel
        </DropdownItem>
      ];
    } else {
      return [
        <DropdownItem key="details" component="button" onClick={onDetailsClick}>
          Details
        </DropdownItem>
      ];
    }
  };
  return (
    <>
      <JobsDetailsModal
        actionType="Job Details"
        modalTitle={setTitle('success', 'Job Details')}
        isModalOpen={isModalOpen}
        handleModalToggle={handleModalToggle}
        modalAction={detailsAction}
        job={job}
      />
      <JobsRescheduleModal
        actionType="Job Reschedule"
        isModalOpen={isRescheduleModalOpen}
        handleModalToggle={handleRescheduleAction}
        modalAction={rescheduleActions}
        job={job}
        rescheduleError={rescheduleError}
        setRescheduleError={setRescheduleError}
        handleJobReschedule={handleJobReschedule}
      />
      <JobsCancelModal
        actionType="Job Cancel"
        isModalOpen={isCancelModalOpen}
        handleModalToggle={handleCancelModalToggle}
        modalTitle={modalTitle}
        modalContent={modalContent}
      />

      <Dropdown
        onSelect={onSelect}
        toggle={<KebabToggle onToggle={onToggle} id="kebab-toggle" />}
        isOpen={isKebabOpen}
        isPlain
        position="right"
        aria-label="Job actions dropdown"
        aria-labelledby="Job actions dropdown"
        dropdownItems={dropdownItems()}
        {...componentOuiaProps(ouiaId, 'job-actions-kebab', ouiaSafe)}
      />
    </>
  );
};

export default JobActionsKebab;
