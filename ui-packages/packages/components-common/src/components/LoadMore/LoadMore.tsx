import React, { useState } from 'react';
import {
  DataList,
  DataListItem,
  DataListCell
} from '@patternfly/react-core/dist/js/components/DataList';
import { Spinner } from '@patternfly/react-core/dist/js/components/Spinner';
import {
  DropdownItem,
  Dropdown,
  DropdownToggle,
  DropdownToggleAction
} from '@patternfly/react-core/dist/js/components/Dropdown';
import { Split, SplitItem } from '@patternfly/react-core/dist/js/layouts/Split';
import {
  OUIAProps,
  componentOuiaProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import '../styles.css';
import { CheckIcon } from '@patternfly/react-icons/dist/js/icons/check-icon';

interface IOwnProps {
  offset: number;
  setOffset: (offset: number) => void;
  getMoreItems: (initval: number, pageSize: number) => void;
  pageSize: number;
  isLoadingMore: boolean;
  setLoadMoreClicked?: (loadMoreClicked: boolean) => void;
}

export const LoadMore: React.FC<IOwnProps & OUIAProps> = ({
  offset,
  setOffset,
  getMoreItems,
  pageSize,
  isLoadingMore,
  setLoadMoreClicked,
  ouiaId,
  ouiaSafe
}) => {
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [loadMoreValue, setLoadMoreValue] = useState<number>(10);

  const loadMore = (newPageSize: number): void => {
    setLoadMoreClicked && setLoadMoreClicked(true);
    const newOffset = offset + pageSize;
    setOffset(newOffset);
    getMoreItems(newOffset, newPageSize);
  };

  const onToggle = (isDropdownOpen: boolean): void => {
    setIsOpen(isDropdownOpen);
  };

  const onSelect = (event: React.SyntheticEvent<HTMLDivElement>): void => {
    const selectedValue: number = parseInt(event.currentTarget.id, 10);
    setLoadMoreValue(selectedValue);
  };

  const dropdownItem = (count: number): JSX.Element => {
    return (
      <DropdownItem
        key={'loadmore' + count}
        component="button"
        data-testid="dropdown-item"
        id={count.toString()}
      >
        <Split hasGutter>
          <SplitItem>Load {count} more</SplitItem>
          {loadMoreValue === count && (
            <SplitItem>
              <CheckIcon size="sm" color="var(--pf-global--info-color--100)" />
            </SplitItem>
          )}
        </Split>
      </DropdownItem>
    );
  };
  return (
    <DataList
      aria-label="Simple data list example"
      {...componentOuiaProps(
        ouiaId,
        'load-more',
        ouiaSafe ? ouiaSafe : !isLoadingMore
      )}
      data-testid="load-more-data-list"
    >
      <DataListItem aria-labelledby="kie-datalist-item">
        <DataListCell className="kogito-components-common__load-more">
          <div className="pf-u-float-right pf-u-mr-md">
            <Dropdown
              onSelect={onSelect}
              direction="up"
              toggle={
                <DropdownToggle
                  data-testid={`toggle-id`}
                  onToggle={onToggle}
                  splitButtonItems={[
                    <DropdownToggleAction
                      key={`toggle-id-${ouiaId}`}
                      onClick={() => {
                        loadMore(loadMoreValue);
                        setIsOpen(false);
                      }}
                      data-testid="toggle-action"
                    >
                      {isLoadingMore ? (
                        <>
                          Loading...
                          <Spinner
                            size="md"
                            className="kogito-components-common__load-more-spinner"
                          />{' '}
                        </>
                      ) : (
                        `Load ${loadMoreValue} more`
                      )}
                    </DropdownToggleAction>
                  ]}
                />
              }
              isOpen={isOpen}
              dropdownItems={[
                dropdownItem(10),
                dropdownItem(20),
                dropdownItem(50),
                dropdownItem(100)
              ]}
            />
          </div>
        </DataListCell>
      </DataListItem>
    </DataList>
  );
};
