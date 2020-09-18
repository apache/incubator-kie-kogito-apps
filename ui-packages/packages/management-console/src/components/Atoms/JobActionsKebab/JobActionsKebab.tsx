/**
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import React, { useState } from 'react';
import { DropdownItem, Dropdown, KebabToggle } from '@patternfly/react-core';
import JobsPanelDetailsModal from '../JobsPanelDetailsModal/JobsPanelDetailsModal';
import { OUIAProps, componentOuiaProps, GraphQL } from '@kogito-apps/common';
import { setTitle } from '../../../utils/Utils';

interface JobActionsProps {
  job: GraphQL.Job;
}

const JobActionsKebab: React.FC<JobActionsProps & OUIAProps> = ({
  job,
  ouiaId,
  ouiaSafe
}) => {
  const [isKebabOpen, setIsKebabOpen] = useState<boolean>(false);
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);

  const handleModalToggle = () => {
    setIsModalOpen(!isModalOpen);
  };

  const onSelect = () => {
    setIsKebabOpen(!isKebabOpen);
  };

  const onToggle = isOpen => {
    setIsKebabOpen(isOpen);
  };

  const onDetailsClick = () => {
    handleModalToggle();
  };
  const dropdownItems = [
    <DropdownItem key="details" component="button" onClick={onDetailsClick}>
      Details
    </DropdownItem>
  ];

  return (
    <>
      <JobsPanelDetailsModal
        modalTitle={setTitle('success', 'Job Details')}
        isModalOpen={isModalOpen}
        handleModalToggle={handleModalToggle}
        job={job}
      />
      <Dropdown
        onSelect={onSelect}
        toggle={<KebabToggle onToggle={onToggle} id="kebab-toggle" />}
        isOpen={isKebabOpen}
        isPlain
        aria-label="Job actions dropdown"
        aria-labelledby="Job actions dropdown"
        dropdownItems={dropdownItems}
        {...componentOuiaProps(ouiaId, 'job-actions-kebab', ouiaSafe)}
      />
    </>
  );
};

export default JobActionsKebab;
