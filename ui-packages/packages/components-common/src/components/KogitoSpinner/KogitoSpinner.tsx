import React from 'react';
import {
  EmptyState,
  EmptyStateIcon
} from '@patternfly/react-core/dist/js/components/EmptyState';
import { Title } from '@patternfly/react-core/dist/js/components/Title';
import { Spinner } from '@patternfly/react-core/dist/js/components/Spinner';
import {
  OUIAProps,
  componentOuiaProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';

interface KogitoSpinnerProps {
  spinnerText: string;
}
export const KogitoSpinner: React.FC<KogitoSpinnerProps & OUIAProps> = ({
  spinnerText,
  ouiaId,
  ouiaSafe
}) => {
  return (
    <EmptyState {...componentOuiaProps(ouiaId, 'kogito-spinner', ouiaSafe)}>
      <EmptyStateIcon variant="container" component={Spinner} />
      <Title size="lg" headingLevel="h3">
        {spinnerText}
      </Title>
    </EmptyState>
  );
};
