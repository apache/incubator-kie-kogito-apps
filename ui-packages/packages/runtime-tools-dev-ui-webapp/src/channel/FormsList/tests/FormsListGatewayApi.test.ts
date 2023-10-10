import { FormInfo } from '@kogito-apps/forms-list';
import {
  FormsListGatewayApi,
  FormsListGatewayApiImpl,
  OnOpenFormListener
} from '../FormsListGatewayApi';

jest.mock('../../apis/apis', () => ({
  getForms: jest.fn()
}));

let gatewayApi: FormsListGatewayApi;

describe('FormsListGatewayApi tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    gatewayApi = new FormsListGatewayApiImpl();
  });

  it('applyFilter', async () => {
    const formsFilter = {
      formNames: ['form1']
    };
    gatewayApi.applyFilter(formsFilter);
    expect(await gatewayApi.getFormFilter()).toEqual(formsFilter);
  });

  it('getForms', async () => {
    const formsFilter = {
      formNames: ['form1']
    };
    gatewayApi.applyFilter(formsFilter);
    gatewayApi.getFormsQuery();
    expect(await gatewayApi.getFormFilter()).toEqual(formsFilter);
  });

  it('openForm', () => {
    const form: FormInfo = {
      name: 'form1',
      type: 'html',
      lastModified: new Date(2020, 6, 12)
    };
    const listener: OnOpenFormListener = {
      onOpen: jest.fn()
    };

    const unsubscribe = gatewayApi.onOpenFormListen(listener);

    gatewayApi.openForm(form);

    expect(listener.onOpen).toHaveBeenLastCalledWith(form);

    unsubscribe.unSubscribe();
  });
});
