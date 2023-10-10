import React, { useEffect } from 'react';
import { OUIAProps } from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import {
  EmbeddedCustomDashboardList,
  CustomDashboardInfo
} from '@kogito-apps/custom-dashboard-list';
import { CustomDashboardListGatewayApi } from '../../../channel/CustomDashboardList';
import { useCustomDashboardListGatewayApi } from '../../../channel/CustomDashboardList/CustomDashboardListContext';
import { useHistory } from 'react-router-dom';

const CustomDashboardListContainer: React.FC<OUIAProps> = () => {
  const history = useHistory();
  const gatewayApi: CustomDashboardListGatewayApi =
    useCustomDashboardListGatewayApi();

  useEffect(() => {
    const unsubscriber = gatewayApi.onOpenCustomDashboardListen({
      onOpen(customDashboardInfo: CustomDashboardInfo) {
        history.push({
          pathname: `/customDashboard/${customDashboardInfo.name}`,
          state: {
            filter: gatewayApi.getCustomDashboardFilter(),
            data: customDashboardInfo
          }
        });
      }
    });
    return () => {
      unsubscriber.unSubscribe();
    };
  }, []);

  return <EmbeddedCustomDashboardList driver={gatewayApi} targetOrigin={'*'} />;
};

export default CustomDashboardListContainer;
