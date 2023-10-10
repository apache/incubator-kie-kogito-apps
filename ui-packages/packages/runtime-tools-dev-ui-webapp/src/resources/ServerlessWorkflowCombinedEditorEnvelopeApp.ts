import * as EditorEnvelope from '@kie-tools-core/editor/dist/envelope';
import { NoOpKeyboardShortcutsService } from '@kie-tools-core/keyboard-shortcuts/dist/envelope';
import { ServerlessWorkflowCombinedEditorChannelApi } from '@kie-tools/serverless-workflow-combined-editor/dist/api';
import { ServerlessWorkflowCombinedEditorEnvelopeApi } from '@kie-tools/serverless-workflow-combined-editor/dist/api/ServerlessWorkflowCombinedEditorEnvelopeApi';
import {
  ServerlessWorkflowCombinedEditorApi,
  ServerlessWorkflowCombinedEditorFactory
} from '@kie-tools/serverless-workflow-combined-editor/dist/editor';
import { ServerlessWorkflowCombinedEditorEnvelopeApiImpl } from '@kie-tools/serverless-workflow-combined-editor/dist/impl';

EditorEnvelope.initCustom<
  ServerlessWorkflowCombinedEditorApi,
  ServerlessWorkflowCombinedEditorEnvelopeApi,
  ServerlessWorkflowCombinedEditorChannelApi
>({
  container: document.getElementById('swf-combined-editor-envelope-app')!,
  bus: {
    postMessage: (message, _targetOrigin, _) =>
      window.parent.postMessage(message, window.location.origin, _)
  },
  apiImplFactory: {
    create: (args) =>
      new ServerlessWorkflowCombinedEditorEnvelopeApiImpl(
        args,
        new ServerlessWorkflowCombinedEditorFactory()
      )
  },
  keyboardShortcutsService: new NoOpKeyboardShortcutsService()
});
