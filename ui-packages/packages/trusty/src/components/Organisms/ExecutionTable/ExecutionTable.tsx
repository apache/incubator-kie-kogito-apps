import React, { useEffect, useState } from 'react';
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
import skeletonRows from '../../../utils/skeletonRows/skeletonRows';
import { IExecution, IExecutions, RemoteData } from '../../../types';

type ExecutionTableProps = {
  data: RemoteData<Error, IExecutions>;
};

const ExecutionTable = (props: ExecutionTableProps) => {
  const { data } = props;
  const columns = ['ID', 'Description', 'Executor', 'Date', 'Execution Status'];
  const [rows, setRows] = useState<IRow[]>(prepareRows(columns.length, data));

  useEffect(() => {
    setRows(prepareRows(columns.length, data));
  }, [data.status]);

  return (
    <Table cells={columns} rows={rows} aria-label="Executions list">
      <TableHeader />
      <TableBody rowKey="executionKey" />
    </Table>
  );
};

const prepareRows = (
  columnsNumber: number,
  data: RemoteData<Error, IExecutions>
) => {
  let rows;
  switch (data.status) {
    case 'NOT_ASKED':
    case 'LOADING':
      rows = skeletonRows(columnsNumber, 10, 'executionKey');
      break;
    case 'SUCCESS':
      if (data.data.headers.length > 0) {
        rows = prepareExecutionsRows(data.data.headers);
      } else {
        rows = noExecutions(columnsNumber);
      }
      break;
    case 'FAILURE':
      rows = loadingError(columnsNumber);
      break;
  }
  return rows;
};

const prepareExecutionsRows = (rowData: IExecution[]) => {
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
    row.executionKey = 'key-' + item.executionId;
    rows.push(row);
  });
  return rows;
};

const noExecutions = (colSpan: number) => {
  return [
    {
      heightAuto: true,
      executionKey: 'no-results',
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
      executionKey: 'no-results',
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

export default ExecutionTable;
