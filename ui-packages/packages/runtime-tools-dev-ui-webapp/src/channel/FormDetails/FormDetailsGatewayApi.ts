import { Form, FormContent } from '@kogito-apps/form-details';

import { getFormContent, saveFormContent } from '../apis';

export interface FormDetailsGatewayApi {
  getFormContent: (formName: string) => Promise<Form>;
  saveFormContent: (formName: string, content: FormContent) => Promise<void>;
}

export class FormDetailsGatewayApiImpl implements FormDetailsGatewayApi {
  getFormContent(formName: string): Promise<Form> {
    return getFormContent(formName);
  }

  saveFormContent(formName: string, content: FormContent): Promise<void> {
    return saveFormContent(formName, content);
  }
}
