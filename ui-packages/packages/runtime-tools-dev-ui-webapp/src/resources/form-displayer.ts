import { init } from '@kogito-apps/form-displayer';
import { ContainerType } from '@kie-tools-core/envelope/dist/api';

init({
  container: document.getElementById('displayer-app')!,
  config: { containerType: ContainerType.IFRAME },
  bus: {
    postMessage: (message, targetOrigin, _) =>
      window.parent.postMessage(message, targetOrigin, _)
  }
});
