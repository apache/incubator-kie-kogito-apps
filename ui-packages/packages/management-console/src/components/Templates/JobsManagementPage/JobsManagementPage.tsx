import React, { useState, useEffect } from 'react';
import { Link, Redirect } from 'react-router-dom';
import {
  componentOuiaProps,
  GraphQL,
  KogitoEmptyState,
  KogitoEmptyStateType,
  KogitoSpinner,
  LoadMore,
  ouiaPageTypeAndObjectId,
  OUIAProps,
  ServerErrors
} from '@kogito-apps/common';
import PageTitle from '../../Molecules/PageTitle/PageTitle';
import {
  Breadcrumb,
  BreadcrumbItem,
  Bullseye,
  Button,
  Card,
  CardBody,
  PageSection,
  Toolbar,
  ToolbarContent,
  ToolbarGroup,
  ToolbarItem,
  DropdownItem,
  OverflowMenu,
  OverflowMenuContent,
  OverflowMenuItem,
  OverflowMenuControl,
  Dropdown,
  KebabToggle
} from '@patternfly/react-core';
import { ISortBy } from '@patternfly/react-table';
import JobsManagementTable from '../../Organisms/JobsManagementTable/JobsManagementTable';
import JobsManagementFiters from '../../Organisms/JobsManagementFilters/JobsManagementFilters';
import JobsPanelDetailsModal from '../../Atoms/JobsPanelDetailsModal/JobsPanelDetailsModal';
import JobsRescheduleModal from '../../Atoms/JobsRescheduleModal/JobsRescheduleModal';
import { refetchContext } from '../../contexts';
import {
  setTitle,
  performMultipleCancel,
  formatForBulkListJob
} from '../../../utils/Utils';
import JobsCancelModal from '../../Atoms/JobsCancelModal/JobsCancelModal';
import { SyncIcon } from '@patternfly/react-icons';
import {
  BulkListType,
  IOperationResults,
  IOperations,
  OperationType
} from '../../Atoms/BulkList/BulkList';

