import React, { useEffect, useMemo, useState } from 'react';
import { Checkbox, Switch, Touchspin } from '@patternfly/react-core';
import { v4 as uuid } from 'uuid';

import { Outcome } from '../../../types';
import './CounterfactualOutcome.scss';

interface CounterfactualOutcomeProps {
  outcome: Outcome;
}

const CounterfactualOutcome = ({ outcome }: CounterfactualOutcomeProps) => {
  const [isChecked, setIsChecked] = useState(false);

  const handleChange = (checked: boolean) => {
    setIsChecked(checked);
  };

  let valueEdit;
  switch (outcome.outcomeResult.typeRef) {
    case 'boolean':
      valueEdit = (
        <CounterfactualOutcomeBoolean
          outcome={outcome}
          isDisabled={!isChecked}
        />
      );
      break;
    case 'number':
      valueEdit = (
        <CounterfactualOutcomeNumber
          outcome={outcome}
          isDisabled={!isChecked}
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
        label={outcome.outcomeName}
        isChecked={isChecked}
        onChange={handleChange}
        aria-label={outcome.outcomeName}
        id={outcome.outcomeId}
        name={outcome.outcomeName}
      />
      <section className="counterfactual-outcome__value">{valueEdit}</section>
    </section>
  );
};

export default CounterfactualOutcome;

interface CounterfactualOutcomeValueProps extends CounterfactualOutcomeProps {
  isDisabled: boolean;
}

const CounterfactualOutcomeBoolean = ({
  outcome,
  isDisabled
}: CounterfactualOutcomeValueProps) => {
  const [booleanValue, setBooleanValue] = useState<boolean>();

  useEffect(() => {
    if (isDisabled) {
      setBooleanValue(outcome.outcomeResult.value as boolean);
    }
  }, [isDisabled, outcome.outcomeResult.value]);

  return (
    <Switch
      id={uuid()}
      label="True"
      labelOff="False"
      isChecked={booleanValue}
      onChange={setBooleanValue}
      isDisabled={isDisabled}
    />
  );
};

const CounterfactualOutcomeNumber = ({
  outcome,
  isDisabled
}: CounterfactualOutcomeValueProps) => {
  const [numberValue, setNumberValue] = useState<number>();

  const touchSpinWidth = useMemo(
    () => String(outcome.outcomeResult.value).length + 2,
    [outcome.outcomeResult.value]
  );

  const onMinus = () => {
    setNumberValue(numberValue - 1);
  };

  const onChange = event => {
    setNumberValue(Number(event.target.value));
  };

  const onPlus = () => {
    setNumberValue(numberValue + 1);
  };

  useEffect(() => {
    if (isDisabled) {
      setNumberValue(outcome.outcomeResult.value as number);
    }
  }, [isDisabled, outcome.outcomeResult.value]);

  return (
    <Touchspin
      value={numberValue}
      onMinus={onMinus}
      onChange={onChange}
      onPlus={onPlus}
      inputName={`${outcome.outcomeName} value`}
      inputAriaLabel="`${outcome.outcomeName} input`"
      minusBtnAriaLabel="minus"
      plusBtnAriaLabel="plus"
      isDisabled={isDisabled}
      widthChars={touchSpinWidth}
    />
  );
};
