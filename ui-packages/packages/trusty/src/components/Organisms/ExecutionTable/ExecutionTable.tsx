import React from 'react';
import { Link } from 'react-router-dom';
import { IRow, Table, TableBody, TableHeader } from '@patternfly/react-table';
import {
  Bullseye,
  EmptyState,
  EmptyStateBody,
  EmptyStateIcon,
  Title
} from '@patternfly/react-core';
import { SearchIcon, ExclamationCircleIcon } from '@patternfly/react-icons';
import ExecutionStatus from '../../Atoms/ExecutionStatus/ExecutionStatus';
import FormattedDate from '../../Atoms/FormattedDate/FormattedDate';
import skeletonRows from '../../Molecules/skeletonRows/skeletonRows';
import { IExecution, IExecutions, RemoteData } from '../../../types';

type ExecutionTableProps = {
  data: RemoteData<Error, IExecutions>;
};

const prepareExecutionTableRows = (rowData: IExecution[]) => {
  const rows: IRow[] = [];

  rowData.forEach(item => {
    const row: IRow = {};
    const cells = [];
    cells.push({
      title: (
        <Link
          to={`/audit/${item.executionType.toLocaleLowerCase()}/${
            item.executionId
          }`}
        >
          {'#' + item.executionId.toUpperCase()}
        </Link>
      )
    });
    cells.push(item.executedModelName);
    cells.push(item.executorName);
    cells.push({
      title: <FormattedDate date={item.executionDate} />
    });
    cells.push({
      title: <ExecutionStatus result={item.executionSucceeded} />
    });
    row.cells = cells;
    row.decisionKey = 'key-' + item.executionId;
    rows.push(row);
  });
  return rows;
};

const noExecutions = (colSpan: number) => {
  return [
    {
      heightAuto: true,
      decisionKey: 'no-results',
      cells: [
        {
          props: { colSpan },
          title: (
            <Bullseye>
              <EmptyState>
                <EmptyStateIcon icon={SearchIcon} />
                <Title headingLevel="h5" size="lg">
                  No executions found
                </Title>
                <EmptyStateBody>
                  No results match the filter criteria. Try removing all
                  filters.
                </EmptyStateBody>
              </EmptyState>
            </Bullseye>
          )
        }
      ]
    }
  ];
};

const loadingError = (colSpan: number) => {
  return [
    {
      heightAuto: true,
      decisionKey: 'no-results',
      cells: [
        {
          props: { colSpan },
          title: (
            <Bullseye>
              <EmptyState>
                <EmptyStateIcon icon={ExclamationCircleIcon} color="#C9190B" />
                <Title headingLevel="h5" size="lg">
                  Loading Error
                </Title>
                <EmptyStateBody>
                  We are unable to load data right now. Try again later.
                </EmptyStateBody>
              </EmptyState>
            </Bullseye>
          )
        }
      ]
    }
  ];
};

const ExecutionTable = (props: ExecutionTableProps) => {
  const { data } = props;
  const columns = ['ID', 'Description', 'Executor', 'Date', 'Execution Status'];

  return (
    <>
      {(data.status === 'LOADING' || data.status === 'NOT_ASKED') && (
        <Table
          cells={columns}
          rows={skeletonRows(columns.length, 10, 'decisionKey')}
          aria-label="Executions list"
        >
          <TableHeader />
          <TableBody rowKey="decisionKey" />
        </Table>
      )}
      {data.status === 'SUCCESS' && data.data.headers.length > 0 && (
        <Table
          cells={columns}
          rows={prepareExecutionTableRows(data.data.headers)}
          aria-label="Executions list"
        >
          <TableHeader />
          <TableBody rowKey="decisionKey" />
        </Table>
      )}
      {data.status === 'SUCCESS' && data.data.headers.length === 0 && (
        <Table
          cells={columns}
          rows={noExecutions(columns.length)}
          aria-label="Executions list"
        >
          <TableHeader />
          <TableBody rowKey="decisionKey" />
        </Table>
      )}
      {data.status === 'FAILURE' && (
        <Table
          cells={columns}
          rows={loadingError(columns.length)}
          aria-label="Executions list"
        >
          <TableHeader />
          <TableBody rowKey="decisionKey" />
        </Table>
      )}
    </>
  );
};

export default ExecutionTable;
