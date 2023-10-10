import { init } from '@kie-tools-core/editor/dist/envelope';
import { ServerlessWorkflowTextEditorFactory } from '@kie-tools/serverless-workflow-text-editor/dist/editor';

init({
  container: document.getElementById('swf-text-editor-envelope-app')!,
  bus: {
    postMessage: (message, targetOrigin, _) =>
      window.parent.postMessage(message, targetOrigin!, _)
  },
  editorFactory: new ServerlessWorkflowTextEditorFactory()
});
