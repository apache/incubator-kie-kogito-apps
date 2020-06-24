import React, { useState, useEffect } from 'react';
import {
  Select,
  SelectVariant,
  SelectGroup,
  SelectOption,
  TextInput,
  Button,
  Dropdown,
  DropdownToggle,
  DropdownItem,
  InputGroup,
  Popover,
  PopoverPosition
} from '@patternfly/react-core';
import { GraphQL } from '../../../graphql/types';

import useGetInputFieldsFromTypeQuery = GraphQL.useGetInputFieldsFromTypeQuery;
import gql from 'graphql-tag';
import _ from 'lodash';
import { query } from 'gql-query-builder';
import { useApolloClient } from 'react-apollo';
import './DomainExplorerFilterOptions.css';
import { QuestionCircleIcon } from '@patternfly/react-icons';

const DomainExplorerFilterOptions = ({
  currentDomain,
  getQuery,
  parameters,
  setColumnFilters,
  setTableLoading,
  setDisplayTable,
  setDisplayEmptyState,
  queryAttributes,
  setQueryAttributes,
  enableFilter,
  setEnableFilter,
  setError,
  getQueryTypes,
  filterChips,
  setFilterChips,
  runFilter,
  setRunFilter,
  finalFilters,
  setFinalFilters,
  getSchema,
  argument
}) => {
  // tslint:disable: forin
  // tslint:disable: no-floating-promises
  const client = useApolloClient();
  const [initData2, setInitData2] = useState<any>({
    __schema: { queryType: [] }
  });
  const [isExpanded, setIsExpanded] = useState(false);
  const [isFilterDropdownOpen, setIsFilterDropdownOpen] = useState(false);
  const [selected, setSelected] = useState('id');
  const [selectTypes, setSelectTypes] = useState('equal');
  const [textValue, setTextValue] = useState('');
  const [inputArray, setInputArray] = useState('');
  const [isOpen, setIsOpen] = useState(false);
  const [currentArgument, setCurrentArgument] = useState('');
  const [currentArgumentScalar, setCurrentArgumentScalar] = useState('String');
  const [typeParent, setTypeParent] = useState<any>([]);
  const [currentBoolean, setCurrentBoolean] = useState('Boolean');
  const [stateToggle, setStateToggle] = useState(false);
  const [multiStateToggle, setMultiStateToggle] = useState(false);
  const [selectedState, setSelectedState] = useState('');
  const [multiState, setMultiState] = useState([]);

  const nullTypes = [
    null,
    'String',
    'Boolean',
    'StringArgument',
    'DateArgument',
    'IdArgument',
    'BooleanArgument',
    'NumericArgument',
    'StringArrayArgument',
    'DateRange',
    'NumericRange',
    'ProcessInstanceState',
    'ProcessInstanceStateArgument'
  ];

  const scalarTypes = ['String', 'Boolean'];

  const nonArgs = [null, 'String', 'Boolean'];
  const stateArray = [
    'ABORTED',
    'ACTIVE',
    'COMPLETED',
    'ERROR',
    'PENDING',
    'SUSPENDED'
  ];
  const temp = [];

  useEffect(() => {
    setInitData2(getQueryTypes.data);
  }, [getQueryTypes.data]);

  !getSchema.loading &&
    getSchema.data.__type &&
    getSchema.data.__type.inputFields.map((field, index) => {
      if (field.type.kind !== 'LIST') {
        temp.push(field.type.name);
      }
    });

  const onFieldToggle = _isExpanded => {
    setIsExpanded(_isExpanded);
  };

  // Second dropdown
  const getTypes = useGetInputFieldsFromTypeQuery({
    variables: {
      type: currentArgument
    }
  });

  const onToggle = _isOpen => {
    setIsFilterDropdownOpen(_isOpen);
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
    if (scalarTypes.includes(typeName.type.name)) {
      setCurrentArgumentScalar(typeName.type.name);
    } else if (selected === 'state' && typeName.type.name === null) {
      setCurrentArgumentScalar('MultiSelection');
    } else if (
      selected === 'state' &&
      typeName.type.name === 'ProcessInstanceState'
    ) {
      setCurrentArgumentScalar('stateSelection');
    } else {
      setCurrentArgumentScalar('ArrayString');
    }
  };

  const typesMenuItems =
    !getTypes.loading &&
    getTypes.data.__type &&
    getTypes.data.__type.inputFields.map((data, index) => (
      <SelectOption key={index} value={data.name} />
    ));

  // Third dropdown

  const textBoxChange = value => {
    setTextValue(value);
  };

  const textGroupChange = value => {
    setInputArray(value);
  };

  const onSelectBoolean = event => {
    setCurrentBoolean(event.target.innerText);
    setIsOpen(!isOpen);
  };

  const onToggleBoolean = _isOpen => {
    setIsOpen(_isOpen);
  };

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

  // filter dropdown starts here
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
  let childItems;
  let finalResult: any = [];
  const childSelectionItems = (_data, title, ...attr) => {
    let nestedTitles = '';
    childItems =
      !getQueryTypes.loading &&
      _data.map(group => {
        const label = title + ' / ' + attr.join();
        const childEle = (
          <SelectGroup
            label={label.replace(/\,/g, '')}
            key={Math.random()}
            id={group.name}
            value={label.replace(/\,/g, '')}
          >
            {group.inputFields !== null &&
              group.inputFields
                .filter((item, _index) => {
                  if (!nullTypes.includes(item.type.name)) {
                    const tempData = [];
                    const n = fetchSchema(item);
                    tempData.push(n);
                    nestedTitles = nestedTitles + ' / ' + item.name;
                    childSelectionItems(tempData, title, attr, nestedTitles);
                  } else {
                    return item;
                  }
                })
                .map(item => {
                  return (
                    <SelectOption
                      key={Math.random()}
                      value={item.name + title + group.name}
                    >
                      {item.name}
                    </SelectOption>
                  );
                })}
          </SelectGroup>
        );
        return childEle;
      });
    finalResult.push(childItems);
  };
  const child = [];
  const rootElementsArray = [];
  const selectionItems = () => {
    !getSchema.loading &&
      getSchema.data.__type &&
      getSchema.data.__type.inputFields
        .filter((group, index) => {
          if (group.type.kind !== 'LIST') {
            return group;
          } else {
            child.push(<SelectOption key={group.name} value={group.name} />);
          }
        })
        .map((group, index) => {
          let groupItem;
          let rootItem;
          group.type.inputFields.filter(item => {
            if (!nonArgs.includes(item.type.name)) {
              groupItem = group;
            } else {
              rootItem = group;
            }
          });
          if (rootItem) {
            const _rootElement = (
              <SelectOption key={rootItem.name} value={rootItem.name} />
            );
            !rootElementsArray.includes(_rootElement) &&
              rootElementsArray.push(_rootElement);
          }

          let ele;
          if (groupItem) {
            ele = (
              <SelectGroup
                label={groupItem.name}
                key={index}
                id={groupItem.name}
                value={groupItem.name}
              >
                {groupItem.type.inputFields &&
                  groupItem.type.inputFields
                    .filter((item, _index) => {
                      if (!nullTypes.includes(item.type.name)) {
                        const tempData = [];
                        const _v = fetchSchema(item);
                        tempData.push(_v);
                        childSelectionItems(
                          tempData,
                          groupItem.name,
                          item.name
                        );
                      } else {
                        return item;
                      }
                    })
                    .map((item, _index) => {
                      return (
                        <SelectOption
                          key={_index}
                          value={item.name + groupItem.name}
                        >
                          {item.name}
                        </SelectOption>
                      );
                    })}
              </SelectGroup>
            );
          }
          ele && !finalResult.includes(ele) && finalResult.push(ele);
        });
  };

  const rootElement: any = (
    <SelectGroup label=" " key={Math.random()} id="" value=" ">
      {rootElementsArray}
    </SelectGroup>
  );
  finalResult.unshift(rootElement);
  function getAllChilds(arr, comp) {
    const unique = arr
      .map(e => e[comp])
      .map((e, i, final) => final.indexOf(e) === i && i)
      .filter(e => arr[e])
      .map(e => arr[e]);

    return unique;
  }
  const clearSelection = () => {
    setSelected('');
    setSelectTypes('');
    setCurrentArgumentScalar('');
  };
  const onChange = (event, selection, isPlaceholder) => {
    if (isPlaceholder) {
      clearSelection();
    }
    const innerText = event.target.textContent;
    setSelected(innerText);
    const parent = event.nativeEvent.target.parentElement.parentElement.getAttribute(
      'value'
    );
    let tempParents;
    let lastEle;
    if (parent !== ' ') {
      tempParents = parent.split(' / ');
      setTypeParent(tempParents);
      lastEle = tempParents.slice(-1)[0];
    } else {
      tempParents = [innerText];
      lastEle = tempParents.slice(-1)[0];
    }

    let arg;
    if (lastEle === 'processInstances') {
      let str = lastEle.charAt(0).toUpperCase() + lastEle.slice(1);
      str = str.substring(0, str.length - 1);
      arg = str + 'MetaArgument';
    } else if (lastEle === 'userTasks') {
      let str = lastEle.charAt(0).toUpperCase() + lastEle.slice(1);
      str = str.substring(0, str.length - 1);
      arg = str + 'InstanceMetaArgument';
    } else {
      const str = lastEle.charAt(0).toUpperCase() + lastEle.slice(1);
      arg = str + 'Argument';
    }
    const b = initData2.__schema.queryType.find(type => {
      if (type.name === arg) {
        return type;
      }
    });
    const y = b.inputFields.find(data => {
      if (data.name === innerText) {
        return data;
      }
    });
    if (y === undefined) {
      setCurrentArgument(b.name);
    } else {
      if (y.type.kind === 'INPUT_OBJECT') {
        setCurrentArgument(y.type.name);
      } else {
        setCurrentArgumentScalar(y.type.name);
      }
    }
    setSelectTypes('');
    setIsExpanded(false);
  };

  const onLoad = () => {
    const innerText = 'id';
    let tempParents;
    tempParents = [innerText];
    const lastEle = tempParents.slice(-1)[0];
    let arg;
    const str = lastEle.charAt(0).toUpperCase() + lastEle.slice(1);
    arg = str + 'Argument';
    const b = getQueryTypes.data.__schema.queryType.find(type => {
      if (type.name === arg) {
        return type;
      }
    });
    const y = b.inputFields.find(data => {
      if (data.name === innerText) {
        return data;
      }
    });
    if (y === undefined) {
      setCurrentArgument(b.name);
    } else {
      if (y.type.kind === 'INPUT_OBJECT') {
        setCurrentArgument(y.type.name);
      } else {
        setCurrentArgumentScalar(y.type.name);
      }
    }
    setIsExpanded(false);
  };

  useEffect(() => {
    onLoad();
  }, []);

  !getSchema.loading && selectionItems();
  finalResult = finalResult.flat();

  // filter dropdown ends here
  // Generate Query

  const obj: any = {};
  const set = (_obj, path, val) => {
    const keys = path.split(',');
    const lastKey = keys.pop();
    // tslint:disable-next-line: no-shadowed-variable
    const lastObj = keys.reduce(
      // tslint:disable-next-line: no-shadowed-variable
      (_obj, key) => (_obj[key] = _obj[key] || {}),
      _obj
    );
    lastObj[lastKey] = val;
  };

  const nestedCheck = (ele, valueObj) => {
    for (const key in ele) {
      const _temp = ele[key];
      if (typeof _temp[0] === 'object') {
        for (const nestedProp in _temp[0]) {
          const nestedObj = {};
          const result = nestedCheck(_temp[0], valueObj);
          if (valueObj.hasOwnProperty(nestedProp)) {
            valueObj[nestedProp] = result;
          } else {
            nestedObj[nestedProp] = result;
            valueObj = { ...valueObj, ...nestedObj };
          }
          return valueObj;
        }
      } else {
        const val = ele[key];
        const tempObj = {};
        tempObj[val[0]] = null;
        const firstKey = Object.keys(valueObj)[0];
        valueObj = { ...valueObj[firstKey], ...tempObj };
        return valueObj;
      }
    }
  };

  const checkFunc = (ele, valueObj) => {
    for (const key in ele) {
      const _temp = ele[key];
      if (typeof _temp[0] === 'object') {
        for (const nestedProp in _temp[0]) {
          const nestedObj = {};
          if (valueObj.hasOwnProperty(nestedProp)) {
            const result = nestedCheck(_temp[0], valueObj);
            valueObj[nestedProp] = result;
          } else {
            const result = checkFunc(_temp[0], valueObj);
            nestedObj[nestedProp] = result;
            valueObj = { ...valueObj, ...nestedObj };
          }
          return valueObj;
        }
      } else {
        const val = ele[key];
        const tempObj = {};
        tempObj[val[0]] = null;
        valueObj = { ...valueObj, ...tempObj };
        return valueObj;
      }
    }
  };

  const validateResponse = _obj => {
    let contentObj = {};
    for (const prop in _obj) {
      const arr = [];
      if (_obj[prop] === null) {
        const parentObj = {};
        parameters.map(params => {
          if (params.hasOwnProperty(prop)) {
            arr.push(params);
          }
        });
        let valueObj = {};
        arr.filter(ele => {
          valueObj = checkFunc(ele, valueObj);
        });
        parentObj[prop] = valueObj;
        contentObj = { ...contentObj, ...parentObj };
      } else {
        const elseObj = {};
        elseObj[prop] = _obj[prop];
        contentObj = { ...contentObj, ...elseObj };
      }
    }
    return contentObj;
  };

  async function generateFilterQuery(Query) {
    currentArgumentScalar === 'String' && setTextValue('');
    if (parameters.length > 1) {
      try {
        await client
          .query({
            query: gql`
              ${Query.query}
            `,
            variables: Query.variables,
            fetchPolicy: 'no-cache'
          })
          .then(response => {
            setTableLoading(false);
            const firstKey = Object.keys(response.data)[0];
            if (response.data[firstKey].length > 0) {
              const resp = response.data;
              const respKeys = Object.keys(resp)[0];
              const tableContent = resp[respKeys];
              const finalResp = [];
              tableContent.map(content => {
                const finalObject = validateResponse(content);
                finalResp.push(finalObject);
              });
              setColumnFilters(finalResp);
              setRunFilter(false);
              setDisplayTable(true);
              setDisplayEmptyState(false);
            } else {
              setDisplayEmptyState(true);
              setRunFilter(false);
            }
          });
      } catch (error) {
        setError(error);
      }
    } else {
      setDisplayEmptyState(false);
      setDisplayTable(false);
    }
  }
  useEffect(() => {
    if (enableFilter === true && runFilter === true) {
      // const arg = domainArg.args[0].type.name;
      setQueryAttributes({
        operation: currentDomain,
        variables: { where: { value: finalFilters, type: argument } },
        fields: parameters
      });
      const Query = query({
        operation: currentDomain,
        variables: { where: { value: finalFilters, type: argument } },
        fields: parameters
      });
      runFilter && generateFilterQuery(Query);
    } else {
      const Query = query({
        operation: currentDomain,
        variables: {},
        fields: parameters
      });
      runFilter && generateFilterQuery(Query);
    }
  }, [enableFilter, runFilter]);

  const validateChip = (parentString, _selected, _selectTypes, value) => {
    if (_selectTypes === 'equal') {
      return parentString
        ? `${parentString} / ${_selected}: ${value}`
        : `${_selected}: ${value}`;
    } else if (_selectTypes === 'isNull') {
      if (value === true) {
        return parentString
          ? `${parentString} / ${_selected}: is null`
          : `${_selected}: is null`;
      } else {
        return parentString
          ? `${parentString} / ${_selected}: is not null`
          : `${_selected}: is not null`;
      }
    } else if (_selectTypes === 'in') {
      return parentString
        ? `${parentString} / ${_selected}: is in ${value}`
        : `${_selected}: is in ${value}`;
    } else {
      return parentString
        ? `${parentString} / ${_selected}: ${_selectTypes} ${value}`
        : `${_selected}: ${_selectTypes} ${value}`;
    }
  };
  const checkChipArray = (chipSelections, chipText) => {
    let value = '';
    filterChips.forEach(item => {
      const tempItem = item.split(':');
      if (tempItem[0] === chipSelections) {
        value = item;
      }
    });
    if (value.length > 0) {
      const index = filterChips.indexOf(value);
      setFilterChips(prev => {
        prev.splice(index, 1);
        return [...prev, chipText];
      });
    } else {
      setFilterChips(prev => [...prev, chipText]);
    }
  };

  const validataScalarArgument = (scalarType, n, value) => {
    let parentString = '';
    let chipText = '';
    typeParent &&
      typeParent.map(parent => (parentString = parentString + '/' + parent));
    parentString = parentString.substring(1);
    if (scalarType === 'ArrayString') {
      chipText = validateChip(parentString, selected, selectTypes, inputArray);
      let chipSelections = '';
      if (typeParent) {
        chipSelections = `${parentString}/${selected}`;
      } else {
        chipSelections = selected;
      }
      checkChipArray(chipSelections, chipText);
      const tempArray = inputArray.split(',');
      set(obj, n, tempArray);
    } else {
      chipText = validateChip(parentString, selected, selectTypes, value);
      let chipSelections = '';
      if (typeParent) {
        chipSelections = `${parentString}/${selected}`;
      } else {
        chipSelections = selected;
      }
      checkChipArray(chipSelections, chipText);
      set(obj, n, value);
    }
    setFinalFilters(() => {
      if (finalFilters.hasOwnProperty(typeParent)) {
        const te: any = Object.values(obj)[0];
        finalFilters[typeParent] = { ...finalFilters[typeParent], ...te };
        return finalFilters;
      } else {
        return { ...finalFilters, ...obj };
      }
    });
    setTypeParent('');
  };

  const onApplyFilter = async () => {
    setRunFilter(true);
    setEnableFilter(true);
    let n;
    if (typeParent.length > 0) {
      n = `${typeParent},${selected},${selectTypes}`;
    } else {
      n = `${selected},${selectTypes}`;
    }
    if (currentArgumentScalar === 'Boolean') {
      validataScalarArgument(currentArgumentScalar, n, currentBoolean);
    } else if (currentArgumentScalar === 'ArrayString') {
      validataScalarArgument(currentArgumentScalar, n, selectedState);
    } else if (currentArgumentScalar === 'stateSelection') {
      validataScalarArgument(currentArgumentScalar, n, selectedState);
    } else if (currentArgumentScalar === 'MultiSelection') {
      validataScalarArgument(currentArgumentScalar, n, multiState);
    } else {
      validataScalarArgument(currentArgumentScalar, n, textValue);
    }
  };

  const onStateToggle = _isOpen => {
    setStateToggle(_isOpen);
  };

  const onStateSelect = (event, selection) => {
    setSelectedState(selection);
    setStateToggle(!stateToggle);
  };

  const onMultiStateToggle = _isOpen => {
    setMultiStateToggle(_isOpen);
  };

  const onMultiStateSelect = (event, selection) => {
    if (multiState.includes(selection)) {
      setMultiState(prev => prev.filter(item => item !== selection));
    } else {
      setMultiState(prev => [...prev, selection]);
    }
    setMultiStateToggle(!multiStateToggle);
  };

  return (
    <>
      {!getSchema.loading && (
        <Select
          variant={SelectVariant.typeahead}
          onToggle={onFieldToggle}
          onSelect={onChange}
          onClear={clearSelection}
          selections={selected}
          isExpanded={isExpanded}
          id="select-field"
          placeholderText="Select a field"
          ariaLabelledBy="Select a field"
          maxHeight="60vh"
          isGrouped
        >
          {getAllChilds(finalResult, 'props')}
        </Select>
      )}
      {!getTypes.loading && (
        <Select
          aria-label="Location"
          onToggle={onToggle}
          onSelect={onSelect}
          selections={selectTypes}
          isExpanded={isFilterDropdownOpen}
          id="select-operator"
          placeholderText="Operator"
        >
          {typesMenuItems}
        </Select>
      )}
      {currentArgumentScalar === 'String' && (
        <>
          <TextInput
            name="filterText"
            type="search"
            aria-label={`filter text for ${selected}`}
            onChange={textBoxChange}
            className="types-selections"
            placeholder="value"
            value={textValue}
          />
          <Button
            variant="primary"
            onClick={onApplyFilter}
            isDisabled={textValue.length === 0}
          >
            Apply Filter
          </Button>
        </>
      )}
      {currentArgumentScalar === 'Boolean' && (
        <>
          <Dropdown
            onSelect={onSelectBoolean}
            toggle={
              <DropdownToggle id="toggle-id" onToggle={onToggleBoolean}>
                {currentBoolean}
              </DropdownToggle>
            }
            isOpen={isOpen}
            dropdownItems={dropdownItems}
            className="types-selections"
          />
          <Button variant="primary" onClick={onApplyFilter}>
            Apply Filter
          </Button>
        </>
      )}
      {currentArgumentScalar === 'ArrayString' && (
        <>
          <InputGroup>
            <TextInput
              name="textInput10"
              id="textInput10"
              type="text"
              onChange={textGroupChange}
              aria-label="input example with popover"
              placeholder="value"
              value={inputArray}
            />
            <Popover
              aria-label="popover example"
              position={PopoverPosition.top}
              bodyContent='This field allows specifying multiple values using "," as value delimiter'
            >
              <Button variant="control" aria-label="popover for input">
                <QuestionCircleIcon />
              </Button>
            </Popover>
          </InputGroup>
          <Button
            variant="primary"
            onClick={onApplyFilter}
            isDisabled={inputArray.length === 0}
          >
            Apply Filter
          </Button>
        </>
      )}
      {currentArgumentScalar === 'stateSelection' && (
        <>
          <Select
            aria-label="Location"
            onToggle={onStateToggle}
            onSelect={onStateSelect}
            selections={selectedState}
            isExpanded={stateToggle}
            placeholderText="State"
            className="types-selections"
          >
            {stateArray.map((item, index) => (
              <SelectOption key={0} value={item} />
            ))}
          </Select>

          <Button variant="primary" onClick={onApplyFilter}>
            Apply Filter
          </Button>
        </>
      )}
      {currentArgumentScalar === 'MultiSelection' && (
        <>
          <Select
            variant={SelectVariant.checkbox}
            aria-label="Select Input"
            onToggle={onMultiStateToggle}
            onSelect={onMultiStateSelect}
            selections={multiState}
            isExpanded={multiStateToggle}
            placeholderText="State"
          >
            {stateArray.map((item, index) => (
              <SelectOption key={0} value={item} />
            ))}
          </Select>
          <Button variant="primary" onClick={onApplyFilter}>
            Apply Filter
          </Button>
        </>
      )}
    </>
  );
};

export default React.memo(DomainExplorerFilterOptions);
