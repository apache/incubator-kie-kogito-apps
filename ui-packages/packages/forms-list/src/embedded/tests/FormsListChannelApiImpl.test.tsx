import { FormInfo, FormsListDriver, FormType } from '../../api';
import { FormsListChannelApiImpl } from '../FormsListChannelApiImpl';
import { MockedFormsListDriver } from './utils/Mocks';

let driver: FormsListDriver;
let api: FormsListChannelApiImpl;

describe('FormsListChannelApiImpl tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    driver = new MockedFormsListDriver();
    api = new FormsListChannelApiImpl(driver);
  });

  it('FormsList__getFormFilter', () => {
    api.formsList__getFormFilter();
    expect(driver.getFormFilter).toHaveBeenCalled();
  });

  it('FormsList__applyFilter', () => {
    const formFilter = {
      formNames: ['form1']
    };
    api.formsList__applyFilter(formFilter);
    expect(driver.applyFilter).toHaveBeenCalledWith(formFilter);
  });

  it('FormsList__getFormsQuery', () => {
    api.formsList__getFormsQuery();
    expect(driver.getFormsQuery).toHaveBeenCalled();
  });

  it('FormsList__openForm', () => {
    const formsData: FormInfo = {
      name: 'form1',
      type: FormType.TSX,
      lastModified: new Date(new Date('2020-07-11T18:30:00.000Z'))
    };
    api.formsList__openForm(formsData);
    expect(driver.openForm).toHaveBeenCalledWith(formsData);
  });
});
