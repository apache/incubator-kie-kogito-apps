import React from 'react';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { EmbeddedCustomDashboardView } from '@kogito-apps/custom-dashboard-view';
import { useCustomDashboardViewGatewayApi } from '../../../channel/CustomDashboardView/CustomDashboardViewContext';
import { CustomDashboardViewGatewayApi } from '../../../channel/CustomDashboardView';
import { useDevUIAppContext } from '../../contexts/DevUIAppContext';

interface CustomDashboardViewContainerContainerProps {
  dashboardName: string;
}

const CustomDashboardViewContainer: React.FC<
  CustomDashboardViewContainerContainerProps & OUIAProps
> = ({ dashboardName, ouiaId, ouiaSafe }) => {
  const gatewayApi: CustomDashboardViewGatewayApi =
    useCustomDashboardViewGatewayApi();
  const appContext = useDevUIAppContext();

  return (
    <EmbeddedCustomDashboardView
      {...componentOuiaProps(ouiaId, 'process-details-container', ouiaSafe)}
      driver={gatewayApi}
      targetOrigin={appContext.getDevUIUrl()}
      dashboardName={dashboardName}
    />
  );
};

export default CustomDashboardViewContainer;
