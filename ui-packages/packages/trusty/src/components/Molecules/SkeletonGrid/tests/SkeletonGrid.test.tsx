import React from 'react';
import SkeletonGrid from '../SkeletonGrid';
import { shallow } from 'enzyme';

describe('SkeletonGrid', () => {
  test('renders a 2x2 grid', () => {
    const wrapper = shallow(<SkeletonGrid rowsNumber={2} colsNumber={2} />);

    expect(wrapper).toMatchSnapshot();
  });
  test('renders a grid with custom columns sizes', () => {
    const wrapper = shallow(
      <SkeletonGrid rowsNumber={2} colsNumber={[1, 2, 1]} />
    );

    expect(wrapper).toMatchSnapshot();
  });
});
