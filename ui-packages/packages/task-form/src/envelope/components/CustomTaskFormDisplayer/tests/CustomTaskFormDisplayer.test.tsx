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
import { mount } from 'enzyme';
import _ from 'lodash';
import { UserTaskInstance } from '@kogito-apps/task-console-shared';
import CustomTaskFormDisplayer, {
  CustomTaskFormDisplayerProps
} from '../CustomTaskFormDisplayer';
import { TaskFormDriver } from '../../../../api';
import { MockedTaskFormDriver } from '../../../../embedded/tests/mocks/Mocks';
import { ApplyForVisaForm } from '../../utils/tests/mocks/ApplyForVisa';
import { CustomForm, FormType } from '../../../../types';
import { KogitoSpinner } from '@kogito-apps/components-common';
import FormFooter from '../../FormFooter/FormFooter';
import {
  EmbeddedFormDisplayer,
  FormOpenedState,
  FormSubmitResponse
} from '@kogito-apps/form-displayer';
import { act } from 'react-dom/test-utils';
import wait from 'waait';
import { FormSubmitResponseType } from '@kogito-apps/form-displayer/src/api';

jest.mock('uuid', () => {
  return () => 'testId';
});

const MockedComponent = (): React.ReactElement => {
  return <></>;
};

jest.mock('@kogito-apps/components-common', () => ({
  ...jest.requireActual('@kogito-apps/components-common'),
  KogitoSpinner: () => {
    return <MockedComponent />;
  }
}));

jest.mock('../../FormFooter/FormFooter');

const userTaskInstance: UserTaskInstance = {
  id: '45a73767-5da3-49bf-9c40-d533c3e77ef3',
  description: null,
  name: 'VisaApplication',
  referenceName: 'Apply for visa',
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
    '{"Skippable":"true","trip":{"city":"Boston","country":"US","begin":"2020-02-19T23:00:00.000+01:00","end":"2020-02-26T23:00:00.000+01:00","visaRequired":true},"TaskName":"VisaApplication","NodeName":"Apply for visa","traveller":{"firstName":"Rachel","lastName":"White","email":"rwhite@gorle.com","nationality":"Polish","address":{"street":"Cabalone","city":"Zerf","zipCode":"765756","country":"Poland"}},"Priority":"1"}',
  outputs: '{}',
  lastUpdate: new Date('2020-02-19T11:11:56.282Z'),
  endpoint:
    'http://localhost:8080/travels/9ae7ce3b-d49c-4f35-b843-8ac3d22fa427/VisaApplication/45a73767-5da3-49bf-9c40-d533c3e77ef3'
};

let driverDoSubmitSpy;

const getTaskFormDriver = (): TaskFormDriver => {
  const driver = new MockedTaskFormDriver();
  driverDoSubmitSpy = jest.spyOn(driver, 'doSubmit');
  return driver;
};

export const customForm: CustomForm = {
  formInfo: {
    type: FormType.HTML,
    name: 'travels_VisaApplication',
    lastModified: new Date(2021, 9, 12)
  },
  source: '<div></div>',
  configuration: {
    schema: '',
    resources: {
      styles: {},
      scripts: {}
    }
  }
};

let props: CustomTaskFormDisplayerProps;

const getWrapper = () => {
  return mount(<CustomTaskFormDisplayer {...props} />, {
    attachTo: document.getElementById('container')
  }).find('CustomTaskFormDisplayer');
};

