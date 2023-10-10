import { FormInfo } from '@kogito-apps/forms-list';

/**
 * Envelope Api
 */
export interface FormDetailsEnvelopeApi {
  /**
   * Initializes the envelope.
   * @param association
   */
  formDetails__init(
    association: Association,
    formData: FormInfo
  ): Promise<void>;
}

export interface Association {
  origin: string;
  envelopeServerId: string;
}

export interface FormResources {
  scripts: {
    [key: string]: string;
  };
  styles: {
    [key: string]: string;
  };
}
interface FormConfiguration {
  schema: string;
  resources: FormResources;
}
export interface Form {
  formInfo: FormInfo;
  source: string;
  configuration: FormConfiguration;
}

export interface FormContent {
  source: string;
  configuration: FormConfiguration;
}
