import React from 'react';
import { Pagination } from '@patternfly/react-core';

interface IOwnProps {
  totalProcesses: number;
  offset: number;
  limit: number;
  setOffset: (arg0: number) => void;
  setLimit: (arg0: number) => void;
  checkedArray: any;
  getProcessInstances: any;
  page: number;
  setPage: any;
  setIsLoading: any;
}

const PaginationComponent: React.FC<IOwnProps> = ({
  totalProcesses,
  offset,
  limit,
  setOffset,
  setLimit,
  checkedArray,
  getProcessInstances,
  page,
  setPage,
  setIsLoading
}) => {
  const onSetPage = (_event, newPage, perPage, startIdx, endIdx) => {
    setIsLoading(true);
    setPage(newPage);
    const newOffset = offset;
    if (newPage > page) {
      setOffset(offset + limit);
      getProcessInstances({
        variables: { state: checkedArray, offset: newOffset + limit, limit }
      });
    } else {
      setOffset(offset - limit);
      getProcessInstances({
        variables: { state: checkedArray, offset: newOffset - limit, limit }
      });
    }
  };

  const onPerPageSelect = (_event, perPage) => {
    setLimit(perPage);
    setOffset(0);
    setPage(0);
    getProcessInstances({
      variables: { state: checkedArray, offset: 0, limit: perPage }
    });
  };

  return (
    <Pagination
      itemCount={totalProcesses}
      perPage={limit}
      page={page}
      onSetPage={onSetPage}
      onPerPageSelect={onPerPageSelect}
      widgetId="pagination-options-menu-top"
      className="pf-u-pb-sm"
    />
  );
};

export default PaginationComponent;
