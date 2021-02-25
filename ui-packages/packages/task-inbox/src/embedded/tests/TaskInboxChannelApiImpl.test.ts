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

import { UserTaskInstance } from '../../types';
import { TaskInboxChannelApiImpl } from '../TaskInboxChannelApiImpl';
import {
  QueryFilter,
  SortBy,
  TaskInboxDriver,
  TaskInboxState
} from '../../api';
import { MockedTaskInboxDriver } from './utils/Mocks';

const initialState: TaskInboxState = {
  filters: {
    taskNames: [],
    taskStates: []
  },
  sortBy: {
    property: 'lastUpdate',
    direction: 'asc'
  },
  currentPage: {
    offset: 0,
    limit: 10
  }
};

const filter: QueryFilter = {
  taskStates: [],
  taskNames: []
};

const sortBy: SortBy = {
  property: 'lastUpdate',
  direction: 'asc'
};

export const userTask: UserTaskInstance = {
  id: '45a73767-5da3-49bf-9c40-d533c3e77ef3',
  description: null,
  name: 'VisaApplication',
  referenceName: 'Apply for visa',
  priority: '1',
  processInstanceId: '9ae7ce3b-d49c-4f35-b843-8ac3d22fa427',
  processId: 'travels',
  rootProcessInstanceId: null,
  rootProcessId: null,
  state: 'Ready',
  actualOwner: null,
  adminGroups: [],
  adminUsers: [],
  completed: null,
  started: new Date('2020-02-19T11:11:56.282Z'),
  excludedUsers: [],
  potentialGroups: [],
  potentialUsers: [],
  inputs:
    '{"Skippable":"true","trip":{"city":"Boston","country":"US","begin":"2020-02-19T23:00:00.000+01:00","end":"2020-02-26T23:00:00.000+01:00","visaRequired":true},"TaskName":"VisaApplication","NodeName":"Apply for visa","traveller":{"firstName":"Rachel","lastName":"White","email":"rwhite@gorle.com","nationality":"Polish","address":{"street":"Cabalone","city":"Zerf","zipCode":"765756","country":"Poland"}},"Priority":"1"}',
  outputs: '{}',
  lastUpdate: new Date('2020-02-19T11:11:56.282Z'),
  endpoint:
    'http://localhost:8080/travels/9ae7ce3b-d49c-4f35-b843-8ac3d22fa427/VisaApplication/45a73767-5da3-49bf-9c40-d533c3e77ef3'
};

let driver: TaskInboxDriver;
let api: TaskInboxChannelApiImpl;

describe('TaskInboxChannelApiImpl tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    driver = new MockedTaskInboxDriver();
    api = new TaskInboxChannelApiImpl(driver);
  });

  it('taskInbox__setInitialState', () => {
    api.taskInbox__setInitialState(initialState);
    expect(driver.setInitialState).toHaveBeenCalledWith(initialState);
  });

  it('taskInbox__applyFilter', () => {
    api.taskInbox__applyFilter(filter);
    expect(driver.applyFilter).toHaveBeenCalledWith(filter);
  });

  it('taskInbox__applySorting', () => {
    api.taskInbox__applySorting(sortBy);
    expect(driver.applySorting).toHaveBeenCalledWith(sortBy);
  });

  it('taskInbox__query', () => {
    api.taskInbox__query(0, 10);
    expect(driver.query).toHaveBeenCalledWith(0, 10);
  });

  it('taskInbox__refresh', () => {
    api.taskInbox__refresh();
    expect(driver.refresh).toHaveBeenCalled();
  });

  it('taskInbox__openTask', () => {
    api.taskInbox__openTask(userTask);
    expect(driver.openTask).toHaveBeenCalledWith(userTask);
  });
});
