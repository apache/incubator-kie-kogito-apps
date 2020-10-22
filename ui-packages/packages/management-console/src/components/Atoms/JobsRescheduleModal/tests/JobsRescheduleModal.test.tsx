import React from 'react';
import JobsRescheduleModal from '../JobsRescheduleModal';
import { GraphQL, getWrapper } from '@kogito-apps/common';
import { InfoCircleIcon } from '@patternfly/react-icons';
import { Button } from '@patternfly/react-core';
import { act } from 'react-dom/test-utils';
import * as Utils from '../../../../utils/Utils';
jest.mock('react-datetime-picker');
// tslint:disable: no-string-literal
// tslint:disable: no-unexpected-multiline
const props = {
  actionType: 'Job Reschedule',
  job: {
    id: '6e74a570-31c8-4020-bd70-19be2cb625f3_0',
    processId: 'travels',
    processInstanceId: '5c56eeff-4cbf-3313-a325-4c895e0afced',
    rootProcessId: '5c56eeff-4cbf-3313-a325-4c895e0afced',
    status: GraphQL.JobStatus.Executed,
    priority: 0,
    callbackEndpoint:
      'http://localhost:8080/management/jobs/travels/instances/5c56eeff-4cbf-3313-a325-4c895e0afced/timers/6e74a570-31c8-4020-bd70-19be2cb625f3_0',
    repeatInterval: 1,
    repeatLimit: 3,
    scheduledId: '0',
    retries: 0,
    lastUpdate: '2020-08-27T03:35:50.147Z',
    expirationTime: '2020-08-27T03:35:50.147Z'
  },
  modalTitle: (
    <>
      <InfoCircleIcon
        className="pf-u-mr-sm"
        color="var(--pf-global--info-color--100)"
      />
      {'Jobs Reschedule'}
    </>
  ),
  isModalOpen: true,
  handleModalToggle: jest.fn(),
  modalAction: [
    <Button key="cancel-reschedule" variant="secondary">
      Cancel
    </Button>
  ]
};

const props2 = {
  actionType: 'Job Reschedule',
  job: {
    id: '6e74a570-31c8-4020-bd70-19be2cb625f3_0',
    processId: 'travels',
    processInstanceId: '5c56eeff-4cbf-3313-a325-4c895e0afced',
    rootProcessId: '5c56eeff-4cbf-3313-a325-4c895e0afced',
    status: GraphQL.JobStatus.Executed,
    priority: 0,
    callbackEndpoint:
      'http://localhost:8080/management/jobs/travels/instances/5c56eeff-4cbf-3313-a325-4c895e0afced/timers/6e74a570-31c8-4020-bd70-19be2cb625f3_0',
    repeatInterval: null,
    repeatLimit: null,
    scheduledId: '0',
    retries: 0,
    lastUpdate: '2020-08-27T03:35:50.147Z',
    expirationTime: '2020-08-27T03:35:50.147Z'
  },
  modalTitle: (
    <>
      <InfoCircleIcon
        className="pf-u-mr-sm"
        color="var(--pf-global--info-color--100)"
      />
      {'Jobs Reschedule'}
    </>
  ),
  isModalOpen: true,
  handleModalToggle: jest.fn(),
  modalAction: [
    <Button key="cancel-reschedule" variant="secondary">
      Cancel
    </Button>
  ]
};

Date.now = jest.fn(() => 1592000000000); // UTC Fri Jun 12 2020 22:13:20
describe('Job reschedule modal tests', () => {
  beforeEach(() => {
    const DATE_TO_USE = new Date('2017-02-02T12:54:59.218Z');
    const _Date = Date;
    const MockDate: any = (...args) => {
      switch (args.length) {
        case 0:
          return DATE_TO_USE;
        default:
          // @ts-ignore
          return new _Date(...args);
      }
    };
    MockDate.UTC = _Date.UTC;
    MockDate.now = () => DATE_TO_USE.getTime();
    global.Date = MockDate;
  });
  it('test job reschedule modal', async () => {
    const handleJobRescheduleSpy = jest.spyOn(Utils, 'handleJobReschedule');
    const wrapper = getWrapper(
      <JobsRescheduleModal {...props} />,
      'JobsRescheduleModal'
    );
    expect(wrapper).toMatchSnapshot();
    wrapper
      .find('#Time-now')
      .first()
      .simulate('click');
    const value: any = '2020-08-27T03:35:50.147Z';
    await act(async () => {
      wrapper
        .find('DateTimePicker')
        .props()
        ['onChange'](value);
    });
    const date = new Date('2020-08-27T03:35:50.147Z');
    expect(wrapper.find('DateTimePicker').props()['value']).toEqual(date);
    const event: any = { target: { value: '303300' } };
    await act(async () => {
      wrapper
        .find('#repeat-interval-input')
        .first()
        .props()
        ['onChange'](event);
    });
    expect(
      wrapper
        .find('#repeat-interval-input')
        .first()
        .props()['isDisabled']
    ).toEqual(false);
    await act(async () => {
      wrapper
        .find('#repeat-limit-input')
        .first()
        .props()
        ['onChange'](event);
    });
    expect(
      wrapper
        .find('#repeat-limit-input')
        .first()
        .props()['isDisabled']
    ).toEqual(false);
    await act(async () => {
      wrapper
        .find('#apply-button')
        .at(0)
        .simulate('click');
    });
    expect(handleJobRescheduleSpy).toHaveBeenCalled();
    wrapper.update();
  });
  it('test reschedule with null interval/limit', () => {
    const wrapper = getWrapper(
      <JobsRescheduleModal {...props2} />,
      'JobsRescheduleModal'
    );
    expect(wrapper).toMatchSnapshot();
  });
});
