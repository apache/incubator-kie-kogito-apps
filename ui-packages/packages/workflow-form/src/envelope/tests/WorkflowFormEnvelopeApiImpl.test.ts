import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import {
  MockedEnvelopeClient,
  MockedWorkflowFormEnvelopeViewApi
} from './mocks/Mocks';
import { WorkflowFormChannelApi, WorkflowFormEnvelopeApi } from '../../api';
import { WorkflowFormEnvelopeViewApi } from '../WorkflowFormEnvelopeView';
import { WorkflowFormEnvelopeContext } from '../WorkflowFormEnvelopeContext';
import { WorkflowFormEnvelopeApiImpl } from '../WorkflowFormEnvelopeApiImpl';

const workflowDefinitionData = {
  workflowName: 'workflow1',
  endpoint: 'http://localhost:4000'
};

describe('WorkflowFormEnvelopeApiImpl tests', () => {
  it('initialize', async () => {
    const envelopeClient = new MockedEnvelopeClient();
    const view = new MockedWorkflowFormEnvelopeViewApi();
    const args: EnvelopeApiFactoryArgs<
      WorkflowFormEnvelopeApi,
      WorkflowFormChannelApi,
      WorkflowFormEnvelopeViewApi,
      WorkflowFormEnvelopeContext
    > = {
      envelopeClient,
      envelopeContext: {},
      viewDelegate: () => Promise.resolve(() => view)
    };

    const envelopeApi = new WorkflowFormEnvelopeApiImpl(args);

    envelopeApi.workflowForm__init(
      {
        envelopeServerId: 'envelopeServerId',
        origin: 'origin'
      },
      {
        workflowDefinition: workflowDefinitionData
      }
    );

    expect(envelopeClient.associate).toHaveBeenCalledWith(
      'origin',
      'envelopeServerId'
    );
    const initializedView = await view.initialize;
    expect(initializedView).toHaveBeenCalledWith({
      workflowDefinition: workflowDefinitionData
    });
  });
});
