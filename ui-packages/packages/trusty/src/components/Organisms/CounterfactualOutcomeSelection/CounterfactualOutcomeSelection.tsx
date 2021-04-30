import React, { useContext, useState } from 'react';
import { Button, Form, Modal, ModalVariant } from '@patternfly/react-core';
import CounterfactualOutcomeEdit from '../../Molecules/CounterfactualOutcomeEdit/CounterfactualOutcomeEdit';
import {
  CFDispatch,
  CFGoal
} from '../../Templates/Counterfactual/Counterfactual';
import './CounterfactualOutcomeSelection.scss';

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
      goal.id === updatedGoal.id
        ? {
            ...updatedGoal,
            isFixed: updatedGoal.value === updatedGoal.originalValue
          }
        : goal
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
        <Form className="counterfactual__outcomes-form">
          {editingGoals.map((goal, index) => (
            <CounterfactualOutcomeEdit
              key={index}
              goal={goal}
              index={index}
              onUpdateGoal={updateGoal}
            />
          ))}
        </Form>
      </Modal>
    </>
  );
};

export default CounterfactualOutcomeSelection;
