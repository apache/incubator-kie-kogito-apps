import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import {
  Association,
  CloudEventFormChannelApi,
  CloudEventFormEnvelopeApi,
  CloudEventFormInitArgs
} from '../api';
import { CloudEventFormEnvelopeViewApi } from './CloudEventFormEnvelopeView';

/**
 * Implementation of the CloudEventFormEnvelopeApi
 */
export class CloudEventFormEnvelopeApiImpl
  implements CloudEventFormEnvelopeApi
{
  private view: () => CloudEventFormEnvelopeViewApi;
  private capturedInitRequestYet = false;

  constructor(
    private readonly args: EnvelopeApiFactoryArgs<
      CloudEventFormEnvelopeApi,
      CloudEventFormChannelApi,
      CloudEventFormEnvelopeViewApi,
      undefined
    >
  ) {}

  private hasCapturedInitRequestYet() {
    return this.capturedInitRequestYet;
  }

  private ackCapturedInitRequest() {
    this.capturedInitRequestYet = true;
  }

  cloudEventForm__init = async (
    association: Association,
    args: CloudEventFormInitArgs
  ): Promise<void> => {
    this.args.envelopeClient.associate(
      association.origin,
      association.envelopeServerId
    );

    if (this.hasCapturedInitRequestYet()) {
      return;
    }
    this.ackCapturedInitRequest();
    this.view = await this.args.viewDelegate();
    this.view().initialize(args);
  };
}
