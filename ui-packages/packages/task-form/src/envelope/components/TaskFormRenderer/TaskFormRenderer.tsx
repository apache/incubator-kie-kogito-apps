import React, { useEffect, useState } from 'react';
import _ from 'lodash';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { UserTaskInstance } from '@kogito-apps/task-console-shared';
import { FormRenderer } from '@kogito-apps/components-common/dist/components/FormRenderer';
import { FormAction } from '@kogito-apps/components-common/dist/components/utils';
import { generateFormData } from '../utils/TaskFormDataUtils';

interface IOwnProps {
  userTask: UserTaskInstance;
  formData?: any;
  formSchema: Record<string, any>;
  enabled: boolean;
  submit: (phase: string, payload: any) => void;
}

const TaskFormRenderer: React.FC<IOwnProps & OUIAProps> = ({
  userTask,
  formData,
  formSchema,
  enabled,
  submit,
  ouiaId,
  ouiaSafe
}) => {
  const [selectedPhase, setSelectedPhase] = useState<string>();
  const [formActions, setFormActions] = useState<FormAction[]>([]);

  useEffect(() => {
    if (formSchema.phases) {
      const actions = formSchema.phases.map((phase) => {
        return {
          name: phase,
          execute: () => {
            setSelectedPhase(phase);
          }
        };
      });
      setFormActions(actions);
    }
  }, []);

  const isReadOnly = (): boolean => {
    if (!enabled || formActions.length === 0) {
      return true;
    }

    if (userTask.completed) {
      return true;
    }

    if (_.isEmpty(formSchema.phases)) {
      return true;
    }

    return false;
  };

  const doSubmit = (data: any) => {
    submit(selectedPhase, data);
  };

  return (
    <div {...componentOuiaProps(ouiaId, 'task-form-renderer', ouiaSafe)}>
      <FormRenderer
        formSchema={formSchema}
        model={formData || generateFormData(userTask)}
        readOnly={isReadOnly()}
        onSubmit={doSubmit}
        formActions={formActions}
      />
    </div>
  );
};

export default TaskFormRenderer;
