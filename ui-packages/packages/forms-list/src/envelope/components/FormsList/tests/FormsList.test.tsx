import React from 'react';
import { mount } from 'enzyme';
import FormsList from '../FormsList';
import { MockedFormsListDriver } from '../../../tests/mocks/MockedFormsListDriver';
import { ToggleGroupItem } from '@patternfly/react-core';
import { act } from 'react-dom/test-utils';
import wait from 'waait';

jest.mock('../../FormsGallery/FormsGallery');
jest.mock('../../FormsListToolbar/FormsListToolbar');
jest.mock('../../FormsTable/FormsTable');

describe('forms list tests', () => {
  const driver = new MockedFormsListDriver();

  it('envelope not connected to channel', () => {
    const props = {
      isEnvelopeConnectedToChannel: false,
      driver: null
    };
    let wrapper;
    act(() => {
      wrapper = mount(<FormsList {...props} />);
    });
    expect(
      wrapper.find(FormsList).props()['isEnvelopeConnectedToChannel']
    ).toBeFalsy();
  });

  it('render forms list - table', () => {
    const props = {
      isEnvelopeConnectedToChannel: true,
      driver: driver
    };
    let wrapper;
    act(() => {
      wrapper = mount(<FormsList {...props} />);
    });
    expect(wrapper).toMatchSnapshot();
  });

  it('render forms list - gallery', async () => {
    const props = {
      isEnvelopeConnectedToChannel: true,
      driver: driver
    };
    let wrapper;
    // switches to gallery view
    act(() => {
      wrapper = mount(<FormsList {...props} />);
      wrapper
        .find(ToggleGroupItem)
        .at(1)
        .find('button')
        .simulate('click');
    });
    await wait(0);
    act(() => {
      wrapper = wrapper.update();
    });
    expect(wrapper).toMatchSnapshot();

    // switches to table view
    act(() => {
      wrapper
        .find(ToggleGroupItem)
        .at(0)
        .find('button')
        .simulate('click');
    });
    await wait(0);
    act(() => {
      wrapper = wrapper.update();
    });
    expect(wrapper.find('MockedFormsTable').exists()).toBeTruthy();
  });
});
