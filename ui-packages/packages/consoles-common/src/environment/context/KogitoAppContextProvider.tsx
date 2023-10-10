import React from 'react';
import { UserContext } from '../auth/Auth';
import KogitoAppContext, { AppContextImpl } from './KogitoAppContext';

interface IOwnProps {
  userContext: UserContext;
}

const KogitoAppContextProvider: React.FC<IOwnProps> = ({
  userContext,
  children
}) => {
  return (
    <KogitoAppContext.Provider value={new AppContextImpl(userContext)}>
      {children}
    </KogitoAppContext.Provider>
  );
};

export default KogitoAppContextProvider;
