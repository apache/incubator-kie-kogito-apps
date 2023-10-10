import React, { useContext } from 'react';

export interface Brand {
  imageSrc: string;
  altText: string;
}

export const BrandContext = React.createContext<Brand>(null);

export const useBrandContext = (): Brand => useContext<Brand>(BrandContext);
