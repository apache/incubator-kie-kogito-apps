import { Form } from '../../../../api';

import uuidv4 from 'uuid';

export function buildTestContext(form: Form) {
  return {
    user: {
      id: 'test',
      groups: ['group1', 'group2']
    },
    schema: JSON.parse(form.configuration.schema),
    task: {
      id: uuidv4(),
      description: 'task description',
      name: 'TaskName',
      referenceName: 'Task Name',
      priority: '1',
      processInstanceId: uuidv4(),
      processId: 'process',
      rootProcessInstanceId: null,
      rootProcessId: null,
      state: 'Ready',
      completed: null,
      started: Date.now(),
      lastUpdate: Date.now()
    },
    phases: ['abort', 'claim', 'complete', 'skip']
  };
}
