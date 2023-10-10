import React from 'react';

const MockedCloudEventFormContainer = ({
  onSuccess,
  onError
}): React.ReactElement => {
  React.useEffect(() => {
    onSuccess();
    onError();
  }, []);
  return <></>;
};

export default MockedCloudEventFormContainer;
