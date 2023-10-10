import { render } from '@testing-library/react';
import React from 'react';
import { EmbeddedCloudEventForm } from '../EmbeddedCloudEventForm';
import { MockedCloudEventFormDriver } from './utils/Mocks';

jest.mock('../../envelope/components/CloudEventForm/CloudEventForm');

describe('EmbeddedCloudEventForm tests', () => {
  it('Snapshot', () => {
    const props = {
      targetOrigin: 'origin',
      driver: new MockedCloudEventFormDriver()
    };

    const container = render(<EmbeddedCloudEventForm {...props} />).container;

    expect(container).toMatchSnapshot();

    const contentDiv = container.querySelector('div');

    expect(contentDiv).toBeTruthy();
  });

  it('Snapshot - isNewInstanceEvent', () => {
    const props = {
      targetOrigin: 'origin',
      driver: new MockedCloudEventFormDriver(),
      isNewInstanceEvent: true
    };

    const container = render(<EmbeddedCloudEventForm {...props} />).container;

    expect(container).toMatchSnapshot();

    const contentDiv = container.querySelector('div');

    expect(contentDiv).toBeTruthy();
  });

  it('Snapshot - defaultValue', () => {
    const props = {
      targetOrigin: 'origin',
      driver: new MockedCloudEventFormDriver(),
      defaultValues: {
        cloudEventSource: '/local/source',
        instanceId: '1234'
      }
    };

    const container = render(<EmbeddedCloudEventForm {...props} />).container;

    expect(container).toMatchSnapshot();

    const contentDiv = container.querySelector('div');

    expect(contentDiv).toBeTruthy();
  });
});
