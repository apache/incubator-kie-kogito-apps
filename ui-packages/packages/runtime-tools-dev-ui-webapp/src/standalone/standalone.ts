import devUIEnvelopeIndex from '!!raw-loader!../../resources/iframe.html';
import { EnvelopeServer } from '@kie-tools-core/envelope-bus/dist/channel';
import {
  RuntimeToolsDevUIChannelApi,
  RuntimeToolsDevUIEnvelopeApi,
  User
} from '../api';
import { RuntimeToolsDevUIChannelApiImpl } from '../standalone/RuntimeToolsDevUIChannelApiImpl';
import { CustomLabels } from '../api/CustomLabels';
import { DiagramPreviewSize } from '@kogito-apps/process-details/dist/api';

export interface StandaloneDevUIApi {
  close: () => void;
}

export interface Consoles {
  open: (args: {
    container: Element;
    users: User[];
    dataIndexUrl: string;
    trustyServiceUrl: string;
    page: string;
    devUIUrl: string;
    openApiPath: string;
    origin?: string;
    availablePages?: string[];
    customLabels?: CustomLabels;
    omittedProcessTimelineEvents?: string[];
    diagramPreviewSize?: DiagramPreviewSize;
    isStunnerEnabled: boolean;
  }) => StandaloneDevUIApi;
}

const createEnvelopeServer = (
  iframe: HTMLIFrameElement,
  isDataIndexAvailable: boolean,
  isTracingEnabled: boolean,
  users: User[],
  dataIndexUrl: string,
  trustyServiceUrl: string,
  page: string,
  devUIUrl: string,
  openApiPath: string,
  customLabels: CustomLabels,
  isStunnerEnabled: boolean,
  diagramPreviewSize?: DiagramPreviewSize,
  origin?: string,
  availablePages?: string[],
  omittedProcessTimelineEvents?: string[]
) => {
  const defaultOrigin =
    window.location.protocol === 'file:' ? '*' : window.location.origin;
  return new EnvelopeServer<
    RuntimeToolsDevUIChannelApi,
    RuntimeToolsDevUIEnvelopeApi
  >(
    {
      postMessage: (message) =>
        iframe.contentWindow?.postMessage(message, origin ?? defaultOrigin)
    },
    origin ?? defaultOrigin,
    (self) => {
      return self.envelopeApi.requests.runtimeToolsDevUI_initRequest(
        {
          origin: self.origin,
          envelopeServerId: self.id
        },
        {
          isDataIndexAvailable,
          isTracingEnabled,
          users,
          dataIndexUrl,
          trustyServiceUrl,
          page,
          devUIUrl,
          openApiPath,
          customLabels,
          availablePages,
          omittedProcessTimelineEvents,
          isStunnerEnabled,
          diagramPreviewSize
        }
      );
    }
  );
};

declare global {
  interface Window {
    RuntimeToolsDevUI: Consoles;
  }
}

export const createDevUI = (
  envelopeServer: EnvelopeServer<
    RuntimeToolsDevUIChannelApi,
    RuntimeToolsDevUIEnvelopeApi
  >,
  listener: (message: MessageEvent) => void,
  iframe: HTMLIFrameElement
): any => {
  return {
    envelopeApi: envelopeServer.envelopeApi,
    close: () => {
      window.removeEventListener('message', listener);
      iframe.remove();
    }
  };
};

export function open(args: {
  container: Element;
  isDataIndexAvailable: boolean;
  isTracingEnabled: boolean;
  users: User[];
  dataIndexUrl: string;
  trustyServiceUrl: string;
  page: string;
  devUIUrl: string;
  openApiPath: string;
  origin?: string;
  availablePages?: string[];
  customLabels?: CustomLabels;
  omittedProcessTimelineEvents?: string[];
  isStunnerEnabled: boolean;
  diagramPreviewSize?: DiagramPreviewSize;
}): StandaloneDevUIApi {
  const iframe = document.createElement('iframe');
  iframe.srcdoc = devUIEnvelopeIndex; // index coming from webapp
  iframe.id = 'iframe';
  iframe.style.width = '100%';
  iframe.style.height = '100%';
  iframe.style.border = 'none';

  const envelopeServer = createEnvelopeServer(
    iframe,
    args.isDataIndexAvailable,
    args.isTracingEnabled,
    args.users,
    args.dataIndexUrl,
    args.trustyServiceUrl,
    args.page,
    args.devUIUrl,
    args.openApiPath,
    args.customLabels ?? {
      singularProcessLabel: 'Process',
      pluralProcessLabel: 'Processes'
    },
    args.isStunnerEnabled,
    args.diagramPreviewSize,
    args.origin,
    args.availablePages,
    args.omittedProcessTimelineEvents ?? []
  );
  const channelApi = new RuntimeToolsDevUIChannelApiImpl();
  const listener = (message: MessageEvent) => {
    envelopeServer.receive(message.data, channelApi);
  };
  window.addEventListener('message', listener);

  args.container.appendChild(iframe);
  envelopeServer.startInitPolling(channelApi);

  return createDevUI(envelopeServer, listener, iframe);
}

window.RuntimeToolsDevUI = { open };
