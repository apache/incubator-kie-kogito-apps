import { Envelope, EnvelopeApiFactory } from '@kogito-tooling/envelope';
import {
  RuntimeToolsDevUIEnvelopeApi,
  RuntimeToolsDevUIChannelApi
} from '../api';
import { RuntimeToolsDevUIEnvelopeViewApi } from './RuntimeToolsDevUIEnvelopeViewApi';
import { RuntimeToolsDevUIEnvelopeView } from './RuntimeToolsDevUIEnvelopeView';
import { RuntimeToolsDevUIEnvelopeContextType } from './RuntimeToolsDevUIEnvelopeContext';

export class RuntimeToolsDevUIEnvelope {
  constructor(
    private readonly envelope: Envelope<
      RuntimeToolsDevUIEnvelopeApi,
      RuntimeToolsDevUIChannelApi,
      RuntimeToolsDevUIEnvelopeViewApi,
      RuntimeToolsDevUIEnvelopeContextType
    >,
    private readonly envelopeApiFactory: EnvelopeApiFactory<
      RuntimeToolsDevUIEnvelopeApi,
      RuntimeToolsDevUIChannelApi,
      RuntimeToolsDevUIEnvelopeViewApi,
      RuntimeToolsDevUIEnvelopeContextType
    >,
    private readonly context: RuntimeToolsDevUIEnvelopeContextType = {
      channelApi: envelope.channelApi
    }
  ) {}

  public start(container: HTMLElement) {
    return this.envelope.start(
      () => Promise.resolve(() => new RuntimeToolsDevUIEnvelopeView()),
      this.context,
      this.envelopeApiFactory
    );
  }
}
