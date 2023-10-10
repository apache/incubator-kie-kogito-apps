import React from 'react';

const MockedProcessFormContainer = ({
  onSubmitSuccess,
  onSubmitError
}): React.ReactElement => {
  React.useEffect(() => {
    onSubmitSuccess();
    onSubmitError();
  }, []);
  return <></>;
};

export default MockedProcessFormContainer;
