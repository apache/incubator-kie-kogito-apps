import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { EnvelopeBus } from '@kie-tools-core/envelope-bus/dist/api';
import { Envelope, EnvelopeIFrameConfig } from '@kie-tools-core/envelope';
import { FormDisplayerChannelApi, FormDisplayerEnvelopeApi } from '../api';
import { FormDisplayerEnvelopeContext } from './FormDisplayerEnvelopeContext';
import {
  FormDisplayerEnvelopeView,
  FormDisplayerEnvelopeViewApi
} from './FormDisplayerEnvelopeView';
import { FormDisplayerEnvelopeApiImpl } from './FormDisplayerEnvelopeApiImpl';

export function init(args: {
  container: HTMLElement;
  bus: EnvelopeBus;
  config: EnvelopeIFrameConfig;
}) {
  const envelope = new Envelope<
    FormDisplayerEnvelopeApi,
    FormDisplayerChannelApi,
    FormDisplayerEnvelopeViewApi,
    FormDisplayerEnvelopeContext
  >(args.bus, args.config);

  const envelopeViewDelegate = async () => {
    const ref = React.createRef<FormDisplayerEnvelopeViewApi>();
    return new Promise<() => FormDisplayerEnvelopeViewApi>((res) => {
      ReactDOM.render(
        <FormDisplayerEnvelopeView
          ref={ref}
          channelApi={envelope.channelApi}
        />,
        args.container,
        () => res(() => ref.current!)
      );
    });
  };

  const context: FormDisplayerEnvelopeContext = {};
  return envelope.start(envelopeViewDelegate, context, {
    create: (apiFactoryArgs) => {
      return new FormDisplayerEnvelopeApiImpl(apiFactoryArgs);
    }
  });
}
