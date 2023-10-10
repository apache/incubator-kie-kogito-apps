import * as EditorEnvelope from '@kie-tools-core/editor/dist/envelope';
import {
  ServerlessWorkflowDiagramEditorChannelApi,
  ServerlessWorkflowDiagramEditorEnvelopeApi
} from '@kie-tools/serverless-workflow-diagram-editor-envelope/dist/api';
import {
  ServerlessWorkflowDiagramEditor,
  ServerlessWorkflowDiagramEditorEnvelopeApiImpl,
  ServerlessWorkflowDiagramEditorFactory
} from '@kie-tools/serverless-workflow-diagram-editor-envelope/dist/envelope';

EditorEnvelope.initCustom<
  ServerlessWorkflowDiagramEditor,
  ServerlessWorkflowDiagramEditorEnvelopeApi,
  ServerlessWorkflowDiagramEditorChannelApi
>({
  container: document.getElementById('swf-diagram-editor-envelope-app')!,
  bus: {
    postMessage: (message, targetOrigin, _) =>
      window.parent.postMessage(message, targetOrigin!, _)
  },
  apiImplFactory: {
    create: (args) =>
      new ServerlessWorkflowDiagramEditorEnvelopeApiImpl(
        args,
        new ServerlessWorkflowDiagramEditorFactory({
          shouldLoadResourcesDynamically: true
        })
      )
  }
});
