import React, { useImperativeHandle, useState } from 'react';
import JSONSchemaBridge from 'uniforms-bridge-json-schema';
import {
  AutoFields,
  AutoForm,
  ErrorsField
} from 'uniforms-patternfly/dist/es6';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { FormAction, lookupValidator, ModelConversionTool } from '../utils';
import { FormFooter } from '../FormFooter/FormFooter';
import '../styles.css';
import { FormRendererApi } from '../../types/types';
interface IOwnProps {
  formSchema: any;
  model?: any;
  onSubmit?: (data: any) => void;
  formActions?: FormAction[];
  readOnly: boolean;
}

export const FormRenderer = React.forwardRef<
  FormRendererApi,
  IOwnProps & OUIAProps
>(
  (
    { formSchema, model, onSubmit, formActions, readOnly, ouiaId, ouiaSafe },
    forwardedRef
  ) => {
    const validator = lookupValidator(formSchema);
    const [formApiRef, setFormApiRef] = useState(null);

    useImperativeHandle(
      forwardedRef,
      () => {
        return {
          doReset() {
            formApiRef.reset();
          }
        };
      },
      [formApiRef]
    );

    const bridge = new JSONSchemaBridge(formSchema, (formModel) => {
      // Converting back all the JS Dates into String before validating the model
      const newModel = ModelConversionTool.convertDateToString(
        formModel,
        formSchema
      );
      return validator.validate(newModel);
    });

    // Converting Dates that are in string format into JS Dates so they can be correctly bound to the uniforms DateField
    const formData = ModelConversionTool.convertStringToDate(model, formSchema);

    const submitFormData = (): void => {
      formApiRef.submit();
    };

    return (
      <React.Fragment>
        <AutoForm
          ref={(ref) => setFormApiRef(ref)}
          placeholder
          model={formData}
          disabled={readOnly}
          schema={bridge}
          showInlineError={true}
          onSubmit={(data) => onSubmit(data)}
          {...componentOuiaProps(ouiaId, 'form-renderer', ouiaSafe)}
        >
          <ErrorsField />
          <AutoFields />
        </AutoForm>
        <FormFooter
          actions={formActions}
          enabled={!readOnly}
          onSubmitForm={submitFormData}
        />
      </React.Fragment>
    );
  }
);
