import * as React from 'react';
import MonitoringWebapp from './components/MonitoringWebapp/MonitoringWebapp';
import '@patternfly/patternfly/patternfly.css';
import { Dashboard } from './Dashboard';

interface Props {
  dataIndexUrl: string;
  dashboard?: Dashboard;
  workflow?: string;
}

export const MonitoringView: React.FC<Props> = ({
  workflow,
  dashboard,
  dataIndexUrl
}) => {
  return (
    <React.Fragment>
      <MonitoringWebapp
        dataIndexUrl={dataIndexUrl}
        dashboard={dashboard}
        workflow={workflow}
      />
    </React.Fragment>
  );
};

export default MonitoringView;
