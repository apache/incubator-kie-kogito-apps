import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import { FormDetailsEnvelopeViewApi } from './FormDetailsEnvelopeView';
import {
  Association,
  FormDetailsChannelApi,
  FormDetailsEnvelopeApi
} from '../api';
import { FormDetailsEnvelopeContext } from './FormDetailsEnvelopeContext';
import { FormInfo } from '@kogito-apps/forms-list';

/**
 * Implementation of the FormDetailsEnvelopeApi
 */
export class FormDetailsEnvelopeApiImpl implements FormDetailsEnvelopeApi {
  private view: () => FormDetailsEnvelopeViewApi;
  private capturedInitRequestYet = false;
  constructor(
    private readonly args: EnvelopeApiFactoryArgs<
      FormDetailsEnvelopeApi,
      FormDetailsChannelApi,
      FormDetailsEnvelopeViewApi,
      FormDetailsEnvelopeContext
    >
  ) {}

  private hasCapturedInitRequestYet() {
    return this.capturedInitRequestYet;
  }

  private ackCapturedInitRequest() {
    this.capturedInitRequestYet = true;
  }

  formDetails__init = async (
    association: Association,
    formData: FormInfo
  ): Promise<void> => {
    this.args.envelopeClient.associate(
      association.origin,
      association.envelopeServerId
    );

    if (this.hasCapturedInitRequestYet()) {
      return;
    }

    this.ackCapturedInitRequest();
    this.view = await this.args.viewDelegate();
    this.view().initialize(formData);
  };
}
