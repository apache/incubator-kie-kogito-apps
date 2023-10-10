import React from 'react';
import RuntimeToolsFormDetailsContext, {
  FormDetailsContextImpl
} from './FormDetailsContext';

interface IOwnProps {}

const FormDetailsContextProvider: React.FC<IOwnProps> = ({ children }) => {
  return (
    <RuntimeToolsFormDetailsContext.Provider
      value={new FormDetailsContextImpl()}
    >
      {children}
    </RuntimeToolsFormDetailsContext.Provider>
  );
};

export default FormDetailsContextProvider;
