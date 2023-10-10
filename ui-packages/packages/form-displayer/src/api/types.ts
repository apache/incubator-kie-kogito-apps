export interface FormDisplayerInitArgs {
  form: Form;
  data?: any;
  context?: Record<string, any>;
}

export type FormSubmitContext = {
  params?: Record<string, string>;
};

export enum FormOpenedState {
  OPENED = 'opened',
  ERROR = 'error'
}

export type FormOpened = {
  state: FormOpenedState;
  size: FormSize;
};

export type FormSize = {
  width: number;
  height: number;
};

export enum FormSubmitResponseType {
  SUCCESS = 'success',
  FAILURE = 'failure'
}

export type FormSubmitResponse = {
  type: FormSubmitResponseType;
  info: any;
};

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

export enum FormType {
  HTML = 'HTML',
  TSX = 'TSX'
}

export interface FormInfo {
  name: string;
  type: FormType;
  lastModified: Date;
}
