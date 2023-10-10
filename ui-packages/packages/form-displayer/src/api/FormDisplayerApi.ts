import {
  FormDisplayerInitArgs,
  FormSubmitContext,
  FormSubmitResponse
} from './types';

export interface FormDisplayerApi {
  init: (args: FormDisplayerInitArgs) => void;

  startSubmit: (context: FormSubmitContext) => Promise<any>;

  notifySubmitResult: (result: FormSubmitResponse) => void;
}
