import React from 'react';

const MockedProcessVariables = ({ setDisplayLabel }): React.ReactElement => {
  React.useEffect(() => {
    setDisplayLabel(true);
  }, []);
  return <></>;
};

export default MockedProcessVariables;
