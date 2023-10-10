import {
  MockedCloudEventFormEnvelopeViewApi,
  MockedEnvelopeClientDefinition
} from './mocks/Mocks';
import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import { EnvelopeClient } from '@kie-tools-core/envelope-bus/dist/envelope';
import { CloudEventFormChannelApi, CloudEventFormEnvelopeApi } from '../../api';
import { CloudEventFormEnvelopeViewApi } from '../CloudEventFormEnvelopeView';
import { CloudEventFormEnvelopeApiImpl } from '../CloudEventFormEnvelopeApiImpl';

describe('CloudEventFormEnvelopeApiImpl tests', () => {
  it('initialize', async () => {
    const envelopeClient = new MockedEnvelopeClientDefinition();
    const view = new MockedCloudEventFormEnvelopeViewApi();

    const args: EnvelopeApiFactoryArgs<
      CloudEventFormEnvelopeApi,
      CloudEventFormChannelApi,
      CloudEventFormEnvelopeViewApi,
      undefined
    > = {
      envelopeClient: envelopeClient as EnvelopeClient<
        CloudEventFormEnvelopeApi,
        CloudEventFormChannelApi
      >,
      envelopeContext: undefined,
      viewDelegate: () => Promise.resolve(() => view)
    };

    const envelopeApi = new CloudEventFormEnvelopeApiImpl(args);

    envelopeApi.cloudEventForm__init(
      {
        envelopeServerId: 'envelopeServerId',
        origin: 'origin'
      },
      {
        isNewInstanceEvent: true,
        defaultValues: {
          cloudEventSource: '/local/test',
          instanceId: '1234'
        }
      }
    );

    expect(envelopeClient.associate).toHaveBeenCalledWith(
      'origin',
      'envelopeServerId'
    );
    const calledView = await view.initialize;
    expect(calledView).toHaveBeenCalled();
  });
});
