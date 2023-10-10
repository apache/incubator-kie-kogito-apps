import React from 'react';
import { mount } from 'enzyme';
import ProcessListPage from '../ProcessListPage';
import { BrowserRouter } from 'react-router-dom';
import * as H from 'history';

jest.mock('../../../containers/ProcessListContainer/ProcessListContainer');

describe('ProcessListPage tests', () => {
  const props = {
    match: {
      params: {
        instanceID: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b'
      },
      url: '',
      isExact: false,
      path: ''
    },
    location: H.createLocation(''),
    history: H.createBrowserHistory()
  };
  it('Snapshot', () => {
    const wrapper = mount(
      <BrowserRouter>
        <ProcessListPage {...props} />
      </BrowserRouter>
    ).find('ProcessListPage');

    expect(wrapper).toMatchSnapshot();
    expect(wrapper.find('MockedProcessListContainer').exists()).toBeTruthy();
  });
});
