import React, { useContext, useEffect, useRef, useState } from 'react';
import {
  Button,
  ButtonVariant,
  Modal,
  ModalVariant,
  Progress,
  ProgressSize,
  Toolbar,
  ToolbarContent,
  ToolbarItem,
  Tooltip
} from '@patternfly/react-core';
import CounterfactualOutcomeSelection from '../CounterfactualOutcomeSelection/CounterfactualOutcomeSelection';
import { CFDispatch } from '../CounterfactualAnalysis/CounterfactualAnalysis';
import {
  CFAnalysisResetType,
  CFExecutionStatus,
  CFGoal,
  CFStatus
} from '../../../types';
import './CounterfactualToolbar.scss';

type CounterfactualToolbarProps = {
  goals: CFGoal[];
  status: CFStatus;
  onRunAnalysis: () => void;
  onSetupNewAnalysis: (resetType: CFAnalysisResetType) => void;
};

const CounterfactualToolbar = (props: CounterfactualToolbarProps) => {
  const { goals, status, onRunAnalysis, onSetupNewAnalysis } = props;
  const [isOutcomeSelectionOpen, setIsOutcomeSelectionOpen] = useState(false);
  const [CFResetType, setCFResetType] = useState<CFAnalysisResetType>();
  const [isConfirmNewCFDialogOpen, setIsConfirmNewCFDialogOpen] = useState(
    false
  );
  const dispatch = useContext(CFDispatch);

  const toggleOutcomeSelection = () => {
    setIsOutcomeSelectionOpen(!isOutcomeSelectionOpen);
  };

  const handleRun = () => {
    onRunAnalysis();
    dispatch({
      type: 'CF_SET_STATUS',
      payload: {
        executionStatus: CFExecutionStatus.RUNNING
      }
    });
  };

  const handleNewCF = () => {
    setIsConfirmNewCFDialogOpen(true);
    setCFResetType('NEW');
  };

  const handleEditSearchDomain = () => {
    setIsConfirmNewCFDialogOpen(true);
    setCFResetType('EDIT');
  };

  const handleNewCFModalClose = () => {
    setIsConfirmNewCFDialogOpen(false);
    setCFResetType(undefined);
  };

  const setupNewCF = () => {
    onSetupNewAnalysis(CFResetType);
    handleNewCFModalClose();
  };

  const handleCFReset = () => {
    onSetupNewAnalysis('NEW');
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
      <Modal
        variant={ModalVariant.small}
        titleIconVariant="warning"
        title="Results will be cleared"
        isOpen={isConfirmNewCFDialogOpen}
        onClose={handleNewCFModalClose}
        actions={[
          <Button key="confirm" variant="primary" onClick={setupNewCF}>
            Continue
          </Button>,
          <Button key="cancel" variant="link" onClick={handleNewCFModalClose}>
            Cancel
          </Button>
        ]}
      >
        If you start a New Counterfactual analysis, or Edit the existing one,
        any results will be cleared and cannot be retrieved.
      </Modal>
      <Toolbar id="cf-toolbar">
        <ToolbarContent>
          {status.executionStatus === CFExecutionStatus.NOT_STARTED && (
            <>
              <ToolbarItem>
                <Tooltip
                  content={
                    status.isDisabled ? (
                      <div>
                        Select Inputs, provide inputs constraints and set up
                        Outcomes to run a counterfactual analysis.
                      </div>
                    ) : (
                      <div>
                        Run the counterfactual analysis based on selected Inputs
                        and Outcomes.
                      </div>
                    )
                  }
                >
                  <Button
                    variant={ButtonVariant.primary}
                    aria-label="Run Counterfactual Analysis"
                    onClick={handleRun}
                    isAriaDisabled={status.isDisabled}
                    className="counterfactual-run"
                  >
                    Run Counterfactual
                  </Button>
                </Tooltip>
              </ToolbarItem>
              <ToolbarItem>
                <Tooltip
                  content={
                    <div>
                      Sets the desired decision outcomes for a counterfactual
                      analysis.
                    </div>
                  }
                >
                  <Button
                    variant="secondary"
                    onClick={toggleOutcomeSelection}
                    className="counterfactual-setup-outcomes"
                  >
                    Set Up Outcomes
                  </Button>
                </Tooltip>
              </ToolbarItem>
              <ToolbarItem variant="separator" />
              <ToolbarItem>
                <Tooltip
                  content={
                    <div>
                      Clear all selections and reverts them to their initial
                      state.
                    </div>
                  }
                >
                  <Button
                    variant="link"
                    isInline={true}
                    onClick={handleCFReset}
                  >
                    Reset
                  </Button>
                </Tooltip>
              </ToolbarItem>
            </>
          )}
          {status.executionStatus === CFExecutionStatus.COMPLETED && (
            <>
              <ToolbarItem>
                <Tooltip
                  content={
                    <div>
                      Clear all and set up a new Counterfactual analysis.
                    </div>
                  }
                >
                  <Button
                    variant={ButtonVariant.primary}
                    aria-label="New Counterfactual Analysis"
                    onClick={handleNewCF}
                  >
                    New Counterfactual
                  </Button>
                </Tooltip>
              </ToolbarItem>
              <ToolbarItem>
                <Tooltip
                  content={
                    <div>
                      Edit Inputs and Outcomes to rerun a counterfactual
                      analysis.
                    </div>
                  }
                >
                  <Button variant="secondary" onClick={handleEditSearchDomain}>
                    Edit Counterfactual
                  </Button>
                </Tooltip>
              </ToolbarItem>
            </>
          )}
          {status.executionStatus === CFExecutionStatus.RUNNING && (
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
