import React from 'react';
import {
  Button,
  DataList,
  DataListItem,
  DataListCell,
  Spinner,
  InjectedOuiaProps,
  withOuiaContext
} from '@patternfly/react-core';
import './LoadMoreComponent.css';
import { componentOuiaProps } from '@kogito-apps/common';

interface IOwnProps {
  offset: number;
  setOffset: (offset: number) => void;
  getMoreItems: (initval: number, pageSize: number) => void;
  pageSize: number;
  isLoadingMore: boolean;
}

const LoadMoreComponent: React.FC<IOwnProps & InjectedOuiaProps> = ({
  offset,
  setOffset,
  getMoreItems,
  pageSize,
  isLoadingMore,
  ouiaContext,
  ouiaId
}) => {
  const loadMore = newPageSize => {
    const newOffset = offset + pageSize;
    setOffset(newOffset);
    getMoreItems(newOffset, newPageSize);
  };

  const load10More = () => {
    loadMore(10);
  };
  const load20More = () => {
    loadMore(20);
  };
  const load50More = () => {
    loadMore(50);
  };
  const load100More = () => {
    loadMore(100);
  };

  return (
    <DataList aria-label="Simple data list example"
      {...componentOuiaProps(ouiaContext, ouiaId, 'LoadMore', !isLoadingMore)}>
      <DataListItem aria-labelledby="kie-datalist-item">
        <DataListCell className="kogito-management-console__load-more">
          {!isLoadingMore ? (
            <>
              <Button onClick={load10More} variant="secondary" id="load10">
                Load 10 more
              </Button>{' '}
              <Button onClick={load20More} variant="secondary" id="load20">
                Load 20 more
              </Button>{' '}
              <Button onClick={load50More} variant="secondary" id="load50">
                Load 50 more
              </Button>{' '}
              <Button onClick={load100More} variant="secondary" id="load100">
                Load 100 more
              </Button>
            </>
          ) : (
            <Button variant="secondary" id="loading">
              Loading... <Spinner size="md" />
            </Button>
          )}
        </DataListCell>
      </DataListItem>
    </DataList>
  );
};

const LoadMoreComponentWithContext = withOuiaContext(LoadMoreComponent);
export default LoadMoreComponentWithContext;
