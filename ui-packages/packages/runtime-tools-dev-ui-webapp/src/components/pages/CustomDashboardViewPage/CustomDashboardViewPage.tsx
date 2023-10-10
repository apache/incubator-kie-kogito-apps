import React from 'react';
import { OUIAProps } from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { Card } from '@patternfly/react-core/dist/js/components/Card';
import { PageSection } from '@patternfly/react-core/dist/js/components/Page';
import { PageTitle } from '@kogito-apps/consoles-common/dist/components/layout/PageTitle';
import { useHistory } from 'react-router-dom';
import CustomDashboardViewContainer from '../../containers/CustomDashboardViewContainer/CustomDashboardViewContainer';
import { CustomDashboardInfo } from '@kogito-apps/custom-dashboard-list';

const CustomDashboardViewPage: React.FC<OUIAProps> = () => {
  const history = useHistory();
  const dashboardInfo: CustomDashboardInfo = history.location.state['data'];
  return (
    <React.Fragment>
      <PageSection variant="light">
        <PageTitle title={dashboardInfo.name} />
      </PageSection>
      <PageSection>
        <Card className="Dev-ui__card-size Dev-ui__custom-dashboard-viewer">
          <CustomDashboardViewContainer dashboardName={dashboardInfo.name} />
        </Card>
      </PageSection>
    </React.Fragment>
  );
};
export default CustomDashboardViewPage;
