import { EnvelopeApiFactoryArgs } from '@kogito-tooling/envelope';
import {
  RuntimeToolsDevUIChannelApi,
  RuntimeToolsDevUIEnvelopeApi
} from '../api';
import { RuntimeToolsDevUIEnvelopeContextType } from './RuntimeToolsDevUIEnvelopeContext';
import { RuntimeToolsDevUIEnvelopeViewApi } from './RuntimeToolsDevUIEnvelopeViewApi';

export class RuntimeToolsDevUIEnvelopeApiImpl
  implements RuntimeToolsDevUIEnvelopeApi {
  constructor(
    private readonly args: EnvelopeApiFactoryArgs<
      RuntimeToolsDevUIEnvelopeApi,
      RuntimeToolsDevUIChannelApi,
      RuntimeToolsDevUIEnvelopeViewApi,
      RuntimeToolsDevUIEnvelopeContextType
    >
  ) {}
  public runtimeToolsDevUI_initRequest = (
    association: any,
    initArgs: any
  ): Promise<void> => {
    this.args.envelopeBusController.associate(
      association.origin,
      association.envelopeServerId
    );
    return Promise.resolve();
  };
}
