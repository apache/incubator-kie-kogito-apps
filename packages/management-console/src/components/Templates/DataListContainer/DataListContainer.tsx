import {
  Breadcrumb,
  BreadcrumbItem,
  Card,
  DataList,
  DataListItem,
  DataListCell,
  Grid,
  GridItem,
  PageSection
} from '@patternfly/react-core';
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import PageTitleComponent from '../../Molecules/PageTitleComponent/PageTitleComponent';
import DataToolbarComponent from '../../Molecules/DataToolbarComponent/DataToolbarComponent';
import './DataList.css';
import DataListComponent from '../../Organisms/DataListComponent/DataListComponent';
import EmptyStateComponent from '../../Atoms/EmptyStateComponent/EmptyStateComponent';
import LoadMoreComponent from '../../Atoms/LoadMoreComponent/LoadMoreComponent';
import ProcessBulkModalComponent from '../../Atoms/ProcessBulkModalComponent/ProcessBulkModalComponent';
import {
  useGetProcessInstancesLazyQuery,
  ProcessInstanceState,
  useGetProcessInstancesWithBusinessKeyLazyQuery
} from '../../../graphql/types';
import axios from 'axios';
import { InfoCircleIcon } from '@patternfly/react-icons';
import ServerErrorsComponent from '../../Molecules/ServerErrorsComponent/ServerErrorsComponent';

