export interface CloudEventFormEnvelopeApi {
  cloudEventForm__init(
    association: Association,
    args: CloudEventFormInitArgs
  ): Promise<void>;
}

export interface Association {
  origin: string;
  envelopeServerId: string;
}

export type CloudEventFormDefaultValues = {
  cloudEventSource?: string;
  instanceId?: string;
};

export type CloudEventFormInitArgs = {
  isNewInstanceEvent: boolean;
  defaultValues?: CloudEventFormDefaultValues;
};
