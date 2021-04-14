import React, { useContext, useEffect, useRef, useState } from 'react';
import {
  Button,
  ButtonVariant,
  Progress,
  ProgressSize,
  Toolbar,
  ToolbarContent,
  ToolbarItem,
  Tooltip
} from '@patternfly/react-core';
import CounterfactualOutcomeSelection from '../CounterfactualOutcomeSelection/CounterfactualOutcomeSelection';
import {
  CFDispatch,
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
  const dispatch = useContext(CFDispatch);

  const runTooltipRef = React.useRef();

  const toggleOutcomeSelection = () => {
    setIsOutcomeSelectionOpen(!isOutcomeSelectionOpen);
  };

  const handleRun = () => {
    dispatch({
      type: 'setStatus',
      payload: {
        executionStatus: 'RUNNING'
      }
    });
  };

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
          {status.executionStatus === 'NOT_STARTED' && (
            <>
              <ToolbarItem>
                {/*wrapping the Button in a div because disabled elements do not emit events*/}
                <div ref={runTooltipRef}>
                  <Button
                    variant={ButtonVariant.primary}
                    aria-label="Run Counterfactual Analysis"
                    isDisabled={status.isDisabled}
                    onClick={handleRun}
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
            </>
          )}
          {status.executionStatus === 'RUN' && (
            <>
              <ToolbarItem>
                <Button
                  variant={ButtonVariant.primary}
                  aria-label="New Counterfactual Analysis"
                  onClick={handleRun}
                >
                  New Counterfactual
                </Button>
              </ToolbarItem>
              <ToolbarItem>
                <Button variant="secondary" onClick={toggleOutcomeSelection}>
                  Edit Counterfactual
                </Button>
              </ToolbarItem>
            </>
          )}
          {status.executionStatus === 'RUNNING' && (
            <ToolbarItem>
              <CounterFactualProgressBar />
            </ToolbarItem>
          )}
        </ToolbarContent>
      </Toolbar>
    </>
  );
};

export default CounterfactualToolbar;

const CounterFactualProgressBar = () => {
  const [value, setValue] = useState(0);

  const intervalID = useRef(null);

  useEffect(() => {
    if (value === 0 && intervalID.current === null) {
      intervalID.current = window.setInterval(() => {
        setValue(prev => prev + 1);
      }, 1000);
    }
    if (value === 10) {
      clearInterval(intervalID.current);
    }
  }, [value, intervalID]);

  return (
    <Progress
      value={(value * 100) / 10}
      title="Calculating..."
      size={ProgressSize.sm}
      style={{ width: 400 }}
      label={`${10 - value} seconds remaining`}
    />
  );
};
