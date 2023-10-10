import { CustomForm } from '../types';

export interface TaskFormChannelApi {
  taskForm__getTaskFormSchema(): Promise<Record<string, any>>;
  taskForm__getCustomForm(): Promise<CustomForm>;
  taskForm__doSubmit(phase?: string, payload?: any): Promise<any>;
}
