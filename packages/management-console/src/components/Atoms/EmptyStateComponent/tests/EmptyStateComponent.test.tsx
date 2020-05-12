import React from 'react';
import EmptyStateComponent from '../EmptyStateComponent';
import { getWrapper } from '@kogito-apps/common';

jest.mock('@patternfly/react-icons')

const props1 = {
  iconType: 'warningTriangleIcon',
  title: 'No child process instances',
  body: 'This process has no related sub processes'
};
describe('Emptystate component tests', () => {
  it('snapshot testing', () => {
    const wrapper = getWrapper(<EmptyStateComponent {...props1} />, "EmptyStateComponent")
    expect(wrapper).toMatchSnapshot();
  });
});
