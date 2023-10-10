import React from 'react';
import CustomDashboardListContext from './CustomDashboardListContext';
import { CustomDashboardListGatewayApiImpl } from './CustomDashboardListGatewayApi';

interface CustomDashboardListContextProviderProps {
  children;
}

const CustomDashboardListContextProvider: React.FC<
  CustomDashboardListContextProviderProps
> = ({ children }) => {
  return (
    <CustomDashboardListContext.Provider
      value={new CustomDashboardListGatewayApiImpl()}
    >
      {children}
    </CustomDashboardListContext.Provider>
  );
};

export default CustomDashboardListContextProvider;
