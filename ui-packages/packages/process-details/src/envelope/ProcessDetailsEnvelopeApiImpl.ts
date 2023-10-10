import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import { ProcessDetailsEnvelopeViewApi } from './ProcessDetailsEnvelopeView';
import {
  ProcessDetailsInitArgs,
  Association,
  ProcessDetailsChannelApi,
  ProcessDetailsEnvelopeApi
} from '../api';
import { ProcessDetailsEnvelopeContext } from './ProcessDetailsEnvelopeContext';
export class ProcessDetailsEnvelopeApiImpl
  implements ProcessDetailsEnvelopeApi
{
  private capturedInitRequestYet = false;
  private view: () => ProcessDetailsEnvelopeViewApi;
  constructor(
    private readonly args: EnvelopeApiFactoryArgs<
      ProcessDetailsEnvelopeApi,
      ProcessDetailsChannelApi,
      ProcessDetailsEnvelopeViewApi,
      ProcessDetailsEnvelopeContext
    >
  ) {}

  private hasCapturedInitRequestYet() {
    return this.capturedInitRequestYet;
  }

  private ackCapturedInitRequest() {
    this.capturedInitRequestYet = true;
  }

  processDetails__init = async (
    association: Association,
    initArgs: ProcessDetailsInitArgs
  ): Promise<void> => {
    this.args.envelopeClient.associate(
      association.origin,
      association.envelopeServerId
    );
    /* istanbul ignore if*/
    if (this.hasCapturedInitRequestYet()) {
      return;
    }

    this.ackCapturedInitRequest();
    this.view = await this.args.viewDelegate();
    this.view().initialize(initArgs);
  };
}
