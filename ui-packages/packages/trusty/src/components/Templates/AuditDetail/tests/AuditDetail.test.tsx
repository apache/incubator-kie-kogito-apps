import * as React from 'react';
import { mount } from 'enzyme';
import AuditDetail from '../AuditDetail';
import useExecutionInfo from '../useExecutionInfo';
import useDecisionOutcomes from '../useDecisionOutcomes';
import { Execution, Outcome, RemoteData } from '../../../../types';
import { MemoryRouter } from 'react-router';

jest.mock('../useExecutionInfo');
jest.mock('../useDecisionOutcomes');
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useParams: () => ({
    executionId: 'b2b0ed8d-c1e2-46b5-3ac54ff4beae-1000'
  }),
  useRouteMatch: () => ({
    path: '/audit/decision/b2b0ed8d-c1e2-46b5-3ac54ff4beae-1000',
    url: '/audit/:executionType/:executionId'
  })
}));

describe('AuditDetail', () => {
  test('renders loading animation while fetching data', () => {
    const execution = {
      status: 'LOADING'
    } as RemoteData<Error, Execution>;
    const outcomes = {
      status: 'LOADING'
    } as RemoteData<Error, Outcome[]>;

    (useExecutionInfo as jest.Mock).mockReturnValue(execution);
    (useDecisionOutcomes as jest.Mock).mockReturnValue(outcomes);

    const wrapper = mount(
      <MemoryRouter
        initialEntries={[
          {
            pathname: '/audit/decision/b2b0ed8d-c1e2-46b5-3ac54ff4beae-1000',
            key: 'audit-detail'
          }
        ]}
      >
        <AuditDetail />
      </MemoryRouter>
    );

    expect(useExecutionInfo).toHaveBeenCalledWith(
      'b2b0ed8d-c1e2-46b5-3ac54ff4beae-1000'
    );
    expect(useDecisionOutcomes).toHaveBeenCalledWith(
      'b2b0ed8d-c1e2-46b5-3ac54ff4beae-1000'
    );

    expect(wrapper.find('ExecutionHeader')).toHaveLength(1);
    expect(wrapper.find('ExecutionHeader').prop('execution')).toStrictEqual(
      execution
    );
    expect(wrapper.find('.audit-detail__nav SkeletonFlexStripes')).toHaveLength(
      1
    );
    expect(wrapper.find('Switch Route')).toHaveLength(1);
    expect(
      wrapper
        .find('Route StackItem')
        .at(0)
        .find('SkeletonStripe')
    ).toHaveLength(1);
    expect(
      wrapper
        .find('Route StackItem')
        .at(1)
        .find('SkeletonCards')
    ).toHaveLength(1);
  });

  test('renders correctly an execution', () => {
    const execution = {
      status: 'SUCCESS',
      data: {
        executionId: 'b2b0ed8d-c1e2-46b5-3ac54ff4beae-1000',
        executionDate: '2020-08-12T12:54:53.933Z',
        executionType: 'DECISION',
        executedModelName: 'fraud-score',
        executionSucceeded: true,
        executorName: 'Technical User'
      }
    } as RemoteData<Error, Execution>;
    const outcomes = {
      status: 'SUCCESS',
      data: [
        {
          outcomeId: '_12268B68-94A1-4960-B4C8-0B6071AFDE58',
          outcomeName: 'Mortgage Approval',
          evaluationStatus: 'SUCCEEDED',
          outcomeResult: {
            name: 'Mortgage Approval',
            typeRef: 'boolean',
            value: true,
            components: []
          },
          messages: [],
          hasErrors: false
        },
        {
          outcomeId: '_9CFF8C35-4EB3-451E-874C-DB27A5A424C0',
          outcomeName: 'Risk Score',
          evaluationStatus: 'SUCCEEDED',
          outcomeResult: {
            name: 'Risk Score',
            typeRef: 'number',
            value: 21.7031851958099,
            components: []
          },
          messages: [],
          hasErrors: false
        }
      ] as Outcome[]
    };

    (useExecutionInfo as jest.Mock).mockReturnValue(execution);
    (useDecisionOutcomes as jest.Mock).mockReturnValue(outcomes);

    const wrapper = mount(
      <MemoryRouter
        initialEntries={[
          {
            pathname: '/audit/decision/b2b0ed8d-c1e2-46b5-3ac54ff4beae-1000',
            key: 'audit-detail'
          }
        ]}
      >
        <AuditDetail />
      </MemoryRouter>
    );

    expect(wrapper.find('ExecutionHeader')).toHaveLength(1);
    expect(wrapper.find('ExecutionHeader').prop('execution')).toStrictEqual(
      execution
    );
    expect(wrapper.find('Nav')).toHaveLength(1);
    expect(wrapper.find('NavItem')).toHaveLength(2);
    expect(
      wrapper
        .find('NavItem a')
        .at(0)
        .text()
    ).toMatch('Outcomes');
    expect(
      wrapper
        .find('NavItem a')
        .at(1)
        .text()
    ).toMatch('Outcomes Details');
    expect(wrapper.find('Switch Route')).toHaveLength(1);
    expect(wrapper.find('Switch Route').prop('path')).toMatch(
      '/audit/decision/b2b0ed8d-c1e2-46b5-3ac54ff4beae-1000/outcomes'
    );
    expect(wrapper.find('ExecutionDetail')).toHaveLength(1);
    expect(wrapper.find('ExecutionDetail').prop('outcome')).toStrictEqual(
      outcomes
    );
  });
});
