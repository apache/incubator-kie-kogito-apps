import { User } from '../api';
import { CustomLabels } from '../api/CustomLabels';
import { DiagramPreviewSize } from '@kogito-apps/process-details/dist/api';

export interface RuntimeToolsDevUIEnvelopeViewApi {
  setDataIndexUrl: (dataIndexUrl: string) => void;
  setTrustyServiceUrl: (trustyServiceUrl: string) => void;
  setUsers: (users: User[]) => void;
  navigateTo: (page: string) => void;
  setDevUIUrl: (url: string) => void;
  setOpenApiPath: (path: string) => void;
  setProcessEnabled: (isProcessEnabled: boolean) => void;
  setTracingEnabled: (isTracingEnabled: boolean) => void;
  setAvailablePages: (availablePages: string[]) => void;
  setCustomLabels: (customLabels: CustomLabels) => void;
  setOmittedProcessTimelineEvents: (
    omittedProcessTimelineEvents: string[]
  ) => void;
  setDiagramPreviewSize: (diagramPreviewSize?: DiagramPreviewSize) => void;
  setIsStunnerEnabled: (isStunnerEnabled: boolean) => void;
}
