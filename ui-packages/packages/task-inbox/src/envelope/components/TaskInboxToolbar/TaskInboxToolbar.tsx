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
  Button,
  Select,
  SelectOption,
  SelectVariant,
  ToolbarFilter,
  ToolbarGroup,
  ToolbarItem,
  ToolbarToggleGroup,
  Toolbar,
  ToolbarContent,
  TextInput,
  InputGroup,
  Tooltip
} from '@patternfly/react-core';
import { FilterIcon, SyncIcon } from '@patternfly/react-icons';
import _ from 'lodash';
import { componentOuiaProps, OUIAProps } from '@kogito-apps/components-common';
import { QueryFilter } from '../../../api';

interface TaskInboxToolbarProps {
  initialState?: QueryFilter;
  allTaskStates: string[];
  activeTaskStates: string[];
  applyFilter: (filter: QueryFilter) => void;
  refresh: () => void;
}

enum Category {
  STATUS = 'Status',
  TASK_NAME = 'Task name'
}

const TaskInboxToolbar: React.FC<TaskInboxToolbarProps & OUIAProps> = ({
  initialState,
  allTaskStates,
  activeTaskStates,
  applyFilter,
  refresh,
  ouiaSafe,
  ouiaId
}) => {
  const [allStates] = useState<string[]>(allTaskStates);
  const [activeStates] = useState<string[]>(activeTaskStates);

  const [filterTaskStates, setFilterTaskStates] = useState<string[]>(
    initialState ? initialState.taskStates : activeStates
  );
  const [filterTaskNames, setFilteraskNames] = useState<string[]>(
    initialState ? initialState.taskNames : []
  );

  const [isStatusExpanded, setStatusExpanded] = useState(false);

  const [selectedTaskStates, setSelectedTaskStates] = useState<string[]>(
    initialState ? initialState.taskStates : activeStates
  );

  const [taskNameInput, setTaskNameInput] = useState<string>('');

  const createStatusMenuItems = () => {
    return allStates.map(state => <SelectOption key={state} value={state} />);
  };

  const onDeleteFilterGroup = (categoryName: Category, value: string): void => {
    const newFilterTaskStates = [...filterTaskStates];
    const newFilterTaskNames = [...filterTaskNames];

    switch (categoryName) {
      case Category.STATUS:
        _.remove(newFilterTaskStates, (status: string) => {
          return status === value;
        });
        setFilterTaskStates(newFilterTaskStates);
        setSelectedTaskStates(newFilterTaskStates);
        break;
      case Category.TASK_NAME:
        _.remove(newFilterTaskNames, (status: string) => {
          return status === value;
        });
        setFilteraskNames(newFilterTaskNames);
        break;
    }
    applyFilter({
      taskNames: newFilterTaskNames,
      taskStates: newFilterTaskStates
    });
  };

  const onSelectTaskState = (
    event: React.MouseEvent,
    selection: string
  ): void => {
    const filter: string[] = [...selectedTaskStates];

    if (!filter.includes(selection)) {
      filter.push(selection);
    } else {
      _.remove(filter, (status: string) => {
        return status === selection;
      });
    }
    setSelectedTaskStates(filter);
  };

  const onStatusToggle = (isExpandedItem: boolean): void => {
    setStatusExpanded(isExpandedItem);
  };

  const onTextBoxChange = (taskName: string): void => {
    setTaskNameInput(taskName);
    if (taskName === '') {
      setTaskNameInput('');
      return;
    }
  };

  const doApplyFilter = () => {
    const taskNames = [...filterTaskNames];
    if (taskNameInput && !taskNames.includes(taskNameInput)) {
      taskNames.push(taskNameInput);
      setFilteraskNames(taskNames);
    }
    setFilterTaskStates([...selectedTaskStates]);
    setTaskNameInput('');
    applyFilter({
      taskStates: [...selectedTaskStates],
      taskNames: taskNames
    });
  };

  const doResetFilter = () => {
    setFilterTaskStates(activeStates);
    setSelectedTaskStates(activeStates);
    setFilteraskNames([]);
    applyFilter({ taskNames: [], taskStates: activeStates });
  };

  const toggleGroupItems = (
    <React.Fragment>
      <ToolbarGroup variant="filter-group">
        <ToolbarFilter
          chips={filterTaskStates}
          deleteChip={onDeleteFilterGroup}
          categoryName={Category.STATUS}
        >
          <Select
            variant={SelectVariant.checkbox}
            aria-label="Status"
            onToggle={onStatusToggle}
            onSelect={onSelectTaskState}
            selections={selectedTaskStates}
            isOpen={isStatusExpanded}
            placeholderText="Status"
          >
            {createStatusMenuItems()}
          </Select>
        </ToolbarFilter>
        <ToolbarFilter
          chips={filterTaskNames}
          deleteChip={onDeleteFilterGroup}
          categoryName={Category.TASK_NAME}
        >
          <InputGroup>
            <TextInput
              name="taskName"
              id="taskName"
              type="search"
              aria-label="task name"
              onChange={onTextBoxChange}
              placeholder="Filter by Task name"
              value={taskNameInput}
            />
          </InputGroup>
        </ToolbarFilter>
        <ToolbarItem>
          <Button
            id="apply-filter"
            variant="primary"
            onClick={doApplyFilter}
            isDisabled={
              _.isEmpty(selectedTaskStates) && _.isEmpty(taskNameInput)
            }
          >
            Apply Filter
          </Button>
        </ToolbarItem>
      </ToolbarGroup>
    </React.Fragment>
  );

  const toolbarItems = (
    <React.Fragment>
      <ToolbarToggleGroup toggleIcon={<FilterIcon />} breakpoint="xl">
        {toggleGroupItems}
      </ToolbarToggleGroup>
      <ToolbarGroup variant="icon-button-group">
        <ToolbarItem>
          <Tooltip content={'Refresh'}>
            <Button variant="plain" onClick={refresh} id="refresh">
              <SyncIcon />
            </Button>
          </Tooltip>
        </ToolbarItem>
      </ToolbarGroup>
    </React.Fragment>
  );

  return (
    <Toolbar
      id="task-inbox-with-filter"
      className="pf-m-toggle-group-container"
      collapseListedFiltersBreakpoint="xl"
      clearAllFilters={doResetFilter}
      clearFiltersButtonText="Reset to default"
      {...componentOuiaProps(ouiaId, 'task-inbox-toolbar', ouiaSafe)}
    >
      <ToolbarContent>{toolbarItems}</ToolbarContent>
    </Toolbar>
  );
};

export default TaskInboxToolbar;
