import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import { WorkflowFormEnvelopeViewApi } from './WorkflowFormEnvelopeView';
import {
  Association,
  WorkflowFormChannelApi,
  WorkflowFormEnvelopeApi
} from '../api';
import { WorkflowFormEnvelopeContext } from './WorkflowFormEnvelopeContext';

/**
 * Implementation of the WorkflowFormEnvelopeApi
 */
export class WorkflowFormEnvelopeApiImpl implements WorkflowFormEnvelopeApi {
  private view: () => WorkflowFormEnvelopeViewApi;
  private capturedInitRequestYet = false;
  constructor(
    private readonly args: EnvelopeApiFactoryArgs<
      WorkflowFormEnvelopeApi,
      WorkflowFormChannelApi,
      WorkflowFormEnvelopeViewApi,
      WorkflowFormEnvelopeContext
    >
  ) {}

  private hasCapturedInitRequestYet() {
    return this.capturedInitRequestYet;
  }

  private ackCapturedInitRequest() {
    this.capturedInitRequestYet = true;
  }

  workflowForm__init = async (
    association: Association,
    workflowDefinition: any
  ): Promise<void> => {
    this.args.envelopeClient.associate(
      association.origin,
      association.envelopeServerId
    );

    if (this.hasCapturedInitRequestYet()) {
      return;
    }
    this.view = await this.args.viewDelegate();
    this.ackCapturedInitRequest();
    this.view().initialize(workflowDefinition);
  };
}