const DataListContainer: React.FC<{}> = () => {
  const pSize = 10;
  const [initData, setInitData] = useState<any>({});
  const [checkedArray, setCheckedArray] = useState<any>(['ACTIVE']);
  const [isLoading, setIsLoading] = useState(false);
  const [isError, setIsError] = useState(false);
  const [isStatusSelected, setIsStatusSelected] = useState(true);
  const [abortedObj, setAbortedObj] = useState({});
  const [isAbortModalOpen, setIsAbortModalOpen] = useState(false);
  const [abortedMessageObj, setAbortedMessageObj] = useState({});
  const [completedMessageObj, setCompletedMessageObj] = useState({});
  const [titleType, setTitleType] = useState('');
  const [modalTitle, setModalTitle] = useState('');
  const [limit, setLimit] = useState(pSize);
  const [offset, setOffset] = useState(10);
  const [pageSize, setPageSize] = useState(pSize);
  const [isLoadingMore, setIsLoadingMore] = useState(false);
  const [isDefiningFilter, setIsDefiningFilter] = useState(true);
  const [filters, setFilters] = useState({
    status: ['ACTIVE'],
    businessKey: []
  });
  const [searchWord, setSearchWord] = useState<string>('');
  const [isFilterClicked, setIsFilterClicked] = useState<boolean>(false);
  const [selectedNumber, setSelectedNumber] = useState<number>(0);
  const [isAllChecked, setIsAllChecked] = useState(false);
  const [
    getProcessInstances,
    { loading, data, error }
  ] = useGetProcessInstancesLazyQuery({
    fetchPolicy: 'network-only',
    notifyOnNetworkStatusChange: true
  });

  const [
    getProcessInstancesWithBusinessKey,
    getProcessInstancesWithBK
  ] = useGetProcessInstancesWithBusinessKeyLazyQuery({
    fetchPolicy: 'network-only',
    notifyOnNetworkStatusChange: true
  });

  const handleAbortModalToggle = () => {
    setIsAbortModalOpen(!isAbortModalOpen);
  };

  const onFilterClick = async (arr = checkedArray) => {
    const searchWordsArray = [];
    const copyOfBusinessKeysArray = [...filters.businessKey];
    if (searchWord.length !== 0) {
      if (!copyOfBusinessKeysArray.includes(searchWord)) {
        copyOfBusinessKeysArray.push(searchWord);
        setFilters({
          ...filters,
          status: checkedArray,
          businessKey: [...filters.businessKey, searchWord]
        });
      }
    }
    copyOfBusinessKeysArray.map(word => {
      const tempBusinessKeys = { businessKey: { like: word } };
      searchWordsArray.push(tempBusinessKeys);
    });
    setIsFilterClicked(true);
    setIsLoading(true);
    setIsLoadingMore(false);
    setIsError(false);
    setAbortedObj({});
    setAbortedMessageObj({});
    setCompletedMessageObj({});
    setIsStatusSelected(true);
    setIsAllChecked(false);
    setSelectedNumber(0);
    setLimit(pSize);
    setPageSize(pSize);
    setOffset(0);
    setInitData({});
    if (searchWordsArray.length === 0) {
      getProcessInstances({
        variables: { state: arr, offset: 0, limit: pSize }
      });
    } else {
      getProcessInstancesWithBusinessKey({
        variables: {
          state: arr,
          offset: 0,
          limit: pSize,
          businessKeys: searchWordsArray
        }
      });
    }
  };

  const onGetMoreInstances = (initVal, _pageSize) => {
    setIsLoadingMore(true);
    setPageSize(_pageSize);
    getProcessInstances({
      variables: { state: checkedArray, offset: initVal, limit: _pageSize }
    });
  };

  useEffect(() => {
    setAbortedObj({});
    setAbortedMessageObj({});
    setCompletedMessageObj({});
    setIsDefiningFilter(false);
    if (isLoadingMore === undefined || !isLoadingMore) {
      setIsLoading(loading);
    }
    setSearchWord('');
    if (!loading && data !== undefined) {
      data.ProcessInstances.map((instance: any) => {
        instance.isChecked = false;
        instance.isOpen = false;
      });
      setLimit(data.ProcessInstances.length);
      if (offset > 0 && initData.ProcessInstances.length > 0) {
        setIsLoadingMore(false);
        initData.ProcessInstances = initData.ProcessInstances.concat(
          data.ProcessInstances
        );
      } else {
        setInitData(data);
      }
    }
  }, [data]);

  useEffect(() => {
    setAbortedObj({});
    setAbortedMessageObj({});
    setCompletedMessageObj({});
    setIsDefiningFilter(false);
    if (isLoadingMore === undefined || !isLoadingMore) {
      setIsLoading(getProcessInstancesWithBK.loading);
    }
    setSearchWord('');
    if (
      !getProcessInstancesWithBK.loading &&
      getProcessInstancesWithBK.data !== undefined
    ) {
      getProcessInstancesWithBK.data.ProcessInstances.map((instance: any) => {
        instance.isChecked = false;
        instance.isOpen = false;
      });
      setLimit(getProcessInstancesWithBK.data.ProcessInstances.length);
      if (offset > 0 && initData.ProcessInstances.length > 0) {
        setIsLoadingMore(false);
        initData.ProcessInstances = initData.ProcessInstances.concat(
          getProcessInstancesWithBK.data.ProcessInstances
        );
      } else {
        setInitData(getProcessInstancesWithBK.data);
      }
    }
  }, [getProcessInstancesWithBK.data]);

  useEffect(() => {
    setOffset(0);
    setLimit(pSize);
    setIsDefiningFilter(true);
  }, [checkedArray]);

  const setTitle = (titleStatus, titleText) => {
    switch (titleStatus) {
      case 'success':
        return (
          <>
            <InfoCircleIcon
              className="pf-u-mr-sm"
              color="var(--pf-global--info-color--100)"
            />{' '}
            {titleText}{' '}
          </>
        );
      case 'failure':
        return (
          <>
            <InfoCircleIcon
              className="pf-u-mr-sm"
              color="var(--pf-global--danger-color--100)"
            />{' '}
            {titleText}{' '}
          </>
        );
    }
  };

  const handleAbortAll = () => {
    const tempAbortedObj = { ...abortedObj };
    const completedAndAborted = {};
    for (const [id, processInstance] of Object.entries(tempAbortedObj)) {
      initData.ProcessInstances.map(instance => {
        if (instance.id === id) {
          if (
            instance.addons.includes('process-management') &&
            instance.serviceUrl !== null
          ) {
            if (
              instance.state === ProcessInstanceState.Completed ||
              instance.state === ProcessInstanceState.Aborted
            ) {
              completedAndAborted[id] = processInstance;
              delete tempAbortedObj[id];
            } else {
              instance.state = ProcessInstanceState.Aborted;
            }
          }
        }
        if (instance.childDataList !== undefined) {
          instance.childDataList.map(child => {
            if (child.id === id) {
              if (
                instance.addons.includes('process-management') &&
                instance.serviceUrl !== null
              ) {
                if (
                  child.state === ProcessInstanceState.Completed ||
                  child.state === ProcessInstanceState.Aborted
                ) {
                  completedAndAborted[id] = processInstance;
                  delete tempAbortedObj[id];
                } else {
                  child.state = ProcessInstanceState.Aborted;
                }
              }
            }
          });
        }
      });
    }
    const promiseArray = [];
    Object.keys(tempAbortedObj).forEach((id: string) => {
      promiseArray.push(
        axios.delete(
          `${tempAbortedObj[id].serviceUrl}/management/processes/${tempAbortedObj[id].processId}/instances/${tempAbortedObj[id].id}`
        )
      );
    });
    setModalTitle('Abort operation');
    Promise.all(promiseArray)
      .then(() => {
        setTitleType('success');
        setAbortedMessageObj(tempAbortedObj);
        setCompletedMessageObj(completedAndAborted);
        handleAbortModalToggle();
      })
      .catch(() => {
        setTitleType('failure');
        setAbortedMessageObj(tempAbortedObj);
        setCompletedMessageObj(completedAndAborted);
        handleAbortModalToggle();
      });
  };

  if (error || getProcessInstancesWithBK.error) {
    return (
      <ServerErrorsComponent
        message={error ? error : getProcessInstancesWithBK.error}
      />
    );
  }
  return (
    <React.Fragment>
      <ProcessBulkModalComponent
        modalTitle={
          titleType === 'success'
            ? setTitle(titleType, modalTitle)
            : setTitle(titleType, modalTitle)
        }
        isModalOpen={isAbortModalOpen}
        abortedMessageObj={abortedMessageObj}
        completedMessageObj={completedMessageObj}
        isAbortModalOpen={isAbortModalOpen}
        checkedArray={checkedArray}
        handleModalToggle={handleAbortModalToggle}
        isSingleAbort={false}
      />
      <PageSection variant="light">
        <PageTitleComponent title="Process Instances" />
        <Breadcrumb>
          <BreadcrumbItem>
            <Link to={'/'}>Home</Link>
          </BreadcrumbItem>
          <BreadcrumbItem isActive>Process instances</BreadcrumbItem>
        </Breadcrumb>
      </PageSection>
      <PageSection>
        <Grid gutter="md">
          <GridItem span={12}>
            <Card className="dataList">
              {!isError && (
                <>
                  {' '}
                  <DataToolbarComponent
                    checkedArray={checkedArray}
                    filterClick={onFilterClick}
                    setCheckedArray={setCheckedArray}
                    setIsStatusSelected={setIsStatusSelected}
                    filters={filters}
                    setFilters={setFilters}
                    initData={initData}
                    setInitData={setInitData}
                    abortedObj={abortedObj}
                    setAbortedObj={setAbortedObj}
                    handleAbortAll={handleAbortAll}
                    setOffset={setOffset}
                    getProcessInstances={getProcessInstances}
                    setLimit={setLimit}
                    pageSize={pSize}
                    setSearchWord={setSearchWord}
                    searchWord={searchWord}
                    isAllChecked={isAllChecked}
                    setIsAllChecked={setIsAllChecked}
                    selectedNumber={selectedNumber}
                    setSelectedNumber={setSelectedNumber}
                  />
                </>
              )}
              {isStatusSelected ? (
                <DataListComponent
                  initData={initData}
                  setInitData={setInitData}
                  isLoading={isLoading}
                  setIsLoading={setIsDefiningFilter}
                  setIsError={setIsError}
                  checkedArray={checkedArray}
                  pageSize={pSize}
                  isLoadingMore={isLoadingMore}
                  abortedObj={abortedObj}
                  setAbortedObj={setAbortedObj}
                  isFilterClicked={isFilterClicked}
                  filters={filters}
                  setIsAllChecked={setIsAllChecked}
                  setSelectedNumber={setSelectedNumber}
                  selectedNumber={selectedNumber}
                />
              ) : (
                <EmptyStateComponent
                  iconType="warningTriangleIcon1"
                  title="No status is selected"
                  body="Try selecting at least one status to see results"
                  filterClick={onFilterClick}
                  setFilters={setFilters}
                  setCheckedArray={setCheckedArray}
                  setSearchWord={setSearchWord}
                  filters={filters}
                />
              )}
              {!loading &&
                !isLoading &&
                !isDefiningFilter &&
                initData !== undefined &&
                limit === pageSize && (
                  <DataList aria-label="Simple data list example">
                    <DataListItem aria-labelledby="kie-datalist-item">
                      <DataListCell className="kogito-management-console-load-more">
                        <LoadMoreComponent
                          offset={offset}
                          setOffset={setOffset}
                          getProcessInstances={onGetMoreInstances}
                          pageSize={pageSize}
                        />
                      </DataListCell>
                    </DataListItem>
                  </DataList>
                )}
            </Card>
          </GridItem>
        </Grid>
      </PageSection>
    </React.Fragment>
  );
};

export default DataListContainer;
