import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import {
  Association,
  FormDisplayerChannelApi,
  FormDisplayerEnvelopeApi,
  FormDisplayerInitArgs,
  FormSubmitContext,
  FormSubmitResponse
} from '../api';
import { FormDisplayerEnvelopeViewApi } from './FormDisplayerEnvelopeView';
import { FormDisplayerEnvelopeContext } from './FormDisplayerEnvelopeContext';
import isEmpty from 'lodash/isEmpty';

export class FormDisplayerEnvelopeApiImpl implements FormDisplayerEnvelopeApi {
  private view: () => FormDisplayerEnvelopeViewApi;
  private capturedInitRequestYet = false;
  constructor(
    private readonly args: EnvelopeApiFactoryArgs<
      FormDisplayerEnvelopeApi,
      FormDisplayerChannelApi,
      FormDisplayerEnvelopeViewApi,
      FormDisplayerEnvelopeContext
    >
  ) {}

  private hasCapturedInitRequestYet() {
    return this.capturedInitRequestYet;
  }

  private ackCapturedInitRequest() {
    this.capturedInitRequestYet = true;
  }

  public async formDisplayer__init(
    association: Association,
    initArgs: FormDisplayerInitArgs
  ) {
    this.args.envelopeClient.associate(
      association.origin,
      association.envelopeServerId
    );

    if (this.hasCapturedInitRequestYet()) {
      return;
    }

    this.ackCapturedInitRequest();
    this.view = await this.args.viewDelegate();

    if (!isEmpty(initArgs.form)) {
      this.view().initForm(initArgs);
    }
  }
  formDisplayer__notifyInit = (initArgs: FormDisplayerInitArgs): void => {
    this.view().initForm(initArgs);
  };

  formDisplayer__startSubmit(context: FormSubmitContext): Promise<any> {
    return this.view().startSubmit(context);
  }

  formDisplayer__notifySubmitResponse(response: FormSubmitResponse) {
    this.view().notifySubmitResponse(response);
  }
}
