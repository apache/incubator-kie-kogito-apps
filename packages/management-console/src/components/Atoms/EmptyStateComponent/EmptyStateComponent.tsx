import React from 'react';
import {
  Title,
  EmptyState,
  EmptyStateIcon,
  EmptyStateBody,
  Bullseye,
  Button,
  EmptyStateVariant,
  withOuiaContext,
  InjectedOuiaProps
} from '@patternfly/react-core';
import {
  SearchIcon,
  ExclamationTriangleIcon,
  InfoCircleIcon
} from '@patternfly/react-icons';
import '@patternfly/patternfly/patternfly-addons.css';
import { componentOuiaProps } from '@kogito-apps/common';

interface IOwnProps {
  iconType: string;
  title: string;
  body: string;
  filterClick?: any;
  setFilters?: any;
  setCheckedArray?: any;
  refetch?: any;
  setSearchWord?: any;
  filters?: any;
  ouiaComponentType?: string;
}
const EmptyStateComponent: React.FC<IOwnProps & InjectedOuiaProps> = ({
  iconType,
  title,
  body,
  filterClick,
  setFilters,
  setCheckedArray,
  refetch,
  setSearchWord,
  filters,
  ouiaContext,
  ouiaId
}) => {
  const resetClick = () => {
    setSearchWord('');
    filterClick(['ACTIVE']);
    setCheckedArray(['ACTIVE']);
    setFilters({ ...filters, status: ['ACTIVE'] });
  };
  return (
    <Bullseye
      {...componentOuiaProps(ouiaContext, ouiaId, 'EmptyState', true)}>
      <EmptyState variant={EmptyStateVariant.full}>
        {iconType === 'searchIcon' && (
          <EmptyStateIcon icon={SearchIcon} size="sm" />
        )}
        {(iconType === 'warningTriangleIcon' ||
          iconType === 'warningTriangleIcon1') && (
          <EmptyStateIcon
            icon={ExclamationTriangleIcon}
            size="sm"
            color="var(--pf-global--warning-color--100)"
          />
        )}
        {iconType === 'infoCircleIcon' && (
          <EmptyStateIcon
            icon={InfoCircleIcon}
            size="sm"
            color="var(--pf-global--info-color--100)"
          />
        )}

        <Title headingLevel="h5" size="lg">
          {title}
        </Title>

        <EmptyStateBody>{body}</EmptyStateBody>

        {iconType === 'warningTriangleIcon' && (
          <Button variant="primary" onClick={() => refetch()}>
            Refresh
          </Button>
        )}

        {iconType === 'warningTriangleIcon1' && (
          <Button variant="link" onClick={resetClick}>
            Reset to default
          </Button>
        )}
      </EmptyState>
    </Bullseye>
  );
};

const EmptyStateComponentWithContext = withOuiaContext(EmptyStateComponent);
export default EmptyStateComponentWithContext;
