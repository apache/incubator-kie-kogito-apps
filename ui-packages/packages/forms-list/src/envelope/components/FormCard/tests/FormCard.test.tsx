import React from 'react';
import { mount } from 'enzyme';
import {
  MockedFormsListDriver,
  formList
} from '../../../tests/mocks/MockedFormsListDriver';
import FormCard from '../FormCard';
import { Card } from '@patternfly/react-core/dist/js/components/Card';

describe('Form card tests', () => {
  Date.now = jest.fn(() => 1487076708000);
  const driver = new MockedFormsListDriver();
  it('renders card - with tsx', () => {
    const wrapper = mount(<FormCard driver={driver} formData={formList[0]} />);
    expect(wrapper).toMatchSnapshot();
  });
  it('renders card - with html', () => {
    const wrapper = mount(<FormCard driver={driver} formData={formList[1]} />);
    expect(wrapper).toMatchSnapshot();
  });
  it('simulate click on card', () => {
    const openFormSpy = jest.spyOn(driver, 'openForm');
    const wrapper = mount(<FormCard driver={driver} formData={formList[0]} />);
    wrapper.find(Card).simulate('click');
    expect(openFormSpy).toHaveBeenCalled();
  });
});
