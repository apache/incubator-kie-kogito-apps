import React from 'react';
import { mount, shallow } from 'enzyme';
import { MemoryRouter } from 'react-router-dom';
import AuditOverview from '../AuditOverview';
import useExecutions from '../useExecutions';

jest.mock('../useExecutions');

describe('Audit overview', () => {
  test('renders correctly a list of executions', () => {
    const mockLoadExecutions = jest.fn();
    const executions = {
      status: 'SUCCESS',
      data: {
        total: 1,
        limit: 10,
        offset: 0,
        headers: [
          {
            executionId: 'b2b0ed8d-c1e2-46b5-ad4f-3ac54ff4beae',
            executionDate: '2020-06-01T12:33:57+0000',
            executionSucceeded: true,
            executorName: 'testUser',
            executedModelName: 'LoanEligibility',
            executionType: 'DECISION'
          }
        ]
      }
    };
    (useExecutions as jest.Mock).mockReturnValue({
      mockLoadExecutions,
      executions
    });
    const wrapper = shallow(
      <AuditOverview
        dateRangePreset={{ fromDate: '2020-01-01', toDate: '2020-02-01' }}
      />
    );
    expect(wrapper).toMatchSnapshot();
  });

  test('loads a list of executions from the last month by default', () => {
    const mockLoadExecutions = jest.fn();
    const executions = {
      status: 'SUCCESS',
      data: {
        total: 1,
        limit: 10,
        offset: 0,
        headers: [
          {
            executionId: 'b2b0ed8d-c1e2-46b5-ad4f-3ac54ff4beae',
            executionDate: '2020-06-01T12:33:57+0000',
            executionSucceeded: true,
            executorName: 'testUser',
            executedModelName: 'LoanEligibility',
            executionType: 'DECISION'
          }
        ]
      }
    };
    (useExecutions as jest.Mock).mockReturnValue({
      mockLoadExecutions,
      executions
    });

    const wrapper = mount(
      <MemoryRouter>
        <AuditOverview />
      </MemoryRouter>
    );
    const oneMonthAgo = new Date();
    oneMonthAgo.setMonth(oneMonthAgo.getMonth() - 1);
    const fromDate = oneMonthAgo.toISOString().substr(0, 10);
    const toDate = new Date().toISOString().substr(0, 10);

    expect(useExecutions).toHaveBeenCalledWith('', fromDate, toDate, 10, 0);
    expect(wrapper.find('ExecutionTable').props().data).toStrictEqual(
      executions
    );
  });
});
