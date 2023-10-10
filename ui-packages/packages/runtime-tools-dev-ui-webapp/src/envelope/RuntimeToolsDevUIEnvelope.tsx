import { Envelope, EnvelopeApiFactory } from '@kie-tools-core/envelope';
import {
  RuntimeToolsDevUIChannelApi,
  RuntimeToolsDevUIEnvelopeApi
} from '../api';
import { RuntimeToolsDevUIEnvelopeViewApi } from './RuntimeToolsDevUIEnvelopeViewApi';
import { RuntimeToolsDevUIEnvelopeView } from './RuntimeToolsDevUIEnvelopeView';
import {
  RuntimeToolsDevUIEnvelopeContext,
  RuntimeToolsDevUIEnvelopeContextType
} from './RuntimeToolsDevUIEnvelopeContext';
import ReactDOM from 'react-dom';
import React from 'react';

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

  public start(container: HTMLElement): any {
    return this.envelope.start(
      () => this.renderView(container),
      this.context,
      this.envelopeApiFactory
    );
  }

  private renderView(container: HTMLElement) {
    const runtimeToolsDevUIEnvelopeViewRef =
      React.createRef<RuntimeToolsDevUIEnvelopeViewApi>();

    const app = () => {
      return (
        <RuntimeToolsDevUIEnvelopeContext.Provider value={this.context}>
          <RuntimeToolsDevUIEnvelopeView
            ref={runtimeToolsDevUIEnvelopeViewRef}
          />
        </RuntimeToolsDevUIEnvelopeContext.Provider>
      );
    };

    return new Promise<() => RuntimeToolsDevUIEnvelopeViewApi>((res) => {
      setTimeout(() => {
        ReactDOM.render(app(), container, () => {
          res(() => runtimeToolsDevUIEnvelopeViewRef.current!);
        });
      }, 0);
    });
  }
}
