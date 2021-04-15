import React from 'react';
import { CFResult } from '../../Templates/Counterfactual/Counterfactual';
import { Badge } from '@patternfly/react-core';
import './CounterfactualExecutionInfo.scss';

type CounterfactualExecutionInfoProps = {
  results: CFResult[];
};

const CounterfactualExecutionInfo = (
  props: CounterfactualExecutionInfoProps
) => {
  const { results } = props;
  return (
    <span className="cf-execution-info">
      <span className="cf-execution-info__label">Total Results</span>
      <Badge>{results[0] && results[0].length}</Badge>
    </span>
  );
};

export default CounterfactualExecutionInfo;
