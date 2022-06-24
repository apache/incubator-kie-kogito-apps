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

import { ApolloClient } from 'apollo-client';
import { GraphQL } from '@kogito-apps/consoles-common';
import {
  ProcessInstance,
  Job,
  JobCancel,
  TriggerableNode,
  NodeInstance
} from '@kogito-apps/management-console-shared';
import {
  handleProcessAbort,
  handleProcessSkip,
  handleProcessRetry,
  jobCancel,
  handleJobReschedule,
  handleNodeTrigger,
  handleProcessVariableUpdate,
  handleNodeInstanceCancel,
  handleNodeInstanceRetrigger
} from '@kogito-apps/runtime-gateway-api';

export interface ProcessDetailsQueries {
  getProcessDetails(id: string): Promise<ProcessInstance>;
  getJobs(id: string): Promise<Job[]>;
  handleProcessSkip(processInstance: ProcessInstance): Promise<void>;
  handleProcessAbort(processInstance: ProcessInstance): Promise<void>;
  handleProcessRetry(processInstance: ProcessInstance): Promise<void>;
  getSVG(processInstance: ProcessInstance): Promise<any>;
  jobCancel(job: Job): Promise<JobCancel>;
  rescheduleJob: (
    job,
    repeatInterval: number | string,
    repeatLimit: number | string,
    scheduleDate: Date
  ) => Promise<{ modalTitle: string; modalContent: string }>;
  getTriggerableNodes(
    processInstance: ProcessInstance
  ): Promise<TriggerableNode[]>;
  handleNodeTrigger(processInstance: ProcessInstance, node: any): Promise<void>;
  handleProcessVariableUpdate: (
    processInstance: ProcessInstance,
    updateJson: Record<string, unknown>
  ) => Promise<Record<string, unknown>>;
  handleNodeInstanceCancel: (
    processInstance: ProcessInstance,
    node: NodeInstance
  ) => Promise<void>;
  handleNodeInstanceRetrigger(
    processInstance: ProcessInstance,
    node: NodeInstance
  ): Promise<void>;
}

export class GraphQLProcessDetailsQueries implements ProcessDetailsQueries {
  private readonly client: ApolloClient<any>;

  constructor(client: ApolloClient<any>) {
    this.client = client;
  }

  async getProcessDetails(id: string): Promise<ProcessInstance> {
    try {
      const response = await this.client.query({
        query: GraphQL.GetProcessInstanceByIdDocument,
        variables: {
          id
        },
        fetchPolicy: 'network-only'
      });
      const emptyResponse = {} as ProcessInstance;
      if (response && response.data.ProcessInstances.length > 0) {
        return Promise.resolve(response.data.ProcessInstances[0]);
      } else {
        return Promise.resolve(emptyResponse);
      }
    } catch (error) {
      return Promise.reject(error);
    }
  }

  async getJobs(id: string): Promise<Job[]> {
    try {
      const response = await this.client.query({
        query: GraphQL.GetJobsByProcessInstanceIdDocument,
        variables: {
          processInstanceId: id
        },
        fetchPolicy: 'network-only'
      });
      return Promise.resolve(response.data.Jobs);
    } catch (error) {
      return Promise.reject(error);
    }
  }

  async handleProcessSkip(processInstance: ProcessInstance): Promise<void> {
    return handleProcessSkip(processInstance, this.client);
  }

  async handleProcessAbort(processInstance: ProcessInstance): Promise<void> {
    return handleProcessAbort(processInstance, this.client);
  }

  async handleProcessRetry(processInstance: ProcessInstance): Promise<void> {
    return handleProcessRetry(processInstance, this.client);
  }

  async getSVG(processInstance: ProcessInstance): Promise<any> {
    return this.client
      .query({
        query: GraphQL.GetProcessInstanceSvgDocument,
        variables: {
          processsId: processInstance.id
        },
        fetchPolicy: 'network-only'
      })
      .then(value => {
        return { svg: value.data.ProcessInstances[0].diagram };
      })
      .catch(reason => {
        return { error: reason.message };
      });
  }

  async jobCancel(job: Job): Promise<JobCancel> {
    return jobCancel(job, this.client);
  }

  async rescheduleJob(
    job,
    repeatInterval: number | string,
    repeatLimit: number | string,
    scheduleDate: Date
  ): Promise<{ modalTitle: string; modalContent: string }> {
    return handleJobReschedule(
      job,
      repeatInterval,
      repeatLimit,
      scheduleDate,
      this.client
    );
  }

  async getTriggerableNodes(
    processInstance: ProcessInstance
  ): Promise<TriggerableNode[]> {
    return new Promise((resolve, reject) => {
      this.client
        .query({
          query: GraphQL.GetProcessInstanceNodeDefinitionsDocument,
          variables: {
            processsId: processInstance.id
          },
          fetchPolicy: 'no-cache'
        })
        .then(value => {
          resolve(value.data.ProcessInstances[0].nodeDefinitions);
        })
        .catch(reason => reject(reason));
    });
  }

  async handleNodeTrigger(
    processInstance: ProcessInstance,
    node: any
  ): Promise<void> {
    return handleNodeTrigger(processInstance, node, this.client);
  }

  async handleNodeInstanceCancel(
    processInstance: ProcessInstance,
    node: NodeInstance
  ): Promise<void> {
    return handleNodeInstanceCancel(processInstance, node, this.client);
  }

  async handleProcessVariableUpdate(
    processInstance: ProcessInstance,
    updateJson: Record<string, unknown>
  ): Promise<Record<string, unknown>> {
    return handleProcessVariableUpdate(
      processInstance,
      updateJson,
      this.client
    );
  }

  async handleNodeInstanceRetrigger(
    processInstance: ProcessInstance,
    node: NodeInstance
  ): Promise<void> {
    return handleNodeInstanceRetrigger(processInstance, node, this.client);
  }
}
