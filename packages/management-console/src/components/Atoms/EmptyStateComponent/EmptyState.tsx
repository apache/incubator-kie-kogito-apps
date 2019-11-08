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
import { SearchIcon } from '@patternfly/react-icons';
import './EmptyState.css';

interface IOwnProps {}
const NoMatchEmptyState: React.FC<IOwnProps> = () => {
  return (
    <div className="alignEmptyState">
      <EmptyState>
        <EmptyStateIcon icon={SearchIcon} />
        <Title size="lg">No results found</Title>
        <EmptyStateBody>No child process instances found</EmptyStateBody>
      </EmptyState>
    </div>
  );
};

export default NoMatchEmptyState;
