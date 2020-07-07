import React, { useState, useEffect } from 'react';
import {
  DataToolbar,
  DataToolbarItem,
  DataToolbarContent,
  DataToolbarFilter,
  DataToolbarToggleGroup,
  DataToolbarGroup,
  Button,
  Select,
  SelectOption,
  SelectVariant,
  InputGroup,
  TextInput,
  Dropdown,
  DropdownToggle,
  DropdownToggleCheckbox,
  DropdownItem,
  DropdownPosition,
  OverflowMenuControl,
  OverflowMenuContent,
  KebabToggle,
  OverflowMenu,
  OverflowMenuItem
} from '@patternfly/react-core';
import { FilterIcon, SyncIcon } from '@patternfly/react-icons';
import _ from 'lodash';
import './ProcessListToolbar.css';
import { GraphQL } from '@kogito-apps/common';
import ProcessInstanceState = GraphQL.ProcessInstanceState;
import ProcessListModal from '../../Atoms/ProcessListModal/ProcessListModal';
import {
  handleMultipleAbort,
  handleMultipleRetry,
  handleMultipleSkip,
  setTitle
} from '../../../utils/Utils';
/* tslint:disable:no-string-literal */
interface IOwnProps {
  checkedArray: any;
  filterClick: any;
  setCheckedArray: any;
  setIsStatusSelected: any;
  filters: any;
  setFilters: any;
  initData: any;
  setInitData: any;
  selectedInstances: any;
  setSelectedInstances: any;
  getProcessInstances: (options: any) => void;
  setSearchWord: (searchWord: string) => void;
  searchWord: string;
  isAllChecked: boolean;
  setIsAllChecked: (isAllChecked: boolean) => void;
  setSelectedNumber: (selectedNumber: number) => void;
  selectedNumber: number;
}
const ProcessListToolbar: React.FC<IOwnProps> = ({
  checkedArray,
  filterClick,
  setCheckedArray,
  filters,
  setFilters,
  setIsStatusSelected,
  selectedInstances,
  getProcessInstances,
  setSearchWord,
  searchWord,
  isAllChecked,
  initData,
  setInitData,
  setIsAllChecked,
  setSelectedInstances,
  selectedNumber,
  setSelectedNumber
}) => {
  const [isExpanded, setIsExpanded] = useState<boolean>(false);
  const [isFilterClicked, setIsFilterClicked] = useState<boolean>(false);
  const [shouldRefresh, setShouldRefresh] = useState<boolean>(true);
  const [isCheckboxDropdownOpen, setisCheckboxDropdownOpen] = useState(false);
  const [ignoredInstances, setIgnoredInstances] = useState({});
  const [requiredInstances, setRequiredInstances] = useState({});
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [modalTitle, setModalTitle] = useState<string>('');
  const [titleType, setTitleType] = useState<string>('');
  const [isKebabOpen, setIsKebabOpen] = useState<boolean>(false);

  const onProcessManagementButtonSelect = () => {
    setIsKebabOpen(!isKebabOpen);
  };

  const onProcessManagementKebabToggle = isOpen => {
    setIsKebabOpen(isOpen);
  };

  const handleModalToggle = () => {
    setIsModalOpen(!isModalOpen);
  };

  const onFilterClick = () => {
    if (checkedArray.length === 0) {
      setFilters({ ...filters, status: checkedArray });
      setIsFilterClicked(true);
      setIsStatusSelected(false);
    } else {
      setFilters({ ...filters, status: checkedArray });
      filterClick();
      setIsFilterClicked(true);
      setIsStatusSelected(true);
    }
    setShouldRefresh(true);
  };

  const onSelect = event => {
    const selection = event.target.id;
    setIsFilterClicked(false);
    setShouldRefresh(false);
    /* istanbul ignore else */
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
    if (type === 'Status') {
      if (checkedArray.length === 1 && filters.status.length === 1) {
        const index = checkedArray.indexOf(id);
        checkedArray.splice(index, 1);
        setCheckedArray([]);
        setFilters({ ...filters, status: [], businessKey: [] });
        setIsStatusSelected(false);
        setShouldRefresh(false);
      } else if (!isFilterClicked) {
        if (filters.status.length === 1) {
          setCheckedArray([]);
          setFilters({ ...filters, status: [], businessKey: [] });
          setIsStatusSelected(false);
          setIsFilterClicked(false);
        } else {
          const index = filters.status.indexOf(id);
          checkedArray.splice(index, 1);
          checkedArray = [...filters.status];
          setCheckedArray(checkedArray);
          filterClick(checkedArray);
          setIsFilterClicked(true);
          setShouldRefresh(true);
        }
      } else {
        const index = checkedArray.indexOf(id);
        checkedArray.splice(index, 1);
        filterClick();
        setShouldRefresh(true);
      }
    }
    if (type === 'Business key') {
      filters.businessKey.splice(filters.businessKey.indexOf(id), 1);
      filterClick();
    }
  };

  useEffect(() => {
    if (!checkedArray.length && isFilterClicked) {
      setSearchWord('');
      setCheckedArray(checkedArray);
      setFilters({
        ...filters,
        status: checkedArray,
        businessKey: [...filters.businessKey]
      });
    }
  }, [checkedArray]);

  const clearAll = () => {
    setSearchWord('');
    setCheckedArray(['ACTIVE']);
    setFilters({ ...filters, status: ['ACTIVE'], businessKey: [] });
    filters.businessKey = [];
    filterClick(['ACTIVE']);
    getProcessInstances({
      variables: {
        state: ProcessInstanceState.Active,
        offset: 0,
        limit: 10
      }
    });
    setShouldRefresh(true);
  };

  const onRefreshClick = () => {
    /* istanbul ignore else */
    if (shouldRefresh && checkedArray.length !== 0) {
      filterClick(checkedArray);
    }
  };
  const onStatusToggle = isExpandedItem => {
    setIsExpanded(isExpandedItem);
  };

  const handleTextBoxChange = event => {
    const word = event;
    setSearchWord(word);
    if (word === '') {
      setSearchWord('');
      return;
    }
  };
  const handleEnterClick = e => {
    /* istanbul ignore else */
    if (e.key === 'Enter') {
      setShouldRefresh(true);
      filterClick(checkedArray);
    }
  };
  const checkboxDropdownToggle = () => {
    setisCheckboxDropdownOpen(!isCheckboxDropdownOpen);
  };

  const handleCheckboxSelectClick = (selection, isCheckboxClicked) => {
    /* istanbul ignore else */

    if (selection === 'none') {
      setIsAllChecked(false);
      setSelectedNumber(0);
      const copyOfInitData = { ...initData };
      const copyOfSelectedInstances = { ...selectedInstances };
      copyOfInitData.ProcessInstances.map(instance => {
        delete copyOfSelectedInstances[instance.id];
        instance.isChecked = false;
        /* istanbul ignore else */
        if (instance.childDataList !== undefined && instance.isOpen) {
          instance.childDataList.map(child => {
            delete copyOfSelectedInstances[child.id];
            child.isChecked = false;
          });
        }
      });
      setSelectedInstances(copyOfSelectedInstances);
      setInitData(copyOfInitData);
    } else if (selection === 'parent') {
      let parentSelectedNumber = 0;
      setIsAllChecked(true);
      const copyOfInitData = { ...initData };
      let copyOfSelectedInstances = { ...selectedInstances };
      copyOfInitData.ProcessInstances.map(instance => {
        const tempObj = {};
        /* istanbul ignore else */
        if (
          instance.addons.includes('process-management') &&
          instance.serviceUrl !== null
        ) {
          instance.isChecked = true;
          tempObj[instance.id] = instance;
          parentSelectedNumber += 1;
        }
        /* istanbul ignore else */
        if (instance.childDataList !== undefined && instance.isOpen) {
          instance.childDataList.map(child => {
            delete copyOfSelectedInstances[child.id];
            child.isChecked = false;
          });
        }
        copyOfSelectedInstances = { ...copyOfSelectedInstances, ...tempObj };
      });
      setSelectedNumber(parentSelectedNumber);
      setSelectedInstances(copyOfSelectedInstances);
      setInitData(copyOfInitData);
    } else if (selection === 'parent&child') {
      let allSelected = 0;
      setIsAllChecked(true);
      const copyOfInitData = { ...initData };
      let copyOfSelectedInstances = { ...selectedInstances };
      copyOfInitData.ProcessInstances.map(instance => {
        const tempObj = {};
        /* istanbul ignore else */
        if (
          instance.addons.includes('process-management') &&
          instance.serviceUrl !== null
        ) {
          instance.isChecked = true;
          tempObj[instance.id] = instance;
          allSelected += 1;
        }
        /* istanbul ignore else */
        if (instance.childDataList !== undefined && instance.isOpen) {
          instance.childDataList.map(child => {
            /* istanbul ignore else */
            if (
              child.addons.includes('process-management') &&
              instance.serviceUrl !== null
            ) {
              tempObj[child.id] = child;
              child.isChecked = true;
              allSelected += 1;
            }
          });
        }
        copyOfSelectedInstances = { ...copyOfSelectedInstances, ...tempObj };
      });
      setSelectedNumber(allSelected);
      setSelectedInstances(copyOfSelectedInstances);
      setInitData(copyOfInitData);
    }
    if (!isCheckboxClicked) {
      setisCheckboxDropdownOpen(!isCheckboxDropdownOpen);
    } else {
      if (isAllChecked) {
        setIsAllChecked(false);
        const copyOfInitData = { ...initData };
        const copyOfSelectedInstances = { ...selectedInstances };
        copyOfInitData.ProcessInstances.map(instance => {
          delete copyOfSelectedInstances[instance.id];
          instance.isChecked = false;
          if (instance.childDataList !== undefined && instance.isOpen) {
            instance.childDataList.map(child => {
              delete copyOfSelectedInstances[child.id];
              child.isChecked = false;
            });
          }
        });
        setSelectedNumber(0);
        setSelectedInstances(copyOfSelectedInstances);
        setInitData(copyOfInitData);
      } else {
        let allSelected = 0;
        setIsAllChecked(true);
        const copyOfInitData = { ...initData };
        let copyOfSelectedInstances = { ...selectedInstances };
        copyOfInitData.ProcessInstances.map(instance => {
          const tempObj = {};
          if (
            instance.addons.includes('process-management') &&
            instance.serviceUrl !== null
          ) {
            instance.isChecked = true;
            tempObj[instance.id] = instance;
            allSelected += 1;
          }
          if (instance.childDataList !== undefined && instance.isOpen) {
            instance.childDataList.map(child => {
              if (
                child.addons.includes('process-management') &&
                instance.serviceUrl !== null
              ) {
                tempObj[child.id] = child;
                child.isChecked = true;
                allSelected += 1;
              }
            });
          }
          copyOfSelectedInstances = { ...copyOfSelectedInstances, ...tempObj };
        });
        setSelectedNumber(allSelected);
        setSelectedInstances(copyOfSelectedInstances);
        setInitData(copyOfInitData);
      }
    }
  };

  const onShowMessage = (title, type) => {
    setModalTitle(title);
    setTitleType(type);
    handleModalToggle();
  };

  const handleMultiAbort = () => {
    const instancesToBeIgnored = {};
    for (const [id, processInstance] of Object.entries(selectedInstances)) {
      initData.ProcessInstances.map(instance => {
        if (instance.id === id) {
          if (
            processInstance['state'] === ProcessInstanceState.Aborted ||
            processInstance['state'] === ProcessInstanceState.Completed
          ) {
            instancesToBeIgnored[id] = processInstance;
            delete selectedInstances[id];
          } else {
            instance.state = ProcessInstanceState.Aborted;
          }
        }
        /* istanbul ignore else */
        if (instance.childDataList && instance.childDataList.length !== 0) {
          instance.childDataList.map(childData => {
            if (childData.id === id) {
              if (
                processInstance['state'] === ProcessInstanceState.Aborted ||
                processInstance['state'] === ProcessInstanceState.Completed
              ) {
                instancesToBeIgnored[id] = processInstance;
                delete selectedInstances[id];
              } else {
                childData.state = ProcessInstanceState.Aborted;
              }
            }
          });
        }
      });
    }
    setRequiredInstances(selectedInstances);
    setIgnoredInstances(instancesToBeIgnored);
    handleMultipleAbort(
      selectedInstances,
      () => onShowMessage('Abort operation', 'success'),
      () => onShowMessage('Abort operation', 'failure')
    );
  };

  const handleMultiRetryOrSkip = operationType => {
    const instancesToBeIgnored = {};
    for (const [id, processInstance] of Object.entries(selectedInstances)) {
      // tslint:disable-next-line
      if (processInstance['state'] !== ProcessInstanceState.Error) {
        instancesToBeIgnored[id] = processInstance;
        delete selectedInstances[id];
      }
    }
    setRequiredInstances(selectedInstances);
    setIgnoredInstances(instancesToBeIgnored);
    /* istanbul ignore else */
    if (operationType === 'Retry') {
      handleMultipleRetry(
        selectedInstances,
        () => onShowMessage('Retry operation', 'success'),
        () => onShowMessage('Retry operation', 'failure')
      );
    } else if (operationType === 'Skip') {
      handleMultipleSkip(
        selectedInstances,
        () => onShowMessage('Skip operation', 'success'),
        () => onShowMessage('Skip operation', 'failure')
      );
    }
  };

  const resetSelected = () => {
    initData.ProcessInstances.map(processInstance => {
      processInstance['isChecked'] = false;
      processInstance['childDataList'] &&
        processInstance['childDataList'].length !== 0 &&
        processInstance['childDataList'].map(
          child => (child['isChecked'] = false)
        );
    });
    setSelectedInstances({});
    setSelectedNumber(0);
    setIsAllChecked(false);
  };

  const checkboxItems = [
    <DropdownItem
      key="none"
      onClick={() => handleCheckboxSelectClick('none', false)}
      id="none"
    >
      Select none
    </DropdownItem>,
    <DropdownItem
      key="all-parent"
      onClick={() => handleCheckboxSelectClick('parent', false)}
      id="all-parent"
    >
      Select all parent processes
    </DropdownItem>,
    <DropdownItem
      key="all-parent-child"
      onClick={() => handleCheckboxSelectClick('parent&child', false)}
      id="all-parent-child"
    >
      Select all processes
    </DropdownItem>
  ];

  const statusMenuItems = [
    <SelectOption key="ACTIVE" value="ACTIVE" />,
    <SelectOption key="COMPLETED" value="COMPLETED" />,
    <SelectOption key="ERROR" value="ERROR" />,
    <SelectOption key="ABORTED" value="ABORTED" />,
    <SelectOption key="SUSPENDED" value="SUSPENDED" />
  ];

  const dropdownItemsProcesManagementButtons = () => {
    if (Object.keys(selectedInstances).length !== 0) {
      return [
        <DropdownItem key="abort" onClick={handleMultiAbort}>
          Abort selected
        </DropdownItem>,
        <DropdownItem key="skip" onClick={() => handleMultiRetryOrSkip('Skip')}>
          Skip selected
        </DropdownItem>,
        <DropdownItem
          key="retry"
          onClick={() => handleMultiRetryOrSkip('Retry')}
        >
          Retry selected
        </DropdownItem>
      ];
    } else {
      return [
        <DropdownItem key="abort" isDisabled>
          Abort selected
        </DropdownItem>,
        <DropdownItem key="skip" isDisabled>
          Skip selected
        </DropdownItem>,
        <DropdownItem key="retry" isDisabled>
          Retry selected
        </DropdownItem>
      ];
    }
  };

  const buttonItems = (
    <OverflowMenu breakpoint="xl">
      <OverflowMenuContent>
        <OverflowMenuItem>
          {Object.keys(selectedInstances).length !== 0 ? (
            <Button variant="secondary" onClick={handleMultiAbort}>
              Abort selected
            </Button>
          ) : (
            <Button variant="secondary" isDisabled>
              Abort selected
            </Button>
          )}
        </OverflowMenuItem>
        <OverflowMenuItem>
          {Object.keys(selectedInstances).length !== 0 ? (
            <Button
              variant="secondary"
              onClick={() => handleMultiRetryOrSkip('Skip')}
            >
              Skip selected
            </Button>
          ) : (
            <Button variant="secondary" isDisabled>
              Skip selected
            </Button>
          )}
        </OverflowMenuItem>
        <OverflowMenuItem>
          {Object.keys(selectedInstances).length !== 0 ? (
            <Button
              variant="secondary"
              onClick={() => handleMultiRetryOrSkip('Retry')}
            >
              Retry selected
            </Button>
          ) : (
            <Button variant="secondary" isDisabled>
              Retry selected
            </Button>
          )}
        </OverflowMenuItem>
      </OverflowMenuContent>
      <OverflowMenuControl>
        <Dropdown
          onSelect={onProcessManagementButtonSelect}
          toggle={<KebabToggle onToggle={onProcessManagementKebabToggle} />}
          isOpen={isKebabOpen}
          isPlain
          dropdownItems={dropdownItemsProcesManagementButtons()}
        />
      </OverflowMenuControl>
    </OverflowMenu>
  );

  const toggleGroupItems = (
    <React.Fragment>
      <DataToolbarGroup variant="filter-group">
        <DataToolbarItem variant="bulk-select" id="bulk-select">
          <Dropdown
            position={DropdownPosition.left}
            toggle={
              <DropdownToggle
                onToggle={checkboxDropdownToggle}
                splitButtonItems={[
                  <DropdownToggleCheckbox
                    id="select-all-checkbox"
                    key="split-checkbox"
                    aria-label="Select all"
                    isChecked={isAllChecked}
                    onChange={() =>
                      handleCheckboxSelectClick('parent&child', true)
                    }
                  />
                ]}
              >
                {selectedNumber === 0 ? '' : selectedNumber + ' selected'}
              </DropdownToggle>
            }
            dropdownItems={checkboxItems}
            isOpen={isCheckboxDropdownOpen}
          />
        </DataToolbarItem>

        <DataToolbarFilter
          chips={filters.status}
          deleteChip={onDelete}
          className="kogito-management-console__state-dropdown-list pf-u-mr-sm"
          categoryName="Status"
          id="datatoolbar-filter-status"
        >
          <Select
            variant={SelectVariant.checkbox}
            aria-label="Status"
            onToggle={onStatusToggle}
            onSelect={onSelect}
            selections={checkedArray}
            isExpanded={isExpanded}
            placeholderText="Status"
            id="status-select"
          >
            {statusMenuItems}
          </Select>
        </DataToolbarFilter>
        <DataToolbarFilter
          chips={filters.businessKey}
          deleteChip={onDelete}
          categoryName="Business key"
          id="datatoolbar-filter-businesskey"
        >
          <InputGroup>
            <TextInput
              name="businessKey"
              id="businessKey"
              type="search"
              aria-label="business key"
              onChange={handleTextBoxChange}
              onKeyPress={handleEnterClick}
              placeholder="Filter by business key"
              value={searchWord}
              isDisabled={checkedArray.length === 0}
            />
          </InputGroup>
        </DataToolbarFilter>
        <DataToolbarItem>
          <Button
            variant="primary"
            onClick={onFilterClick}
            id="Apply-filter-button"
          >
            Apply filter
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
      <DataToolbarGroup variant="icon-button-group">
        <DataToolbarItem>
          <Button
            variant="plain"
            onClick={onRefreshClick}
            aria-label="Refresh list"
            id="refresh-button"
          >
            <SyncIcon />
          </Button>
        </DataToolbarItem>
      </DataToolbarGroup>
      <DataToolbarItem variant="separator" />
      <DataToolbarGroup className="pf-u-ml-md" id="process-management-buttons">
        {buttonItems}
      </DataToolbarGroup>
    </React.Fragment>
  );

  return (
    <>
      <ProcessListModal
        modalTitle={setTitle(titleType, modalTitle)}
        isModalOpen={isModalOpen}
        requiredInstances={requiredInstances}
        ignoredInstances={ignoredInstances}
        checkedArray={checkedArray}
        handleModalToggle={handleModalToggle}
        isSingleAbort={false}
        resetSelected={resetSelected}
        titleString={modalTitle}
      />
      <DataToolbar
        id="data-toolbar-with-filter"
        className="pf-m-toggle-group-container kogito-management-console__state-dropdown-list"
        collapseListedFiltersBreakpoint="xl"
        clearAllFilters={() => clearAll()}
        clearFiltersButtonText="Reset to default"
      >
        <DataToolbarContent>{toolbarItems}</DataToolbarContent>
      </DataToolbar>
    </>
  );
};

export default ProcessListToolbar;
