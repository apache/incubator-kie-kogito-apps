import React, { useImperativeHandle } from 'react';
import '@patternfly/patternfly/patternfly.css';
import '@patternfly/react-core/dist/styles/base.css';
import { RuntimeToolsDevUIEnvelopeViewApi } from './RuntimeToolsDevUIEnvelopeViewApi';
import RuntimeTools from '../components/DevUI/RuntimeTools/RuntimeTools';
import { User } from '@kogito-apps/consoles-common/dist/environment/auth';
import { DiagramPreviewSize } from '@kogito-apps/process-details/dist/api';

export const RuntimeToolsDevUIEnvelopeView =
  React.forwardRef<RuntimeToolsDevUIEnvelopeViewApi>((props, forwardingRef) => {
    const [dataIndexUrl, setDataIndexUrl] = React.useState('');
    const [trustyServiceUrl, setTrustyServiceUrl] = React.useState('');
    const [DevUiUsers, setDevUiUsers] = React.useState<User[]>([]);
    const [navigate, setNavigate] = React.useState<string>('');
    const [devUIUrl, setDevUIUrl] = React.useState<string>('');
    const [openApiPath, setOpenApiPath] = React.useState<string>('');
    const [isProcessEnabled, setProcessEnabled] = React.useState(false);
    const [isTracingEnabled, setTracingEnabled] = React.useState(false);
    const [availablePages, setAvailablePages] = React.useState<string[]>([]);
    const [customLabels, setCustomLabels] = React.useState(undefined);
    const [omittedProcessTimelineEvents, setOmittedProcessTimelineEvents] =
      React.useState<string[]>([]);
    const [diagramPreviewSize, setDiagramPreviewSize] =
      React.useState<DiagramPreviewSize>();
    const [isStunnerEnabled, setIsStunnerEnabled] =
      React.useState<boolean>(false);

    useImperativeHandle(
      forwardingRef,
      () => {
        return {
          setDataIndexUrl: (dataIndexUrl) => {
            setDataIndexUrl(dataIndexUrl);
          },
          setTrustyServiceUrl: (trustyServiceUrl) => {
            setTrustyServiceUrl(trustyServiceUrl);
          },
          setUsers: (users) => {
            setDevUiUsers(users);
          },
          navigateTo: (page) => {
            setNavigate(page);
          },
          setDevUIUrl: (url) => {
            setDevUIUrl(url);
          },
          setOpenApiPath: (path) => {
            setOpenApiPath(path);
          },
          setProcessEnabled: (isProcessEnabled) => {
            setProcessEnabled(isProcessEnabled);
          },
          setTracingEnabled: (isTracingEnabled) => {
            setTracingEnabled(isTracingEnabled);
          },
          setAvailablePages: (availablePages) => {
            setAvailablePages(availablePages);
          },
          setCustomLabels: (customLabels) => {
            setCustomLabels(customLabels);
          },
          setOmittedProcessTimelineEvents: (omittedProcessTimelineEvents) => {
            setOmittedProcessTimelineEvents(omittedProcessTimelineEvents);
          },
          setDiagramPreviewSize: (diagramPreviewSize) => {
            setDiagramPreviewSize(diagramPreviewSize);
          },
          setIsStunnerEnabled: (isStunnerEnabled) => {
            setIsStunnerEnabled(isStunnerEnabled);
          }
        };
      },
      []
    );
    return (
      <>
        {(isProcessEnabled || isTracingEnabled) && navigate.length > 0 && (
          <RuntimeTools
            users={DevUiUsers}
            dataIndexUrl={dataIndexUrl}
            trustyServiceUrl={trustyServiceUrl}
            navigate={navigate}
            openApiPath={openApiPath}
            devUIUrl={devUIUrl}
            isProcessEnabled={isProcessEnabled}
            isTracingEnabled={isTracingEnabled}
            availablePages={availablePages}
            customLabels={customLabels}
            omittedProcessTimelineEvents={omittedProcessTimelineEvents}
            diagramPreviewSize={diagramPreviewSize}
            isStunnerEnabled={isStunnerEnabled}
          />
        )}
      </>
    );
  });
