import React from 'react';
import { EmbeddedProcessDefinitionList } from '../EmbeddedProcessDefinitionList';
import { MockedProcessDefinitionListDriver } from './utils/Mocks';
import { render } from '@testing-library/react';

describe('EmbeddedProcessDefinitionList tests', () => {
  it('Snapshot', () => {
    const props = {
      targetOrigin: 'origin',
      envelopePath: 'path',
      driver: new MockedProcessDefinitionListDriver(),
      singularProcessLabel: 'Workflow'
    };

    const container = render(
      <EmbeddedProcessDefinitionList {...props} />
    ).container;

    expect(container).toMatchSnapshot();

    const contentDiv = container.querySelector('div');

    expect(contentDiv).toBeTruthy();
  });
});
