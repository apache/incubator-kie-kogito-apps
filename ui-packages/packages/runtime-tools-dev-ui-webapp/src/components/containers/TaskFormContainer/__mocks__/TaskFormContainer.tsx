import React from 'react';

const MockedTaskFormContainer = ({
  onSubmitSuccess,
  onSubmitError
}): React.ReactElement => {
  React.useEffect(() => {
    try {
      onSubmitSuccess('complete');
    } catch (e) {
      onSubmitError();
    }
  }, []);
  return <></>;
};

export default MockedTaskFormContainer;
