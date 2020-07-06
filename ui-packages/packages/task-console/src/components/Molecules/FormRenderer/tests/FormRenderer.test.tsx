import React from 'react';
import { mount } from 'enzyme';
import axios from 'axios';
import _ from 'lodash';
import { AutoForm } from 'uniforms-patternfly';
import FormFooter from '../../../Atoms/FormFooter/FormFooter';
import { UserTaskInstance } from '../../../../graphql/types';
import { TaskInfoImpl } from '../../../../model/TaskInfo';
import FormRenderer from '../FormRenderer';
import ApplyForVisaForm from '../../../../util/tests/mocks/ApplyForVisa';

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
  started: '2020-02-19T11:11:56.282Z',
  excludedUsers: [],
  potentialGroups: [],
  potentialUsers: [],
  inputs:
    '{"Skippable":"true","trip":{"city":"Boston","country":"US","visaRequired":true},"TaskName":"VisaApplication","NodeName":"Apply for visa","traveller":{"firstName":"Rachel","lastName":"White","email":"rwhite@gorle.com","nationality":"Polish","address":{"street":"Cabalone","city":"Zerf","zipCode":"765756","country":"Poland"}},"Priority":"1"}',
  outputs: '{}',
  referenceName: 'VisaApplication',
  lastUpdate: '2020-02-19T11:11:56.282Z'
};

jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;
let formData;
let props;

const testSuccessfulRequest = async (phase: string, expectedPayload) => {
  const response = {
    status: 200
  };
  mockedAxios.post.mockResolvedValue(response);

  const wrapper = mount(<FormRenderer {...props} />);

  const form = wrapper.findWhere(node => node.type() === AutoForm);
  expect(form).toHaveLength(1);

  const formFooter = wrapper.findWhere(node => node.type() === FormFooter);
  expect(formFooter).toHaveLength(1);

  // clicking on a form action
  const footerAction = formFooter.props().actions.find(action => {
    return action.name === phase;
  });

  footerAction.onActionClick();

  // forcing the form submit
  await form.props().onSubmit(formData);

  const calls = mockedAxios.post.mock.calls;

  const postParams = calls[calls.length - 1];

  expect(postParams).toHaveLength(3);

  const expectedEndpoint =
    props.taskInfo.getTaskEndPoint() + (phase ? '?phase=' + phase : '');

  expect(postParams[0]).toBe(expectedEndpoint);
  expect(postParams[1]).toMatchObject(expectedPayload);

  expect(props.successCallback).toBeCalledWith(phase);
  expect(props.errorCallback).not.toBeCalled();
};

const testUnSuccessfulRequest = async (
  response,
  phase,
  expecteErrorMessage
) => {
  mockedAxios.post.mockResolvedValue(response);

  const wrapper = mount(<FormRenderer {...props} />);

  const form = wrapper.findWhere(node => node.type() === AutoForm);
  expect(form).toHaveLength(1);

  const formFooter = wrapper.findWhere(node => node.type() === FormFooter);
  expect(formFooter).toHaveLength(1);

  // clicking on a form action
  const footerAction = formFooter.props().actions.find(action => {
    return action.name === phase;
  });

  footerAction.onActionClick();

  // forcing the form submit
  await form.props().onSubmit(formData);

  expect(props.errorCallback).toBeCalledWith(phase, expecteErrorMessage);
  expect(props.successCallback).not.toBeCalled();
};

const testUnexpectedRequestError = async (
  error,
  phase,
  expecteErrorMessage
) => {
  mockedAxios.post.mockRejectedValue(error);

  const wrapper = mount(<FormRenderer {...props} />);

  const form = wrapper.findWhere(node => node.type() === AutoForm);
  expect(form).toHaveLength(1);

  const formFooter = wrapper.findWhere(node => node.type() === FormFooter);
  expect(formFooter).toHaveLength(1);

  // clicking on a form action
  const footerAction = formFooter.props().actions.find(action => {
    return action.name === phase;
  });

  footerAction.onActionClick();

  // forcing the form submit
  await form.props().onSubmit(formData);

  expect(mockedAxios.post).toBeCalled();

  expect(props.errorCallback).toBeCalledWith(phase, expecteErrorMessage);
  expect(props.successCallback).not.toBeCalled();
};

describe('FormRenderer test', () => {
  beforeEach(() => {
    formData = JSON.parse(userTaskInstance.inputs);
    props = {
      taskInfo: new TaskInfoImpl(
        userTaskInstance,
        'http://localhost:8080/travels'
      ),
      formSchema: _.cloneDeep(ApplyForVisaForm),
      successCallback: jest.fn(),
      errorCallback: jest.fn()
    };
  });

  it('Render form with actions', () => {
    const wrapper = mount(<FormRenderer {...props} />);
    expect(wrapper).toMatchSnapshot();

    const form = wrapper.findWhere(node => node.type() === AutoForm);
    expect(form).toHaveLength(1);

    expect(form.props().disabled).toBeFalsy();
  });

  it('Render form without actions', () => {
    props.formSchema.phases = [];

    const wrapper = mount(<FormRenderer {...props} />);
    expect(wrapper).toMatchSnapshot();

    const form = wrapper.findWhere(node => node.type() === AutoForm);
    expect(form).toHaveLength(1);

    expect(form.props().disabled).toBe(true);
  });

  it('Form Render and successfully submit', async () => {
    await testSuccessfulRequest(props.formSchema.phases[1], {
      traveller: formData.traveller
    });
  });

  it('Form Render and unsuccessfully submit', async () => {
    const response = {
      status: 500,
      data: 'Task cannot be completed'
    };

    await testUnSuccessfulRequest(
      response,
      props.formSchema.phases[1],
      response.data
    );
  });

  it('Form Render and unexpected error on submit with full response', async () => {
    const error = {
      response: {
        status: 500,
        data: 'Task cannot be completed'
      }
    };
    await testUnexpectedRequestError(
      error,
      props.formSchema.phases[1],
      error.response.data
    );
  });

  it('Form Render and unexpected error on submit with response no data', async () => {
    const error = {
      response: {
        status: 500
      }
    };
    await testUnexpectedRequestError(
      error,
      props.formSchema.phases[1],
      undefined
    );
  });

  it('Form Render and unexpected error on submit with JS error', async () => {
    const error = {
      message: 'something really ugly happened!'
    };
    await testUnexpectedRequestError(
      error,
      props.formSchema.phases[1],
      error.message
    );
  });
});
