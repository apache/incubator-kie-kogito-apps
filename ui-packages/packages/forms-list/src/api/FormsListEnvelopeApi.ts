/**
 * Envelope Api
 */
export interface FormsListEnvelopeApi {
  /**
   * Initializes the envelope.
   * @param association
   */
  formsList__init(association: Association): Promise<void>;
}

export interface Association {
  origin: string;
  envelopeServerId: string;
}

export interface FormFilter {
  formNames: string[];
}

export enum FormType {
  HTML = 'HTML',
  TSX = 'TSX'
}

export interface FormInfo {
  name: string;
  type: FormType;
  lastModified: Date;
}
