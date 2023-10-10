import { init } from '@kogito-apps/form-displayer';
import { EnvelopeBusMessage } from '@kie-tools-core/envelope-bus/dist/api';
import { ContainerType } from '@kie-tools-core/envelope/dist/api';

init({
  container: document.getElementById('displayer-app')!,
  config: { containerType: ContainerType.IFRAME },
  bus: {
    postMessage<D, Type>(
      message: EnvelopeBusMessage<D, Type>,
      targetOrigin?: string,
      transfer?: any
    ) {
      window.parent.postMessage(message, '*', transfer);
    }
  }
});
