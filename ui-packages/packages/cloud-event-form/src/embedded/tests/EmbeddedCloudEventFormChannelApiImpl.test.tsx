import {
  CloudEventFormChannelApi,
  CloudEventFormDriver,
  CloudEventMethod,
  CloudEventRequest
} from '../../api';
import { MockedCloudEventFormDriver } from './utils/Mocks';
import { EmbeddedCloudEventFormChannelApiImpl } from '../EmbeddedCloudEventFormChannelApiImpl';

let driver: CloudEventFormDriver;
let api: CloudEventFormChannelApi;

describe('EmbeddedCloudEventFormChannelApiImpl tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    driver = new MockedCloudEventFormDriver();
    api = new EmbeddedCloudEventFormChannelApiImpl(driver);
  });

  it('cloudEventForm__triggerCloudEvent', () => {
    const eventRequest: CloudEventRequest = {
      method: CloudEventMethod.POST,
      endpoint: '/',
      headers: {
        type: 'test',
        source: 'test',
        extensions: {}
      },
      data: ''
    };

    api.cloudEventForm__triggerCloudEvent(eventRequest);
    expect(driver.triggerCloudEvent).toHaveBeenCalledWith(eventRequest);
  });
});
