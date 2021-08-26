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
import {
  Card,
  CardBody,
  CardHeaderMain,
  CardHeader,
  TextVariants,
  Text,
  Label,
  FormGroup,
  Form
} from '@patternfly/react-core';
import { FormInfo } from '../../../api/FormsListEnvelopeApi';
import { FormsListDriver } from '../../../api/FormsListDriver';
import Moment from 'react-moment';
export interface FormCardProps {
  formData: FormInfo;
  driver: FormsListDriver;
}

const FormCard: React.FC<FormCardProps & OUIAProps> = ({
  formData,
  driver,
  ouiaId,
  ouiaSafe
}) => {
  const getLabel = () => {
    switch (formData.type) {
      case 'html':
        return <Label variant="outline">HTML</Label>;
      case 'tsx':
        return <Label variant="outline">REACT</Label>;
      /* istanbul ignore next */
      default:
        return '';
    }
  };

  const handleCardClick = () => {
    driver.openForm(formData);
  };

  return (
    <Card
      {...componentOuiaProps(ouiaId, 'forms-list', ouiaSafe)}
      isSelectable
      onClick={handleCardClick}
    >
      <CardHeader>
        <CardHeaderMain>{getLabel()}</CardHeaderMain>
      </CardHeader>
      <CardHeader>
        <Text component={TextVariants.h1} className="pf-u-font-weight-bold">
          {formData.name}
        </Text>
      </CardHeader>
      <CardBody>
        <div className="pf-u-mt-md">
          <Form>
            <FormGroup label="Type" fieldId="type">
              <Text component={TextVariants.p}>{formData.type}</Text>
            </FormGroup>
            <FormGroup label="LastModified" fieldId="lastModified">
              <Text component={TextVariants.p}>
                <Moment fromNow>{formData.lastModified}</Moment>
              </Text>
            </FormGroup>
          </Form>
        </div>
      </CardBody>
    </Card>
  );
};

export default FormCard;
