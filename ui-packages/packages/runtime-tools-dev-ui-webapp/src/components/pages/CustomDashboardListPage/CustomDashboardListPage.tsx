import React, { useEffect } from 'react';
import { Card } from '@patternfly/react-core/dist/js/components/Card';
import { PageSection } from '@patternfly/react-core/dist/js/components/Page';
import {
  OUIAProps,
  ouiaPageTypeAndObjectId
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { PageSectionHeader } from '@kogito-apps/consoles-common/dist/components/layout/PageSectionHeader';
import CustomDashboardListContainer from '../../containers/CustomDashboardListContainer/CustomDashboardListContainer';
import '../../styles.css';

const CustomDashboardListPage: React.FC<OUIAProps> = () => {
  useEffect(() => {
    return ouiaPageTypeAndObjectId('custom-dashboard-list');
  });

  return (
    <React.Fragment>
      <PageSectionHeader titleText="Dashboards" />
      <PageSection>
        <Card className="Dev-ui__card-size">
          <CustomDashboardListContainer />
        </Card>
      </PageSection>
    </React.Fragment>
  );
};

export default CustomDashboardListPage;
