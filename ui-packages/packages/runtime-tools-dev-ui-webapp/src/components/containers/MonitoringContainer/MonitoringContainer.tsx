import React from 'react';
import { Dashboard, MonitoringView } from '@kogito-apps/monitoring';

interface Props {
  dataIndexUrl?: string;
  dashboard?: Dashboard;
  workflow?: string;
}

const MonitoringContainer: React.FC<Props> = ({
  workflow,
  dashboard,
  dataIndexUrl
}) => {
  const _dashboard = dashboard || Dashboard.MONITORING;
  return (
    <MonitoringView
      dashboard={_dashboard}
      workflow={workflow}
      dataIndexUrl={dataIndexUrl}
    />
  );
};

export default MonitoringContainer;
