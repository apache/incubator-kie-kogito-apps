import React from 'react';
import { mount } from 'enzyme';
import FormView from '../FormView';

const MockedComponent = (): React.ReactElement => {
  return <></>;
};
jest.mock('@patternfly/react-code-editor', () => ({
  ...jest.requireActual('@patternfly/react-code-editor'),
  CodeEditor: () => {
    return <MockedComponent />;
  }
}));

describe('FormView test', () => {
  it('render source - html', () => {
    const props = {
      code: '<div><span>1</span></div>',
      isSource: true,
      isConfig: false,
      formType: 'html'
    };
    const wrapper = mount(<FormView {...props} />);
    expect(wrapper).toMatchSnapshot();
  });
  it('render source - tsx', () => {
    const props = {
      code: '<React.FC><div><span>1</span></div></React.FC>',
      isSource: true,
      isConfig: false,
      formType: 'tsx'
    };
    const wrapper = mount(<FormView {...props} />);
    expect(wrapper).toMatchSnapshot();
  });
  it('render config', () => {
    const props = {
      code: JSON.stringify({
        1: '1',
        2: '2'
      }),
      isSource: false,
      isConfig: true
    };
    const wrapper = mount(<FormView {...props} />);
    expect(wrapper).toMatchSnapshot();
  });
});
