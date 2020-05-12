import React from 'react';
import LoadMoreComponent from '../LoadMoreComponent';
import { getWrapper } from '@kogito-apps/common';

describe('LoadMore component tests with isLoading false', () => {
  const props = {
    offset: 0,
    setOffset: jest.fn(),
    getMoreItems: jest.fn(),
    pageSize: 10,
    isLoadingMore: false
  };
  it('snapshot testing', () => {
    const wrapper = getWrapper(<LoadMoreComponent {...props} />, 'LoadMoreComponent');
    expect(wrapper).toMatchSnapshot();
  });
  it('test loadMore button click', () => {
    const wrapper = getWrapper(<LoadMoreComponent {...props} />, 'LoadMoreComponent');
    const button1 = wrapper.find('#load10').first();
    const button2 = wrapper.find('#load20').first();
    const button3 = wrapper.find('#load50').first();
    const button4 = wrapper.find('#load100').first();
    button1.simulate('click');
    button2.simulate('click');
    button3.simulate('click');
    button4.simulate('click');
    expect(props.getMoreItems).toHaveBeenCalledTimes(4);
    expect(props.getMoreItems.mock.calls).toEqual([
      [10, 10],
      [10, 20],
      [10, 50],
      [10, 100]
    ]);
  });
});

describe('LoadMore component tests with isLoading true', () => {
  const props = {
    offset: 0,
    setOffset: jest.fn(),
    getMoreItems: jest.fn(),
    pageSize: 10,
    isLoadingMore: true
  };
  it('snapshot testing', () => {
    const wrapper = getWrapper(<LoadMoreComponent {...props} />, 'LoadMoreComponent');
    expect(wrapper).toMatchSnapshot();
  });
  it('test Loading button displayed', () => {
    const wrapper = getWrapper(<LoadMoreComponent {...props} />, 'LoadMoreComponent');
    expect(wrapper.find('#loading').exists()).toBeTruthy();
  });
});
