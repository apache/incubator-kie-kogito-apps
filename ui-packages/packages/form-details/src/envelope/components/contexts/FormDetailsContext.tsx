import React, { useContext } from 'react';
import { Form } from '../../../api';

export interface FormDetailsContext {
  updateContent(formContent: Form): void;
  onUpdateContent(listener: UpdateContentListener): UnSubscribeHandler;
}

export interface UpdateContentListener {
  onUpdateContent: (formContent: Form) => void;
}

export interface UnSubscribeHandler {
  unSubscribe: () => void;
}

export class FormDetailsContextImpl implements FormDetailsContext {
  private readonly updateContentListeners: UpdateContentListener[] = [];

  updateContent(formContent: Form): void {
    if (formContent) {
      this.updateContentListeners.forEach((listener) =>
        listener.onUpdateContent(formContent)
      );
    }
  }

  onUpdateContent(listener: UpdateContentListener): UnSubscribeHandler {
    this.updateContentListeners.push(listener);

    return {
      unSubscribe: () => {
        const index = this.updateContentListeners.indexOf(listener);
        if (index > -1) {
          this.updateContentListeners.splice(index, 1);
        }
      }
    };
  }
}

const RuntimeToolsFormDetailsContext =
  React.createContext<FormDetailsContext>(null);

export default RuntimeToolsFormDetailsContext;

export const useFormDetailsContext = () =>
  useContext<FormDetailsContext>(RuntimeToolsFormDetailsContext);
