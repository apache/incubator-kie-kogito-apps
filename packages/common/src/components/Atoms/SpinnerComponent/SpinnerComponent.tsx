import React from 'react';
import { Title, EmptyState, EmptyStateIcon, InjectedOuiaProps, withOuiaContext } from '@patternfly/react-core';
import { Spinner } from '@patternfly/react-core/dist/esm/experimental';
import { componentOuiaProps } from '../../../utils/OuiaUtils';

interface IOwnProps {
  spinnerText: string;
}
const EmptyStateSpinner: React.FC<IOwnProps & InjectedOuiaProps> = ({
  spinnerText,
  ouiaContext,
  ouiaId
}) => {
  return (
    <EmptyState {...componentOuiaProps(ouiaContext, ouiaId, 'EmptyStateSpinner', true)}>
      <EmptyStateIcon variant="container" component={Spinner} />
      <Title size="lg">{spinnerText}</Title>
    </EmptyState>
  );
};

export default withOuiaContext(EmptyStateSpinner);
