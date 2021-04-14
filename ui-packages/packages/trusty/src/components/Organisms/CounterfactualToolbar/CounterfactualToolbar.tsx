import React, { useState } from 'react';
import {
  Button,
  ButtonVariant,
  Toolbar,
  ToolbarContent,
  ToolbarItem,
  Tooltip
} from '@patternfly/react-core';
import CounterfactualOutcomeSelection from '../CounterfactualOutcomeSelection/CounterfactualOutcomeSelection';
import {
  CFGoal,
  CFStatus
} from '../../Templates/Counterfactual/Counterfactual';

type CounterfactualToolbarProps = {
  status: CFStatus;
  goals: CFGoal[];
};

const CounterfactualToolbar = (props: CounterfactualToolbarProps) => {
  const { goals, status } = props;
  const [isOutcomeSelectionOpen, setIsOutcomeSelectionOpen] = useState(false);
  const toggleOutcomeSelection = () => {
    setIsOutcomeSelectionOpen(!isOutcomeSelectionOpen);
  };
  const runTooltipRef = React.useRef();
  return (
    <>
      {isOutcomeSelectionOpen && (
        <CounterfactualOutcomeSelection
          isOpen={isOutcomeSelectionOpen}
          onClose={toggleOutcomeSelection}
          goals={goals}
        />
      )}
      <Toolbar id="toolbar">
        <ToolbarContent>
          <ToolbarItem>
            {/*wrapping the Button in a div because disabled elements do not emit events*/}
            <div ref={runTooltipRef}>
              <Button
                variant={ButtonVariant.primary}
                aria-label="Run Counterfactual Analysis"
                isDisabled={status.isDisabled}
              >
                Run Counterfactual
              </Button>
            </div>
            {status.isDisabled && (
              <Tooltip
                content={
                  <div>
                    Select both an Outcome and Data Inputs to run a
                    counterfactual analysis
                  </div>
                }
                reference={runTooltipRef}
              />
            )}
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
