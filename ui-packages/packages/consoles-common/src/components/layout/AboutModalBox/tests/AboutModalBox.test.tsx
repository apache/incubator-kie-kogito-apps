import React from 'react';
import { render, screen } from '@testing-library/react';
import AboutModal from '../AboutModalBox';
import { BrandContext } from '../../BrandContext/BrandContext';

const props = {
  isOpenProp: true,
  handleModalToggleProp: jest.fn()
};
describe('AboutModal component tests', () => {
  it('snapshot testing', () => {
    process.env.KOGITO_APP_VERSION = '1.2.3-MOCKED-VERSION';
    const { container } = render(
      <BrandContext.Provider
        value={{
          imageSrc: 'kogito-image-src',
          altText: 'kogito image alt text'
        }}
      >
        <AboutModal {...props} />
      </BrandContext.Provider>
    );
    expect(container).toMatchSnapshot();
  });
});
