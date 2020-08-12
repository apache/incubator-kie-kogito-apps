import React from 'react';
import { shallow, mount } from 'enzyme';
import ProcessDetailsProcessVariables from '../ProcessDetailsProcessVariables';
import MockedReactJson from 'react-json-view';

jest.mock('react-json-view', () => jest.fn(() => null));
const props = {
  setUpdateJson: jest.fn(),
  displayLabel: false,
  updateJson: {
    trip: {
      begin: '2019-10-22T22:00:00Z[UTC]',
      city: 'Berlin',
      country: 'Germany',
      end: '2019-10-30T22:00:00Z[UTC]',
      visaRequired: false
    }
  },
  setDisplayLabel: jest.fn(),
  displaySuccess: false
};

const props2 = {
  setUpdateJson: jest.fn(),
  displayLabel: true,
  updateJson: {
    trip: {
      begin: '2019-10-22T22:00:00Z[UTC]',
      city: 'Berlin',
      country: 'Germany',
      end: '2019-10-30T22:00:00Z[UTC]',
      visaRequired: false
    }
  },
  setDisplayLabel: jest.fn(),
  displaySuccess: true
};
describe('ProcessVariables component tests', () => {
  it('snapshot testing without variables', () => {
    const wrapper = shallow(<ProcessDetailsProcessVariables {...props} />);
    expect(wrapper).toMatchSnapshot();
  });
  it('snapshot testing with variables', () => {
    const wrapper = mount(<ProcessDetailsProcessVariables {...props2} />);
    expect(wrapper).toMatchSnapshot();
    const onEdit = () => {
      return null;
    };
    const obj = {
      name: false,
      onEdit,
      src: {
        trip: {
          begin: '2019-10-22T22:00:00Z[UTC]',
          city: 'Berlin',
          country: 'Germany',
          end: '2019-10-30T22:00:00Z[UTC]',
          visaRequired: false
        }
      }
    };
    // expect(MockedReactJson).toHaveBeenCalledWith({...obj})
  });
});
