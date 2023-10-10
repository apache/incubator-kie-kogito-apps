import {
  FormsListDriver,
  FormsListChannelApi,
  FormFilter,
  FormInfo
} from '../api';

/**
 * Implementation of the FormsListChannelApiImpl delegating to a FormsListDriver
 */
export class FormsListChannelApiImpl implements FormsListChannelApi {
  constructor(private readonly driver: FormsListDriver) {}

  formsList__getFormFilter(): Promise<FormFilter> {
    return this.driver.getFormFilter();
  }

  formsList__applyFilter(formFilter: FormFilter): Promise<void> {
    return this.driver.applyFilter(formFilter);
  }

  formsList__getFormsQuery(): Promise<FormInfo[]> {
    return this.driver.getFormsQuery();
  }

  formsList__openForm(formData: FormInfo): Promise<void> {
    return this.driver.openForm(formData);
  }
}
