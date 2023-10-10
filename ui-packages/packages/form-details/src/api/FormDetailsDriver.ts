import { Form, FormContent } from './FormDetailsEnvelopeApi';

/**
 * Interface that defines a Driver for FormDetails views.
 */
export interface FormDetailsDriver {
  getFormContent(formName: string): Promise<Form>;
  saveFormContent(formName: string, content: FormContent);
}
