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
  // private renderView(container: HTMLElement) {
  //     const editorEnvelopeViewRef = React.createRef<RuntimeToolsDevUIEnvelopeViewApi>();

  //     const app = (
  //       <RuntimeToolsDevUIEnvelopeContext.Provider value={this.context}>
  //             {({ setLocale }) => <RuntimeToolsDevUIEView ref={editorEnvelopeViewRef} setLocale={setLocale} />}
  //       </RuntimeToolsDevUIEnvelopeContext.Provider>
  //     );

  //     return new Promise<() => RuntimeToolsDevUIEnvelopeViewApi>((res) => {
  //       setTimeout(() => {
  //         ReactDOM.render(app, container, () => {
  //           res(() => editorEnvelopeViewRef.current!);
  //         });
  //       }, 0);
  //     });
  //   }

  public start(container: HTMLElement) {
    return this.envelope.start(
      () => Promise.resolve(() => new RuntimeToolsDevUIEnvelopeView()),
      this.context,
      this.envelopeApiFactory
    );
  }
}
