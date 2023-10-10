import { ProcessDefinition } from '@kogito-apps/process-definition-list';
import { getProcessDefinitionList } from '../apis';

export interface ProcessDefinitionListGatewayApi {
  getProcessDefinitionFilter: () => Promise<string[]>;
  setProcessDefinitionFilter: (filter: string[]) => Promise<void>;
  getProcessDefinitionsQuery: () => Promise<ProcessDefinition[]>;
  openProcessForm: (processDefinition: ProcessDefinition) => Promise<void>;
  openTriggerCloudEvent: () => void;
  onOpenProcessFormListen: (
    listener: OnOpenProcessFormListener
  ) => UnSubscribeHandler;
  onOpenTriggerCloudEventListen: (
    listener: OnOpenTriggerCloudEventListener
  ) => UnSubscribeHandler;
}

export interface OnOpenProcessFormListener {
  onOpen: (processDefinition: ProcessDefinition) => void;
}

export interface OnOpenTriggerCloudEventListener {
  onOpen: () => void;
}

export interface UnSubscribeHandler {
  unSubscribe: () => void;
}

export class ProcessDefinitionListGatewayApiImpl
  implements ProcessDefinitionListGatewayApi
{
  private readonly onOpenProcessListeners: OnOpenProcessFormListener[] = [];
  private readonly onOpenTriggerCloudEventListeners: OnOpenTriggerCloudEventListener[] =
    [];

  private readonly devUIUrl: string;
  private readonly openApiPath: string;
  private processDefinitonFilter: string[] = [];

  constructor(url: string, path: string) {
    this.devUIUrl = url;
    this.openApiPath = path;
  }

  getProcessDefinitionFilter(): Promise<string[]> {
    return Promise.resolve(this.processDefinitonFilter);
  }

  setProcessDefinitionFilter(filter: string[]): Promise<void> {
    this.processDefinitonFilter = filter;
    return Promise.resolve();
  }

  openProcessForm(processDefinition: ProcessDefinition): Promise<void> {
    this.onOpenProcessListeners.forEach((listener) =>
      listener.onOpen(processDefinition)
    );
    return Promise.resolve();
  }

  onOpenProcessFormListen(
    listener: OnOpenProcessFormListener
  ): UnSubscribeHandler {
    this.onOpenProcessListeners.push(listener);

    const unSubscribe = () => {
      const index = this.onOpenProcessListeners.indexOf(listener);
      if (index > -1) {
        this.onOpenProcessListeners.splice(index, 1);
      }
    };

    return {
      unSubscribe
    };
  }

  onOpenTriggerCloudEventListen(
    listener: OnOpenTriggerCloudEventListener
  ): UnSubscribeHandler {
    this.onOpenTriggerCloudEventListeners.push(listener);

    const unSubscribe = () => {
      const index = this.onOpenTriggerCloudEventListeners.indexOf(listener);
      if (index > -1) {
        this.onOpenTriggerCloudEventListeners.splice(index, 1);
      }
    };

    return {
      unSubscribe
    };
  }

  getProcessDefinitionsQuery(): Promise<ProcessDefinition[]> {
    return getProcessDefinitionList(this.devUIUrl, this.openApiPath);
  }

  openTriggerCloudEvent(): void {
    this.onOpenTriggerCloudEventListeners.forEach((listener) =>
      listener.onOpen()
    );
  }
}
