import React, { useState } from 'react';
import {
  DataToolbar,
  DataToolbarItem,
  DataToolbarContent,
  DataToolbarFilter,
  DataToolbarToggleGroup,
  DataToolbarGroup
} from '@patternfly/react-core/dist/esm/experimental';
import { Button, Select, SelectOption, SelectVariant } from '@patternfly/react-core';
import { FilterIcon } from '@patternfly/react-icons';
import _ from 'lodash';

interface IOwnProps {
  checkedArray: Array<String>;
  filterClick: () => void;
  setCheckedArray: any;
}
const DataToolbarWithFilter: React.FC<IOwnProps> = ({ checkedArray, filterClick, setCheckedArray }) => {
  const [isExpanded, setisExpanded] = useState(false);
  const [statusIsExpanded, setstatusIsExpanded] = useState(false);
  const [filters, setfilters] = useState({ status: [] });

  const onFilterClick = () => {
    filterClick();
    setfilters({
      status: checkedArray
    });
  };
  const onSelect = (type, event, selection) => {
    const checked = event.target.checked;
    if (type === 'status') {
      if (selection === 'ACTIVE') {
        if (checked) {
          setCheckedArray([...checkedArray, 'ACTIVE']);
        } else {
          const tempArr = checkedArray.slice();
          _.remove(tempArr, _temp => {
            return _temp === 'ACTIVE';
          });
          setCheckedArray(tempArr);
        }
      } else if (selection === 'COMPLETED') {
        if (checked) {
          setCheckedArray([...checkedArray, 'COMPLETED']);
        } else {
          const tempArr = checkedArray.slice();
          _.remove(tempArr, _temp => {
            return _temp === 'COMPLETED';
          });
          setCheckedArray(tempArr);
        }
      } else if (selection === 'ERROR') {
        if (checked) {
          setCheckedArray([...checkedArray, 'ERROR']);
        } else {
          const tempArr = checkedArray.slice();
          _.remove(tempArr, _temp => {
            return _temp === 'ERROR';
          });
          setCheckedArray(tempArr);
        }
      } else if (selection === 'ABORTED') {
        if (checked) {
          setCheckedArray([...checkedArray, 'ABORTED']);
        } else {
          const tempArr = checkedArray.slice();
          _.remove(tempArr, _temp => {
            return _temp === 'ABORTED';
          });
          setCheckedArray(tempArr);
        }
      } else if (selection === 'SUSPENDED') {
        if (checked) {
          setCheckedArray([...checkedArray, 'SUSPENDED']);
        } else {
          const tempArr = checkedArray.slice();
          _.remove(tempArr, _temp => {
            return _temp === 'SUSPENDED';
          });
          setCheckedArray(tempArr);
        }
      }
    }
  };

  const onStatusSelect = (event, selection) => {
    onSelect('status', event, selection);
  };

  const onDelete = (type = '', id = '') => {
    let index = checkedArray.indexOf(id);
    checkedArray.splice(index, 1);
    onFilterClick();
  };

  const onStatusToggle = isExpandedItem => {
    setisExpanded(isExpandedItem);
    setstatusIsExpanded(isExpandedItem);
  };
  const statusMenuItems = [
    <SelectOption key="=ACTIVE" value="ACTIVE" />,
    <SelectOption key="COMPLETED" value="COMPLETED" />,
    <SelectOption key="ERROR" value="ERROR" />,
    <SelectOption key="ABORTED" value="ABORTED" />,
    <SelectOption key="SUSPENDED" value="SUSPENDED" />
  ];

  const toggleGroupItems = (
    <React.Fragment>
      <DataToolbarGroup variant="filter-group">
        <DataToolbarFilter chips={filters.status} deleteChip={onDelete} categoryName="Status">
          <Select
            variant={SelectVariant.checkbox}
            aria-label="Status"
            onToggle={onStatusToggle}
            onSelect={onStatusSelect}
            selections={filters.status}
            isExpanded={statusIsExpanded}
            placeholderText="Status"
          >
            {statusMenuItems}
          </Select>
        </DataToolbarFilter>
        <DataToolbarItem>
          <Button variant="primary" onClick={onFilterClick}>
            Apply Filter
          </Button>
        </DataToolbarItem>
      </DataToolbarGroup>
    </React.Fragment>
  );

  const toolbarItems = (
    <React.Fragment>
      <DataToolbarToggleGroup toggleIcon={<FilterIcon />} breakpoint="xl">
        {toggleGroupItems}
      </DataToolbarToggleGroup>
    </React.Fragment>
  );

  return (
    <DataToolbar
      id="data-toolbar-with-filter"
      className="pf-m-toggle-group-container"
      collapseListedFiltersBreakpoint="xl"
      clearAllFilters={onDelete}
    >
      <DataToolbarContent>{toolbarItems}</DataToolbarContent>
    </DataToolbar>
  );
};

export default DataToolbarWithFilter;
