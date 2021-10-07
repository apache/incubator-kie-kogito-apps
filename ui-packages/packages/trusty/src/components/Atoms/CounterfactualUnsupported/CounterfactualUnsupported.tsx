import React from 'react';
import {
  Bullseye,
  EmptyState,
  EmptyStateBody,
  EmptyStateIcon,
  EmptyStateVariant,
  List,
  ListItem,
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
    <Bullseye className="cf-unsupported">
      <EmptyState variant={EmptyStateVariant.full}>
        <EmptyStateIcon
          icon={InfoCircleIcon}
          color="var(--pf-global--info-color--100)"
        />
        <Title headingLevel="h1" size="4xl">
          Unsupported model
        </Title>
        <EmptyStateBody>
          <p>The model contains elements that are unsupported.</p>
          <div>
            {!(isAtLeastOneInputSupported && isAtLeastOneOutcomeSupported) && (
              <List>
                {!isAtLeastOneInputSupported && (
                  <ListItem data-ouia-component-id={'inputs-message'}>
                    <p>All of the model inputs are structured data-types.</p>
                  </ListItem>
                )}
                {!isAtLeastOneOutcomeSupported && (
                  <ListItem data-ouia-component-id={'outputs-message'}>
                    <p>All of the model outcomes are structured data-types.</p>
                  </ListItem>
                )}
              </List>
            )}
          </div>
          <p>Consequentially Counterfactuals cannot be generated.</p>
        </EmptyStateBody>
      </EmptyState>
    </Bullseye>
  );
};

export default CounterfactualUnsupported;
