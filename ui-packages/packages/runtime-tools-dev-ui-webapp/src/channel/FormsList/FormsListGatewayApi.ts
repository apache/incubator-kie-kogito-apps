import { FormFilter, FormInfo } from '@kogito-apps/forms-list';
import { getForms } from '../apis';

/* eslint-disable @typescript-eslint/no-empty-interface */
export interface FormsListGatewayApi {
  getFormFilter(): Promise<FormFilter>;
  applyFilter(formList: FormFilter): Promise<void>;
  getFormsQuery(): Promise<FormInfo[]>;
  openForm: (formData: FormInfo) => Promise<void>;
  onOpenFormListen: (listener: OnOpenFormListener) => UnSubscribeHandler;
}

export interface OnOpenFormListener {
  onOpen: (formData: FormInfo) => void;
}

export interface UnSubscribeHandler {
  unSubscribe: () => void;
}

export class FormsListGatewayApiImpl implements FormsListGatewayApi {
  private _FormFilter: FormFilter = {
    formNames: []
  };
  private readonly listeners: OnOpenFormListener[] = [];

  getFormFilter = (): Promise<FormFilter> => {
    return Promise.resolve(this._FormFilter);
  };

  applyFilter = (formFilter: FormFilter): Promise<void> => {
    this._FormFilter = formFilter;
    return Promise.resolve();
  };

  getFormsQuery(): Promise<FormInfo[]> {
    return getForms(this._FormFilter.formNames);
  }

  openForm = (formData: FormInfo): Promise<void> => {
    this.listeners.forEach((listener) => listener.onOpen(formData));
    return Promise.resolve();
  };

  onOpenFormListen(listener: OnOpenFormListener): UnSubscribeHandler {
    this.listeners.push(listener);

    const unSubscribe = () => {
      const index = this.listeners.indexOf(listener);
      if (index > -1) {
        this.listeners.splice(index, 1);
      }
    };

    return {
      unSubscribe
    };
  }
}
