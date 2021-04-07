import React from 'react';
import { Button, Modal, ModalVariant } from '@patternfly/react-core';
import CounterfactualOutcome from '../../Molecules/CounterfactualOutcome/CounterfactualOutcome';
import { Outcome } from '../../../types';

type CounterfactualOutcomeSelection = {
  isOpen: boolean;
  onClose: () => void;
};

const CounterfactualOutcomeSelection = (
  props: CounterfactualOutcomeSelection
) => {
  const { isOpen, onClose } = props;
  const outcomes: Outcome[] = [
    {
      outcomeId: '_12268B68-94A1-4960-B4C8-0B6071AFDE58',
      outcomeName: 'Mortgage Approval',
      evaluationStatus: 'SUCCEEDED',
      outcomeResult: {
        name: 'Mortgage Approval',
        typeRef: 'boolean',
        value: true,
        components: null
      },
      messages: [],
      hasErrors: false
    },
    {
      outcomeId: '_9CFF8C35-4EB3-451E-874C-DB27A5A424C0',
      outcomeName: 'Risk Score',
      evaluationStatus: 'SUCCEEDED',
      outcomeResult: {
        name: 'Risk Score',
        typeRef: 'number',
        value: 150,
        components: null
      },
      messages: [],
      hasErrors: false
    }
  ];

  return (
    <>
      <Modal
        variant={ModalVariant.medium}
        aria-label="Counterfactual desired outcome"
        title="Select a desired outcome"
        isOpen={isOpen}
        onClose={onClose}
        description="Select and define one or more outcomes for the counterfactual analysis."
        actions={[
          <Button key="confirm" variant="primary" onClick={onClose}>
            Select
          </Button>,
          <Button key="cancel" variant="link" onClick={onClose}>
            Cancel
          </Button>
        ]}
      >
        <CounterfactualOutcome outcome={outcomes[0]} />
        <CounterfactualOutcome outcome={outcomes[1]} />
      </Modal>
    </>
  );
};

export default CounterfactualOutcomeSelection;
