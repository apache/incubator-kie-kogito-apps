import React from 'react';
import { act } from 'react-dom/test-utils';
import { render, screen } from '@testing-library/react';
import { MockedMessageBusClientApi } from './mocks/Mocks';
import CustomDashboardList from '../components/CustomDashboardList/CustomDashboardList';
import CustomDashboardListEnvelopeView, {
  CustomDashboardListEnvelopeViewApi
} from '../CustomDashboardListEnvelopeView';

describe('CustomDashboardListEnvelopeView tests', () => {
  jest.mock('../components/CustomDashboardList/CustomDashboardList');
  it('Snapshot', async () => {
    const channelApi = new MockedMessageBusClientApi();

    const forwardRef = React.createRef<CustomDashboardListEnvelopeViewApi>();
    let container;
    await act(async () => {
      container = render(
        <CustomDashboardListEnvelopeView
          channelApi={channelApi}
          ref={forwardRef}
        />
      ).container;
    });

    expect(container).toMatchSnapshot();

    act(() => {
      if (forwardRef.current) {
        forwardRef.current.initialize();
      }
    });

    const CustomDashboardListEnvelopeViewCheck =
      container.getElementsByClassName('pf-m-toggle-group-container');
    expect(CustomDashboardListEnvelopeViewCheck).toMatchSnapshot();

    const dashboardList = screen.getByText('Loading Dashboard...');

    expect(dashboardList).toBeTruthy();

    const listTable = container.querySelector('table');

    expect(listTable).toBeTruthy();
  });
});
