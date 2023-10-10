import React from 'react';
import { render, screen } from '@testing-library/react';
import { ItemDescriptor } from '../ItemDescriptor';

const item1 = {
  id: 'a1e139d5-4e77-48c9-84ae-34578e904e5a',
  name: 'HotelBooking',
  description: 'T1234HotelBooking01'
};

const item2 = {
  id: 'a1e139d5-4e77-48c9-84ae-34578e904e5a',
  name: 'HotelBooking'
};

const mockMath = Object.create(global.Math);
mockMath.random = () => 0.5;
global.Math = mockMath;
describe('ItemDescriptor component tests', () => {
  it('snapshot testing for business key available', () => {
    const { container } = render(<ItemDescriptor itemDescription={item1} />);
    expect(container).toMatchSnapshot();
  });
  it('snapshot testing for business key null', () => {
    const { container } = render(<ItemDescriptor itemDescription={item2} />);
    expect(container).toMatchSnapshot();
  });
});
