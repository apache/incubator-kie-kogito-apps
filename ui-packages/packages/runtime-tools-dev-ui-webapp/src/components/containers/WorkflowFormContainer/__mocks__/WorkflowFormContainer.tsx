import React from 'react';

const MockedWorkflowFormContainer = ({
  onSubmitSuccess,
  onSubmitError
}): React.ReactElement => {
  React.useEffect(() => {
    onSubmitSuccess();
    onSubmitError();
  }, []);
  return <></>;
};

export default MockedWorkflowFormContainer;
