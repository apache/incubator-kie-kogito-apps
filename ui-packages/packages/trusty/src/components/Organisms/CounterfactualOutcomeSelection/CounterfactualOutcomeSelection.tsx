import React, { useContext, useState } from 'react';
import { Button, Modal, ModalVariant } from '@patternfly/react-core';
import CounterfactualOutcomeEdit from '../../Molecules/CounterfactualOutcomeEdit/CounterfactualOutcomeEdit';
import {
  CFDispatch,
  CFGoal
} from '../../Templates/Counterfactual/Counterfactual';

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

  const updateGoal = (updatedGoal: CFGoal) => {
    const updatedGoals = editingGoals.map(goal =>
      goal.id === updatedGoal.id ? updatedGoal : goal
    );
    setEditingGoals(updatedGoals);
  };

  const handleApply = () => {
    // removing checked goals with no changed values
    const cleanedGoals = editingGoals.map(goal =>
      !goal.isFixed && goal.originalValue === goal.value
        ? { ...goal, isFixed: true }
        : goal
    );
    setEditingGoals(cleanedGoals);
    dispatch({ type: 'setOutcomes', payload: cleanedGoals });
    onClose();
  };

  return (
    <>
      <Modal
        variant={ModalVariant.medium}
        aria-label="Counterfactual desired outcomes"
        title="Modify Outcome"
        isOpen={isOpen}
        onClose={onClose}
        description="Use the fields below to define the desired counterfactual outcomes."
        actions={[
          <Button key="confirm" variant="primary" onClick={handleApply}>
            Confirm
          </Button>,
          <Button key="cancel" variant="link" onClick={onClose}>
            Cancel
          </Button>
        ]}
      >
        {editingGoals.map((goal, index) => (
          <CounterfactualOutcomeEdit
            key={index}
            goal={goal}
            onUpdateGoal={updateGoal}
          />
        ))}
      </Modal>
    </>
  );
};

export default CounterfactualOutcomeSelection;
