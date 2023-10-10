import { Form, FormContent } from './FormDetailsEnvelopeApi';

/**
 * Channel Api for Forms Details
 */
export interface FormDetailsChannelApi {
  formDetails__getFormContent(formName: string): Promise<Form>;
  formDetails__saveFormContent(
    formName: string,
    formContent: FormContent
  ): Promise<void>;
}
