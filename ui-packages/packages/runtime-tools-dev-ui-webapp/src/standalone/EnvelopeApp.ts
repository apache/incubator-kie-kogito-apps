import { Envelope } from '@kie-tools-core/envelope';
import {
  RuntimeToolsDevUIEnvelopeApi,
  RuntimeToolsDevUIChannelApi
} from '../api';
import {
  RuntimeToolsDevUIEnvelopeViewApi,
  RuntimeToolsDevUIEnvelopeContextType,
  RuntimeToolsDevUIEnvelope,
  RuntimeToolsDevUIEnvelopeApiImpl
} from '../envelope';

const initEnvelope = () => {
  const container = document.getElementById('envelope-app')!;

  const bus = {
    postMessage: (message, targetOrigin, _) =>
      window.parent.postMessage(message, targetOrigin!, _)
  };
  const apiImplFactory = {
    create: (args) => new RuntimeToolsDevUIEnvelopeApiImpl(args)
  };

  const envelope = new Envelope<
    RuntimeToolsDevUIEnvelopeApi,
    RuntimeToolsDevUIChannelApi,
    RuntimeToolsDevUIEnvelopeViewApi,
    RuntimeToolsDevUIEnvelopeContextType
  >(bus);

  const runtimeToolsDevUIEnvelope = new RuntimeToolsDevUIEnvelope(
    envelope,
    apiImplFactory
  );

  runtimeToolsDevUIEnvelope.start(container);
};

// Envelope should be initialized only after page was loaded.
if (document.readyState !== 'loading') {
  initEnvelope();
} else {
  document.addEventListener('DOMContentLoaded', () => {
    initEnvelope();
  });
}
