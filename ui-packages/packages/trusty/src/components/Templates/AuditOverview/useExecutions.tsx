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
import { useCallback, useEffect, useMemo, useState } from 'react';
import {
  EXECUTIONS_PATH,
  callOnceHandler
} from '../../../utils/api/httpClient';
import { RemoteData, Executions } from '../../../types';
import axios, { AxiosRequestConfig } from 'axios';

type useExecutionsParameters = {
  searchString: string;
  from: string;
  to: string;
  limit: number;
  offset: number;
};

const useExecutions = (parameters: useExecutionsParameters) => {
  const { searchString, from, to, limit, offset } = parameters;
  const [executions, setExecutions] = useState<RemoteData<Error, Executions>>({
    status: 'NOT_ASKED'
  });

  const getExecutions = useMemo(() => callOnceHandler(), []);

  const loadExecutions = useCallback(() => {
    let isMounted = true;
    setExecutions({ status: 'LOADING' });

    const config: AxiosRequestConfig = {
      url: EXECUTIONS_PATH,
      method: 'get',
      params: { search: searchString, from, to, limit, offset }
    };

    getExecutions(config)
      .then(response => {
        if (isMounted) {
          setExecutions({ status: 'SUCCESS', data: response.data });
        }
      })
      .catch(error => {
        if (!axios.isCancel(error)) {
          setExecutions({ status: 'FAILURE', error });
        }
      });
    return () => {
      isMounted = false;
    };
  }, [searchString, from, to, limit, offset]);

  useEffect(() => {
    loadExecutions();
  }, [searchString, from, to, limit, offset, loadExecutions]);

  return { loadExecutions, executions };
};

export default useExecutions;
