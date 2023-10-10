export const KOGITO_PROCESS_REFERENCE_ID = 'kogitoprocrefid';
export const KOGITO_BUSINESS_KEY = 'kogitobusinesskey';

export enum CloudEventMethod {
  POST = 'POST',
  PUT = 'PUT'
}

export interface CloudEventRequest {
  endpoint: string;
  method: CloudEventMethod;

  headers: CloudEventHeaders;
  data: string;
}

export interface CloudEventHeaders {
  type: string; // Type of the cloud event
  source: string; // Source of the cloud event

  extensions: Record<string, string>;
}

export interface CloudEventFormChannelApi {
  cloudEventForm__triggerCloudEvent(event: CloudEventRequest): Promise<void>;
}
