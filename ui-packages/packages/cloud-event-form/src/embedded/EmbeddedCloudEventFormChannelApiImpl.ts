import {
  CloudEventRequest,
  CloudEventFormChannelApi,
  CloudEventFormDriver
} from '../api';

export class EmbeddedCloudEventFormChannelApiImpl
  implements CloudEventFormChannelApi
{
  constructor(private readonly driver: CloudEventFormDriver) {}

  cloudEventForm__triggerCloudEvent(event: CloudEventRequest): Promise<void> {
    return this.driver.triggerCloudEvent(event);
  }
}
