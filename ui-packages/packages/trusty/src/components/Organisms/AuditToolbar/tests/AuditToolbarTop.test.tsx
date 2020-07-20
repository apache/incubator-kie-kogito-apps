import React from 'react';
import { AuditToolbarTop } from '../AuditToolbar';
import { mount, shallow } from 'enzyme';

describe('Audit top toolbar', () => {
  test('renders correctly', () => {
    const wrapper = renderAuditToolbarTop('shallow');
    expect(wrapper).toMatchSnapshot();
  });

  test('allows search by ID', () => {
    const setSearchString = jest.fn();
    const searchString = '12345';
    const wrapper = renderAuditToolbarTop('mount', { setSearchString });
    const searchInput = wrapper.find('input#audit-search-input');
    const searchButton = wrapper.find('button#audit-search');
    const inputNode = searchInput.getDOMNode<HTMLInputElement>();

    inputNode.value = searchString;
    searchButton.simulate('click');

    expect(setSearchString).toBeCalledTimes(1);
    expect(setSearchString).toBeCalledWith(searchString);

    // @ts-ignore
    searchInput.props().onKeyDown({ key: 'Enter', keyCode: 13, which: 13 });

    expect(setSearchString).toBeCalledTimes(2);
    expect(setSearchString).toBeCalledWith(searchString);
  });

  test('handles from date filter', () => {
    const setFromDate = jest.fn();
    const fromDate = '2020-02-01';
    const wrapper = renderAuditToolbarTop('mount', { setFromDate });

    wrapper.props().setFromDate(fromDate);

    expect(setFromDate).toBeCalledTimes(1);
    expect(setFromDate).toBeCalledWith(fromDate);
  });

  test('handles to date filter', () => {
    const setToDate = jest.fn();
    const toDate = '2020-04-01';
    const wrapper = renderAuditToolbarTop('mount', { setToDate });

    wrapper.props().setToDate(toDate);

    expect(setToDate).toBeCalledTimes(1);
    expect(setToDate).toBeCalledWith(toDate);
  });

  test('handles pagination', () => {
    const setPage = jest.fn();
    const setPageSize = jest.fn();
    const page = 2;
    const pageSize = 50;
    const wrapper = renderAuditToolbarTop('mount', {
      setPage,
      setPageSize
    });

    wrapper.props().setPage(page);
    wrapper.props().setPageSize(pageSize);

    expect(setPage).toBeCalledTimes(1);
    expect(setPage).toBeCalledWith(page);
    expect(setPageSize).toBeCalledTimes(1);
    expect(setPageSize).toBeCalledWith(pageSize);
  });

  test('handles data refresh', () => {
    const onRefresh = jest.fn();
    const wrapper = renderAuditToolbarTop('mount', { onRefresh });

    wrapper.find('button#executions-refresh').simulate('click');

    expect(onRefresh).toBeCalledTimes(1);
  });
});

const renderAuditToolbarTop = (method: 'shallow' | 'mount', props?: object) => {
  const defaultProps = {
    setSearchString: jest.fn(),
    fromDate: '2020-01-01',
    setFromDate: jest.fn(),
    toDate: '2020-02-01',
    setToDate: jest.fn(),
    total: 20,
    page: 1,
    pageSize: 10,
    setPage: jest.fn(),
    setPageSize: jest.fn(),
    onRefresh: jest.fn(),
    ...props
  };
  if (method === 'shallow') {
    return shallow(<AuditToolbarTop {...defaultProps} />);
  } else {
    return mount(<AuditToolbarTop {...defaultProps} />);
  }
};
