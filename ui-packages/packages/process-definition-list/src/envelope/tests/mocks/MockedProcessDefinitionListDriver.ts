import { ProcessDefinition, ProcessDefinitionListDriver } from '../../../api';

export class MockedProcessDefinitionListDriver
  implements ProcessDefinitionListDriver
{
  setProcessDefinitionFilter(filter: any) {
    return Promise.resolve();
  }
  getProcessDefinitionFilter() {
    return Promise.resolve(['process1,process2,process3']);
  }
  getProcessDefinitionsQuery(): Promise<ProcessDefinition[]> {
    return Promise.resolve([
      {
        processName: 'process1',
        endpoint: 'http://localhost:4000'
      },
      {
        processName: 'process2',
        endpoint: 'http://localhost:4000'
      },
      {
        processName: 'process3',
        endpoint: 'http://localhost:4000'
      }
    ]);
  }
  openProcessForm(processDefinition: ProcessDefinition): Promise<void> {
    return Promise.resolve();
  }
  openTriggerCloudEvent(): void {}
}
