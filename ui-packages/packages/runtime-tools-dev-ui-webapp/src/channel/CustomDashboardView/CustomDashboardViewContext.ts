import React, { useContext } from 'react';
import { CustomDashboardViewGatewayApi } from './CustomDashboardViewGatewayApi';

const CustomDashboardViewContext =
  React.createContext<CustomDashboardViewGatewayApi>(null);

export const useCustomDashboardViewGatewayApi =
  (): CustomDashboardViewGatewayApi =>
    useContext<CustomDashboardViewGatewayApi>(CustomDashboardViewContext);

export default CustomDashboardViewContext;
