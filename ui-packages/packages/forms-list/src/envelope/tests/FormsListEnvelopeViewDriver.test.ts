import {
  MessageBusClientApi,
  RequestPropertyNames
} from '@kie-tools-core/envelope-bus/dist/api';
import { MockedMessageBusClientApi } from './mocks/Mocks';
import FormsListEnvelopeViewDriver from '../FormsListEnvelopeViewDriver';
import { FormInfo, FormsListChannelApi, FormType } from '../../api';

let channelApi: MessageBusClientApi<FormsListChannelApi>;
let requests: Pick<
  FormsListChannelApi,
  RequestPropertyNames<FormsListChannelApi>
>;
let driver: FormsListEnvelopeViewDriver;

describe('FormsListEnvelopeViewDriver tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    channelApi = new MockedMessageBusClientApi();
    requests = channelApi.requests;
    driver = new FormsListEnvelopeViewDriver(channelApi);
  });

  describe('Requests', () => {
    it('get forms query', () => {
      driver.getFormsQuery();
      expect(requests.formsList__getFormsQuery).toHaveBeenCalled();
    });

    it('getFormFilter', () => {
      driver.getFormFilter();
      expect(requests.formsList__getFormFilter).toHaveBeenCalled();
    });

    it('applyFilter', () => {
      const formsFilter = {
        formNames: ['form1']
      };
      driver.applyFilter(formsFilter);
      expect(requests.formsList__applyFilter).toHaveBeenCalledWith(formsFilter);
    });

    it('open form', () => {
      const formData: FormInfo = {
        name: 'form1',
        type: FormType.HTML,
        lastModified: new Date(new Date('2020-07-11T18:30:00.000Z'))
      };
      driver.openForm(formData);
      expect(requests.formsList__openForm).toHaveBeenCalledWith(formData);
    });
  });
});
