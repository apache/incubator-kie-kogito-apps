import React, { useEffect, useMemo, useState } from 'react';
import { Checkbox, Switch, Touchspin } from '@patternfly/react-core';
import { v4 as uuid } from 'uuid';
import { CFGoal } from '../../Templates/Counterfactual/Counterfactual';
import './CounterfactualOutcomeEdit.scss';

interface CounterfactualOutcomeEditProps {
  goal: CFGoal;
  onUpdateGoal: (goal: CFGoal) => void;
}

const CounterfactualOutcomeEdit = (props: CounterfactualOutcomeEditProps) => {
  const { goal, onUpdateGoal } = props;
  const [isChecked, setIsChecked] = useState<boolean>();

  const handleChange = (checked: boolean) => {
    if (checked) {
      onUpdateGoal({ ...goal, isFixed: !checked });
    } else {
      onUpdateGoal({ ...goal, isFixed: !checked, value: goal.originalValue });
    }
  };

  useEffect(() => {
    setIsChecked(!goal.isFixed);
  }, [goal.isFixed]);

  let valueEdit;
  switch (goal.typeRef) {
    case 'boolean':
      valueEdit = (
        <CounterfactualOutcomeBoolean
          goal={goal}
          isDisabled={!isChecked}
          onUpdateGoal={onUpdateGoal}
        />
      );
      break;
    case 'number':
      valueEdit = (
        <CounterfactualOutcomeNumber
          goal={goal}
          isDisabled={!isChecked}
          onUpdateGoal={onUpdateGoal}
        />
      );
      break;
    default:
      valueEdit = <em>Not supported</em>;
      break;
  }

  return (
    <section className="counterfactual-outcome">
      <Checkbox
        label={goal.name}
        isChecked={isChecked}
        onChange={handleChange}
        aria-label={goal.name}
        id={goal.name}
        name={goal.name}
      />
      <section className="counterfactual-outcome__value">{valueEdit}</section>
    </section>
  );
};

export default CounterfactualOutcomeEdit;

interface CounterfactualOutcomeValueProps
  extends CounterfactualOutcomeEditProps {
  isDisabled: boolean;
  onUpdateGoal: (goal: CFGoal) => void;
}

const CounterfactualOutcomeBoolean = (
  props: CounterfactualOutcomeValueProps
) => {
  const { goal, isDisabled, onUpdateGoal } = props;
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
      isDisabled={isDisabled}
    />
  );
};

const CounterfactualOutcomeNumber = (
  props: CounterfactualOutcomeValueProps
) => {
  const { goal, isDisabled, onUpdateGoal } = props;
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
      inputName={`${goal.name} value`}
      inputAriaLabel="`${outcome.outcomeName} input`"
      minusBtnAriaLabel="minus"
      plusBtnAriaLabel="plus"
      isDisabled={isDisabled}
      widthChars={touchSpinWidth}
    />
  );
};
