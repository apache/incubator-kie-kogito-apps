import React from 'react';
import SkeletonDataList from '../SkeletonDataList';
import { shallow } from 'enzyme';

describe('SkeletonDatalist', () => {
  test('renders a 2x2 data list', () => {
    const wrapper = shallow(<SkeletonDataList rowsNumber={2} colsNumber={2} />);

    expect(wrapper).toMatchSnapshot();
  });

  test('renders a 2x2 data list with header styling', () => {
    const wrapper = shallow(
      <SkeletonDataList rowsNumber={2} colsNumber={2} hasHeader />
    );

    expect(wrapper.find('.skeleton-datalist__header')).toHaveLength(1);
  });
});
