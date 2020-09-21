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
import React, { useState, useEffect } from 'react';
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
  ToolbarContent
} from '@patternfly/react-core';
import { FilterIcon, SyncIcon } from '@patternfly/react-icons';
import _ from 'lodash';

interface IOwnProps {
  checkedArray: any;
  filterClick: any;
  setCheckedArray: any;
  setIsStatusSelected: any;
  filters: any;
  setFilters: any;
}
const DataToolbarWithFilter: React.FC<IOwnProps> = ({
  checkedArray,
  filterClick,
  setCheckedArray,
  filters,
  setFilters,
  setIsStatusSelected
}) => {
  const [isExpanded, setIsExpanded] = useState(false);
  const [isFilterClicked, setIsFilterClicked] = useState(false);
  const [isClearAllClicked, setIsClearAllClicked] = useState(false);

  const onFilterClick = () => {
    if (checkedArray.length === 0) {
      setFilters(checkedArray);
      setIsFilterClicked(true);
      setIsStatusSelected(false);
    } else {
      setFilters(checkedArray);
      filterClick();
      setIsFilterClicked(true);
      setIsStatusSelected(true);
    }
  };

  const onSelect = (event, selection) => {
    setIsFilterClicked(false);
    setIsClearAllClicked(false);
    if (selection) {
      const index = checkedArray.indexOf(selection);
      if (index === -1) {
        setCheckedArray([...checkedArray, selection]);
      } else {
        const tempArr = checkedArray.slice();
        _.remove(tempArr, _temp => {
          return _temp === selection;
        });
        setCheckedArray(tempArr);
      }
    }
  };

  const onDelete = (type = '', id = '') => {
    if (checkedArray.length === 1) {
      const index = checkedArray.indexOf(id);
      checkedArray.splice(index, 1);
      setCheckedArray([]);
      setFilters([]);
      setIsStatusSelected(false);
    } else {
      const index = checkedArray.indexOf(id);
      checkedArray.splice(index, 1);
      filterClick();
    }
  };

  useEffect(() => {
    if (!checkedArray.length && isFilterClicked) {
      setFilters(checkedArray);
    }
  }, [checkedArray]);

  const clearAll = () => {
    setIsClearAllClicked(true);
    setCheckedArray(['Ready']);
    setFilters(['Ready']);
    filterClick(['Ready']);
  };

  const onRefreshClick = () => {
    if (checkedArray.length === 0) {
      checkedArray.length = 0;
    } else if (isFilterClicked) {
      filterClick(checkedArray);
    } else if (isClearAllClicked) {
      filterClick(['Ready']);
    }
  };
  const onStatusToggle = isExpandedItem => {
    setIsExpanded(isExpandedItem);
  };
  const statusMenuItems = [
    <SelectOption key="Ready" value="Ready" />,
    <SelectOption key="Completed" value="Completed" />,
    <SelectOption key="Aborted" value="Aborted" />
  ];

  const toggleGroupItems = (
    <React.Fragment>
      <ToolbarGroup variant="filter-group">
        <ToolbarFilter
          chips={filters}
          deleteChip={onDelete}
          categoryName="Status"
        >
          <Select
            variant={SelectVariant.checkbox}
            aria-label="Status"
            onToggle={onStatusToggle}
            onSelect={onSelect}
            selections={checkedArray}
            isOpen={isExpanded}
            placeholderText="Status"
          >
            {statusMenuItems}
          </Select>
        </ToolbarFilter>
        <ToolbarItem>
          <Button variant="primary" onClick={onFilterClick}>
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
          <Button variant="plain" onClick={onRefreshClick}>
            <SyncIcon />
          </Button>
        </ToolbarItem>
      </ToolbarGroup>
    </React.Fragment>
  );

  return (
    <Toolbar
      id="data-toolbar-with-filter"
      className="pf-m-toggle-group-container"
      collapseListedFiltersBreakpoint="xl"
      clearAllFilters={() => clearAll()}
      clearFiltersButtonText="Reset to default"
    >
      <ToolbarContent>{toolbarItems}</ToolbarContent>
    </Toolbar>
  );
};

export default DataToolbarWithFilter;
