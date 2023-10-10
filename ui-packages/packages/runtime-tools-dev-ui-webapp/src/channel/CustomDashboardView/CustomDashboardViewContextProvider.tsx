import React from 'react';
import CustomDashboardViewContext from './CustomDashboardViewContext';
import { CustomDashboardViewGatewayApiImpl } from './CustomDashboardViewGatewayApi';

interface CustomDashboardViewContextProviderProps {
  children;
}

const CustomDashboardViewContextProvider: React.FC<
  CustomDashboardViewContextProviderProps
> = ({ children }) => {
  return (
    <CustomDashboardViewContext.Provider
      value={new CustomDashboardViewGatewayApiImpl()}
    >
      {children}
    </CustomDashboardViewContext.Provider>
  );
};

export default CustomDashboardViewContextProvider;
