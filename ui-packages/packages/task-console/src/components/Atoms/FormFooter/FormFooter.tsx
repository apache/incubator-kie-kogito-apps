import React from 'react';
import { ActionGroup, Button } from '@patternfly/react-core';
import { FormActionDescription } from '../../../model/FormDescription';

interface IOwnProps {
  actions?: FormActionDescription[];
  onActionClick: (action: FormActionDescription) => void;
}

const FormFooter: React.FC<IOwnProps> = ({ actions, onActionClick }) => {
  return actions && actions.length > 0 ? (
    <ActionGroup>
      {actions.map(action => {
        return (
          <Button
            key={'submit-' + action.name}
            type="submit"
            variant={action.primary ? 'primary' : 'secondary'}
            onClick={() => onActionClick(action)}
          >
            {action.name}
          </Button>
        );
      })}
    </ActionGroup>
  ) : null;
};

export default FormFooter;
