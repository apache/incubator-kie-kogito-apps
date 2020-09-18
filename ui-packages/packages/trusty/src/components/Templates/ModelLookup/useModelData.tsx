/**
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import { useEffect, useState } from 'react';
import { RemoteData, ModelData } from '../../../types';
import { AxiosRequestConfig } from 'axios';
import { EXECUTIONS_PATH, httpClient } from '../../../utils/api/httpClient';

const useModelData = (executionId: string) => {
  const [modelData, setModelData] = useState<RemoteData<Error, ModelData>>({
    status: 'NOT_ASKED'
  });

  useEffect(() => {
    let isMounted = true;
    const config: AxiosRequestConfig = {
      url: `${EXECUTIONS_PATH}/${executionId}/model`,
      method: 'get'
    };
    setModelData({ status: 'LOADING' });
    httpClient(config)
      .then(response => {
        if (isMounted) {
          setModelData({ status: 'SUCCESS', data: response.data });
        }
      })
      .catch(error => {
        setModelData({ status: 'FAILURE', error });
      });
    return () => {
      isMounted = false;
    };
  }, [executionId]);

  return modelData;
};

export default useModelData;
