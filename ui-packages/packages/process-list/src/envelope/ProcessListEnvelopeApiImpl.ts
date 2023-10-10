import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import { ProcessListEnvelopeViewApi } from './ProcessListEnvelopeView';
import {
  Association,
  ProcessListChannelApi,
  ProcessListEnvelopeApi,
  ProcessListInitArgs
} from '../api';
import { ProcessListEnvelopeContext } from './ProcessListEnvelopeContext';
export class ProcessListEnvelopeApiImpl implements ProcessListEnvelopeApi {
  private view: () => ProcessListEnvelopeViewApi;
  private capturedInitRequestYet = false;

  constructor(
    private readonly args: EnvelopeApiFactoryArgs<
      ProcessListEnvelopeApi,
      ProcessListChannelApi,
      ProcessListEnvelopeViewApi,
      ProcessListEnvelopeContext
    >
  ) {}

  private hasCapturedInitRequestYet() {
    return this.capturedInitRequestYet;
  }

  private ackCapturedInitRequest() {
    this.capturedInitRequestYet = true;
  }

  public processList__init = async (
    association: Association,
    initArgs: ProcessListInitArgs
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
