import React, { useEffect, useRef, useState } from 'react';
import { Progress, ProgressSize } from '@patternfly/react-core';

const CounterfactualProgressBar = () => {
  const [value, setValue] = useState(0);

  const intervalID = useRef(null);

  useEffect(() => {
    if (value === 0 && intervalID.current === null) {
      intervalID.current = window.setInterval(() => {
        setValue(prev => prev + 1);
      }, 1000);
    }
    if (value === 10) {
      clearInterval(intervalID.current);
    }
  }, [value, intervalID]);

  return (
    <Progress
      value={(value * 100) / 10}
      title="Calculating..."
      size={ProgressSize.sm}
      style={{ width: 400 }}
      label={`${10 - value} seconds remaining`}
    />
  );
};

export default CounterfactualProgressBar;
