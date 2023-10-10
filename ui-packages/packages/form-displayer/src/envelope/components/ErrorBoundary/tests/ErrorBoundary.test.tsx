import React from 'react';
import { mount } from 'enzyme';
import ErrorBoundary from '../ErrorBoundary';
import FormErrorsWrapper from '../../FormErrorsWrapper/FormErrorsWrapper';

jest.mock('../../FormErrorsWrapper/FormErrorsWrapper');

const BuggyComponent = (): React.ReactElement => {
  throw new Error('test error!');
  return <></>;
};

describe('ErrorBoundary tests', () => {
  it('Regular rendering', () => {
    const wrapper = mount(
      <ErrorBoundary notifyOnError={jest.fn()}>
        <h1>Hello world!</h1>
      </ErrorBoundary>
    );

    expect(wrapper).toMatchSnapshot();

    const errorWrapper = wrapper.find(FormErrorsWrapper);
    expect(errorWrapper.exists()).toBeFalsy();

    expect(wrapper.html()).toContain('Hello world!');
  });

  it('Error rendering', () => {
    const wrapper = mount(
      <ErrorBoundary notifyOnError={jest.fn()}>
        <BuggyComponent />
      </ErrorBoundary>
    );

    expect(wrapper).toMatchSnapshot();

    const errorWrapper = wrapper.find(FormErrorsWrapper);
    expect(errorWrapper.exists()).toBeTruthy();
  });
});
