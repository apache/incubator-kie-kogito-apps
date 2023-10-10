import { CloudEventFormDriver } from '../../../api';

export const MockedCloudEventFormDriver = jest.fn<CloudEventFormDriver, []>(
  () => ({
    triggerCloudEvent: jest.fn()
  })
);
