import React, { useContext } from 'react';
import { CustomDashboardListGatewayApi } from './CustomDashboardListGatewayApi';

const CustomDashboardListContext =
  React.createContext<CustomDashboardListGatewayApi>(null);

export const useCustomDashboardListGatewayApi =
  (): CustomDashboardListGatewayApi =>
    useContext<CustomDashboardListGatewayApi>(CustomDashboardListContext);

export default CustomDashboardListContext;
