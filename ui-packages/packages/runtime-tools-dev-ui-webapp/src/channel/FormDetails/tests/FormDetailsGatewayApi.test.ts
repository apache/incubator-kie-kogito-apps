import { FormContent } from '@kogito-apps/form-details/dist/api/FormDetailsEnvelopeApi';
import { getFormContent, saveFormContent } from '../../apis/apis';
import {
  FormDetailsGatewayApi,
  FormDetailsGatewayApiImpl
} from '../FormDetailsGatewayApi';

jest.mock('../../apis/apis', () => ({
  getFormContent: jest.fn(),
  saveFormContent: jest.fn()
}));

let gatewayApi: FormDetailsGatewayApi;

describe('FormDetailsGatewayApi tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    gatewayApi = new FormDetailsGatewayApiImpl();
  });

  it('getFormContent', async () => {
    const formName = 'form1';
    await gatewayApi.getFormContent(formName);
    expect(getFormContent).toHaveBeenCalledWith(formName);
  });

  it('saveFormContent', async () => {
    const formName = 'form1';
    const content: FormContent = {
      source: '',
      configuration: {
        schema: '',
        resources: {
          scripts: {
            name: 'test1'
          },
          styles: {
            padding: 'test2'
          }
        }
      }
    };
    await gatewayApi.saveFormContent(formName, content);
    expect(saveFormContent).toHaveBeenCalledWith(formName, content);
  });
});
