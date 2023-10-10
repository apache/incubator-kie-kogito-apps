import React, { useState } from 'react';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import {
  ActionType,
  FormAction
} from '@kogito-apps/components-common/dist/components/utils';
import { KogitoSpinner } from '@kogito-apps/components-common/dist/components/KogitoSpinner';
import { FormRenderer } from '@kogito-apps/components-common/dist/components/FormRenderer';
import { FormRendererApi } from '@kogito-apps/components-common/dist/types';
import { WorkflowFormDriver } from '../../../api/WorkflowFormDriver';
import { WorkflowDefinition } from '../../../api';
import { Bullseye } from '@patternfly/react-core/dist/js/layouts/Bullseye';

export interface CustomWorkflowFormProps {
  customFormSchema: Record<string, any>;
  driver: WorkflowFormDriver;
  workflowDefinition: WorkflowDefinition;
}
const CustomWorkflowForm: React.FC<CustomWorkflowFormProps & OUIAProps> = ({
  workflowDefinition,
  customFormSchema,
  driver,
  ouiaId,
  ouiaSafe
}) => {
  const formRendererApi = React.useRef<FormRendererApi>();
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const formAction: FormAction[] = [
    {
      name: 'Start'
    },
    {
      name: 'Reset',
      execute: () => {
        formRendererApi?.current?.doReset();
      },
      actionType: ActionType.RESET
    }
  ];

  const startWorkflow = (data: Record<string, any>): void => {
    setIsLoading(true);
    driver
      .startWorkflow(workflowDefinition.endpoint, data)
      .then(() => {
        formRendererApi?.current?.doReset();
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  if (isLoading) {
    return (
      <Bullseye>
        <KogitoSpinner
          spinnerText="Starting workflow..."
          ouiaId="custom-workflow-form-loading"
        />
      </Bullseye>
    );
  }

  return (
    <div
      {...componentOuiaProps(
        ouiaId,
        'custom-workflow-form',
        ouiaSafe ? ouiaSafe : !isLoading
      )}
    >
      <FormRenderer
        formSchema={customFormSchema}
        readOnly={false}
        onSubmit={startWorkflow}
        formActions={formAction}
        ref={formRendererApi}
      />
    </div>
  );
};

export default CustomWorkflowForm;
