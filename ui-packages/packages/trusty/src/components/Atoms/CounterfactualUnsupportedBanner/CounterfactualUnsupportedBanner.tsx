import React from 'react';
import {
  Hint,
  HintBody,
  HintTitle,
  Split,
  SplitItem
} from '@patternfly/react-core';
import { IconSize, InfoCircleIcon } from '@patternfly/react-icons';
import './CounterfactualUnsupportedBanner.scss';

const CounterfactualUnsupportedBanner = () => {
  return (
    <Hint className="cf-unsupported-banner">
      <HintTitle>Experimental Feature</HintTitle>
      <HintBody>
        <Split hasGutter={true} className="cf-unsupported-banner__text">
          <SplitItem>
            <InfoCircleIcon
              size={IconSize.lg}
              color={'var(--pf-global--info-color--100)'}
            />
          </SplitItem>
          <SplitItem isFilled={true}>
            Counterfactuals is an experimental feature and does not currently
            support all types of model.
          </SplitItem>
        </Split>
      </HintBody>
    </Hint>
  );
};

export default CounterfactualUnsupportedBanner;
