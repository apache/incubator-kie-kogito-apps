import React from 'react';
import ProcessDetailsProcessDiagram from '../ProcessDetailsProcessDiagram';
import { getWrapper } from '@kogito-apps/common';

localStorage.setItem('ouia:enabled', "false")
describe('Process Details Diagram component', () => {
  it('Snapshot tests', () => {
    const wrapper = getWrapper(<ProcessDetailsProcessDiagram />, 'ProcessDetailsProcessDiagram');
    expect(wrapper).toMatchSnapshot();
  });
});
