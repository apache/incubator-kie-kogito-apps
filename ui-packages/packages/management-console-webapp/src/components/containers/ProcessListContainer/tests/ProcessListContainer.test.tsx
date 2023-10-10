import React from 'react';
import { mount } from 'enzyme';
import { ProcessListState } from '@kogito-apps/management-console-shared/dist/types';
import ProcessListContainer from '../ProcessListContainer';
import * as ProcessListContext from '../../../../channel/ProcessList/ProcessListContext';
import { ProcessListQueries } from '../../../../channel/ProcessList/ProcessListQueries';
import { ProcessListGatewayApiImpl } from '../../../../channel/ProcessList/ProcessListGatewayApi';

const MockQueries = jest.fn<ProcessListQueries, []>(() => ({
  getProcessInstances: jest.fn(),
  getChildProcessInstances: jest.fn(),
  handleProcessSkip: jest.fn(),
  handleProcessAbort: jest.fn(),
  handleProcessRetry: jest.fn(),
  handleProcessMultipleAction: jest.fn()
}));

jest
  .spyOn(ProcessListContext, 'useProcessListGatewayApi')
  .mockImplementation(() => new ProcessListGatewayApiImpl(new MockQueries()));

describe('ProcessListContainer tests', () => {
  const props = {
    initialState: {} as ProcessListState
  };
  it('Snapshot', () => {
    const wrapper = mount(<ProcessListContainer {...props} />).find(
      'ProcessListContainer'
    );

    expect(wrapper).toMatchSnapshot();

    const forwardRef = wrapper.childAt(0);

    expect(forwardRef.props().driver).not.toBeNull();
    // expect(forwardRef.props().envelopePath).toBe('/envelope/process-list.html');
    expect(forwardRef.props().targetOrigin).toBe('http://localhost');
  });
});
