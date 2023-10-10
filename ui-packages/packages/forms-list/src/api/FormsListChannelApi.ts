import { FormFilter, FormInfo } from './FormsListEnvelopeApi';
/**
 * Channel Api for Forms List
 */
export interface FormsListChannelApi {
  formsList__getFormFilter(): Promise<FormFilter>;
  formsList__applyFilter(formFilter: FormFilter): Promise<void>;
  formsList__getFormsQuery(): Promise<FormInfo[]>;
  formsList__openForm(formData: FormInfo): Promise<void>;
}
