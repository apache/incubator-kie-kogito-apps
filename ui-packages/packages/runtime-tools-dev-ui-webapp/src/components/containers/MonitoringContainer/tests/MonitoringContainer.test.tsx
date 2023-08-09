import React from 'react';
import { mount } from 'enzyme';
import MonitoringContainer from '../MonitoringContainer';
import { Dashboard, MonitoringView } from '@kogito-apps/monitoring';

describe('WebApp - MonitorinContainer tests', () => {
  const props = {
    dataIndexUrl: 'http://localhost:4000',
    dashboard: Dashboard.DETAILS,
    workflow: ''
  };

  it('Snapshot test', () => {
    const wrapper = mount(
      <MonitoringContainer>
        <MonitoringView {...props} />
      </MonitoringContainer>
    );
    expect(wrapper).toMatchSnapshot();
  });
});
