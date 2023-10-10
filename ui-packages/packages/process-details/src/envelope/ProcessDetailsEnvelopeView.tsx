import * as React from 'react';
import { useImperativeHandle, useState } from 'react';
import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import {
  DiagramPreviewSize,
  ProcessDetailsChannelApi,
  ProcessDetailsInitArgs
} from '../api';
import ProcessDetails from './components/ProcessDetails/ProcessDetails';
import ProcessDetailsEnvelopeViewDriver from './ProcessDetailsEnvelopeViewDriver';
import { ProcessInstance } from '@kogito-apps/management-console-shared/dist/types';
import '@patternfly/patternfly/patternfly.css';

export interface ProcessDetailsEnvelopeViewApi {
  initialize: (initArgs: ProcessDetailsInitArgs) => void;
}

interface Props {
  channelApi: MessageBusClientApi<ProcessDetailsChannelApi>;
}

export const ProcessDetailsEnvelopeView = React.forwardRef<
  ProcessDetailsEnvelopeViewApi,
  Props
>((props, forwardedRef) => {
  const [isEnvelopeConnectedToChannel, setEnvelopeConnectedToChannel] =
    useState<boolean>(false);
  const [processInstance, setProcessInstance] = useState<ProcessInstance>(
    {} as ProcessInstance
  );
  const [omittedProcessTimelineEvents, setOmittedProcessTimelineEvents] =
    useState<string[]>([]);
  const [diagramPreviewSize, setDiagramPreviewSize] =
    useState<DiagramPreviewSize>();
  const [showSwfDiagram, setShowSwfDiagram] = useState<boolean>(false);
  const [isStunnerEnabled, setIsStunnerEnabled] = useState<boolean>(false);
  const [singularProcessLabel, setSingularProcessLabel] = useState<string>('');
  const [pluralProcessLabel, setPluralProcessLabel] = useState<string>('');
  useImperativeHandle(
    forwardedRef,
    () => ({
      initialize: /* istanbul ignore next */ (initArgs) => {
        setProcessInstance(initArgs.processInstance);
        setOmittedProcessTimelineEvents(initArgs.omittedProcessTimelineEvents);
        setDiagramPreviewSize(initArgs.diagramPreviewSize);
        setShowSwfDiagram(initArgs.showSwfDiagram);
        setIsStunnerEnabled(initArgs.isStunnerEnabled);
        setSingularProcessLabel(initArgs.singularProcessLabel);
        setPluralProcessLabel(initArgs.pluralProcessLabel);
        setEnvelopeConnectedToChannel(true);
      }
    }),
    []
  );

  return (
    <React.Fragment>
      <ProcessDetails
        isEnvelopeConnectedToChannel={isEnvelopeConnectedToChannel}
        driver={new ProcessDetailsEnvelopeViewDriver(props.channelApi)}
        processDetails={processInstance}
        omittedProcessTimelineEvents={omittedProcessTimelineEvents}
        diagramPreviewSize={diagramPreviewSize}
        showSwfDiagram={showSwfDiagram}
        isStunnerEnabled={isStunnerEnabled}
        singularProcessLabel={singularProcessLabel}
        pluralProcessLabel={pluralProcessLabel}
      />
    </React.Fragment>
  );
});

export default ProcessDetailsEnvelopeView;
