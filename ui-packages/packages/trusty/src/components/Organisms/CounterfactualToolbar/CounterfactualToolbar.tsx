import React, { useState } from 'react';
import {
  Button,
  ButtonVariant,
  Toolbar,
  ToolbarContent,
  ToolbarItem
} from '@patternfly/react-core';
import CounterfactualOutcomeSelection from '../CounterfactualOutcomeSelection/CounterfactualOutcomeSelection';

const CounterfactualToolbar = () => {
  const [isOutcomeSelectionOpen, setIsOutcomeSelectionOpen] = useState(false);
  const toggleOutcomeSelection = () => {
    setIsOutcomeSelectionOpen(!isOutcomeSelectionOpen);
  };
  return (
    <>
      <CounterfactualOutcomeSelection
        isOpen={isOutcomeSelectionOpen}
        onClose={toggleOutcomeSelection}
      />
      <Toolbar id="toolbar">
        <ToolbarContent>
          <ToolbarItem>
            <Button
              variant={ButtonVariant.primary}
              aria-label="Run Counterfactual Analysis"
              isDisabled={true}
            >
              Run Counterfactual
            </Button>
          </ToolbarItem>
          <ToolbarItem>
            <Button variant="secondary" onClick={toggleOutcomeSelection}>
              Select Outcome
            </Button>
          </ToolbarItem>
          <ToolbarItem variant="separator" />
          <ToolbarItem>
            <Button variant="link" isInline={true}>
              Reset
            </Button>
          </ToolbarItem>
        </ToolbarContent>
      </Toolbar>
    </>
  );
};

export default CounterfactualToolbar;
