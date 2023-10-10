import { TaskFormChannelApi, TaskFormDriver } from '../api';
import { CustomForm } from '../types';

export class EmbeddedTaskFormChannelApiImpl implements TaskFormChannelApi {
  constructor(private readonly driver: TaskFormDriver) {}

  taskForm__doSubmit(phase?: string, payload?: any): Promise<any> {
    return this.driver.doSubmit(phase, payload);
  }

  taskForm__getTaskFormSchema(): Promise<Record<string, any>> {
    return this.driver.getTaskFormSchema();
  }

  taskForm__getCustomForm(): Promise<CustomForm> {
    return this.driver.getCustomForm();
  }
}
