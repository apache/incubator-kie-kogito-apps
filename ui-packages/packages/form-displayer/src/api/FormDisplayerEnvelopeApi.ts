import {
  Association,
  FormDisplayerInitArgs,
  FormSubmitContext,
  FormSubmitResponse
} from './types';

export interface FormDisplayerEnvelopeApi {
  formDisplayer__init(
    association: Association,
    initArgs: FormDisplayerInitArgs
  ): Promise<void>;
  formDisplayer__startSubmit(context: FormSubmitContext): Promise<any>;
  formDisplayer__notifySubmitResponse(result: FormSubmitResponse): void;
  formDisplayer__notifyInit(initArgs: FormDisplayerInitArgs): void;
}
