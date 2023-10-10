import { FormSubmitContext, FormSubmitResponse } from '../../../../api';

declare global {
  interface Window {
    Form: {
      openForm: (config: FormConfig) => EmbeddedFormApi;
    };
  }
}

export interface FormConfig {
  onOpen?: (args: InitArgs) => void;
  [key: string]: any;
}

export interface EmbeddedFormApi {
  beforeSubmit?: (context: FormSubmitContext) => void;
  afterSubmit?: (response: FormSubmitResponse) => void;
  getFormData?: () => any;
}

export interface InternalFormDisplayerApi extends EmbeddedFormApi {
  onOpen: (args: InitArgs) => void;
}

export class InternalFormDisplayerApiImpl implements InternalFormDisplayerApi {
  private readonly wrapped: EmbeddedFormApi;
  private readonly onOpenCallback: (data: any, ctx: any) => void;

  constructor(
    api: EmbeddedFormApi,
    onOpenCallback: (data: any, ctx: any) => void
  ) {
    this.wrapped = api;
    this.onOpenCallback = onOpenCallback;
  }

  onOpen(args: InitArgs): void {
    if (this.onOpenCallback) {
      this.onOpenCallback(args.data, args.context);
    }
  }

  afterSubmit(response: FormSubmitResponse): void {
    if (this.wrapped.afterSubmit) {
      this.wrapped.afterSubmit(response);
    }
  }

  beforeSubmit(context: FormSubmitContext): void {
    if (this.wrapped.beforeSubmit) {
      this.wrapped.beforeSubmit(context);
    }
  }

  getFormData(): any {
    if (this.wrapped.getFormData) {
      return this.wrapped.getFormData();
    }
    return null;
  }
}

export type InitArgs = {
  data: any;
  context: Record<string, any>;
};

export enum SubmitResultType {
  SUCCESS = 'success',
  ERROR = 'error'
}