describe('CustomTaskFormDisplayer Test', () => {
  beforeEach(() => {
    const div = document.createElement('div');
    div.setAttribute('id', 'container');
    document.body.appendChild(div);

    jest.clearAllMocks();
    props = {
      userTask: userTaskInstance,
      user: {
        id: 'jdoe',
        groups: ['admin', 'managers']
      },
      schema: _.cloneDeep(ApplyForVisaForm),
      customForm: customForm,
      driver: getTaskFormDriver()
    };
  });

  afterEach(() => {
    const div = document.getElementById('container');
    if (div) {
      document.body.removeChild(div);
    }
  });

  it('Rendering form', async () => {
    let wrapper = getWrapper();

    expect(wrapper).toMatchSnapshot();

    let spinner = wrapper.find(KogitoSpinner);
    expect(spinner.exists()).toBeTruthy();

    let form = wrapper.find(EmbeddedFormDisplayer);
    expect(form.exists()).toBeTruthy();

    let formFooter = wrapper.find(FormFooter);
    expect(formFooter.exists()).toBeFalsy();

    act(() => {
      form.props().onOpenForm({
        state: FormOpenedState.OPENED,
        size: {
          height: 450,
          width: 250
        }
      });
    });

    wrapper = wrapper.update();

    expect(wrapper).toMatchSnapshot();

    spinner = wrapper.find(KogitoSpinner);
    expect(spinner.exists()).toBeFalsy();

    form = wrapper.find(EmbeddedFormDisplayer);
    expect(form.exists()).toBeTruthy();

    formFooter = wrapper.find(FormFooter);
    expect(formFooter.exists()).toBeTruthy();
  });

  it('Rendering form with error', async () => {
    let wrapper = getWrapper();

    expect(wrapper).toMatchSnapshot();

    let spinner = wrapper.find(KogitoSpinner);
    expect(spinner.exists()).toBeTruthy();

    let form = wrapper.find(EmbeddedFormDisplayer);
    expect(form.exists()).toBeTruthy();

    let formFooter = wrapper.find(FormFooter);
    expect(formFooter.exists()).toBeFalsy();

    act(() => {
      form.props().onOpenForm({
        state: FormOpenedState.ERROR,
        size: {
          height: 450,
          width: 250
        }
      });
    });

    wrapper = wrapper.update();

    expect(wrapper).toMatchSnapshot();

    spinner = wrapper.find(KogitoSpinner);
    expect(spinner.exists()).toBeFalsy();

    form = wrapper.find(EmbeddedFormDisplayer);
    expect(form.exists()).toBeTruthy();

    formFooter = wrapper.find(FormFooter);
    expect(formFooter.exists()).toBeFalsy();
  });

  it('Successful form submit', async () => {
    let wrapper = getWrapper();

    let form = wrapper.find(EmbeddedFormDisplayer);
    expect(form.exists()).toBeTruthy();

    let formFooter = wrapper.find(FormFooter);
    expect(formFooter.exists()).toBeFalsy();

    act(() => {
      form.props().onOpenForm({
        state: FormOpenedState.OPENED,
        size: {
          height: 450,
          width: 250
        }
      });
    });

    wrapper = wrapper.update();

    form = wrapper.find(EmbeddedFormDisplayer);
    expect(form.exists()).toBeTruthy();

    formFooter = wrapper.find(FormFooter);
    expect(formFooter.exists()).toBeTruthy();

    const payload = {
      value: 'ABC'
    };

    const ref = form.getElement()['ref'].current;

    const startSubmitSpy = jest.spyOn(ref, 'startSubmit');
    const notifySubmitSpy = jest.spyOn(ref, 'notifySubmitResult');

    startSubmitSpy.mockReturnValue(Promise.resolve(payload));

    await act(async () => {
      formFooter.props().actions[0].execute();
      wait();
    });

    expect(driverDoSubmitSpy).toHaveBeenCalledWith('complete', payload);
    expect(notifySubmitSpy).toHaveBeenCalled();

    const submitResponse: FormSubmitResponse = notifySubmitSpy.mock
      .calls[0][0] as FormSubmitResponse;
    expect(submitResponse.type).toStrictEqual(FormSubmitResponseType.SUCCESS);

    wrapper = wrapper.update();

    formFooter = wrapper.find(FormFooter);
    expect(formFooter.exists()).toBeTruthy();
    expect(formFooter.props().enabled).toBeFalsy();
  });

  it('Unsuccessful form submit', async () => {
    let wrapper = getWrapper();

    let form = wrapper.find(EmbeddedFormDisplayer);
    expect(form.exists()).toBeTruthy();

    let formFooter = wrapper.find(FormFooter);
    expect(formFooter.exists()).toBeFalsy();

    act(() => {
      form.props().onOpenForm({
        state: FormOpenedState.OPENED,
        size: {
          height: 450,
          width: 250
        }
      });
    });

    wrapper = wrapper.update();

    form = wrapper.find(EmbeddedFormDisplayer);
    expect(form.exists()).toBeTruthy();

    formFooter = wrapper.find(FormFooter);
    expect(formFooter.exists()).toBeTruthy();

    const payload = {
      value: 'ABC'
    };

    const ref = form.getElement()['ref'].current;

    const startSubmitSpy = jest.spyOn(ref, 'startSubmit');
    const notifySubmitSpy = jest.spyOn(ref, 'notifySubmitResult');

    startSubmitSpy.mockReturnValue(Promise.resolve(payload));
    driverDoSubmitSpy.mockImplementation(() => {
      return Promise.reject('Error 500');
    });

    await act(async () => {
      formFooter.props().actions[0].execute();
      wait();
    });

    expect(driverDoSubmitSpy).toHaveBeenCalledWith('complete', payload);
    expect(notifySubmitSpy).toHaveBeenCalled();

    const submitResponse: FormSubmitResponse = notifySubmitSpy.mock
      .calls[0][0] as FormSubmitResponse;
    expect(submitResponse.type).toStrictEqual(FormSubmitResponseType.FAILURE);

    wrapper = wrapper.update();

    formFooter = wrapper.find(FormFooter);
    expect(formFooter.exists()).toBeTruthy();
    expect(formFooter.props().enabled).toBeFalsy();
  });

  it('Form submit attempt with validation failure', async () => {
    let wrapper = getWrapper();

    let form = wrapper.find(EmbeddedFormDisplayer);
    expect(form.exists()).toBeTruthy();

    let formFooter = wrapper.find(FormFooter);
    expect(formFooter.exists()).toBeFalsy();

    act(() => {
      form.props().onOpenForm({
        state: FormOpenedState.OPENED,
        size: {
          height: 450,
          width: 250
        }
      });
    });

    wrapper = wrapper.update();

    form = wrapper.find(EmbeddedFormDisplayer);
    expect(form.exists()).toBeTruthy();

    formFooter = wrapper.find(FormFooter);
    expect(formFooter.exists()).toBeTruthy();

    const ref = form.getElement()['ref'].current;

    const startSubmitSpy = jest.spyOn(ref, 'startSubmit');
    const notifySubmitSpy = jest.spyOn(ref, 'notifySubmitResult');

    startSubmitSpy.mockReturnValue(Promise.reject('error'));

    await act(async () => {
      formFooter.props().actions[0].execute();
      wait();
    });

    expect(driverDoSubmitSpy).not.toHaveBeenCalled();
    expect(notifySubmitSpy).not.toHaveBeenCalled();

    wrapper = wrapper.update();

    formFooter = wrapper.find(FormFooter);
    expect(formFooter.exists()).toBeTruthy();
    expect(formFooter.props().enabled).toBeTruthy();
  });
});
