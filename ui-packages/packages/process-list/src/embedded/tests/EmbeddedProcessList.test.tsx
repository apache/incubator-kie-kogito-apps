import React from 'react';
import { EmbeddedProcessList } from '../EmbeddedProcessList';
import { MockedProcessListDriver } from './utils/Mocks';
import { render } from '@testing-library/react';
import {
  ProcessInstanceState,
  OrderBy
} from '@kogito-apps/management-console-shared/dist/types';

describe('EmbeddedProcessList tests', () => {
  it('Snapshot', () => {
    const props = {
      targetOrigin: 'origin',
      envelopePath: 'path',
      driver: new MockedProcessListDriver(),
      initialState: {
        filters: {
          status: [ProcessInstanceState.Active],
          businessKey: []
        },
        sortBy: {
          lastUpdate: OrderBy.DESC
        }
      },
      singularProcessLabel: 'Workflow',
      pluralProcessLabel: 'Workflows',
      isWorkflow: true
    };

    const container = render(<EmbeddedProcessList {...props} />).container;

    expect(container).toMatchSnapshot();
    const contentDiv = container.querySelector('div');
    expect(contentDiv).toBeTruthy();
  });
});
