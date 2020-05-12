import React from 'react';
import ProcessDetailsProcessVariables from '../ProcessDetailsProcessVariables';
import { getWrapper } from '@kogito-apps/common';

jest.mock('react-json-view', () => {
  const MockedJsonDetailsComponent = () => <div />;
  return MockedJsonDetailsComponent;
})

const props = {
  loading: true,
  data: {
    ProcessInstances: []
  }
};

const props2 = {
  loading: false,
  data: {
    ProcessInstances: [
      {
        variables:
          '{"trip":{"begin":"2019-10-22T22:00:00Z[UTC]","city":"Berlin","country":"Germany","end":"2019-10-30T22:00:00Z[UTC]","visaRequired":false},"hotel":{"address":{"city":"Berlin","country":"Germany","street":"street","zipCode":"12345"},"bookingNumber":"XX-012345","name":"Perfect hotel","phone":"09876543"},"traveller":{"address":{"city":"Karkow","country":"Poland","street":"palna","zipCode":"200300"},"email":"rob@redhat.com","firstName":"Rob","lastName":"Rob","nationality":"Polish"}}'
      }
    ]
  }
};
describe('Process Variables component', () => {
  it('Sample test case', () => {
    const wrapper = getWrapper(<ProcessDetailsProcessVariables {...props}/>, 'ProcessDetailsProcessVariables');
    expect(wrapper).toMatchSnapshot();
  });
  it('Assertion for props', () => {
    const wrapper = getWrapper(<ProcessDetailsProcessVariables {...props2}/>, 'ProcessDetailsProcessVariables');
    expect(wrapper).toMatchSnapshot();
  });
});
