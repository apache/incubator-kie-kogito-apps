import { EnvelopeApiFactoryArgs } from '@kogito-tooling/envelope';
import {
  RuntimeToolsDevUIChannelApi,
  RuntimeToolsDevUIEnvelopeApi
} from '../api';
import { RuntimeToolsDevUIEnvelopeContextType } from './RuntimeToolsDevUIEnvelopeContext';
import { RuntimeToolsDevUIEnvelopeViewApi } from './RuntimeToolsDevUIEnvelopeViewApi';

export class RuntimeToolsDevUIEnvelopeApiImpl
  implements RuntimeToolsDevUIEnvelopeApi {
  private capturedInitRequestYet = false;

  constructor(
    private readonly args: EnvelopeApiFactoryArgs<
      RuntimeToolsDevUIEnvelopeApi,
      RuntimeToolsDevUIChannelApi,
      RuntimeToolsDevUIEnvelopeViewApi,
      RuntimeToolsDevUIEnvelopeContextType
    >
  ) {}

  private hasCapturedInitRequestYet() {
    return this.capturedInitRequestYet;
  }

  private ackCapturedInitRequest() {
    this.capturedInitRequestYet = true;
  }

  public runtimeToolsDevUI_initRequest = (
    association: any,
    initArgs: any
  ): Promise<void> => {
    this.args.envelopeBusController.associate(
      association.origin,
      association.envelopeServerId
    );

    if (this.hasCapturedInitRequestYet()) {
      return;
    }

    this.ackCapturedInitRequest();

    this.args.view().setDataIndexUrl(initArgs.dataIndexUrl);

    return new Promise<void>(res => res());
  };
}
