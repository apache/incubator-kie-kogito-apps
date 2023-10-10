import { FormFilter, FormInfo, FormsListDriver, FormType } from '../../../api';

export const formList: FormInfo[] = [
  {
    name: 'form1',
    type: FormType.TSX,
    lastModified: new Date('2020-07-11T18:30:00.000Z')
  },
  {
    name: 'form2',
    type: FormType.HTML,
    lastModified: new Date('2020-07-11T18:30:00.000Z')
  }
];
export class MockedFormsListDriver implements FormsListDriver {
  getFormFilter(): Promise<FormFilter> {
    return Promise.resolve({ formNames: [] });
  }
  applyFilter(formFilter: FormFilter): Promise<void> {
    return Promise.resolve();
  }
  getFormsQuery(): Promise<FormInfo[]> {
    return Promise.resolve(formList);
  }
  openForm(formData: FormInfo): Promise<void> {
    return Promise.resolve();
  }
}
