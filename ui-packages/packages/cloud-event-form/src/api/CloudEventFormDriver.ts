import { CloudEventRequest } from './CloudEventFormChannelApi';

/**
 * Interface that defines a Driver for CloudEventForm views.
 */
export interface CloudEventFormDriver {
  triggerCloudEvent(event: CloudEventRequest): Promise<void>;
}
