import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import {
  CloudEventRequest,
  CloudEventFormChannelApi,
  CloudEventFormDriver
} from '../api';

export class CloudEventFormEnvelopeViewDriver implements CloudEventFormDriver {
  constructor(
    private readonly channelApi: MessageBusClientApi<CloudEventFormChannelApi>
  ) {}

  triggerCloudEvent(event: CloudEventRequest): Promise<void> {
    return this.channelApi.requests.cloudEventForm__triggerCloudEvent(event);
  }
}
