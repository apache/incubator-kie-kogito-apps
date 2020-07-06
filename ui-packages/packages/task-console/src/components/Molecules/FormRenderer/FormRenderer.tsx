import React, { useCallback } from 'react';
import axios from 'axios';
import JSONSchemaBridge from 'uniforms-bridge-json-schema';
import { AutoFields, AutoForm, ErrorsField } from 'uniforms-patternfly';
import FormFooter from '../../Atoms/FormFooter/FormFooter';
import { FormSchema } from '../../../model/FormSchema';
import { TaskInfo } from '../../../model/TaskInfo';
import ModelConversionTool from '../../../util/uniforms/ModelConversionTool';
import { DefaultFormValidator } from '../../../util/uniforms/FormValidator';

interface IOwnProps {
  taskInfo: TaskInfo;
  formSchema: FormSchema;
  model?: any;
  successCallback?: (result: string) => void;
  errorCallback?: (errorMessage: string, error?: string) => void;
}

interface FormAssignments {
  inputs: string[];
  outputs: string[];
}

const FormRenderer: React.FC<IOwnProps> = ({
  taskInfo,
  formSchema,
  model,
  successCallback,
  errorCallback
}) => {
  const formInputs = [];
  const formOutputs = [];

  for (const key of Object.keys(formSchema.properties)) {
    const property = formSchema.properties[key];
    if (property.hasOwnProperty('input')) {
      if (property.input) {
        formInputs.push(key);
      }
      delete property.input;
    }
    if (property.hasOwnProperty('output')) {
      if (property.output) {
        formOutputs.push(key);
      }
      delete property.output;
    }

    if (!formOutputs.includes(key)) {
      if (!property.uniforms) {
        property.uniforms = {};
      }
      property.uniforms.disabled = true;
    }
  }

  const formAssignments = {
    inputs: formInputs,
    outputs: formOutputs
  };

  const validator = new DefaultFormValidator(formSchema);

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

  let selectedPhase;

  const notifySuccess = (phase: string) => {
    if (successCallback) {
      successCallback(phase);
    }
  };

  const notifyError = (phase: string, text?: string) => {
    if (errorCallback) {
      errorCallback(phase, text);
    }
  };

  const submitForm = useCallback(
    async (formModel: any, phase: string, assignments: FormAssignments) => {
      try {
        const data = {};

        let endpoint = taskInfo.getTaskEndPoint();

        if (phase) {
          endpoint += '?phase=' + phase;
        }

        assignments.outputs.forEach(output => {
          if (formModel[output]) {
            data[output] = formModel[output];
          }
        });

        const response = await axios.post(endpoint, data, {
          headers: {
            'Content-Type': 'application/json',
            Accept: 'application/json',
            crossorigin: 'true',
            'Access-Control-Allow-Origin': '*'
          }
        });

        if (response.status === 200) {
          notifySuccess(selectedPhase);
        } else {
          notifyError(selectedPhase, response.data);
        }
      } catch (e) {
        const message = e.response ? e.response.data : e.message;
        notifyError(selectedPhase, message);
      }
    },
    []
  );

  let formActions = [];

  // Adding actions if the task isn't completed
  if (taskInfo.task.state !== 'Completed' && formSchema.phases) {
    formActions = formSchema.phases.map(phase => {
      return {
        name: phase,
        onActionClick: () => {
          selectedPhase = phase;
        }
      };
    });
  }

  return (
    <AutoForm
      placeholder
      model={formData}
      disabled={formActions.length === 0}
      schema={bridge}
      showInlineError={true}
      onSubmit={formModel =>
        submitForm(formModel, selectedPhase, formAssignments)
      }
    >
      <ErrorsField />
      <AutoFields />
      <FormFooter actions={formActions} />
    </AutoForm>
  );
};

export default FormRenderer;
