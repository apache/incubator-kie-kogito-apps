import { CloudEventRequest } from '@kogito-apps/cloud-event-form';
import { triggerCloudEvent, triggerStartCloudEvent } from '../apis';

export interface CloudEventFormGatewayApi {
  triggerStartCloudEvent(event: CloudEventRequest): Promise<string>;
  triggerCloudEvent(event: CloudEventRequest): Promise<any>;
}

export class CloudEventFormGatewayApiImpl implements CloudEventFormGatewayApi {
  constructor(private readonly baseUrl: string) {}

  async triggerStartCloudEvent(event: CloudEventRequest): Promise<string> {
    const response = await triggerStartCloudEvent(event, this.baseUrl);
    return response;
  }

  triggerCloudEvent(event: CloudEventRequest): Promise<any> {
    return triggerCloudEvent(event, this.baseUrl);
  }
}
