import React, { useEffect, useMemo, useState } from 'react';
import { Badge, FlexItem } from '@patternfly/react-core';
import { v4 as uuid } from 'uuid';

import './CounterfactualExecutionInfo.scss';
import FormattedDate from '../../Atoms/FormattedDate/FormattedDate';
import { CFResult } from '../../../types';

type CounterfactualExecutionInfoProps = {
  results: CFResult[];
};

const CounterfactualExecutionInfo = (
  props: CounterfactualExecutionInfoProps
) => {
  const { results } = props;
  const executionDate = useMemo(() => new Date().toISOString(), []);
  const [id, setId] = useState(uuid());

  useEffect(() => {
    const timer = setInterval(() => {
      setId(uuid());
    }, 61000);
    return () => {
      clearInterval(timer);
    };
  }, []);

  return (
    <>
      <FlexItem>
        <span className="cf-execution-info">
          <span className="cf-execution-info__label">Completed</span>
          <span key={id}>
            <FormattedDate date={executionDate} />
          </span>
        </span>
      </FlexItem>
      <FlexItem>
        <span className="cf-execution-info">
          <span className="cf-execution-info__label">Total Results</span>
          <Badge>{results[0] && results[0].length}</Badge>
        </span>
      </FlexItem>
    </>
  );
};

export default CounterfactualExecutionInfo;
