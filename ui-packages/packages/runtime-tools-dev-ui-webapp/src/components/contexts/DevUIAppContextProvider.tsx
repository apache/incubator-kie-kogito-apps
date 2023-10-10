import React from 'react';
import { User } from '@kogito-apps/consoles-common/dist/environment/auth';
import RuntimeToolsDevUIAppContext, {
  DevUIAppContextImpl
} from './DevUIAppContext';
import { CustomLabels } from '../../api/CustomLabels';
import { DiagramPreviewSize } from '@kogito-apps/process-details/dist/api';

interface IOwnProps {
  users: User[];
  devUIUrl: string;
  openApiPath: string;
  isProcessEnabled: boolean;
  isTracingEnabled: boolean;
  availablePages: string[];
  customLabels: CustomLabels;
  omittedProcessTimelineEvents: string[];
  diagramPreviewSize: DiagramPreviewSize;
  isStunnerEnabled: boolean;
}

const DevUIAppContextProvider: React.FC<IOwnProps> = ({
  users,
  devUIUrl,
  openApiPath,
  isProcessEnabled,
  isTracingEnabled,
  availablePages,
  customLabels,
  omittedProcessTimelineEvents,
  diagramPreviewSize,
  isStunnerEnabled,
  children
}) => {
  return (
    <RuntimeToolsDevUIAppContext.Provider
      value={
        new DevUIAppContextImpl({
          users,
          devUIUrl,
          openApiPath,
          isProcessEnabled,
          isTracingEnabled,
          availablePages,
          customLabels,
          omittedProcessTimelineEvents,
          diagramPreviewSize,
          isStunnerEnabled
        })
      }
    >
      {children}
    </RuntimeToolsDevUIAppContext.Provider>
  );
};

export default DevUIAppContextProvider;
