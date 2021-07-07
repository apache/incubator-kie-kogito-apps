import React, { useEffect, useMemo, useState } from 'react';
import {
  Checkbox,
  FormGroup,
  Switch,
  TextInput,
  Touchspin
} from '@patternfly/react-core';
import { v4 as uuid } from 'uuid';
import { CFGoal, CFGoalRole } from '../../../types';
import './CounterfactualOutcomeEdit.scss';
import CounterfactualOutcomeUnsupported from '../../Atoms/CounterfactualOutcomeUnsupported/CounterfactualOutcomeUnsupported';

type CounterfactualOutcomeEditProps = {
  goal: CFGoal;
  index: number;
  onUpdateGoal: (goal: CFGoal) => void;
};

const CounterfactualOutcomeEdit = (props: CounterfactualOutcomeEditProps) => {
  const { goal, index, onUpdateGoal } = props;

  if (goal.role === CFGoalRole.UNSUPPORTED) {
    return <CounterfactualOutcomeUnsupported goal={goal} />;
  }

  let valueEdit;
  switch (typeof goal.value) {
    case 'boolean':
      valueEdit = (
        <CounterfactualOutcomeBoolean
          goal={goal}
          index={index}
          onUpdateGoal={onUpdateGoal}
        />
      );
      break;
    case 'number':
      valueEdit = (
        <CounterfactualOutcomeNumber
          goal={goal}
          index={index}
          onUpdateGoal={onUpdateGoal}
        />
      );
      break;
    case 'string':
      valueEdit = (
        <CounterfactualOutcomeString
          goal={goal}
          index={index}
          onUpdateGoal={onUpdateGoal}
        />
      );
      break;
  }

  return (
    <>
      <Checkbox
        id={goal.id}
        isChecked={goal.role === CFGoalRole.FLOATING}
        onChange={checked => {
          let updatedRole = CFGoalRole.FIXED;
          if (checked) {
            updatedRole = CFGoalRole.FLOATING;
          } else if (goal.value === goal.originalValue) {
            updatedRole = CFGoalRole.ORIGINAL;
          }
          onUpdateGoal({
            ...goal,
            role: updatedRole
          });
        }}
        label={
          <FormGroup
            fieldId={goal.id}
            label={goal.name}
            style={{ paddingTop: '4px' }}
          />
        }
      />
      <div className={'counterfactual-outcome__child'}>{valueEdit}</div>
    </>
  );
};

export default CounterfactualOutcomeEdit;

const CounterfactualOutcomeBoolean = (
  props: CounterfactualOutcomeEditProps
) => {
  const { goal, onUpdateGoal } = props;
  const [booleanValue, setBooleanValue] = useState(goal.value as boolean);

  useEffect(() => {
    setBooleanValue(goal.value as boolean);
  }, [goal.value]);

  const handleChange = (checked: boolean) => {
    if (isFloating(goal)) {
      return;
    }
    onUpdateGoal({ ...goal, value: checked });
  };

  return (
    <Switch
      id={uuid()}
      label="True"
      labelOff="False"
      isChecked={booleanValue}
      isDisabled={isFloating(goal)}
      onChange={handleChange}
    />
  );
};

const CounterfactualOutcomeNumber = (props: CounterfactualOutcomeEditProps) => {
  const { goal, index, onUpdateGoal } = props;
  const [numberValue, setNumberValue] = useState<number>();

  const touchSpinWidth = useMemo(() => String(goal.value).length + 2, [
    goal.value
  ]);

  const onMinus = () => {
    if (isFloating(goal)) {
      return;
    }
    onUpdateGoal({ ...goal, value: (goal.value as number) - 1 });
  };

  const onChange = event => {
    if (isFloating(goal)) {
      return;
    }
    onUpdateGoal({ ...goal, value: Number(event.target.value) });
  };

  const onPlus = () => {
    if (isFloating(goal)) {
      return;
    }
    onUpdateGoal({ ...goal, value: (goal.value as number) + 1 });
  };

  useEffect(() => {
    setNumberValue(goal.value as number);
  }, [goal.value]);

  return (
    <Touchspin
      value={numberValue}
      onMinus={onMinus}
      onChange={onChange}
      onPlus={onPlus}
      inputName={`goal-${index}-${goal.name}`}
      id={`goal-${index}-${goal.name}`}
      inputAriaLabel="`${outcome.outcomeName} input`"
      minusBtnAriaLabel="minus"
      plusBtnAriaLabel="plus"
      widthChars={touchSpinWidth}
      isDisabled={isFloating(goal)}
    />
  );
};

const CounterfactualOutcomeString = (props: CounterfactualOutcomeEditProps) => {
  const { goal, index, onUpdateGoal } = props;

  const handleChange = (value: string) => {
    if (isFloating(goal)) {
      return;
    }
    onUpdateGoal({ ...goal, value });
  };

  return (
    <TextInput
      id={`goal-${index}-${goal.name}`}
      name={`goal-${index}-${goal.name}`}
      value={goal.value as string}
      onChange={handleChange}
      style={{ width: 250 }}
      isDisabled={isFloating(goal)}
    />
  );
};

const isFloating = (goal: CFGoal) => {
  return goal.role === CFGoalRole.FLOATING;
};
