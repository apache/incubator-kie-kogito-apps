import React from 'react';
import { shallow } from 'enzyme';
import DataListContainer from '../DataListContainer';
import { ProcessInstanceState } from '../../../../graphql/types';

const props = {
  checkedArray: [ProcessInstanceState.Active, ProcessInstanceState.Error],
  setCheckedArray: jest.fn(),
  filters: {
    status: [ProcessInstanceState.Active, ProcessInstanceState.Error],
    businessKey: ['Tra']
  },
  setFilters: jest.fn(),
  wordsArray: [{ businessKey: { like: 'Tra' } }],
  setWordsArray: jest.fn()
};
describe('DataListContainer component tests', () => {
  it('Snapshot tests', () => {
    const wrapper = shallow(<DataListContainer {...props} />);
    expect(wrapper).toMatchSnapshot();
  });
});
