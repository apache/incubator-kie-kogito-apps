import React from 'react';
import { act } from 'react-dom/test-utils';
import { render } from '@testing-library/react';
import { MockedMessageBusClientApi } from './mocks/Mocks';
import ProcessDefinitionListEnvelopeView, {
  ProcessDefinitionListEnvelopeViewApi
} from '../ProcessDefinitionListEnvelopeView';

describe('ProcessDefinitionListEnvelopeView tests', () => {
  it('Snapshot', () => {
    const channelApi = new MockedMessageBusClientApi();

    const forwardRef = React.createRef<ProcessDefinitionListEnvelopeViewApi>();

    const container = render(
      <ProcessDefinitionListEnvelopeView
        channelApi={channelApi}
        ref={forwardRef}
      />
    ).container;

    expect(container).toMatchSnapshot();

    act(() => {
      if (forwardRef.current) {
        forwardRef.current.initialize({ singularProcessLabel: 'Workflow' });
      }
    });

    const processDefinitionList = container.querySelector(
      '[data-ouia-component-type="process-definition-list"]'
    );

    expect(processDefinitionList).toBeTruthy();
    const checkIsEnvelopeConnectedToChannel = container.querySelector('h3');

    expect(checkIsEnvelopeConnectedToChannel?.textContent).toEqual(
      'Loading  definitions...'
    );
  });
});
