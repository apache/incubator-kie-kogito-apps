import { init } from '@kogito-apps/form-displayer';
import { EnvelopeBusMessage } from '@kogito-tooling/envelope-bus/dist/api';
import { ContainerType } from '@kogito-tooling/envelope/dist/api';

console.log('test-displayer');
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
