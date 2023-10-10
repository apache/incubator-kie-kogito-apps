import React from 'react';
import _ from 'lodash';
import { Button } from '@patternfly/react-core/dist/js/components/Button';

export enum ActionType {
  SUBMIT = 'submit',
  BUTTON = 'button',
  RESET = 'reset'
}
export interface FormAction {
  name: string;
  execute?(): void;
  actionType?: ActionType;
}

interface FormButton {
  key: string;
  label: string;
  variant: 'primary' | 'secondary';
  actionType: ActionType;
  onClick: () => void;
}

export const convertActionsToButton = (
  actions: FormAction[],
  enabled: boolean,
  onSubmitForm?: () => void
) => {
  if (_.isEmpty(actions)) {
    return null;
  }

  const capitalize = (label) => {
    return label.charAt(0).toUpperCase() + label.slice(1);
  };

  const isPrimary = (label: string): boolean => {
    // Assuming that Complete will be the default act
    return (
      label.toLowerCase() === 'complete' ||
      label.toLowerCase() === 'start' ||
      (actions && actions.length === 1)
    );
  };

  const resolveButtonVariant = (action: FormAction) => {
    if (isPrimary(action.name)) {
      return 'primary';
    }
    return 'secondary';
  };

  const buttons: FormButton[] = actions.map((action) => {
    const actionType = action.actionType
      ? action.actionType
      : ActionType.SUBMIT;
    return {
      key: `submit-${action.name}`,
      label: capitalize(action.name),
      variant: resolveButtonVariant(action),
      actionType: actionType,
      onClick: () => {
        action.execute && action.execute();
        actionType === ActionType.SUBMIT && onSubmitForm && onSubmitForm();
      }
    };
  });

  buttons.sort((buttonA, buttonB) => {
    if (isPrimary(buttonA.label)) {
      return -1;
    }
    if (isPrimary(buttonB.label)) {
      return 2;
    }
    return buttonA.label.localeCompare(buttonB.label);
  });

  return buttons.map((button) => {
    return (
      <Button
        type={button.actionType}
        key={button.key}
        variant={button.variant}
        onClick={() => {
          if (enabled) {
            button.onClick();
          }
        }}
        isDisabled={!enabled}
      >
        {button.label}
      </Button>
    );
  });
};
