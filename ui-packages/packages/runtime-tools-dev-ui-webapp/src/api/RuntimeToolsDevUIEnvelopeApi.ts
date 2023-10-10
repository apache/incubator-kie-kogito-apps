import { CustomLabels } from './CustomLabels';
import { DiagramPreviewSize } from '@kogito-apps/process-details/dist/api';

export interface RuntimeToolsDevUIEnvelopeApi {
  runtimeToolsDevUI_initRequest(
    association: Association,
    initArgs: RuntimeToolsDevUIInitArgs
  ): Promise<void>;
}

export interface Association {
  origin: string;
  envelopeServerId: string;
}

export interface User {
  id: string;
  groups: string[];
}

export interface RuntimeToolsDevUIInitArgs {
  users?: User[];
  dataIndexUrl: string;
  trustyServiceUrl?: string;
  page: string;
  devUIUrl: string;
  openApiPath: string;
  isDataIndexAvailable: boolean;
  isTracingEnabled?: boolean;
  availablePages?: string[];
  customLabels: CustomLabels;
  omittedProcessTimelineEvents?: string[];
  diagramPreviewSize?: DiagramPreviewSize;
  isStunnerEnabled: boolean;
}
