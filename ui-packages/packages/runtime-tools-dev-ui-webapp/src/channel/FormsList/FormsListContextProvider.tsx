import React from 'react';
import FormsListContext from './FormsListContext';
import { FormsListGatewayApiImpl } from './FormsListGatewayApi';

interface FormsListContextProviderProps {
  children;
}

const FormsListContextProvider: React.FC<FormsListContextProviderProps> = ({
  children
}) => {
  return (
    <FormsListContext.Provider value={new FormsListGatewayApiImpl()}>
      {children}
    </FormsListContext.Provider>
  );
};

export default FormsListContextProvider;
