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

const formContent = {
  name: 'from1',
  source: {
    'source-content': '<div><span>1</span></div>'
  },
  formConfiguration: {
    resources: {
      styles: {},
      scripts: {}
    },
    schema:
      '{"$schema":"http://json-schema.org/draft-07/schema#","type":"object","properties":{"candidate":{"type":"object","properties":{"email":{"type":"string"},"name":{"type":"string"},"salary":{"type":"integer"},"skills":{"type":"string"}},"input":true},"approve":{"type":"boolean","output":true}}}'
  }
};
describe('FormView test', () => {
  it('render source - html', () => {
    const props = {
      code: '<div><span>1</span></div>',
      isSource: true,
      isConfig: false,
      formType: 'html',
      formContent: formContent,
      setFormContent: jest.fn()
    };
    const wrapper = mount(<FormView {...props} />);
    expect(wrapper).toMatchSnapshot();
  });
  it('render source - tsx', () => {
    const props = {
      code: '<React.FC><div><span>1</span></div></React.FC>',
      isSource: true,
      isConfig: false,
      formType: 'tsx',
      formContent: formContent,
      setFormContent: jest.fn()
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
      isConfig: true,
      formContent: formContent,
      setFormContent: jest.fn()
    };
    const wrapper = mount(<FormView {...props} />);
    expect(wrapper).toMatchSnapshot();
  });
});
