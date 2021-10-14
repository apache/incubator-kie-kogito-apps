import { useEffect, useState } from 'react';
import { ItemObject, RemoteData, RemoteDataStatus } from '../../../types';
import { AxiosRequestConfig } from 'axios';
import { EXECUTIONS_PATH, httpClient } from '../../../utils/api/httpClient';

const useOutcomeDetail = (executionId: string, outcomeId: string | null) => {
  const [outcomeDetail, setOutcomeDetail] = useState<
    RemoteData<Error, ItemObject[]>
  >({
    status: RemoteDataStatus.NOT_ASKED
  });

  useEffect(() => {
    let isMounted = true;
    if (executionId && outcomeId) {
      const config: AxiosRequestConfig = {
        url: `${EXECUTIONS_PATH}/decisions/${executionId}/outcomes/${outcomeId}`,
        method: 'get'
      };
      setOutcomeDetail({ status: RemoteDataStatus.LOADING });
      httpClient(config)
        .then(response => {
          if (isMounted) {
            setOutcomeDetail({
              status: RemoteDataStatus.SUCCESS,
              data: response.data.outcomeInputs
            });
          }
        })
        .catch(error => {
          setOutcomeDetail({ status: RemoteDataStatus.FAILURE, error });
        });
    }
    return () => {
      isMounted = false;
    };
  }, [executionId, outcomeId]);

  return outcomeDetail;
};

export default useOutcomeDetail;
