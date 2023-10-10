import React, { useContext } from 'react';
import { RuntimeToolsDevUIChannelApi } from '../api/RuntimeToolsDevUIChannelApi';
import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';

export interface RuntimeToolsDevUIEnvelopeContextType {
  channelApi: MessageBusClientApi<RuntimeToolsDevUIChannelApi>;
}

export const RuntimeToolsDevUIEnvelopeContext =
  React.createContext<RuntimeToolsDevUIEnvelopeContextType>(
    {} as RuntimeToolsDevUIEnvelopeContextType
  );

export function useRuntimeToolsDevUIEnvelopeContext() {
  return useContext(
    RuntimeToolsDevUIEnvelopeContext
  ) as RuntimeToolsDevUIEnvelopeContextType;
}
