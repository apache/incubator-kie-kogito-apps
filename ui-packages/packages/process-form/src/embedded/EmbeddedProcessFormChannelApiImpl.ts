import {
  ProcessDefinition,
  ProcessFormChannelApi,
  ProcessFormDriver
} from '../api';

export class EmbeddedProcessFormChannelApiImpl
  implements ProcessFormChannelApi
{
  constructor(private readonly driver: ProcessFormDriver) {}

  processForm__getProcessFormSchema(
    processDefinitionData: ProcessDefinition
  ): Promise<Record<string, any>> {
    return this.driver.getProcessFormSchema(processDefinitionData);
  }

  processForm__startProcess(formData: any): Promise<void> {
    return this.driver.startProcess(formData);
  }
}
