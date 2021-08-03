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

import React from 'react';
import { OUIAProps, componentOuiaProps } from '@kogito-apps/ouia-tools';
import { FormDetailsDriver } from '../../../api/FormDetailsDriver';
import { FormInfo } from '@kogito-apps/forms-list';
export interface FormDetailsProps {
  isEnvelopeConnectedToChannel: boolean;
  driver: FormDetailsDriver;
  formData: FormInfo;
}

const FormDetails: React.FC<FormDetailsProps & OUIAProps> = ({
  isEnvelopeConnectedToChannel,
  driver,
  formData,
  ouiaId,
  ouiaSafe
}) => {
  console.log(formData);
  return (
    <div {...componentOuiaProps(ouiaId, 'form-details', ouiaSafe)}>
      I am form details
    </div>
  );
};

export default FormDetails;
