/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
import React, { useEffect, useRef, useState } from 'react';
import uuidv4 from 'uuid';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { EmbeddedFormDisplayer } from '@kogito-apps/form-displayer/dist/embedded';
import {
  FormDisplayerApi,
  FormOpenedState,
  FormSubmitResponseType,
  FormOpened
} from '@kogito-apps/form-displayer/dist/api';
import { CustomForm } from '@kogito-apps/components-common/dist/types';
import { Stack, StackItem } from '@patternfly/react-core/layouts/Stack';
import { Bullseye } from '@patternfly/react-core/layouts/Bullseye';
import { KogitoSpinner } from '@kogito-apps/components-common/dist/components/KogitoSpinner';
import { FormFooter } from '@kogito-apps/components-common/dist/components/FormFooter';
import { FormAction } from '@kogito-apps/components-common/dist/components/utils';
import { ProcessFormDriver } from 'packages/process-form/src/api';

export interface CustomProcessFormDisplayerProps {
  schema: Record<string, any>;
  customForm: CustomForm;
  driver: ProcessFormDriver;
  targetOrigin: string;
}

const CustomProcessFormDisplayer: React.FC<
  CustomProcessFormDisplayerProps & OUIAProps
> = ({ customForm, schema, driver, targetOrigin, ouiaId, ouiaSafe }) => {
  const formDisplayerApiRef = useRef<FormDisplayerApi>();
  const [formUUID] = useState<string>(uuidv4());
  const [formData] = useState({});
  const [formActions, setFormActions] = useState<FormAction[]>([]);
  // const [formOpened, setFormOpened] = useState<FormOpened>();
  const [submitted, setSubmitted] = useState<boolean>(false);

  const doSubmit = async (phase: string, payload: any) => {
    const formDisplayerApi = formDisplayerApiRef.current;

    try {
      const response = await driver.startProcess(payload);
      formDisplayerApi.notifySubmitResult({
        type: FormSubmitResponseType.SUCCESS,
        info: response
      });
    } catch (error) {
      formDisplayerApi.notifySubmitResult({
        type: FormSubmitResponseType.FAILURE,
        info: error
      });
    } finally {
      setSubmitted(true);
    }
  };

  useEffect(() => {
    if (schema.phases) {
      const actions = schema.phases.map((phase) => {
        return {
          name: phase,
          execute: () => {
            const formDisplayerApi = formDisplayerApiRef.current;
            formDisplayerApi
              .startSubmit({
                params: {
                  phase: phase
                }
              })
              .then((formOutput) => doSubmit(phase, formOutput))
              .catch((error) =>
                console.log(`Couldn't submit form due to: ${error}`)
              );
          }
        };
      });
      setFormActions(actions);
    }
  }, []);

  // useEffect(() => {
  //   if (formOpened) {
  //     document.getElementById(`${formUUID}-form`).style.visibility = 'visible';
  //   }
  // }, [formOpened]);

  return (
    <div
      {...componentOuiaProps(ouiaId, 'custom-form-displayer', ouiaSafe)}
      style={{ height: '100%' }}
    >
      {/* {!formOpened && (
        <Bullseye
          {...componentOuiaProps(
            (ouiaId ? ouiaId : 'process-form-envelope-view') + '-loading-spinner',
            'process-form',
            true
          )}
        >
          <KogitoSpinner spinnerText={`Loading process form...`} />
        </Bullseye>
      )} */}
      <Stack hasGutter>
        <StackItem
          id={`${formUUID}-form`}
          style={{ visibility: 'visible', height: 'inherit' }}
        >
          <EmbeddedFormDisplayer
            targetOrigin={targetOrigin}
            envelopePath={'resources/form-displayer.html'}
            formContent={customForm}
            data={formData}
            context={{}}
            onOpenForm={(opened) => {
              /* do nothing */
            }}
            ref={formDisplayerApiRef}
          />
        </StackItem>
        {/* {formOpened && formOpened.state === FormOpenedState.OPENED && ( */}
        <StackItem>
          <FormFooter actions={formActions} /*enabled={!submitted}*/ />
        </StackItem>
        {/* )} */}
      </Stack>
    </div>
  );
};

export default CustomProcessFormDisplayer;
