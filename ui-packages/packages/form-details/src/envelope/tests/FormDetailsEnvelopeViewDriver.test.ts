import {
  MessageBusClientApi,
  RequestPropertyNames
} from '@kie-tools-core/envelope-bus/dist/api';
import { MockedMessageBusClientApi } from './mocks/Mocks';
import FormDetailsEnvelopeViewDriver from '../FormDetailsEnvelopeViewDriver';
import { FormDetailsChannelApi } from '../../api';

let channelApi: MessageBusClientApi<FormDetailsChannelApi>;
let requests: Pick<
  FormDetailsChannelApi,
  RequestPropertyNames<FormDetailsChannelApi>
>;
let driver: FormDetailsEnvelopeViewDriver;

describe('FormDetailsEnvelopeViewDriver tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    channelApi = new MockedMessageBusClientApi();
    requests = channelApi.requests;
    driver = new FormDetailsEnvelopeViewDriver(channelApi);
  });

  describe('Requests', () => {
    it('get form content query', () => {
      const formName = 'form1';
      driver.getFormContent(formName);
      expect(requests.formDetails__getFormContent).toHaveBeenCalledWith(
        formName
      );
    });
  });
});
