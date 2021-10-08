import React from 'react';
import {
  EmptyState,
  EmptyStateBody,
  EmptyStateIcon,
  EmptyStateVariant,
  Title
} from '@patternfly/react-core';
import { InfoCircleIcon } from '@patternfly/react-icons';
import './CounterfactualUnsupported.scss';

type CounterfactualUnsupportedProps = {
  isAtLeastOneInputSupported: boolean;
  isAtLeastOneOutcomeSupported: boolean;
};

const CounterfactualUnsupported = (props: CounterfactualUnsupportedProps) => {
  const { isAtLeastOneInputSupported, isAtLeastOneOutcomeSupported } = props;

  return (
    <EmptyState variant={EmptyStateVariant.full}>
      <EmptyStateIcon icon={InfoCircleIcon} />
      <Title headingLevel="h1">Unsupported model</Title>
      <EmptyStateBody>
        <p>The model contains elements that are unsupported.</p>
        {!isAtLeastOneInputSupported && !isAtLeastOneOutcomeSupported && (
          <p data-ouia-component-id="message-inputs-outcomes">
            All of the model inputs and outcomes are structured data-types.
          </p>
        )}
        {!isAtLeastOneInputSupported && isAtLeastOneOutcomeSupported && (
          <p data-ouia-component-id="message-inputs">
            All of the model inputs are structured data-types.
          </p>
        )}
        {isAtLeastOneInputSupported && !isAtLeastOneOutcomeSupported && (
          <p data-ouia-component-id="message-outcomes">
            All of the model outcomes are structured data-types.
          </p>
        )}
        <p>Consequentially Counterfactuals cannot be generated.</p>
      </EmptyStateBody>
    </EmptyState>
  );
};

export default CounterfactualUnsupported;
