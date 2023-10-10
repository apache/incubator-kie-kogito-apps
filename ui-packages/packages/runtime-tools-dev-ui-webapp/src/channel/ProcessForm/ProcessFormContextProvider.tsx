import React from 'react';
import ProcessFormContext from './ProcessFormContext';
import { ProcessFormGatewayApiImpl } from './ProcessFormGatewayApi';

interface IOwnProps {
  children;
}

const ProcessFormContextProvider: React.FC<IOwnProps> = ({ children }) => {
  return (
    <ProcessFormContext.Provider value={new ProcessFormGatewayApiImpl()}>
      {children}
    </ProcessFormContext.Provider>
  );
};

export default ProcessFormContextProvider;
