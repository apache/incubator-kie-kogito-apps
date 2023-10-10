import React from 'react';
import { mount } from 'enzyme';
import FormsListToolbar from '../FormsListToolbar';
import { act } from 'react-dom/test-utils';
import { Button, ToolbarFilter, Tooltip } from '@patternfly/react-core';

describe('forms list toolbar tests', () => {
  it('render toolbar', () => {
    const wrapper = mount(
      <FormsListToolbar
        applyFilter={jest.fn()}
        setFilterFormNames={jest.fn()}
        filterFormNames={[]}
      />
    );
    expect(wrapper).toMatchSnapshot();
  });

  it('apply filter click', () => {
    const applyFilter = jest.fn();
    const wrapper = mount(
      <FormsListToolbar
        applyFilter={applyFilter}
        setFilterFormNames={jest.fn()}
        filterFormNames={[]}
      />
    );
    act(() => {
      wrapper
        .find('TextInputBase')
        .props()
        ['onChange']({
          target: {
            value: 'form1'
          }
        } as any);
    });
    wrapper.find('#apply-filter').find('button').simulate('click');
    expect(applyFilter).toHaveBeenCalled();
  });

  it('reset click', () => {
    const applyFilter = jest.fn();
    const wrapper = mount(
      <FormsListToolbar
        applyFilter={applyFilter}
        setFilterFormNames={jest.fn()}
        filterFormNames={[]}
      />
    );
    act(() => {
      wrapper.find('Toolbar').props()['clearAllFilters']();
    });
    expect(applyFilter).toHaveBeenCalled();
  });

  it('refresh click', () => {
    const applyFilter = jest.fn();
    const wrapper = mount(
      <FormsListToolbar
        applyFilter={applyFilter}
        setFilterFormNames={jest.fn()}
        filterFormNames={[]}
      />
    );
    act(() => {
      wrapper.find(Tooltip).find(Button).simulate('click');
    });
    expect(applyFilter).toHaveBeenCalled();
  });

  it('enter clicked', () => {
    const applyFilter = jest.fn();
    const wrapper = mount(
      <FormsListToolbar
        applyFilter={applyFilter}
        setFilterFormNames={jest.fn()}
        filterFormNames={[]}
      />
    );
    act(() => {
      wrapper
        .find('TextInputBase')
        .props()
        ['onKeyPress']({
          key: 'Enter',
          target: {
            value: 'form1'
          }
        } as any);
    });
    wrapper.find('#apply-filter').find('button').simulate('click');
    expect(applyFilter).toHaveBeenCalled();
  });

  it('on delete chip', () => {
    const applyFilter = jest.fn();
    const wrapper = mount(
      <FormsListToolbar
        applyFilter={applyFilter}
        setFilterFormNames={jest.fn()}
        filterFormNames={[]}
      />
    );
    act(() => {
      wrapper.find(ToolbarFilter).props()['deleteChip']('Form name', 'form1');
    });
    expect(applyFilter).toHaveBeenCalled();
  });
});
