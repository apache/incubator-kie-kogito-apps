import React from 'react';
import { FormGroup, Text, TextVariants } from '@patternfly/react-core';
import { CFGoal, CFGoalRole } from '../../../types';
import './CounterfactualOutcome.scss';
import CounterfactualOutcomeUnsupported from '../../Atoms/CounterfactualOutcomeUnsupported/CounterfactualOutcomeUnsupported';

type CounterfactualOutcomeProps = {
  goal: CFGoal;
};

const CounterfactualOutcome = (props: CounterfactualOutcomeProps) => {
  const { goal } = props;

  if (goal.role === CFGoalRole.UNSUPPORTED) {
    return <CounterfactualOutcomeUnsupported goal={goal} />;
  }

  let value;
  switch (typeof goal.value) {
    case 'boolean':
      value = <CounterfactualOutcomeBoolean goal={goal} />;
      break;
    case 'number':
      value = <CounterfactualOutcomeNumber goal={goal} />;
      break;
    case 'string':
      value = <CounterfactualOutcomeString goal={goal} />;
      break;
  }

  return (
    <FormGroup fieldId={goal.id} label={goal.name}>
      {value}
    </FormGroup>
  );
};

export default CounterfactualOutcome;

const CounterfactualOutcomeBoolean = (props: CounterfactualOutcomeProps) => {
  const { goal } = props;

  return (
    <Text component={TextVariants.p} className="counterfactual-outcome__text">
      {goal.originalValue ? 'True' : 'False'}
    </Text>
  );
};

const CounterfactualOutcomeNumber = (props: CounterfactualOutcomeProps) => {
  const { goal } = props;
  return (
    <Text component={TextVariants.p} className="counterfactual-outcome__text">
      {goal.originalValue}
    </Text>
  );
};

const CounterfactualOutcomeString = (props: CounterfactualOutcomeProps) => {
  const { goal } = props;

  return (
    <Text component={TextVariants.p} className="counterfactual-outcome__text">
      {goal.originalValue}
    </Text>
  );
};
