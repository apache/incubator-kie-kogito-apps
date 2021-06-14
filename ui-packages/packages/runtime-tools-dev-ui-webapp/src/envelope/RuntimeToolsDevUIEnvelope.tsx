import { Envelope, EnvelopeApiFactory } from '@kogito-tooling/envelope';
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
import {
  //appRenderWithAxiosInterceptorConfig,
  UserContext
} from '../../../consoles-common';

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
      () => this.renderView(container),
      this.context,
      this.envelopeApiFactory
    );
  }

  private renderView(container: HTMLElement) {
    const runtimeToolsDevUIEnvelopeViewRef = React.createRef<
      RuntimeToolsDevUIEnvelopeViewApi
    >();

    const app = (userContext: UserContext) => {
      return (
        <RuntimeToolsDevUIEnvelopeContext.Provider value={this.context}>
          <RuntimeToolsDevUIEnvelopeView
            ref={runtimeToolsDevUIEnvelopeViewRef}
            userContext={userContext}
          />
        </RuntimeToolsDevUIEnvelopeContext.Provider>
      );
    };

    return new Promise<() => RuntimeToolsDevUIEnvelopeViewApi>(res => {
      setTimeout(() => {
        //appRenderWithAxiosInterceptorConfig((userContext: UserContext) => {
        ReactDOM.render(
          app({
            getCurrentUser: () => {
              return { id: 'user', groups: [] };
            }
          }),
          container,
          () => {
            res(() => runtimeToolsDevUIEnvelopeViewRef.current!);
          }
        );
        //});
      }, 0);
    });
  }
}
