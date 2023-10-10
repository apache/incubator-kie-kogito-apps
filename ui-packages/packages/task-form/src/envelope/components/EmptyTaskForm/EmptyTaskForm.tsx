import React from 'react';
import _ from 'lodash';
import {
  EmptyState,
  EmptyStateBody,
  EmptyStateIcon,
  EmptyStateSecondaryActions
} from '@patternfly/react-core/dist/js/components/EmptyState';
import { Title } from '@patternfly/react-core/dist/js/components/Title';
import { Bullseye } from '@patternfly/react-core/dist/js/layouts/Bullseye';
import { InfoCircleIcon } from '@patternfly/react-icons/dist/js/icons/info-circle-icon';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { UserTaskInstance } from '@kogito-apps/task-console-shared';
import { convertActionsToButton } from '@kogito-apps/components-common/dist/components/utils';

interface IOwnProps {
  userTask: UserTaskInstance;
  formSchema: Record<string, any>;
  enabled: boolean;
  submit: (phase: string) => void;
}

const EmptyTaskForm: React.FC<IOwnProps & OUIAProps> = ({
  userTask,
  formSchema,
  enabled,
  submit,
  ouiaId,
  ouiaSafe
}) => {
  const canTransition = (): boolean => {
    return !userTask.completed && !_.isEmpty(formSchema.phases);
  };

  const buildFormActions = () => {
    return formSchema.phases.map((phase) => {
      return {
        name: phase,
        execute: () => {
          submit(phase);
        }
      };
    });
  };

  const actions = canTransition() ? (
    <EmptyStateSecondaryActions>
      {convertActionsToButton(buildFormActions(), enabled)}
    </EmptyStateSecondaryActions>
  ) : null;

  return (
    <Bullseye {...componentOuiaProps(ouiaId, 'empty-task-form', ouiaSafe)}>
      <EmptyState variant={'large'}>
        <EmptyStateIcon
          icon={InfoCircleIcon}
          color="var(--pf-global--info-color--100)"
        />
        <Title headingLevel="h4" size="lg">
          {'Cannot show task form'}
        </Title>
        <EmptyStateBody>
          <p>
            Task{' '}
            <b>
              {userTask.referenceName} ({userTask.id.substring(0, 5)})
            </b>
            &nbsp;doesn&apos;t have a form to show. This usually means that it
            doesn&apos;t require data to be filled by the user.
          </p>
          {canTransition() && (
            <>
              <br />
              <p>
                You can still use the actions bellow to move the task to the
                next phase.
              </p>
            </>
          )}
        </EmptyStateBody>
        {actions}
      </EmptyState>
    </Bullseye>
  );
};

export default EmptyTaskForm;
