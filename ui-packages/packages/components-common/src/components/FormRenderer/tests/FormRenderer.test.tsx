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
import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import cloneDeep from 'lodash/cloneDeep';

import { FormRenderer } from '../FormRenderer';
import { FormAction } from '../../utils';
import { ApplyForVisaForm } from '../../utils/tests/mocks/ApplyForVisa';

export type UserTaskInstance = {
  id: string;
  description?: string;
  name?: string;
  priority?: string;
  processInstanceId: string;
  processId: string;
  rootProcessInstanceId?: string;
  rootProcessId?: string;
  state: string;
  actualOwner?: string;
  adminGroups?: string[];
  adminUsers?: string[];
  completed?: Date;
  started: Date;
  excludedUsers?: string[];
  potentialGroups?: string[];
  potentialUsers?: string[];
  inputs?: string;
  outputs?: string;
  referenceName?: string;
  lastUpdate: Date;
  endpoint?: string;
};

const userTaskInstance: UserTaskInstance = {
  id: '45a73767-5da3-49bf-9c40-d533c3e77ef3',
  description: null,
  name: 'Apply for visa',
  priority: '1',
  processInstanceId: '9ae7ce3b-d49c-4f35-b843-8ac3d22fa427',
  processId: 'travels',
  rootProcessInstanceId: null,
  rootProcessId: null,
  state: 'Ready',
  actualOwner: null,
  adminGroups: [],
  adminUsers: [],
  completed: null,
  started: new Date('2020-02-19T11:11:56.282Z'),
  excludedUsers: [],
  potentialGroups: [],
  potentialUsers: [],
  inputs:
    '{"Skippable":"true","trip":{"city":"Boston","country":"US","visaRequired":true},"TaskName":"VisaApplication","NodeName":"Apply for visa","traveller":{"firstName":"Rachel","lastName":"White","email":"rwhite@gorle.com","nationality":"Polish","address":{"street":"Cabalone","city":"Zerf","zipCode":"765756","country":"Poland"}},"Priority":"1"}',
  outputs: '{}',
  referenceName: 'VisaApplication',
  lastUpdate: new Date('2020-02-19T11:11:56.282Z')
};

const MockedComponent = (): React.ReactElement => {
  return <></>;
};

jest.mock('uniforms-patternfly/dist/es6', () =>
  Object.assign({}, jest.requireActual('uniforms-patternfly/dist/es6'), {
    AutoFields: () => {
      return <MockedComponent />;
    },
    ErrorsField: () => {
      return <MockedComponent />;
    }
  })
);

let model;
let props;
let formActions: FormAction[];

// Clearing unneeded assignments to avoid issues on Uniforms Autoform
delete ApplyForVisaForm.properties.trip.input;
delete ApplyForVisaForm.properties.traveller.input;
delete ApplyForVisaForm.properties.traveller.output;
delete ApplyForVisaForm.properties.visaApplication.input;

describe('FormRenderer test', () => {
  beforeEach(() => {
    model = JSON.parse(userTaskInstance.inputs);
    formActions = [];
    props = {
      formSchema: cloneDeep(ApplyForVisaForm),
      model,
      formActions,
      readOnly: false,
      onSubmit: jest.fn()
    };
  });

  it('Render form with actions', () => {
    formActions.push({
      name: 'complete',
      execute: jest.fn()
    });

    const { container } = render(<FormRenderer {...props} />);
    expect(container).toMatchSnapshot();

    const checkForm = container.querySelector('form');
    expect(checkForm).toBeTruthy();

    const checkFormFooter = container.querySelector(
      '[data-ouia-component-type="form-footer"]'
    );
    expect(checkFormFooter).toBeTruthy();

    const checkSubmitButton = screen.getByText('Complete');
    expect(checkSubmitButton).toBeTruthy();
  });

  it('Render readonly form with actions', () => {
    formActions.push({
      name: 'complete',
      execute: jest.fn()
    });

    props.readOnly = true;

    const { container } = render(<FormRenderer {...props} />);

    const checkForm = container.querySelector('form');
    expect(checkForm).toBeTruthy();

    fireEvent.submit(container.querySelector('form')!);
    const checkFormFooter = container.querySelector(
      '[data-ouia-component-type="form-footer"]'
    );
    expect(checkFormFooter).toBeTruthy();

    const checkSubmitButton = screen.getByText('Complete');
    expect(checkSubmitButton).toBeTruthy();
  });

  it('Render form without actions', () => {
    const { container } = render(<FormRenderer {...props} />);
    expect(container).toMatchSnapshot();

    const checkForm = container.querySelector('form');
    expect(checkForm).toBeTruthy();

    expect(container.querySelector('button')).toBeFalsy();
  });
});
