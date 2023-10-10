import { CustomForm } from '../types';

/**
 * Interface that defines a Driver for TaskForm views.
 */
export interface TaskFormDriver {
  getTaskFormSchema(): Promise<Record<string, any>>;
  getCustomForm(): Promise<CustomForm>;
  doSubmit(phase?: string, payload?: any): Promise<any>;
}
