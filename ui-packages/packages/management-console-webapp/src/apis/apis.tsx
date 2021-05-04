/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// import React from 'react';
import { GraphQL } from '@kogito-apps/consoles-common';
import axios from 'axios';
import { ProcessInstance } from '@kogito-apps/management-console-shared';

//Rest Api to Cancel multiple Jobs
export const performMultipleCancel = async (
  jobsToBeActioned: (GraphQL.Job & { errorMessage?: string })[]
) => {
  const successJobs = [];
  const failedJobs = [];
  for (const job of jobsToBeActioned) {
    try {
      await axios.delete(`${job.endpoint}/${job.id}`);
      successJobs.push(job);
    } catch (error) {
      job.errorMessage = JSON.stringify(error.message);
      failedJobs.push(job);
    }
  }
  return { successJobs, failedJobs };
};

//Rest Api to Cancel a Job
export const jobCancel = async (
  job: Pick<GraphQL.Job, 'id' | 'endpoint'>
): Promise<{ modalTitle: string; modalContent: string }> => {
  let modalTitle: string;
  let modalContent: string;
  try {
    await axios.delete(`${job.endpoint}/${job.id}`);
    modalTitle = 'success';
    modalContent = `The job: ${job.id} is canceled successfully`;
    return { modalTitle, modalContent };
  } catch (error) {
    modalTitle = 'failure';
    modalContent = `The job: ${job.id} failed to cancel. Error message: ${error.message}`;
    return { modalTitle, modalContent };
  }
};

// Rest Api to Reschedule a Job
export const handleJobReschedule = async (
  job,
  repeatInterval: number | string,
  repeatLimit: number | string,
  scheduleDate: Date
): Promise<{ modalTitle: string; modalContent: string }> => {
  let modalTitle: string;
  let modalContent: string;
  let parameter = {};
  if (repeatInterval === null && repeatLimit === null) {
    parameter = {
      expirationTime: new Date(scheduleDate)
    };
  } else {
    parameter = {
      expirationTime: new Date(scheduleDate),
      repeatInterval,
      repeatLimit
    };
  }
  try {
    await axios.patch(`${job.endpoint}/${job.id}`, parameter);
    modalTitle = 'success';
    modalContent = `Reschedule of job: ${job.id} is successful`;
    return { modalTitle, modalContent };
  } catch (error) {
    modalTitle = 'failure';
    modalContent = `Reschedule of job ${job.id} failed. Message: ${error.message}`;
    return { modalTitle, modalContent };
  }
};

// Rest Api to fetch Process Diagram
export const getSvg = async (data: ProcessInstance): Promise<any> => {
  return await axios
    .get(`/svg/processes/${data.processId}/instances/${data.id}`)
    .then(res => {
      return { svg: res.data };
    })
    .catch(async error => {
      /* istanbul ignore else*/
      if (data.serviceUrl) {
        return axios
          .get(
            `${data.serviceUrl}/svg/processes/${data.processId}/instances/${data.id}`
          )
          .then(res => {
            return { svg: res.data };
          })
          .catch(err => {
            /* istanbul ignore else*/
            if (err.response && err.response.status !== 404) {
              return { error: err.message };
            }
          });
      }
    });
};
