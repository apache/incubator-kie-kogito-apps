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
  DataToolbar,
  DataToolbarContent,
  DataToolbarItem
} from '@patternfly/react-core';
import {
  useGetQueryTypesQuery,
  useGetInputFieldsFromQueryQuery,
  useGetInputFieldsFromTypeQuery
} from '../../../graphql/types';
import gql from 'graphql-tag';
import { query } from 'gql-query-builder';
import { useApolloClient } from 'react-apollo';
import './DomainExplorerFilterOptions.css';

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
  setError
}) => {
  // tslint:disable: forin
  // tslint:disable: no-floating-promises
  const client = useApolloClient();
  const [initData2, setInitData2] = useState<any>({
    __schema: { queryType: [] }
  });
  const [isExpanded, setIsExpanded] = useState(false);
  const [isFilterDropdownOpen, setIsFilterDropdownOpen] = useState(false);
  const [selected, setSelected] = useState('');
  const [selectTypes, setSelectTypes] = useState('');
  const [textValue, setTextValue] = useState('');
  const [isOpen, setIsOpen] = useState(false);
  const [currentArgument, setCurrentArgument] = useState('');
  const [currentArgumentScalar, setCurrentArgumentScalar] = useState('');
  const [typeParent, setTypeParent] = useState('');
  const [currentBoolean, setCurrentBoolean] = useState('Boolean');
  const [finalFilters, setFinalFilters] = useState({});
  const [runFilter, setRunFilter] = useState(false);
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
    'ProcessInstanceState',
    'ProcessInstanceStateArgument'
  ];
  const temp = [];

  const domainArg =
    !getQuery.loading &&
    getQuery.data.__type.fields.find(item => {
      if (item.name === currentDomain) {
        return item;
      }
    });

  const getQueryTypes = useGetQueryTypesQuery();

  useEffect(() => {
    setInitData2(getQueryTypes.data);
  }, [getQueryTypes.data]);

  const getSchema = useGetInputFieldsFromQueryQuery({
    variables: {
      currentQuery: domainArg.args[0].type.name
    }
  });
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
    // const _temp = event.target.innerText;
    // setTypeChips([_temp]);
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
  // let parentItems: any;
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
          let ele;
          ele = (
            <SelectGroup
              label={group.name}
              key={index}
              id={group.name}
              value={group.name}
            >
              {group.type.inputFields &&
                group.type.inputFields
                  .filter((item, _index) => {
                    if (!nullTypes.includes(item.type.name)) {
                      const tempData = [];
                      const _v = fetchSchema(item);
                      tempData.push(_v);
                      childSelectionItems(tempData, group.name, item.name);
                    } else {
                      return item;
                    }
                  })
                  .map((item, _index) => {
                    return (
                      <SelectOption key={_index} value={item.name + group.name}>
                        {item.name}
                      </SelectOption>
                    );
                  })}
            </SelectGroup>
          );
          !finalResult.includes(ele) && finalResult.push(ele);
        });
  };

  function getAllChilds(arr, comp) {
    const unique = arr
      .map(e => e[comp])
      .map((e, i, final) => final.indexOf(e) === i && i)
      .filter(e => arr[e])
      .map(e => arr[e]);

    return unique;
  }

  const onChange = (event, selection) => {
    const innerText = event.nativeEvent.target.outerText;
    setSelected(innerText);
    const parent = event.nativeEvent.target.parentElement.parentElement.getAttribute(
      'value'
    );
    const tempParents = parent.split(' / ');
    setTypeParent(tempParents);
    const lastEle = tempParents.slice(-1)[0];
    const arg = lastEle.charAt(0).toUpperCase() + lastEle.slice(1) + 'Argument';

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

    if (y.type.kind === 'INPUT_OBJECT') {
      setCurrentArgument(y.type.name);
    } else {
      setCurrentArgumentScalar(y.type.name);
    }
    setIsExpanded(false);
  };

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
            } else {
              setDisplayEmptyState(true);
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
      const arg = 'TravelsArgument';
      setQueryAttributes({
        operation: currentDomain,
        variables: { where: { value: finalFilters, type: arg } },
        fields: parameters
      });
      const Query = query({
        operation: currentDomain,
        variables: { where: { value: finalFilters, type: arg } },
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

  const onApplyFilter = async () => {
    setRunFilter(true);
    setEnableFilter(true);
    const n = `${typeParent},${selected},${selectTypes}`;
    if (currentArgumentScalar === 'Boolean') {
      set(obj, n, currentBoolean === 'true');
      setFinalFilters(() => {
        if (finalFilters.hasOwnProperty(typeParent)) {
          const te: any = Object.values(obj)[0];
          finalFilters[typeParent] = { ...finalFilters[typeParent], ...te };
          return finalFilters;
        } else {
          return { ...finalFilters, ...obj };
        }
      });
    } else {
      set(obj, n, textValue);
      setFinalFilters(() => {
        if (finalFilters.hasOwnProperty(typeParent)) {
          const te: any = Object.values(obj)[0];
          finalFilters[typeParent] = { ...finalFilters[typeParent], ...te };
          return finalFilters;
        } else {
          return { ...finalFilters, ...obj };
        }
      });
    }
  };

  const onClearFilters = () => {
    setEnableFilter(false);
    setRunFilter(true);
    setFinalFilters({});
  };

  return (
    <DataToolbar
      id="data-toolbar-with-chip-groups"
      className="pf-m-toggle-group-container"
      collapseListedFiltersBreakpoint="md"
    >
      <DataToolbarContent>
        <DataToolbarItem>
          {!getSchema.loading && (
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
              {getAllChilds(finalResult, 'props')}
            </Select>
          )}
          {!getTypes.loading && selected.length > 0 && (
            <Select
              aria-label="Location"
              onToggle={onToggle}
              onSelect={onSelect}
              selections={selectTypes}
              isExpanded={isFilterDropdownOpen}
              placeholderText="Types"
              className="types-selections"
            >
              {typesMenuItems}
            </Select>
          )}
          {selectTypes.length > 0 && currentArgumentScalar === 'String' && (
            <>
              <TextInput
                name="filterText"
                type="search"
                aria-label={`filter text for ${selected}`}
                onChange={textBoxChange}
                className="types-selections"
                placeholder={`filter by ${selected}`}
                value={textValue}
              />
              <Button
                variant="primary"
                onClick={onApplyFilter}
                isDisabled={textValue.length === 0}
              >
                Apply Filter
              </Button>
              <Button variant="link" onClick={onClearFilters}>
                Clear filter
              </Button>
            </>
          )}
          {selectTypes.length > 0 && currentArgumentScalar === 'Boolean' && (
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
              <Button variant="link">Clear filter</Button>
            </>
          )}
        </DataToolbarItem>
      </DataToolbarContent>
    </DataToolbar>
  );
};

export default React.memo(DomainExplorerFilterOptions);
