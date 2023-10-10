import React from 'react';
import { mount } from 'enzyme';
import FormDisplayerContainer from '../FormDisplayerContainer';
import { formContent } from '../../../tests/mocks/MockedFormDetailsDriver';
import RuntimeToolsFormDetailsContext, {
  FormDetailsContextImpl
} from '../../../components/contexts/FormDetailsContext';

jest.mock('uuid', () => {
  return () => 'testId';
});

Date.now = jest.fn(() => 1592000000000); // UTC Fri Jun 12 2020 22:13:20

describe('FormDisplayerContainer', () => {
  const props = {
    formContent: formContent,
    targetOrigin: 'http://localhost:9000'
  };
  it('render embeded formdisplayer', () => {
    const wrapper = mount(
      <RuntimeToolsFormDetailsContext.Provider
        value={new FormDetailsContextImpl()}
      >
        <FormDisplayerContainer {...props} />
      </RuntimeToolsFormDetailsContext.Provider>
    );
    expect(wrapper).toMatchSnapshot();
  });
});