const JobsManagementPage: React.FC<OUIAProps> = ({ ouiaId, ouiaSafe }) => {
  const defaultPageSize: number = 10;
  const defaultOrderBy: GraphQL.JobOrderBy = {
    lastUpdate: GraphQL.OrderBy.Asc
  };
  const defaultSortBy: ISortBy = { index: 6, direction: 'asc' };
  const defaultStatus: GraphQL.JobStatus[] = [GraphQL.JobStatus.Scheduled];
  const [initData, setInitData] = useState<GraphQL.GetJobsWithFiltersQuery>({});
  const [isDetailsModalOpen, setIsDetailsModalOpen] = useState<boolean>(false);
  const [isRescheduleModalOpen, setIsRescheduleModalOpen] = useState<boolean>(
    false
  );
  const [isCancelModalOpen, setIsCancelModalOpen] = useState<boolean>(false);
  const [modalTitle, setModalTitle] = useState<JSX.Element>(null);
  const [modalContent, setModalContent] = useState<string>('');
  const [selectedJob, setSelectedJob] = useState<any>({});
  const [selectedStatus, setSelectedStatus] = useState<GraphQL.JobStatus[]>(
    defaultStatus
  );
  const [chips, setChips] = useState<GraphQL.JobStatus[]>(defaultStatus);
  const [values, setValues] = useState<GraphQL.JobStatus[]>(defaultStatus);
  const [orderBy, setOrderBy] = useState<GraphQL.JobOrderBy>(defaultOrderBy);
  const [sortBy, setSortBy] = useState<ISortBy>(defaultSortBy);
  const [limit, setLimit] = useState<number>(defaultPageSize);
  const [offset, setOffset] = useState<number>(0);
  const [pageSize, setPageSize] = useState<number>(defaultPageSize);
  const [isLoadingMore, setIsLoadingMore] = useState<boolean>(false);
  const [isKebabOpen, setIsKebabOpen] = useState<boolean>(false);
  const [displayTable, setDisplayTable] = useState<boolean>(false);
  const [isActionPerformed, setIsActionPerformed] = useState(false);
  const [selectedJobInstances, setSelectedJobInstances] = useState<
    GraphQL.Job[]
  >([]);
  const [jobOperationResults, setJobOperationResults] = useState<
    IOperationResults
  >({
    CANCEL: {
      successItems: [],
      failedItems: [],
      ignoredItems: []
    }
  });

  const { loading, data, error, refetch } = GraphQL.useGetJobsWithFiltersQuery({
    fetchPolicy: 'network-only',
    notifyOnNetworkStatusChange: true,
    variables: { values, orderBy, offset, limit: pageSize }
  });

  const onGetMoreInstances = (initVal: number, _pageSize: number): void => {
    setIsLoadingMore(true);
    setOffset(initVal);
    setPageSize(_pageSize);
  };

  const jobOperations: IOperations = {
    CANCEL: {
      type: BulkListType.JOB,
      results: jobOperationResults[OperationType.CANCEL],
      messages: {
        successMessage: 'Canceled jobs: ',
        noItemsMessage: 'No jobs were canceled',
        warningMessage:
          'Note: The job status has been updated. The list may appear inconsistent until you refresh any applied filters.',
        ignoredMessage:
          'These jobs were ignored because they were already canceled or executed.'
      },
      functions: {
        perform: async () => {
          setIsActionPerformed(true);
          const ignoredJobs = [];
          const remainingInstances = selectedJobInstances.filter(job => {
            if (
              job.status === GraphQL.JobStatus.Canceled ||
              job.status === GraphQL.JobStatus.Executed
            ) {
              ignoredJobs.push(job);
            } else {
              return true;
            }
          });
          await performMultipleCancel(
            remainingInstances,
            (successJobs: GraphQL.Job[], failedJobs: GraphQL.Job[]) => {
              setModalTitle(setTitle('success', 'Job Cancel'));
              setModalContent('');
              setJobOperationResults({
                ...jobOperationResults,
                [OperationType.CANCEL]: {
                  ...jobOperationResults[OperationType.CANCEL],
                  successItems: formatForBulkListJob(successJobs),
                  failedItems: formatForBulkListJob(failedJobs),
                  ignoredItems: formatForBulkListJob(ignoredJobs)
                }
              });
              handleCancelModalToggle();
            }
          );
        }
      }
    }
  };

  useEffect(() => {
    return ouiaPageTypeAndObjectId('jobs-management');
  });

  useEffect(() => {
    if (!loading && data && Object.keys(data).length > 0) {
      setDisplayTable(false);
      setLimit(data.Jobs.length);
      if (offset > 0 && initData.Jobs.length > 0) {
        setIsLoadingMore(false);
        const tempData: GraphQL.GetJobsWithFiltersQuery = {
          Jobs: initData.Jobs.concat(data.Jobs)
        };
        setInitData(tempData);
        setDisplayTable(true);
      } else {
        setInitData(data);
        setDisplayTable(true);
      }
    }
  }, [loading]);

  const jobManagementButtonSelect = () => {
    setIsKebabOpen(!isKebabOpen);
  };

  const jobManagementKebabToggle = isOpen => {
    setIsKebabOpen(isOpen);
  };

  const handleDetailsToggle = (): void => {
    setIsDetailsModalOpen(!isDetailsModalOpen);
  };

  const handleRescheduleToggle = (): void => {
    setIsRescheduleModalOpen(!isRescheduleModalOpen);
  };

  const handleCancelModalToggle = (): void => {
    setIsCancelModalOpen(!isCancelModalOpen);
  };

  const detailsAction: JSX.Element[] = [
    <Button
      key="confirm-selection"
      variant="primary"
      onClick={handleDetailsToggle}
    >
      OK
    </Button>
  ];

  const rescheduleActions: JSX.Element[] = [
    <Button
      key="cancel-reschedule"
      variant="secondary"
      onClick={handleRescheduleToggle}
    >
      Cancel
    </Button>
  ];

  const onRefresh = (): void => {
    window.location.reload();
  };

  const onReset = (): void => {
    setSelectedJobInstances([]);
    setSelectedStatus(defaultStatus);
    setChips(defaultStatus);
    setValues(defaultStatus);
    setOffset(0);
  };

  const dropdownItemsJobsManagementButtons = (): JSX.Element[] => {
    return [
      <DropdownItem
        key="cancel"
        onClick={jobOperations[OperationType.CANCEL].functions.perform}
        isDisabled={selectedJobInstances.length === 0}
      >
        Cancel selected
      </DropdownItem>
    ];
  };

  const jobManagementButtons: JSX.Element = (
    <OverflowMenu breakpoint="xl">
      <OverflowMenuContent>
        <OverflowMenuItem>
          <Button
            variant="secondary"
            onClick={jobOperations[OperationType.CANCEL].functions.perform}
            isDisabled={selectedJobInstances.length === 0}
          >
            Cancel selected
          </Button>
        </OverflowMenuItem>
      </OverflowMenuContent>
      <OverflowMenuControl>
        <Dropdown
          onSelect={jobManagementButtonSelect}
          toggle={<KebabToggle onToggle={jobManagementKebabToggle} />}
          isOpen={isKebabOpen}
          isPlain
          dropdownItems={dropdownItemsJobsManagementButtons()}
        />
      </OverflowMenuControl>
    </OverflowMenu>
  );

  const renderToolbar = (): JSX.Element => {
    return (
      <Toolbar
        id="data-toolbar-with-chip-groups"
        className="pf-m-toggle-group-container"
        collapseListedFiltersBreakpoint="md"
        clearAllFilters={() => {
          onReset();
          setSelectedJobInstances([]);
        }}
        clearFiltersButtonText="Reset to default"
      >
        <ToolbarContent>
          <JobsManagementFiters
            selectedStatus={selectedStatus}
            setSelectedStatus={setSelectedStatus}
            setValues={setValues}
            chips={chips}
            setChips={setChips}
            setOffset={setOffset}
            setSelectedJobInstances={setSelectedJobInstances}
          />
          <ToolbarGroup>
            <ToolbarItem>
              <Button
                variant="plain"
                onClick={() => {
                  onRefresh();
                  setSelectedJobInstances([]);
                }}
                id="refresh-button"
                ouiaId="refresh-button"
                aria-label={'Refresh list'}
              >
                <SyncIcon />
              </Button>
            </ToolbarItem>
          </ToolbarGroup>
          <ToolbarItem variant="separator" />
          <ToolbarGroup className="pf-u-ml-md" id="jobs-management-buttons">
            {jobManagementButtons}
          </ToolbarGroup>
        </ToolbarContent>
      </Toolbar>
    );
  };

  if (data) {
    if (!loading && values.length > 0 && data.Jobs.length === 0) {
      return (
        <Redirect
          to={{
            pathname: '/NoData',
            state: {
              prev: '/ProcessInstances',
              title: 'Jobs not found',
              description: `There are no jobs associated with any process instance.`,
              buttonText: 'Go to process instance'
            }
          }}
        />
      );
    }
  }
  return (
    <div
      {...componentOuiaProps(
        ouiaId,
        'JobsManagementPage',
        ouiaSafe ? ouiaSafe : !loading
      )}
    >
      {!error ? (
        <>
          <PageSection variant="light">
            <PageTitle title="Jobs Management" />
            <Breadcrumb>
              <BreadcrumbItem>
                <Link to={'/'}>Home</Link>
              </BreadcrumbItem>
              <BreadcrumbItem isActive>Jobs</BreadcrumbItem>
            </Breadcrumb>
          </PageSection>
          <PageSection>
            {renderToolbar()}
            {!loading || isLoadingMore ? (
              <Card>
                <CardBody>
                  {displayTable && (
                    <refetchContext.Provider value={refetch}>
                      <JobsManagementTable
                        data={initData}
                        handleDetailsToggle={handleDetailsToggle}
                        handleRescheduleToggle={handleRescheduleToggle}
                        handleCancelModalToggle={handleCancelModalToggle}
                        setModalTitle={setModalTitle}
                        setModalContent={setModalContent}
                        setOffset={setOffset}
                        setOrderBy={setOrderBy}
                        setSelectedJob={setSelectedJob}
                        setSortBy={setSortBy}
                        selectedJobInstances={selectedJobInstances}
                        setSelectedJobInstances={setSelectedJobInstances}
                        sortBy={sortBy}
                        setIsActionPerformed={setIsActionPerformed}
                        isActionPerformed={isActionPerformed}
                      />
                    </refetchContext.Provider>
                  )}
                  {chips.length === 0 && (
                    <KogitoEmptyState
                      type={KogitoEmptyStateType.Reset}
                      title="No filter applied."
                      body="Try applying at least one filter to see results"
                      onClick={() => onReset()}
                    />
                  )}
                </CardBody>
              </Card>
            ) : (
              <>
                {!isLoadingMore && (
                  <Card>
                    <Bullseye>
                      <KogitoSpinner spinnerText="Loading jobs list..." />
                    </Bullseye>
                  </Card>
                )}
              </>
            )}
            {selectedJob && Object.keys(selectedJob).length > 0 && (
              <JobsPanelDetailsModal
                actionType="Job Details"
                modalTitle={setTitle('success', 'Job Details')}
                isModalOpen={isDetailsModalOpen}
                handleModalToggle={handleDetailsToggle}
                modalAction={detailsAction}
                job={selectedJob}
              />
            )}
            {selectedJob && Object.keys(selectedJob).length > 0 && (
              <refetchContext.Provider value={refetch}>
                <JobsRescheduleModal
                  actionType="Job Reschedule"
                  modalTitle={setTitle('success', 'Job Reschedule')}
                  isModalOpen={isRescheduleModalOpen}
                  handleModalToggle={handleRescheduleToggle}
                  modalAction={rescheduleActions}
                  job={selectedJob}
                  setRescheduleClicked={setIsRescheduleModalOpen}
                  rescheduleClicked={isRescheduleModalOpen}
                />
              </refetchContext.Provider>
            )}
            <JobsCancelModal
              actionType="Job Cancel"
              isModalOpen={isCancelModalOpen}
              handleModalToggle={handleCancelModalToggle}
              modalTitle={modalTitle}
              modalContent={modalContent}
              jobOperations={jobOperations[OperationType.CANCEL]}
            />
            {(!loading || isLoadingMore) &&
              (limit === pageSize || isLoadingMore) && (
                <LoadMore
                  offset={offset}
                  setOffset={setOffset}
                  getMoreItems={onGetMoreInstances}
                  pageSize={pageSize}
                  isLoadingMore={isLoadingMore}
                />
              )}
          </PageSection>
        </>
      ) : (
        <ServerErrors error={error} variant="large" />
      )}
    </div>
  );
};

export default JobsManagementPage;
