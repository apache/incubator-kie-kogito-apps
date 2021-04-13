import React from 'react';
import { CFSearchInput } from '../../Templates/Counterfactual/Counterfactual';

type CounterfactualInputDomainProps = {
  input: CFSearchInput;
};

const CounterfactualInputDomain = ({
  input
}: CounterfactualInputDomainProps) => {
  let domain;
  switch (input.domain.type) {
    case 'numerical':
      domain = (
        <span>
          {input.domain.lowerBound}-{input.domain.upperBound}
        </span>
      );
      break;
    default:
      domain = '';
  }
  return <>{domain}</>;
};

export default CounterfactualInputDomain;
