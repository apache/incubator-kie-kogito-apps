import {
  MockedEnvelopeClient,
  MockedJobsManagementEnvelopeViewApi
} from './mocks/Mocks';
import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import { JobsManagementChannelApi, JobsManagementEnvelopeApi } from '../../api';
import { JobsManagementEnvelopeApiImpl } from '../JobsManagementEnvelopeApiImpl';
import { JobsManagementEnvelopeViewApi } from '../JobsManagementEnvelopeView';
import { JobsManagementEnvelopeContext } from '../JobsManagementEnvelopeContext';

describe('JobsManagementEnvelopeApiImpl tests', () => {
  it('initialize', async () => {
    const envelopeClient = MockedEnvelopeClient;
    const view = new MockedJobsManagementEnvelopeViewApi();
    const args: EnvelopeApiFactoryArgs<
      JobsManagementEnvelopeApi,
      JobsManagementChannelApi,
      JobsManagementEnvelopeViewApi,
      JobsManagementEnvelopeContext
    > = {
      envelopeClient,
      envelopeContext: {},
      viewDelegate: () => Promise.resolve(() => view)
    };

    const envelopeApi = new JobsManagementEnvelopeApiImpl(args);

    envelopeApi.jobsManagement__init({
      envelopeServerId: 'envelopeServerId',
      origin: 'origin'
    });

    expect(envelopeClient.associate).toHaveBeenCalledWith(
      'origin',
      'envelopeServerId'
    );
    const calledView = await view.initialize;
    expect(calledView).toHaveBeenCalled();
  });
});
