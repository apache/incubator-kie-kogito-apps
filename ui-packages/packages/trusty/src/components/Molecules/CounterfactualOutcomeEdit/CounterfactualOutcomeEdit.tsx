import React, { useEffect, useMemo, useState } from 'react';
import {
  FormGroup,
  Switch,
  TextInput,
  Touchspin
} from '@patternfly/react-core';
import { v4 as uuid } from 'uuid';
import { CFGoal } from '../../../types';

type CounterfactualOutcomeEditProps = {
  goal: CFGoal;
  index: number;
  onUpdateGoal: (goal: CFGoal) => void;
};

const CounterfactualOutcomeEdit = (props: CounterfactualOutcomeEditProps) => {
  const { goal, index, onUpdateGoal } = props;

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
    default:
      valueEdit = <em>Not yet supported</em>;
      break;
  }

  return (
    <FormGroup label={goal.name} fieldId={goal.id}>
      {valueEdit}
    </FormGroup>
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
    onUpdateGoal({ ...goal, value: checked });
  };

  return (
    <Switch
      id={uuid()}
      label="True"
      labelOff="False"
      isChecked={booleanValue}
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
    onUpdateGoal({ ...goal, value: (goal.value as number) - 1 });
  };

  const onChange = event => {
    onUpdateGoal({ ...goal, value: Number(event.target.value) });
  };

  const onPlus = () => {
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
    />
  );
};

const CounterfactualOutcomeString = (props: CounterfactualOutcomeEditProps) => {
  const { goal, index, onUpdateGoal } = props;

  const handleChange = (value: string) => {
    onUpdateGoal({ ...goal, value });
  };

  return (
    <TextInput
      id={`goal-${index}-${goal.name}`}
      name={`goal-${index}-${goal.name}`}
      value={goal.value as string}
      onChange={handleChange}
      style={{ width: 250 }}
    />
  );
};
