import { FormDetailsDriver } from '../../api';
import { FormDetailsChannelApiImpl } from '../FormDetailsChannelApiImpl';
import { MockedFormDetailsDriver } from './utils/Mocks';

let driver: FormDetailsDriver;
let api: FormDetailsChannelApiImpl;

describe('FormDetailsChannelApiImpl tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    driver = new MockedFormDetailsDriver();
    api = new FormDetailsChannelApiImpl(driver);
  });

  it('FormDetails__getFormContent', () => {
    const formName = 'form1';
    api.formDetails__getFormContent(formName);
    expect(driver.getFormContent).toHaveBeenCalledWith(formName);
  });
});
