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

import { ProcessInstance } from '@kogito-apps/management-console-shared';
import {
  ProcessInstanceFilter,
  ProcessListDriver,
  SortBy
} from '../../../../api';

export default class TestProcessListDriver implements ProcessListDriver {
  private readonly processInstances: ProcessInstance[];
  private readonly childProcessInstances: ProcessInstance[];
  private offset: number = 0;
  private limit: number = 10;

  constructor(
    processInstances: ProcessInstance[],
    childProcessInstances: ProcessInstance[]
  ) {
    this.processInstances = processInstances;
    this.childProcessInstances = childProcessInstances;
  }
  /* eslint-disable  @typescript-eslint/no-unused-vars */
  private doSetState(processListFilter: ProcessInstanceFilter, sortBy: SortBy) {
    // do nothing
  }

  initialLoad(filter: ProcessInstanceFilter, sortBy: SortBy): Promise<void> {
    this.doSetState(filter, sortBy);
    return Promise.resolve();
  }

  openProcess(process: ProcessInstance): Promise<void> {
    return Promise.resolve();
  }
  applyFilter(filter: ProcessInstanceFilter): Promise<void> {
    // do nothing
    return Promise.resolve();
  }

  applySorting(sortBy: SortBy): Promise<void> {
    // do nothing
    return Promise.resolve(undefined);
  }

  query(offset: number, limit: number): Promise<ProcessInstance[]> {
    this.offset = offset;
    this.limit = limit;
    return this.doQuery(offset, this.getQueryLimit());
  }

  getChildProcessesQuery(
    rootProcessInstanceId: string
  ): Promise<ProcessInstance[]> {
    return Promise.resolve(this.childProcessInstances.slice(0, 10));
  }
  /* eslint-enable  @typescript-eslint/no-unused-vars */
  private doQuery(start: number, limit: number): Promise<ProcessInstance[]> {
    const queryLimit =
      limit > this.processInstances.length
        ? this.processInstances.length
        : limit;

    return Promise.resolve(this.processInstances.slice(start, queryLimit));
  }

  private getQueryLimit = (): number => {
    return this.offset + this.limit;
  };
}
