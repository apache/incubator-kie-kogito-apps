import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import { ProcessDefinitionListEnvelopeViewApi } from './ProcessDefinitionListEnvelopeView';
import {
  Association,
  ProcessDefinitionListChannelApi,
  ProcessDefinitionListEnvelopeApi,
  ProcessDefinitionListInitArgs
} from '../api';
import { ProcessDefinitionListEnvelopeContext } from './ProcessDefinitionListEnvelopeContext';

/**
 * Implementation of the ProcessDefinitionListEnvelopeApi
 */
export class ProcessDefinitionListEnvelopeApiImpl
  implements ProcessDefinitionListEnvelopeApi
{
  private view: () => ProcessDefinitionListEnvelopeViewApi;
  private capturedInitRequestYet = false;
  constructor(
    private readonly args: EnvelopeApiFactoryArgs<
      ProcessDefinitionListEnvelopeApi,
      ProcessDefinitionListChannelApi,
      ProcessDefinitionListEnvelopeViewApi,
      ProcessDefinitionListEnvelopeContext
    >
  ) {}

  private hasCapturedInitRequestYet() {
    return this.capturedInitRequestYet;
  }

  private ackCapturedInitRequest() {
    this.capturedInitRequestYet = true;
  }

  processDefinitionList__init = async (
    association: Association,
    initArgs: ProcessDefinitionListInitArgs
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
    this.view().initialize(initArgs);
  };
}
