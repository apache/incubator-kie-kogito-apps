import { init } from '@kie-tools-core/editor/dist/envelope';
import { NoOpKeyboardShortcutsService } from '@kie-tools-core/keyboard-shortcuts/dist/envelope';
import { ServerlessWorkflowMermaidViewerFactory } from '@kie-tools/serverless-workflow-mermaid-viewer/dist/viewer';

init({
  container: document.getElementById('swf-mermaid-viewer-envelope-app')!,
  bus: {
    postMessage: (message, targetOrigin, _) =>
      window.parent.postMessage(message, targetOrigin!, _)
  },
  editorFactory: new ServerlessWorkflowMermaidViewerFactory(),
  keyboardShortcutsService: new NoOpKeyboardShortcutsService()
});
