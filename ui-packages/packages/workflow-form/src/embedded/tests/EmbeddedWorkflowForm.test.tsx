import { render } from '@testing-library/react';
import React from 'react';
import {
  EmbeddedWorkflowForm,
  EmbeddedWorkflowFormProps
} from '../EmbeddedWorkflowForm';
import { MockedWorkflowFormDriver } from './mocks/Mocks';

describe('EmbeddedWorkflowForm tests', () => {
  it('Snapshot', () => {
    const props: EmbeddedWorkflowFormProps = {
      driver: new MockedWorkflowFormDriver(),
      workflowDefinition: null,
      targetOrigin: 'origin'
    };

    const container = render(<EmbeddedWorkflowForm {...props} />).container;

    expect(container).toMatchSnapshot();

    const contentDiv = container.querySelector('div');

    expect(contentDiv).toBeTruthy();
  });
});
