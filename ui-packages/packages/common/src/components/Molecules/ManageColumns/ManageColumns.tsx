import React, { useState, useEffect } from 'react';
import {
  Button,
  DataList,
  DataListCheck,
  DataListItem,
  DataListItemRow,
  DataListCell,
  DataListItemCells,
  DataListToggle,
  DataListContent,
  Dropdown,
  DropdownItem,
  DropdownPosition,
  DropdownToggle,
  DropdownToggleCheckbox,
  Modal,
  Text,
  TextContent,
  TextVariants
} from '@patternfly/react-core';
import { SyncIcon } from '@patternfly/react-icons';
import './ManageColumns.css';
import _ from 'lodash';
import gql from 'graphql-tag';
import { query } from 'gql-query-builder';
import { useApolloClient } from 'react-apollo';

export interface IOwnProps {
  columnPickerType: any;
  setColumnFilters: any;
  setTableLoading: any;
  getQueryTypes: any;
  setDisplayTable: any;
  parameters: any;
  setParameters: any;
  selected: any;
  setSelected: any;
  data: any;
  getPicker: any;
  setError: any;
  setDisplayEmptyState: any;
  rememberedParams: any;
  enableCache: boolean;
  setEnableCache: any;
  offsetVal: number;
  pageSize: number;
  setOffsetVal: (offsetVal: number) => void;
  setPageSize: (pageSize: number) => void;
  setIsLoadingMore: (isLoadingMoreVal: boolean) => void;
  isLoadingMore: boolean;
  metaData: any;
}

