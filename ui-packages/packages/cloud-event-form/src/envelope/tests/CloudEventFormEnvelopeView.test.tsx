import React from 'react';
import { render } from '@testing-library/react';
import { act } from 'react-dom/test-utils';
import CloudEventFormEnvelopeView, {
  CloudEventFormEnvelopeViewApi
} from '../CloudEventFormEnvelopeView';
import { MockedMessageBusClientApi } from './mocks/Mocks';

describe('CloudEventFormEnvelopeView tests', () => {
  beforeAll(() => {
    Object.defineProperty(window, 'matchMedia', {
      writable: true,
      value: jest.fn().mockImplementation((query) => ({
        matches: false,
        media: query,
        onchange: null,
        addEventListener: jest.fn(),
        removeEventListener: jest.fn(),
        dispatchEvent: jest.fn()
      }))
    });
  });
  it('Snapshot', () => {
    const channelApi = new MockedMessageBusClientApi();

    const forwardRef = React.createRef<CloudEventFormEnvelopeViewApi>();

    const container = render(
      <CloudEventFormEnvelopeView channelApi={channelApi} ref={forwardRef} />
    ).container;

    expect(container).toMatchSnapshot();

    act(() => {
      if (forwardRef.current) {
        forwardRef.current.initialize({
          isNewInstanceEvent: true,
          defaultValues: {
            cloudEventSource: '/local/test',
            instanceId: '1234'
          }
        });
      }
    });

    const checkWorkflowForm = container.querySelector(
      '[data-ouia-component-type="workflow-form"]'
    );
    expect(checkWorkflowForm).toBeTruthy();
  });
});
