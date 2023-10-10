import {
  MessageBusClientApi,
  RequestPropertyNames
} from '@kie-tools-core/envelope-bus/dist/api';
import { MockedMessageBusClientApi } from './mocks/Mocks';
import CustomDashboardViewEnvelopeViewDriver from '../CustomDashboardViewEnvelopeViewDriver';
import { CustomDashboardViewChannelApi } from '../../api';

let channelApi: MessageBusClientApi<CustomDashboardViewChannelApi>;
let requests: Pick<
  CustomDashboardViewChannelApi,
  RequestPropertyNames<CustomDashboardViewChannelApi>
>;
let driver: CustomDashboardViewEnvelopeViewDriver;

describe('CustomDashboardViewEnvelopeViewDriver tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    channelApi = new MockedMessageBusClientApi();
    requests = channelApi.requests;
    driver = new CustomDashboardViewEnvelopeViewDriver(channelApi);
  });

  it('open dashboard', () => {
    driver.getCustomDashboardContent('name');
    expect(
      requests.customDashboardView__getCustomDashboardView
    ).toHaveBeenCalledWith('name');
  });
});
