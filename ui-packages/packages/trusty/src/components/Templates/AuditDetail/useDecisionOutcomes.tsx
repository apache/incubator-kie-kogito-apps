import { useEffect, useState } from 'react';
import { getDecisionOutcome } from '../../../utils/api/auditApi';
import { RemoteData, IOutcome } from '../../../types';

const useDecisionOutcomes = (executionId: string) => {
  const [outcomes, setOutcomes] = useState<RemoteData<Error, IOutcome[]>>({
    status: 'NOT_ASKED'
  });

  useEffect(() => {
    let isMounted = true;
    setOutcomes({ status: 'LOADING' });
    getDecisionOutcome(executionId)
      .then(response => {
        if (isMounted) {
          setOutcomes({ status: 'SUCCESS', data: response.data.outcomes });
        }
      })
      .catch(error => {
        setOutcomes({ status: 'FAILURE', error });
      });
    return () => {
      isMounted = false;
    };
  }, [executionId]);

  return outcomes;
};

export default useDecisionOutcomes;
