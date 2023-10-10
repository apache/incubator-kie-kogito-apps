import { ProcessInstance } from '@kogito-apps/management-console-shared/dist/types';

export interface ProcessDetailsEnvelopeApi {
  processDetails__init(
    association: Association,
    initArgs: ProcessDetailsInitArgs
  );
}

export interface Association {
  origin: string;
  envelopeServerId: string;
}

export interface DiagramPreviewSize {
  width: number;
  height: number;
}

export interface ProcessDetailsInitArgs {
  processInstance: ProcessInstance;
  omittedProcessTimelineEvents?: string[];
  diagramPreviewSize?: DiagramPreviewSize;
  showSwfDiagram: boolean;
  isStunnerEnabled?: boolean;
  singularProcessLabel: string;
  pluralProcessLabel: string;
}
