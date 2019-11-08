import React from 'react';
import {
  Title,
  Button,
  EmptyState,
  EmptyStateVariant,
  EmptyStateIcon,
  EmptyStateBody,
  EmptyStateSecondaryActions
} from '@patternfly/react-core';

interface IOwnProps {}
const EmptyStateSpinner: React.FC<IOwnProps> = () => {
  const Spinner = () => (
    <span className="pf-c-spinner" role="progressbar" aria-valuetext="Loading...">
      <span className="pf-c-spinner__clipper" />
      <span className="pf-c-spinner__lead-ball" />
      <span className="pf-c-spinner__tail-ball" />
    </span>
  );
  return (
    <EmptyState>
      <EmptyStateIcon variant="container" component={Spinner} />
      <Title size="lg">Loading</Title>
    </EmptyState>
  );
};

export default EmptyStateSpinner;
