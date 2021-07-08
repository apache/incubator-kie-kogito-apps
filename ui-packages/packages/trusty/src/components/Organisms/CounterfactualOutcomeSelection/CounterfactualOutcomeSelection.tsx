import React, { useCallback, useContext, useState } from 'react';
import {
  Button,
  Checkbox,
  Form,
  Modal,
  ModalVariant,
  Title,
  TitleSizes,
  Tooltip
} from '@patternfly/react-core';
import { CFGoal, CFGoalRole } from '../../../types';
import { CFDispatch } from '../CounterfactualAnalysis/CounterfactualAnalysis';
import './CounterfactualOutcomeSelection.scss';
import { InfoCircleIcon } from '@patternfly/react-icons';
import CounterfactualOutcome from '../../Molecules/CounterfactualOutcome/CounterfactualOutcome';
import CounterfactualOutcomeEdit from '../../Molecules/CounterfactualOutcomeEdit/CounterfactualOutcomeEdit';

type CounterfactualOutcomeSelection = {
  isOpen: boolean;
  onClose: () => void;
  goals: CFGoal[];
};

const CounterfactualOutcomeSelection = (
  props: CounterfactualOutcomeSelection
) => {
  const { isOpen, onClose, goals } = props;
  const [editingGoals, setEditingGoals] = useState(goals);
  const dispatch = useContext(CFDispatch);

  const adjustAllOutcomes = useCallback(
    () =>
      editingGoals.filter(
        goal =>
          goal.role === CFGoalRole.FLOATING ||
          goal.role === CFGoalRole.UNSUPPORTED
      ).length === editingGoals.length,
    [editingGoals]
  );

  const setAdjustAllOutcomes = (checked: boolean) => {
    const updatedGoals = editingGoals.map(goal => {
      if (goal.role === CFGoalRole.UNSUPPORTED) {
        return goal;
      }
      let updatedRole = CFGoalRole.FIXED;
      if (checked) {
        updatedRole = CFGoalRole.FLOATING;
      } else if (goal.value === goal.originalValue) {
        updatedRole = CFGoalRole.ORIGINAL;
      }
      return {
        ...goal,
        role: updatedRole
      };
    });
    setEditingGoals(updatedGoals);
  };

  const updateGoal = (updatedGoal: CFGoal) => {
    const updatedGoals = editingGoals.map(goal => {
      if (goal.id !== updatedGoal.id) {
        return goal;
      }
      if (goal.role === CFGoalRole.UNSUPPORTED) {
        return goal;
      }
      let updatedRole = CFGoalRole.FIXED;
      if (updatedGoal.role === CFGoalRole.FLOATING) {
        updatedRole = CFGoalRole.FLOATING;
      } else if (updatedGoal.value === updatedGoal.originalValue) {
        updatedRole = CFGoalRole.ORIGINAL;
      }
      return {
        ...updatedGoal,
        role: updatedRole
      };
    });
    setEditingGoals(updatedGoals);
  };

  const handleApply = () => {
    // removing checked goals with no changed values
    const cleanedGoals = editingGoals.map(goal => {
      if (goal.role === CFGoalRole.UNSUPPORTED) {
        return goal;
      }
      if (goal.role === CFGoalRole.FLOATING) {
        return goal;
      }
      if (goal.originalValue === goal.value) {
        return { ...goal, role: CFGoalRole.ORIGINAL };
      }
      return goal;
    });
    setEditingGoals(cleanedGoals);
    dispatch({ type: 'CF_SET_OUTCOMES', payload: cleanedGoals });
    onClose();
  };

  return (
    <>
      <Modal
        variant={ModalVariant.medium}
        aria-label="Counterfactual desired outcomes"
        title="Select the desired outcomes"
        isOpen={isOpen}
        onClose={onClose}
        description="Define the desired outcome that must be achieved by the counterfactual scenarios."
        actions={[
          <Button key="confirm" variant="primary" onClick={handleApply}>
            Confirm
          </Button>,
          <Button key="cancel" variant="link" onClick={onClose}>
            Cancel
          </Button>
        ]}
      >
        <Form className="counterfactual__outcomes-form">
          <div className="counterfactual__outcomes-grid">
            <div className="counterfactual__outcomes-grid__row counterfactual__outcomes-grid__separator">
              <Title headingLevel="h5" size={TitleSizes.lg}>
                Original decision outcome
                <Tooltip content={<div>Outcome of the original decision.</div>}>
                  <Button variant="plain">
                    <InfoCircleIcon
                      color={'var(--pf-global--info-color--100)'}
                    />
                  </Button>
                </Tooltip>
              </Title>
            </div>
            <div className="counterfactual__outcomes-grid__row">
              <Title headingLevel="h5" size={TitleSizes.lg}>
                Desired counterfactual outcome
                <Tooltip
                  content={
                    <div>
                      Outcome to be fulfilled by the counterfactual processing.
                    </div>
                  }
                >
                  <Button variant="plain">
                    <InfoCircleIcon
                      color={'var(--pf-global--info-color--100)'}
                    />
                  </Button>
                </Tooltip>
              </Title>
              <Checkbox
                id={'allowAllAdjustments'}
                label={'Allow all counterfactual outcomes to be adjusted.'}
                isChecked={adjustAllOutcomes()}
                onChange={setAdjustAllOutcomes}
              />
            </div>
            {editingGoals.map((goal, index) => (
              <React.Fragment key={index}>
                <div className="counterfactual__outcomes-grid__row counterfactual__outcomes-grid__separator">
                  <CounterfactualOutcome key={index} goal={goal} />
                </div>
                <div className="counterfactual__outcomes-grid__row counterfactual__outcomes-form__desired">
                  <CounterfactualOutcomeEdit
                    key={index}
                    goal={goal}
                    index={index}
                    onUpdateGoal={updateGoal}
                  />
                </div>
              </React.Fragment>
            ))}
          </div>
        </Form>
      </Modal>
    </>
  );
};

export default CounterfactualOutcomeSelection;
