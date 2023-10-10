import { mount } from 'enzyme';
import React from 'react';
import JobsManagementContainer from '../JobsManagementContainer';

describe('WebApp - JobsManagementContainer tests', () => {
  it('Snapshot test with default values', () => {
    const wrapper = mount(<JobsManagementContainer />).find(
      'JobsManagementContainer'
    );
    expect(wrapper).toMatchSnapshot();
  });
});
