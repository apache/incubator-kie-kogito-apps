import React from 'react';
import { mount } from 'enzyme';
import FormDetails from '../FormDetails';
import { MockedFormDetailsDriver } from '../../../tests/mocks/MockedFormDetailsDriver';
import { act } from 'react-dom/test-utils';

jest.mock('../../FormEditor/FormEditor');
jest.mock('../../../containers/FormDisplayerContainer/FormDisplayerContainer');

describe('form details tests', () => {
  const driver = new MockedFormDetailsDriver();

  it('render form details - source', async () => {
    const props = {
      isEnvelopeConnectedToChannel: true,
      driver: driver,
      formData: {
        name: 'form1',
        type: 'HTML' as any,
        lastModified: new Date('2020-07-11T18:30:00.000Z')
      },
      targetOrigin: 'http://localhost:9000'
    };
    let wrapper;
    await act(async () => {
      wrapper = mount(<FormDetails {...props} />);
    });
    wrapper = wrapper.update();
    expect(wrapper).toMatchSnapshot();
  });

  it('render form details - config', async () => {
    const props = {
      isEnvelopeConnectedToChannel: true,
      driver: driver,
      formData: {
        name: 'form1',
        type: 'html' as any,
        lastModified: new Date('2020-07-11T18:30:00.000Z')
      },
      targetOrigin: 'http://localhost:9000'
    };
    let wrapper;
    await act(async () => {
      wrapper = mount(<FormDetails {...props} />);
    });
    wrapper = wrapper.update();
    await act(async () => {
      wrapper.find('TabButton').at(1).find('button').simulate('click');
    });
    await act(async () => {
      wrapper = wrapper.update();
    });
    expect(wrapper).toMatchSnapshot();
  });
});
