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

const fs = require('fs');
const _ = require('lodash');
const graphQL = require('./graphql');
const confirmTravelForm = require('./forms/ConfirmTravel');
const applyForVisaForm = require('./forms/ApplyForVisa');

const restData = require('./rest');

const tasksUnableToTransition = [
  '047ec38d-5d57-4330-8c8d-9bd67b53a529',
  '841b9dba-3d91-4725-9de3-f9f4853b417e'
]

module.exports = controller = {
  callCompleteTask: (req, res) => {
    console.log(
      `......ProcessId:${req.params.processId} --piId:${req.params.processId} --taskId:${req.params.taskId}`
    );

    console.log(req.data);

    const processId = restData.process.filter(data => {
      return data.processId === req.params.processId;
    });

    const task = graphQL.UserTaskInstances.find(userTask => {
      return userTask.id === req.params.taskId;
    });

    if (tasksUnableToTransition.includes(task.id)) {
      res.status(500).send('Unexpected failure when doing a transition!');
    } else {
      const phase = req.query.phase;

      if(phase === 'complete') {
        task.state = 'Completed';
        task.completed = new Date().toISOString()
      }

      res.send(task.inputs);
    }
  },

  getTaskForm: (req, res) => {
    console.log(
      `......ProcessId:${req.params.processId} --piId:${req.params.processInstanceId} --taskId:${req.params.taskId}`
    );

    const task = graphQL.UserTaskInstances.find(userTask => {
      return userTask.id === req.params.taskId;
    });

    const clearPhases = task.completed || task.state === 'Aborted';

    res.send(JSON.stringify(getTaskSchema(task.name, clearPhases)));
  },

  getTaskDefinitionForm: (req, res) => {
    console.log(
      `......ProcessId:${req.params.processId} --TaskName:${req.params.taskName}`
    );

    res.send(JSON.stringify(getTaskSchema(req.params.taskName, true)));
  }
};

function getTaskSchema (taskName, clearPhases) {
  let schema;

  console.log(`Getting Schema for task: ${taskName} --clearPhases: ${clearPhases}`)

  switch (taskName) {
    case 'ConfirmTravel': {
      schema = _.cloneDeep(confirmTravelForm);
      break;
    }
    case 'VisaApplication': {
      schema = _.cloneDeep(applyForVisaForm);
      break;
    }
  }

  if(clearPhases) {
    delete schema.phases;
  }

  return schema;
}