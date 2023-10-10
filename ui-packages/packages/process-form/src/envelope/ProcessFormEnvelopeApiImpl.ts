import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import { ProcessFormEnvelopeViewApi } from './ProcessFormEnvelopeView';
import {
  Association,
  ProcessFormChannelApi,
  ProcessFormEnvelopeApi
} from '../api';
import { ProcessFormEnvelopeContext } from './ProcessFormEnvelopeContext';

/**
 * Implementation of the ProcessFormEnvelopeApi
 */
export class ProcessFormEnvelopeApiImpl implements ProcessFormEnvelopeApi {
  private view: () => ProcessFormEnvelopeViewApi;
  private capturedInitRequestYet = false;
  constructor(
    private readonly args: EnvelopeApiFactoryArgs<
      ProcessFormEnvelopeApi,
      ProcessFormChannelApi,
      ProcessFormEnvelopeViewApi,
      ProcessFormEnvelopeContext
    >
  ) {}

  private hasCapturedInitRequestYet() {
    return this.capturedInitRequestYet;
  }

  private ackCapturedInitRequest() {
    this.capturedInitRequestYet = true;
  }

  processForm__init = async (
    association: Association,
    processDefinition: any
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
    this.view().initialize(processDefinition);
  };
}
