import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import {
  Form,
  FormContent,
  FormDetailsChannelApi,
  FormDetailsDriver
} from '../api';

/**
 * Implementation of FormDetailsDriver that delegates calls to the channel Api
 */
export default class FormDetailsEnvelopeViewDriver
  implements FormDetailsDriver
{
  constructor(
    protected readonly channelApi: MessageBusClientApi<FormDetailsChannelApi>
  ) {}
  getFormContent(formName: string): Promise<Form> {
    return this.channelApi.requests.formDetails__getFormContent(formName);
  }

  saveFormContent(formName: string, content: FormContent) {
    return this.channelApi.requests.formDetails__saveFormContent(
      formName,
      content
    );
  }
}
