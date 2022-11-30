import React from 'react';
import { mount } from 'enzyme';
import MonitoringContainer from '../MonitoringContainer';

describe('WebApp - MonitorinContainer tests', () => {
  it('Snapshot test', () => {
    const wrapper = mount(<MonitoringContainer />);
    expect(wrapper).toMatchSnapshot();
  });
});
