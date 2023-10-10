import React from 'react';
import FormDetailsContext from './FormDetailsContext';
import { FormDetailsGatewayApiImpl } from './FormDetailsGatewayApi';

interface IOwnProps {
  children;
}

const FormDetailsContextProvider: React.FC<IOwnProps> = ({ children }) => {
  return (
    <FormDetailsContext.Provider value={new FormDetailsGatewayApiImpl()}>
      {children}
    </FormDetailsContext.Provider>
  );
};

export default FormDetailsContextProvider;
