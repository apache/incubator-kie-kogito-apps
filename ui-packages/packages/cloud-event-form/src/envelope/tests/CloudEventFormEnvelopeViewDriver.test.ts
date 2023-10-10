import {
  MessageBusClientApi,
  RequestPropertyNames
} from '@kie-tools-core/envelope-bus/dist/api';
import { MockedMessageBusClientApi } from './mocks/Mocks';
import {
  CloudEventFormChannelApi,
  CloudEventMethod,
  CloudEventRequest
} from '../../api';
import { CloudEventFormEnvelopeViewDriver } from '../CloudEventFormEnvelopeViewDriver';

let channelApi: MessageBusClientApi<CloudEventFormChannelApi>;
let requests: Pick<
  CloudEventFormChannelApi,
  RequestPropertyNames<CloudEventFormChannelApi>
>;
let driver: CloudEventFormEnvelopeViewDriver;

describe('CloudEventFormEnvelopeViewDriver tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    channelApi = new MockedMessageBusClientApi();
    requests = channelApi.requests;
    driver = new CloudEventFormEnvelopeViewDriver(channelApi);
  });

  describe('Requests', () => {
    it('trigger cloud event', () => {
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
      driver.triggerCloudEvent(eventRequest);

      expect(requests.cloudEventForm__triggerCloudEvent).toHaveBeenCalledWith(
        eventRequest
      );
    });
  });
});
