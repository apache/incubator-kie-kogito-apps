import { FormFilter, FormInfo } from './FormsListEnvelopeApi';

/**
 * Interface that defines a Driver for FormsList views.
 */
export interface FormsListDriver {
  getFormFilter(): Promise<FormFilter>;
  applyFilter(formFilter: FormFilter): Promise<void>;
  getFormsQuery(): Promise<FormInfo[]>;
  openForm(formData: FormInfo): Promise<void>;
}
