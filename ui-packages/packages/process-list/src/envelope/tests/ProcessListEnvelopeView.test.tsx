import React from 'react';
import { act } from 'react-dom/test-utils';
import { render } from '@testing-library/react';
import { MockedMessageBusClientApi } from './mocks/Mocks';
import ProcessListEnvelopeView, {
  ProcessListEnvelopeViewApi
} from '../ProcessListEnvelopeView';

describe('ProcessListEnvelopeView tests', () => {
  it('Snapshot', async () => {
    const channelApi = new MockedMessageBusClientApi();

    const forwardRef = React.createRef<ProcessListEnvelopeViewApi>();

    const container = render(
      <ProcessListEnvelopeView channelApi={channelApi} ref={forwardRef} />
    );
    await act(async () => {
      if (forwardRef.current) {
        forwardRef.current.initialize({
          initialState: {
            filters: {
              status: []
            },
            sortBy: {}
          },
          singularProcessLabel: 'Workflow',
          pluralProcessLabel: 'Workflows',
          isWorkflow: true
        });
      }
    });
    expect(container).toMatchSnapshot();
  });
});