const ManageColumns: React.FC<IOwnProps> = ({
  columnPickerType,
  setColumnFilters,
  setTableLoading,
  getQueryTypes,
  setDisplayTable,
  parameters,
  setParameters,
  selected,
  setSelected,
  data,
  getPicker,
  setError,
  setDisplayEmptyState,
  rememberedParams,
  enableCache,
  setEnableCache,
  pageSize,
  offsetVal,
  setOffsetVal,
  setPageSize,
  setIsLoadingMore,
  isLoadingMore,
  metaData
}) => {
  // tslint:disable: forin
  // tslint:disable: no-floating-promises
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [expanded, setExpanded] = useState([]);
  const [enableRefresh, setEnableRefresh] = useState(true);
  const [isDropDownOpen, setIsDropDownOpen] = useState(false);
  const allSelections = [];

  const nullTypes = [null, 'String', 'Boolean', 'Int', 'DateTime'];
  const client = useApolloClient();

  const handleModalToggle = () => {
    setIsModalOpen(!isModalOpen);
  };

  const handleChange = (checked, event) => {
    const target = event.target;
    const selection = target.name;
    const selectionArray = target.name
      .split('/')
      .map(item => item.charAt(0).toLowerCase() + item.slice(1));
    setEnableRefresh(false);
    if (selected.includes(selection)) {
      setSelected(prevState => prevState.filter(item => item !== selection));
      const objValue = selectionArray.shift();
      const rest = filterColumnSelection(selectionArray, objValue);
      setParameters(prevState =>
        prevState.filter(obj => {
          if (!_.isEqual(obj, rest)) {
            return obj;
          }
        })
      );
    } else {
      setSelected(prevState => [...prevState, selection]);
      const objValue = selectionArray.shift();
      const rest = filterColumnSelection(selectionArray, objValue);
      setParameters(prevState => [...prevState, rest]);
    }
  };
  
  const filterColumnSelection = (selectionArray, objValue) => {
    let res = {};
    if (selectionArray.length === 0) {
      res = objValue;
      return res;
    }
    for (let i = selectionArray.length - 1; i >= 0; i--) {
      if (i === selectionArray.length - 1) {
        if (selectionArray[i] === '-') {
          res = objValue;
        } else {
          res = { [selectionArray[i]]: [objValue] }; // assign the value
        }
      } else {
        res = { [selectionArray[i]]: [res] }; // put the prev object
      }
    }
    return res;
  };
  const tempExpanded = [];
  const toggle = id => {
    const index = expanded.indexOf(id);
    const newExpanded =
      index >= 0
        ? [
          ...expanded.slice(0, index),
          ...expanded.slice(index + 1, expanded.length)
        ]
        : [...expanded, id];
    tempExpanded.push(newExpanded);
    setExpanded(newExpanded);
  };

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
  const rootElements = [];
  let finalResult: any = [];
  let childItems;

  const childSelectionItems = (_data, title, ...attr) => {
    let nestedTitles = '';
    childItems =
      !getQueryTypes.loading &&
      _data.map((group, index) => {
        const label = title + ' / ' + attr.join();
        const childEle = (
          <DataListItem
            aria-labelledby={'kie-datalist-item-' + label.replace(/\,/g, '')}
            isExpanded={expanded.includes(label.replace(/\,/g, ''))}
          >
            <DataListItemRow>
              <DataListToggle
                onClick={() => toggle(label.replace(/\,/g, ''))}
                isExpanded={expanded.includes(label.replace(/\,/g, ''))}
                id={'kie-datalist-toggle-' + label.replace(/\,/g, '')}
                aria-controls="kie-datalist-expand"
              />
              <DataListItemCells
                dataListCells={[
                  <DataListCell
                    id={'kie-datalist-item-' + label.replace(/\,/g, '')}
                    key={index}
                  >
                    {label.replace(/\,/g, '')}
                  </DataListCell>
                ]}
              />
            </DataListItemRow>
            {group.fields
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
              .map((item, _index) => {
                allSelections.push(item.name + '/' + title + '/' + group.name);
                return (
                  <DataListContent
                    aria-label="Primary Content Details"
                    id="ex-expand1"
                    isHidden={!expanded.includes(label.replace(/\,/g, ''))}
                    className="kogito-common--manage-columns__data-list-content"
                    key={Math.random()}
                  >
                    <DataListItemRow>
                      <DataListCheck
                        aria-labelledby="table-column-management-item2"
                        name={item.name + '/' + title + '/' + group.name}
                        checked={
                          selected.includes(
                            item.name + '/' + title + '/' + group.name
                          )
                        }
                        onChange={handleChange}
                      />
                      <DataListItemCells
                        dataListCells={[
                          <DataListCell
                            id={'kie-datalist-item-' + item.name}
                            key={_index}
                          >
                            {item.name}
                          </DataListCell>
                        ]}
                      />
                    </DataListItemRow>
                  </DataListContent>
                );
              })}
          </DataListItem>
        );
        return childEle;
      });
    finalResult.push(childItems);
  };

  const selectionItems = _data => {
    !getPicker.loading &&
      _data
        .filter((group, index) => {
          if (group.type.kind !== 'SCALAR') {
            return group;
          } else {
            allSelections.push(group.name);
            const rootEle = (
              <DataListItem aria-labelledby={'kie-datalist-item-' + group.name}>
                <DataListItemRow>
                  <DataListCheck
                    aria-labelledby={'kie-datalist-item-' + group.name}
                    name={group.name}
                    checked={
                      selected.includes(group.name)
                    }
                    onChange={handleChange}
                  />
                  <DataListItemCells
                    dataListCells={[
                      <DataListCell
                        id={'kie-datalist-item-' + group.name}
                        key={index}
                      >
                        {group.name}
                      </DataListCell>
                    ]}
                  />
                </DataListItemRow>
              </DataListItem>
            );
            rootElements.push(rootEle);
          }
        })
        .map((group, index) => {
          const nestedEle = (
            <DataListItem
              aria-labelledby={'kie-datalist-item-' + group.name}
              isExpanded={expanded.includes(group.name)}
            >
              <DataListItemRow>
                <DataListToggle
                  onClick={() => toggle(group.name)}
                  isExpanded={expanded.includes(group.name)}
                  id={'kie-datalist-toggle-' + group.name}
                  aria-controls="kie-datalist-expand"
                />
                <DataListItemCells
                  dataListCells={[
                    <DataListCell
                      id={'kie-datalist-item-' + group.name}
                      key={index}
                    >
                      {group.name}
                    </DataListCell>
                  ]}
                />
              </DataListItemRow>
              {group.type.fields &&
                group.type.fields
                  .filter((item, _index) => {
                    if (!nullTypes.includes(item.type.name)) {
                      const tempData = [];
                      const _v = fetchSchema(item);
                      tempData.push(_v);
                      childSelectionItems(tempData, group.name, item.name);
                    } else {
                      if (item.type.kind !== 'LIST') {
                        return item;
                      }
                    }
                  })
                  .map((item, _index) => {
                    allSelections.push(item.name + '/' + group.name);
                    return (
                      <DataListContent
                        aria-label="Primary Content Details"
                        id="ex-expand1"
                        isHidden={!expanded.includes(group.name)}
                        key={_index}
                        className="kogito-common--manage-columns__data-list-content"
                      >
                        <DataListItemRow>
                          <DataListCheck
                            aria-labelledby={
                              'kie-datalist-item-' +
                              item.name +
                              '/' +
                              group.name
                            }
                            name={item.name + '/' + group.name}
                            checked={
                              selected.includes(
                                item.name + '/' + group.name
                              )
                            }
                            onChange={handleChange}
                          />
                          <DataListItemCells
                            dataListCells={[
                              <DataListCell
                                id={'kie-datalist-item-' + item.name}
                                key={_index}
                              >
                                {item.name}
                              </DataListCell>
                            ]}
                          />
                        </DataListItemRow>
                      </DataListContent>
                    );
                  })}
            </DataListItem>
          );
          !finalResult.includes(nestedEle) && finalResult.push(nestedEle);
        });
  };
  columnPickerType && selectionItems(data);

  finalResult = finalResult.flat();
  finalResult.unshift(rootElements);

  function getAllChilds(arr, comp) {
    const unique = arr
      .map(e => e[comp])
      .map((e, i, final) => final.indexOf(e) === i && i)
      .filter(e => arr[e])
      .map(e => arr[e]);

    return unique;
  }

  useEffect(() => {
    if (isLoadingMore) {
      generateQuery(parameters);
    }
  }, [isLoadingMore]);

  useEffect(() => {
    /* istanbul ignore else */
    if (
      (rememberedParams.length === 0 && parameters.length !== 1) ||
      rememberedParams.length > 0
    ) {
      generateQuery(parameters);
    }
  }, [parameters.length > 1]);

  const nestedCheck = (ele, valueObj) => {
    for (const key in ele) {
      const temp = ele[key];
      if (typeof temp[0] === 'object') {
        for (const nestedProp in temp[0]) {
          const nestedObj = {};
          const result = nestedCheck(temp[0], valueObj);
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
      const temp = ele[key];
      if (typeof temp[0] === 'object') {
        for (const nestedProp in temp[0]) {
          const nestedObj = {};
          if (valueObj.hasOwnProperty(nestedProp)) {
            const result = nestedCheck(temp[0], valueObj);
            valueObj[nestedProp] = result;
          } else {
            const result = checkFunc(temp[0], valueObj);
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

  const validateResponse = (obj, paramFields) => {
    let contentObj = {};
    for (const prop in obj) {
      const arr = [];
      if (obj[prop] === null) {
        const parentObj = {};
        paramFields.map(params => {
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
        elseObj[prop] = obj[prop];
        contentObj = { ...contentObj, ...elseObj };
      }
    }
    return contentObj;
  };

  async function generateQuery(paramFields) {
    setTableLoading(true);
    setEnableRefresh(true);
    if (columnPickerType && paramFields.length > 1) {
      const Query = query({
        operation: columnPickerType,
        fields: paramFields,
        variables: {
          pagination: {
            value: { offset: offsetVal, limit: pageSize },
            type: 'Pagination'
          }
        }
      });
      try {
        const response = await client.query({
          query: gql`
            ${Query.query}
          `,
          variables: Query.variables,
          fetchPolicy: enableCache ? 'cache-first' : 'network-only'
        });
        const firstKey = Object.keys(response.data)[0];
        if (response.data[firstKey].length > 0) {
          const resp = response.data;
          const respKeys = Object.keys(resp)[0];
          const tableContent = resp[respKeys];
          const finalResp = [];
          tableContent.map(content => {
            const finalObject = validateResponse(content, paramFields);
            finalResp.push(finalObject);
          });
          setColumnFilters(finalResp);
          setTableLoading(false);
          setDisplayTable(true);
          setEnableCache(false);
          setIsLoadingMore(false);
        } else {
          setTableLoading(false);
          setDisplayEmptyState(true);
          setEnableCache(false);
        }
      } catch (error) {
        setError(error);
      }
    } else {
      setTableLoading(false);
      setDisplayEmptyState(false);
      setDisplayTable(false);
    }
  }

  const renderModal = () => {
    const numSelected = selected.length;
    const allSelected = numSelected === allSelections.length;
    const anySelected = numSelected > 0;
    const someChecked = anySelected ? null : false;
    const isChecked = allSelected ? true : someChecked;

    const onDropDownToggle = isOpen => {
      setIsDropDownOpen(isOpen)
    };

    const onDropDownSelect = event => {
      setIsDropDownOpen(!isDropDownOpen)
    };
    
    const handleSelectClick = newState => {
      if (newState === 'none') {
        setSelected([]);
        setParameters([metaData]);
      } else {
        setSelected(allSelections)
        const selectionArray = allSelections.map(ele =>
          ele.split('/').map(item => item.charAt(0).toLowerCase() + item.slice(1))
        );
        const finalObj = [];
        selectionArray.map(arr => {
          const objValue = arr.shift();
          const rest = filterColumnSelection(arr, objValue);
          finalObj.push(rest);
        });
        finalObj.push(metaData);
        setParameters(finalObj);
      }
    };

    const items = [
      <DropdownItem key="none" onClick={() => handleSelectClick('none')}>
        Select none
      </DropdownItem>,
      <DropdownItem key="selectAll" onClick={() => handleSelectClick('all')}>
        Select all
      </DropdownItem>
    ];
    const bulkSelection = (<Dropdown
      onSelect={onDropDownSelect}
      position={DropdownPosition.left}
      toggle={
        <DropdownToggle
          splitButtonItems={[
            <DropdownToggleCheckbox
              id="selectAll-dropdown"
              key="split-checkbox"
              aria-label={anySelected ? 'Deselect all' : 'Select all'}
              isChecked={isChecked}
              onClick={() => {
                anySelected ? handleSelectClick('none') : handleSelectClick('all');
              }}
            />
          ]}
          onToggle={onDropDownToggle}
        >
          {numSelected !== 0 && <React.Fragment>{numSelected} selected</React.Fragment>}
        </DropdownToggle>
      }
      isOpen={isDropDownOpen}
      dropdownItems={items}
    />)
    return (
      <Modal
        title="Manage columns"
        isOpen={isModalOpen}
        isSmall
        description={
          <TextContent>
            <Text component={TextVariants.p}>
              Selected categories will be displayed in the table.
            </Text>  
          </TextContent>
        }
        onClose={handleModalToggle}
        className="kogito-common--manage-columns__modal"
        actions={[
          <Button
            key="save"
            variant="primary"
            onClick={() => {
              onResetQuery(parameters);
            }}
          >
            Save
          </Button>,
          <Button key="cancel" variant="secondary" onClick={handleModalToggle}>
            Cancel
          </Button>
        ]}
      >
        {bulkSelection}
        <DataList
          aria-label="Table column management"
          id="table-column-management"
          className="kogito-common--manage-columns__data-list"
          isCompact
        >
          {getAllChilds(finalResult, 'props')}
        </DataList>
      </Modal>
    );
  };

  const onResetQuery = _parameters => {
    setOffsetVal(0);
    offsetVal = 0;
    setPageSize(10);
    pageSize = 10;
    generateQuery(_parameters);
    setIsLoadingMore(false);
    setIsModalOpen(!isModalOpen);
  };

  const onRefresh = _parameters => {
    if (enableRefresh && parameters.length > 1) {
      setOffsetVal(0);
      offsetVal = 0;
      setPageSize(10);
      pageSize = 10;
      generateQuery(_parameters);
      setIsLoadingMore(false);
    }
  };

  return (
    <>
      <Button variant="link" onClick={handleModalToggle}>
        Manage columns
      </Button>
      {renderModal()}
      <Button
        variant="plain"
        onClick={() => {
          onRefresh(parameters);
        }}
        className="pf-u-m-md"
        id="refresh-button"
        aria-label={'Refresh list'}
      >
        <SyncIcon />
      </Button>
    </>
  );
};

export default ManageColumns;
