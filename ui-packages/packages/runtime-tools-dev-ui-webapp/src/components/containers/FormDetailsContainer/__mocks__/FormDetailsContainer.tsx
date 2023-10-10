import React from 'react';

const MockedFormDetailsContainer = ({
  onSuccess,
  onError
}): React.ReactElement => {
  React.useEffect(() => {
    onSuccess();
    onError();
  }, []);
  return <></>;
};

export default MockedFormDetailsContainer;
