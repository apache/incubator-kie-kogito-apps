import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import { JobsManagementEnvelopeViewApi } from './JobsManagementEnvelopeView';
import {
  Association,
  JobsManagementChannelApi,
  JobsManagementEnvelopeApi
} from '../api';
import { JobsManagementEnvelopeContext } from './JobsManagementEnvelopeContext';
export class JobsManagementEnvelopeApiImpl
  implements JobsManagementEnvelopeApi
{
  private view: () => JobsManagementEnvelopeViewApi;
  private capturedInitRequestYet = false;
  constructor(
    private readonly args: EnvelopeApiFactoryArgs<
      JobsManagementEnvelopeApi,
      JobsManagementChannelApi,
      JobsManagementEnvelopeViewApi,
      JobsManagementEnvelopeContext
    >
  ) {}

  private hasCapturedInitRequestYet() {
    return this.capturedInitRequestYet;
  }

  private ackCapturedInitRequest() {
    this.capturedInitRequestYet = true;
  }

  jobsManagement__init = async (association: Association): Promise<void> => {
    this.args.envelopeClient.associate(
      association.origin,
      association.envelopeServerId
    );

    if (this.hasCapturedInitRequestYet()) {
      return;
    }

    this.ackCapturedInitRequest();
    this.view = await this.args.viewDelegate();
    this.view().initialize();
  };
}
