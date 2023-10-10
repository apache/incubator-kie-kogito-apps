import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import {
  FormFilter,
  FormsListChannelApi,
  FormsListDriver,
  FormInfo
} from '../api';

/**
 * Implementation of FormsListDriver that delegates calls to the channel Api
 */
export default class FormsListEnvelopeViewDriver implements FormsListDriver {
  constructor(
    private readonly channelApi: MessageBusClientApi<FormsListChannelApi>
  ) {}

  getFormFilter(): Promise<FormFilter> {
    return this.channelApi.requests.formsList__getFormFilter();
  }

  applyFilter(formFilter: FormFilter): Promise<void> {
    return this.channelApi.requests.formsList__applyFilter(formFilter);
  }

  getFormsQuery(): Promise<FormInfo[]> {
    return this.channelApi.requests.formsList__getFormsQuery();
  }

  openForm(formData: FormInfo): Promise<void> {
    return this.channelApi.requests.formsList__openForm(formData);
  }
}
