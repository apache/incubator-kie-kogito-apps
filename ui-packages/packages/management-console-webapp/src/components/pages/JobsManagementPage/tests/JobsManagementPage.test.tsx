import { mount } from 'enzyme';
import React from 'react';
import JobsManagementPage from '../JobsManagementPage';
import { BrowserRouter } from 'react-router-dom';

describe('WebApp - JobsManagementPage tests', () => {
  it('Snapshot test with default values', () => {
    const wrapper = mount(
      <BrowserRouter>
        <JobsManagementPage />
      </BrowserRouter>
    ).find('JobsManagementPage');
    expect(wrapper).toMatchSnapshot();
  });
});
