import React, {
  useCallback,
  useEffect,
  useImperativeHandle,
  useState
} from 'react';
import { Bullseye } from '@patternfly/react-core/dist/js/layouts/Bullseye';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { KogitoSpinner } from '@kogito-apps/components-common/dist/components/KogitoSpinner';
import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import { WorkflowDefinition, WorkflowFormChannelApi } from '../api';
import WorkflowForm from './components/WorkflowForm/WorkflowForm';
import CustomWorkflowForm from './components/CustomWorkflowForm/CustomWorkflowForm';
import { WorkflowFormEnvelopeViewDriver } from './WorkflowFormEnvelopeViewDriver';
import '@patternfly/patternfly/patternfly.css';

export interface WorkflowFormEnvelopeViewApi {
  initialize: (workflowDefinitionData: WorkflowDefinition) => void;
}

interface Props {
  channelApi: MessageBusClientApi<WorkflowFormChannelApi>;
}

export const WorkflowFormEnvelopeView = React.forwardRef<
  WorkflowFormEnvelopeViewApi,
  Props & OUIAProps
>(({ channelApi, ouiaId }, forwardedRef) => {
  const [workflowDefinition, setWorkflowDefinition] =
    useState<WorkflowDefinition>();
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [driver] = useState<WorkflowFormEnvelopeViewDriver>(
    new WorkflowFormEnvelopeViewDriver(channelApi)
  );
  const [isEnvelopeConnectedToChannel, setEnvelopeConnectedToChannel] =
    useState<boolean>(false);
  const [workflowSchema, setWorkflowSchema] =
    useState<Record<string, any>>(null);

  useImperativeHandle(
    forwardedRef,
    () => ({
      initialize: (workflowDefinitionData: WorkflowDefinition) => {
        setEnvelopeConnectedToChannel(true);
        setWorkflowDefinition(workflowDefinitionData);
      }
    }),
    []
  );

  useEffect(() => {
    if (isEnvelopeConnectedToChannel) {
      getCustomForm();
    }
  }, [isEnvelopeConnectedToChannel]);

  const getCustomForm = useCallback(() => {
    /* istanbul ignore if */
    if (!isEnvelopeConnectedToChannel) {
      setIsLoading(true);
    }
    driver
      .getCustomWorkflowSchema()
      .then((schema: Record<string, any>) => {
        setWorkflowSchema(schema);
      })
      .finally(() => {
        setIsLoading(false);
      });
  }, [isEnvelopeConnectedToChannel]);

  if (isLoading) {
    return (
      <Bullseye
        {...componentOuiaProps(
          /* istanbul ignore next */
          (ouiaId ? ouiaId : 'workflow-form-envelope-view') +
            '-loading-spinner',
          'workflow-form',
          true
        )}
      >
        <KogitoSpinner spinnerText={`Loading workflow form...`} />
      </Bullseye>
    );
  }

  if (
    workflowSchema !== null &&
    workflowSchema.properties &&
    Object.keys(workflowSchema.properties).length > 0
  ) {
    return (
      <CustomWorkflowForm
        customFormSchema={workflowSchema}
        driver={driver}
        workflowDefinition={workflowDefinition}
      />
    );
  }

  return (
    <WorkflowForm workflowDefinition={workflowDefinition} driver={driver} />
  );
});

export default WorkflowFormEnvelopeView;
