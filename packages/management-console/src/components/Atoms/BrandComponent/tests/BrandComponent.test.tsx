import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import BrandComponent from '../BrandComponent';
import { getWrapper } from '@kogito-apps/common';

describe('Brand component tests', () => {
  it('snapshot testing', () => {
    const wrapper = getWrapper(<Router><BrandComponent/></Router>, 'BrandComponent')
    expect(wrapper).toMatchSnapshot();
  });
});