/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React, { useState } from 'react';
import JSONSchemaBridge from 'uniforms-bridge-json-schema';
import { AutoFields, AutoForm, ErrorsField } from 'uniforms-patternfly/dist/es6';
import { componentOuiaProps, OUIAProps } from '@kogito-apps/ouia-tools';
import { FormAction, lookupValidator, ModelConversionTool } from '../utils';
import FormFooter from '../FormFooter/FormFooter';
import '../styles.css';
interface IOwnProps {
  formSchema: any;
  model?: any;
  onSubmit?: (data: any, formApiRef: any) => void;
  formActions?: FormAction[];
  readOnly: boolean;
}

const FormRenderer: React.FC<IOwnProps & OUIAProps> = ({
  formSchema,
  model,
  onSubmit,
  formActions,
  readOnly,
  ouiaId,
  ouiaSafe
}) => {
  const validator = lookupValidator(formSchema);
  const [formApiRef, setFormApiRef] = useState(null);

  const bridge = new JSONSchemaBridge(formSchema, formModel => {
    // Converting back all the JS Dates into String before validating the model
    const newModel = ModelConversionTool.convertDateToString(
      formModel,
      formSchema
    );
    return validator.validate(newModel);
  });

  // Converting Dates that are in string format into JS Dates so they can be correctly bound to the uniforms DateField
  const formData = ModelConversionTool.convertStringToDate(model, formSchema);

  return (
    <React.Fragment>
      {' '}
      <div className="kogito-components-common__form-renderer">
        <AutoForm
          ref={ref => setFormApiRef(ref)}
          placeholder
          model={formData}
          disabled={readOnly}
          schema={bridge}
          showInlineError={true}
          onSubmit={data => onSubmit(data, formApiRef)}
          {...componentOuiaProps(ouiaId, 'form-renderer', ouiaSafe)}
        >
          <ErrorsField />
          <AutoFields />
        </AutoForm>
      </div>
      <FormFooter
        actions={formActions}
        enabled={!readOnly}
        formApiRef={formApiRef}
      />
    </React.Fragment>
  );
};

export default FormRenderer;
