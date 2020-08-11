import { useEffect, useState } from 'react';
import { getExecution } from '../../../utils/api/auditApi';
import { RemoteData, Execution } from '../../../types';

const useExecutionInfo = (executionId: string) => {
  const [execution, setExecution] = useState<RemoteData<Error, Execution>>({
    status: 'NOT_ASKED'
  });

  useEffect(() => {
    let isMounted = true;
    setExecution({ status: 'LOADING' });
    getExecution(executionId)
      .then(response => {
        if (isMounted) {
          setExecution({ status: 'SUCCESS', data: response.data });
        }
      })
      .catch(error => {
        setExecution({ status: 'FAILURE', error });
      });
    return () => {
      isMounted = false;
    };
  }, [executionId]);

  return execution;
};

export default useExecutionInfo;
