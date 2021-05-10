import React, { useContext } from 'react';
import { RuntimeToolsDevUIChannelApi } from '../api/RuntimeToolsDevUIChannelApi';
import { MessageBusClientApi } from '@kogito-tooling/envelope-bus/dist/api';

export interface RuntimeToolsDevUIEnvelopeContextType {
  channelApi: MessageBusClientApi<RuntimeToolsDevUIChannelApi>;
}

export const RuntimeToolsDevUIEnvelopeContext = React.createContext<
  RuntimeToolsDevUIEnvelopeContextType
>({} as any);

export function useRuntimeToolsDevUIEnvelopeContext() {
  return useContext(
    RuntimeToolsDevUIEnvelopeContext
  ) as RuntimeToolsDevUIEnvelopeContextType;
}
