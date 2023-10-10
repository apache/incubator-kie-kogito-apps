import React from 'react';
import { act } from 'react-dom/test-utils';
import { render } from '@testing-library/react';
import { MockedMessageBusClientApi } from './mocks/Mocks';
import CustomDashboardViewEnvelopeView, {
  CustomDashboardViewEnvelopeViewApi
} from '../CustomDashboardViewEnvelopeView';

describe('CustomDashboardViewEnvelopeView tests', () => {
  it('Snapshot', async () => {
    const channelApi = new MockedMessageBusClientApi();
    (
      channelApi.requests
        .customDashboardView__getCustomDashboardView as jest.Mock
    ).mockResolvedValue('its a yml file');
    const forwardRef = React.createRef<CustomDashboardViewEnvelopeViewApi>();
    let container;
    await act(async () => {
      container = render(
        <CustomDashboardViewEnvelopeView
          channelApi={channelApi}
          ref={forwardRef}
        />
      ).container;
    });
    expect(container).toMatchSnapshot();

    act(() => {
      if (forwardRef.current) {
        forwardRef.current.initialize('name', 'targetOrigin');
      }
    });

    const checkIframe = container.querySelector('iframe');

    expect(checkIframe).toMatchSnapshot();
    const iframeWrapper = container.querySelector('iframe');

    expect(iframeWrapper?.getAttribute('src')).toEqual(
      'resources/webapp/custom-dashboard-view/dashbuilder/index.html'
    );
  });
});
