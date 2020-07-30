import { httpClient, callOnce } from './httpClient';
import { AxiosRequestConfig } from 'axios';

const EXECUTIONS_PATH = '/executions';

export const getExecutions = (
  searchString: string,
  from: string,
  to: string,
  limit: number,
  offset: number
) => {
  const config: AxiosRequestConfig = {
    url: EXECUTIONS_PATH,
    method: 'get',
    params: { search: searchString, from, to, limit, offset }
  };
  return callOnce(config);
};

export const getExecution = (id: string) => {
  const getExecConfig: AxiosRequestConfig = {
    url: `${EXECUTIONS_PATH}/decision/${id}`,
    method: 'get'
  };
  return httpClient(getExecConfig);
};

export const getDecisionOutcome = (id: string) => {
  const getDecisionOutcomeConfig: AxiosRequestConfig = {
    url: `${EXECUTIONS_PATH}/decision/${id}/outcomes`,
    method: 'get'
  };
  return httpClient(getDecisionOutcomeConfig);
};
