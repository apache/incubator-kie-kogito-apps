import React, { useState, useEffect } from 'react';
import { useQuery } from '@apollo/react-hooks';
import {
  DataToolbar,
  DataToolbarContent,
  DataToolbarToggleGroup,
  DataToolbarGroup,
  DataToolbarFilter
} from '@patternfly/react-core/dist/esm/experimental';
import {
  Select,
  SelectOption,
  Dropdown,
  DropdownToggle,
  DropdownItem,
  TextInput,
  SelectGroup,
  SelectVariant,
  Grid,
  GridItem,
  PageSection
} from '@patternfly/react-core';
import { FilterIcon } from '@patternfly/react-icons';

import DomainExplorerColumnPicker from '../../Organisms/DomainExplorerColumnPicker/DomainExplorerColumnPicker';
import DomainExplorerTable from '../../Organisms/DomainExplorerTable/DomainExplorerTable';

import {
  useGetQueryTypesQuery,
  useGetQueryQuery,
  useGetSchemaQuery,
  useGetTypesQuery
} from '../../../graphql/types';
import './DomainExplorerPage.css';

const DomainExplorerPage = () => {
  const [initData2, setInitData2] = useState<any>({
    __schema: { queryType: [] }
  });
  const [schemaDropdown, setSchemaDropDown] = useState(false);
  const [currentCategory, setCurrentCategory] = useState('Query');
  const [currentArgument, setCurrentArgument] = useState('');
  const [currentArgumentScalar, setCurrentArgumentScalar] = useState('');
  const [currentSchema, setCurrentSchema] = useState([]);
  const [currentQuery, setCurrentQuery] = useState('');
  const [isExpanded, setIsExpanded] = useState(false);
  const [selectTypes, setSelectTypes] = useState('');
  const [typesDropdown, setTypesDropdown] = useState(false);
  const [isFilterDropdownOpen, setIsFilterDropdownOpen] = useState(false);
  const [columnPickerType, setColumnPickerType] = useState('');
  const [isCategoryDropdownOpen, setIsCategoryDropdownOpen] = useState(false);
  const [isOpen, setIsOpen] = useState(false);
  const [selected, setSelected] = useState('');
  const [columnFilters, setColumnFilters] = useState({});
  const [tableLoading, setTableLoading] = useState(true);
  const [displayTable, setDisplayTable] = useState(false);
  const temp = [];
  const nullTypes = [
    null,
    'String',
    'Boolean',
    'StringArgument',
    'DateArgument',
    'IdArgument',
    'BooleanArgument',
    'NumericArgument',
    'DateRange',
    'NumericRange',
    'StringArrayArgument'
  ];

  const getQuery = useGetQueryQuery();

  const getQueryTypes = useGetQueryTypesQuery();

  useEffect(() => {
    setInitData2(getQueryTypes.data);
  }, [getQueryTypes.data]);

  const getSchema: any = useGetSchemaQuery({
    variables: { currentQuery }
  });

  useEffect(() => {
    setCurrentSchema(temp);
  }, [getSchema.data]);

  const samp =
    !getQueryTypes.loading &&
    !getSchema.loading &&
    initData2.__schema.queryType.filter(item =>
      currentSchema.includes(item.name)
    );

  const getTypes = useGetTypesQuery({
    variables: { currentArgument }
  });

  const onCategoryToggle = _isOpen => {
    setIsCategoryDropdownOpen(_isOpen);
  };

  const onCategorySelect = event => {
    setCurrentCategory(event.target.innerText);
    const tempChip = [];
    tempChip.push(event.target.innerText);
    const _a =
      !getQuery.loading &&
      getQuery.data.__type.fields.find(item => {
        if (item.name === event.target.innerText) {
          return item;
        }
      });
    setCurrentQuery(_a.args[0].type.name);
    setColumnPickerType(_a.type.ofType.name);
    setIsCategoryDropdownOpen(!isCategoryDropdownOpen);
    setSchemaDropDown(true);
  };

  const onChange = (_value, selection, event) => {
    setTypesDropdown(true);
    setSelected(selection);

    const x = samp.find(item =>
      item.inputFields.find(option => {
        if (option.name === selection) {
          return option;
        }
      })
    );
    const y = x.inputFields.find(data => {
      if (data.name === selection) {
        return data;
      }
    });
    if (y.type.kind === 'INPUT_OBJECT') {
      setCurrentArgument(y.type.name);
    } else {
      setCurrentArgumentScalar(y.type.name);
    }

    setIsExpanded(false);
  };

  const onToggle = _isOpen => {
    setIsFilterDropdownOpen(_isOpen);
  };

  const onFieldToggle = _isExpanded => {
    setIsExpanded(_isExpanded);
  };

  const onSelect = event => {
    setSelectTypes(event.target.innerText);
    setIsFilterDropdownOpen(false);
    const typeName =
      getTypes.data.__type &&
      getTypes.data.__type.inputFields.find(item => {
        if (item.name === event.target.innerText) {
          return item;
        }
      });
    setCurrentArgumentScalar(typeName.type.name);
  };

  const onToggleBoolean = _isOpen => {
    setIsOpen(_isOpen);
  };

  const onSelectBoolean = event => {
    setIsOpen(!isOpen);
  };

  const buildCategoryDropdown = () => {
    const queryDropDown =
      !getQuery.loading && getQuery.data.__type.fields.slice(2);
    const dropdownItems =
      !getQuery.loading &&
      queryDropDown.map((item, index) => (
        <DropdownItem key={index}>{item.name}</DropdownItem>
      ));
    return (
      <DataToolbarFilter categoryName="Category">
        <Dropdown
          onSelect={onCategorySelect}
          position="left"
          toggle={
            <DropdownToggle
              onToggle={onCategoryToggle}
              style={{ width: '100%' }}
            >
              <FilterIcon /> {currentCategory}
            </DropdownToggle>
          }
          isOpen={isCategoryDropdownOpen}
          dropdownItems={dropdownItems}
          style={{ width: '100%' }}
        />
      </DataToolbarFilter>
    );
  };

  const buildFilterDropdown = () => {
    !getSchema.loading &&
      getSchema.data.__type &&
      getSchema.data.__type.inputFields.map((field, index) => {
        if (field.type.kind !== 'LIST') {
          temp.push(field.type.name);
        }
      });

    const typesMenuItems =
      !getTypes.loading &&
      getTypes.data.__type &&
      getTypes.data.__type.inputFields.map((data, index) => (
        <SelectOption key={index} value={data.name} />
      ));

    const dropdownItems = [
      <DropdownItem key="true" component="button">
        {' '}
        true{' '}
      </DropdownItem>,
      <DropdownItem key="false" component="button">
        {' '}
        false{' '}
      </DropdownItem>
    ];

    const fetchSchema = option => {
      return (
        !getQueryTypes.loading &&
        getQueryTypes.data.__schema &&
        getQueryTypes.data.__schema.queryType.find(item => {
          if (item.name === option.type.name) {
            return item;
          }
        })
      );
    };

    const childOptions = inputFields => {
      if (inputFields) {
        return inputFields.map((field, _i) => {
          if (nullTypes.includes(field.type.name)) {
            return <SelectOption key={_i} value={field.name} />;
          } else {
            const m = fetchSchema(field);
            return (
              <SelectGroup key={_i} label={field.name} value={field.name}>
                {m.inputFields.map((opt, index) => {
                  if (nullTypes.includes(opt.type.name)) {
                    return <SelectOption key={index} value={opt.name} />;
                  } else {
                    const tem = fetchSchema(opt);
                    return (
                      <SelectGroup
                        key={index}
                        label={opt.name}
                        value={opt.name}
                      >
                        {childOptions(tem.inputFields)}
                      </SelectGroup>
                    );
                  }
                })}
              </SelectGroup>
            );
          }
        });
      }
    };

    const options =
      !getSchema.loading &&
      getSchema.data.__type &&
      getSchema.data.__type.inputFields
        .filter((group, index) => {
          if (group.type.name !== null) {
            return group;
          }
        })
        .map((group, index) => {
          return (
            <SelectGroup label={group.name} key={index} value={group.name}>
              {group.type.inputFields &&
                group.type.inputFields.map((option, i) => {
                  if (!nullTypes.includes(option.type.name)) {
                    const _v = fetchSchema(option);
                    return (
                      <SelectGroup
                        label={option.name}
                        key={i}
                        value={option.name}
                      >
                        {childOptions(_v.inputFields)}
                      </SelectGroup>
                    );
                  } else {
                    return <SelectOption key={i} value={option.name} />;
                  }
                })}
            </SelectGroup>
          );
        });

    return (
      <React.Fragment>
        {schemaDropdown
          ? !getSchema.loading && (
              <DataToolbarFilter categoryName="Schema">
                <Select
                  variant={SelectVariant.single}
                  onToggle={onFieldToggle}
                  onSelect={onChange}
                  selections={selected}
                  isExpanded={isExpanded}
                  placeholderText="Select a field"
                  ariaLabelledBy="Select a field"
                  maxHeight="60vh"
                  isGrouped
                >
                  {options}
                </Select>
              </DataToolbarFilter>
            )
          : ''}

        {!getTypes.loading && typesDropdown && (
          <DataToolbarFilter categoryName="Types">
            <Select
              aria-label="Location"
              onToggle={onToggle}
              onSelect={onSelect}
              selections={selectTypes}
              isExpanded={isFilterDropdownOpen}
              placeholderText="Types"
            >
              {typesMenuItems}
            </Select>
          </DataToolbarFilter>
        )}

        {currentArgumentScalar === 'String' && (
          <TextInput type="text" aria-label="text input example" />
        )}
        {currentArgumentScalar === 'Boolean' && (
          <Dropdown
            onSelect={onSelectBoolean}
            toggle={
              <DropdownToggle id="toggle-id" onToggle={onToggleBoolean}>
                Boolean
              </DropdownToggle>
            }
            isOpen={isOpen}
            dropdownItems={dropdownItems}
          />
        )}
      </React.Fragment>
    );
  };

  const renderToolbar = () => {
    return (
      <Grid>
        <GridItem span={columnPickerType ? 9 : 12}>
          <DataToolbar
            id="data-toolbar-with-chip-groups"
            collapseListedFiltersBreakpoint="xl"
          >
            <DataToolbarContent>
              <DataToolbarToggleGroup
                toggleIcon={<FilterIcon />}
                breakpoint="xl"
              >
                <DataToolbarGroup variant="filter-group">
                  {buildCategoryDropdown()}
                  {buildFilterDropdown()}
                </DataToolbarGroup>
              </DataToolbarToggleGroup>
            </DataToolbarContent>
          </DataToolbar>
        </GridItem>
        <GridItem span={3} rowSpan={2} style={{ backgroundColor: 'white' }}>
          {!getSchema.loading && (
            <DomainExplorerColumnPicker
              columnPickerType={columnPickerType}
              setColumnFilters={setColumnFilters}
              setTableLoading={setTableLoading}
              setDisplayTable={setDisplayTable}
              getQueryTypes={getQueryTypes}
              nullTypes={nullTypes}
            />
          )}
        </GridItem>
      </Grid>
    );
  };

  return (
    <PageSection isFilled>
      <Grid>
        <GridItem span={12}>{renderToolbar()}</GridItem>
        <GridItem span={12}>
          <DomainExplorerTable
            columnFilters={columnFilters}
            tableLoading={tableLoading}
            displayTable={displayTable}
          />
        </GridItem>
      </Grid>
    </PageSection>
  );
};

export default React.memo(DomainExplorerPage);
