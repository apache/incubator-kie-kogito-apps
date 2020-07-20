import React, { useRef } from 'react';
import {
  Button,
  ButtonVariant,
  DataToolbar,
  DataToolbarContent,
  DataToolbarItem,
  DataToolbarItemVariant,
  InputGroup,
  TextInput
} from '@patternfly/react-core';
import { SearchIcon, SyncIcon } from '@patternfly/react-icons';
import PaginationContainer from '../PaginationContainer/PaginationContainer';
import DatePicker from '../../Molecules/DatePicker/DatePicker';

interface AuditToolbarBaseProps {
  page: number;
  pageSize: number;
  setPage: (page: number) => void;
  setPageSize: (pageSize: number) => void;
  total: number;
}

interface AuditToolbarCompleteProps extends AuditToolbarBaseProps {
  setSearchString: (searchString: string) => void;
  fromDate: string;
  setFromDate: (fromDate: string) => void;
  toDate: string;
  setToDate: (toDate: string) => void;
  onRefresh: () => void;
}

export const AuditToolbarTop = (props: AuditToolbarCompleteProps) => {
  const {
    setSearchString,
    fromDate,
    setFromDate,
    toDate,
    setToDate,
    total,
    pageSize,
    page,
    setPage,
    setPageSize,
    onRefresh
  } = props;

  const searchField = useRef<HTMLInputElement>(null);
  const onSearchSubmit = (): void => {
    if (searchField && searchField.current) {
      setSearchString(searchField.current.value);
    }
  };
  const onSearchEnter = (event: React.KeyboardEvent): void => {
    if (searchField && searchField.current && event.key === 'Enter') {
      setSearchString(searchField.current.value);
    }
  };

  return (
    <DataToolbar id="audit-overview-top-toolbar">
      <DataToolbarContent>
        <DataToolbarItem variant="label">Search</DataToolbarItem>
        <DataToolbarItem>
          <InputGroup>
            <TextInput
              name="audit-search-input"
              ref={searchField}
              id="audit-search-input"
              type="search"
              aria-label="search executions"
              onKeyDown={onSearchEnter}
              placeholder="Search by ID"
            />
            <Button
              id="audit-search"
              variant={ButtonVariant.control}
              aria-label="search button for search input"
              onClick={onSearchSubmit}
            >
              <SearchIcon />
            </Button>
          </InputGroup>
        </DataToolbarItem>
        <DataToolbarItem variant="label">From</DataToolbarItem>
        <DataToolbarItem>
          <DatePicker
            fromDate={fromDate}
            maxDate={toDate}
            value={fromDate}
            label="Starting date"
            id="audit-from-date"
            onDateUpdate={setFromDate}
          />
        </DataToolbarItem>
        <DataToolbarItem variant="label">To</DataToolbarItem>
        <DataToolbarItem>
          <DatePicker
            value={toDate}
            minDate={fromDate}
            onDateUpdate={setToDate}
            id="audit-to-date"
            label="Ending date"
          />
        </DataToolbarItem>
        <DataToolbarItem>
          <DataToolbarItem>
            <Button
              id="executions-refresh"
              variant="plain"
              title="Refresh"
              aria-label="Refresh"
              onClick={() => onRefresh()}
            >
              <SyncIcon />
            </Button>
          </DataToolbarItem>
        </DataToolbarItem>
        <DataToolbarItem variant={DataToolbarItemVariant.pagination}>
          <PaginationContainer
            total={total}
            page={page}
            pageSize={pageSize}
            onSetPage={setPage}
            onSetPageSize={setPageSize}
            paginationId="audit-overview-top-pagination"
          />
        </DataToolbarItem>
      </DataToolbarContent>
    </DataToolbar>
  );
};

export const AuditToolbarBottom = (props: AuditToolbarBaseProps) => {
  const { total, pageSize, page, setPage, setPageSize } = props;
  return (
    <DataToolbar id="audit-overview-bottom-toolbar">
      <DataToolbarContent>
        <DataToolbarItem variant="pagination">
          <PaginationContainer
            total={total}
            page={page}
            pageSize={pageSize}
            onSetPage={setPage}
            onSetPageSize={setPageSize}
            paginationId="audit-overview-bottom-pagination"
          />
        </DataToolbarItem>
      </DataToolbarContent>
    </DataToolbar>
  );
};
