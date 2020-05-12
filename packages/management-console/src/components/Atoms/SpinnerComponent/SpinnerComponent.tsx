import React from 'react';
import {
  Title,
  EmptyState,
  EmptyStateIcon,
  Spinner,
  InjectedOuiaProps,
  withOuiaContext
} from '@patternfly/react-core';
import { componentOuiaProps } from '@kogito-apps/common';

interface IOwnProps {
  spinnerText: string;
}

const EmptyStateSpinner: React.FC<IOwnProps & InjectedOuiaProps> = ({
  spinnerText,
  ouiaContext,
  ouiaId
}) => {
  return (
    <EmptyState
      {...componentOuiaProps(ouiaContext, ouiaId, 'EmptyStateSpinner', true)}
    > 
      <EmptyStateIcon variant="container" component={Spinner} />
      <Title size="lg">{spinnerText}</Title>
    </EmptyState>
  );
};

const EmptyStateSpinnerWithContext = withOuiaContext(EmptyStateSpinner);
export default EmptyStateSpinnerWithContext;
