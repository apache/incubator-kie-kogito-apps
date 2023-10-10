import React from 'react';
import { mount } from 'enzyme';
import { act } from 'react-dom/test-utils';
import FormErrorsWrapper from '../FormErrorsWrapper';
import { EmptyState } from '@patternfly/react-core/dist/js/components/EmptyState';
import { Button } from '@patternfly/react-core/dist/js/components/Button';
import { ClipboardCopy } from '@patternfly/react-core/dist/js/components/ClipboardCopy';

const MockedComponent = (): React.ReactElement => {
  return <></>;
};

jest.mock('@patternfly/react-core/dist/js/components/Button', () =>
  Object.assign({}, jest.requireActual('@patternfly/react-core'), {
    Button: () => <MockedComponent />
  })
);

jest.mock('@patternfly/react-core/dist/js/components/ClipboardCopy', () =>
  Object.assign({}, jest.requireActual('@patternfly/react-core'), {
    ClipboardCopy: () => <MockedComponent />,
    ClipboardCopyVariant: {
      expansion: 'expansion'
    }
  })
);

describe('FormErrorsWrapper tests', () => {
  it('Snapshot', () => {
    const error = new Error('Test error');

    let wrapper = mount(<FormErrorsWrapper error={error} />);

    expect(wrapper).toMatchSnapshot();

    const emptyState = wrapper.find(EmptyState);
    expect(emptyState.exists()).toBeTruthy();

    const button = wrapper.find(Button);
    expect(button.exists()).toBeTruthy();

    let clipboard = wrapper.find(ClipboardCopy);
    expect(clipboard.exists()).toBeFalsy();

    act(() => {
      button.props().onClick(undefined);
    });

    wrapper = wrapper.update();
    expect(wrapper).toMatchSnapshot();

    clipboard = wrapper.find(ClipboardCopy);

    expect(clipboard.exists()).toBeTruthy();
    expect(clipboard.props().isExpanded).toBeTruthy();
  });
});
