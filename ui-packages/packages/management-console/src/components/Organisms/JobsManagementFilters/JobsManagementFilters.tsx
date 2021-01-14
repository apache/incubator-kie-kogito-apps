import React, { useState } from 'react';
import {
  Button,
  Select,
  SelectOption,
  SelectVariant,
  ToolbarFilter,
  ToolbarGroup,
  ToolbarItem
} from '@patternfly/react-core';
import { componentOuiaProps, GraphQL, OUIAProps } from '@kogito-apps/common';
interface IOwnProps {
  selectedStatus: GraphQL.JobStatus[];
  setSelectedStatus: (
    selectedStatus: (status: GraphQL.JobStatus[]) => GraphQL.JobStatus[]
  ) => void;
  chips: GraphQL.JobStatus[];
  setChips: (
    chips:
      | ((chip: GraphQL.JobStatus[]) => GraphQL.JobStatus[])
      | GraphQL.JobStatus[]
  ) => void;
  setValues: (
    values:
      | ((value: GraphQL.JobStatus[]) => GraphQL.JobStatus[])
      | GraphQL.JobStatus[]
  ) => void;
  setOffset: (offSet: number) => void;
}

const JobsManagementFilters: React.FC<IOwnProps & OUIAProps> = ({
  selectedStatus,
  setSelectedStatus,
  chips,
  setChips,
  setValues,
  setOffset,
  ouiaId,
  ouiaSafe
}) => {
  const [isExpanded, setIsExpanded] = useState<boolean>(false);

  const statusMenuItems: JSX.Element[] = [
    <SelectOption key="CANCELED" value="CANCELED" />,
    <SelectOption key="ERROR" value="ERROR" />,
    <SelectOption key="EXECUTED" value="EXECUTED" />,
    <SelectOption key="RETRY" value="RETRY" />,
    <SelectOption key="SCHEDULED" value="SCHEDULED" />
  ];

  const onStatusToggle = (): void => {
    setIsExpanded(!isExpanded);
  };

  const onSelect = (event, selection: GraphQL.JobStatus): void => {
    let selectionText = event.target.id;
    selectionText = selectionText.split('pf-random-id-')[1].split('-')[1];
    if (selectedStatus.includes(selectionText)) {
      setSelectedStatus(prev => prev.filter(item => item !== selectionText));
    } else {
      setSelectedStatus(prev => [...prev, selectionText]);
    }
  };

  const onApplyFilter = (): void => {
    setChips(selectedStatus);
    setValues(selectedStatus);
  };

  const onDelete = (type: string = '', id: string = ''): void => {
    setOffset(0);
    setChips(prev => prev.filter(item => item !== id));
    setSelectedStatus(prev => prev.filter(item => item !== id));
    setValues(prev => prev.filter(item => item !== id));
  };

  return (
    <>
      <ToolbarGroup
        variant="filter-group"
        {...componentOuiaProps(ouiaId, 'job-filters', ouiaSafe)}
      >
        <ToolbarFilter
          chips={chips}
          deleteChip={onDelete}
          categoryName="Status"
          className="kogito-management-console__state-dropdown-list"
        >
          <Select
            variant={SelectVariant.checkbox}
            aria-label="Status"
            onToggle={onStatusToggle}
            onSelect={onSelect}
            selections={selectedStatus}
            isOpen={isExpanded}
            placeholderText="Status"
            id="status-select"
          >
            {statusMenuItems}
          </Select>
        </ToolbarFilter>
      </ToolbarGroup>
      <ToolbarGroup
        {...componentOuiaProps(ouiaId, 'job-filters/button', ouiaSafe)}
      >
        <ToolbarItem>
          <Button
            variant="primary"
            onClick={onApplyFilter}
            id="apply-filter"
            isDisabled={!(selectedStatus.length > 0)}
          >
            Apply Filter
          </Button>
        </ToolbarItem>
      </ToolbarGroup>
    </>
  );
};

export default JobsManagementFilters;
