import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import { FormsListEnvelopeViewApi } from './FormsListEnvelopeView';
import { Association, FormsListChannelApi, FormsListEnvelopeApi } from '../api';
import { FormsListEnvelopeContext } from './FormsListEnvelopeContext';

/**
 * Implementation of the FormsListEnvelopeApi
 */
export class FormsListEnvelopeApiImpl implements FormsListEnvelopeApi {
  private view: () => FormsListEnvelopeViewApi;
  private capturedInitRequestYet = false;
  constructor(
    private readonly args: EnvelopeApiFactoryArgs<
      FormsListEnvelopeApi,
      FormsListChannelApi,
      FormsListEnvelopeViewApi,
      FormsListEnvelopeContext
    >
  ) {}

  private hasCapturedInitRequestYet() {
    return this.capturedInitRequestYet;
  }

  private ackCapturedInitRequest() {
    this.capturedInitRequestYet = true;
  }

  formsList__init = async (association: Association): Promise<void> => {
    this.args.envelopeClient.associate(
      association.origin,
      association.envelopeServerId
    );

    if (this.hasCapturedInitRequestYet()) {
      return;
    }

    this.ackCapturedInitRequest();
    this.view = await this.args.viewDelegate();
    this.view().initialize();
  };
}
