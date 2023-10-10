import {
  FormDetailsDriver,
  FormDetailsChannelApi,
  Form,
  FormContent
} from '../api';

/**
 * Implementation of the TaskInboxChannelApi delegating to a TaskInboxDriver
 */
export class FormDetailsChannelApiImpl implements FormDetailsChannelApi {
  constructor(protected readonly driver: FormDetailsDriver) {}

  formDetails__getFormContent(formName: string): Promise<Form> {
    return this.driver.getFormContent(formName);
  }

  formDetails__saveFormContent(
    formName: string,
    formContent: FormContent
  ): Promise<void> {
    return this.driver.saveFormContent(formName, formContent);
  }
}
