import React from 'react';
import { ActionGroup, Button } from '@patternfly/react-core';

export interface IFormAction {
  name: string;
  onActionClick: () => void;
}

interface IOwnProps {
  actions?: IFormAction[];
}

const FormFooter: React.FC<IOwnProps> = ({ actions }) => {
  const capitalize = label => {
    return label.charAt(0).toUpperCase() + label.slice(1);
  };

  return actions && actions.length > 0 ? (
    <ActionGroup>
      {actions.map(action => {
        return (
          <Button
            key={'submit-' + action.name}
            type="submit"
            onClick={action.onActionClick}
          >
            {capitalize(action.name)}
          </Button>
        );
      })}
    </ActionGroup>
  ) : null;
};

export default FormFooter;
