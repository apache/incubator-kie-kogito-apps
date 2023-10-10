import React, { useEffect } from 'react';
import { Card } from '@patternfly/react-core/dist/js/components/Card';
import { PageSection } from '@patternfly/react-core/dist/js/components/Page';
import {
  OUIAProps,
  ouiaPageTypeAndObjectId
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { PageSectionHeader } from '@kogito-apps/consoles-common/dist/components/layout/PageSectionHeader';
import JobsManagementContainer from '../../containers/JobsManagementContainer/JobsManagementContainer';
import '../../styles.css';

const JobsManagementPage: React.FC<OUIAProps> = () => {
  useEffect(() => {
    return ouiaPageTypeAndObjectId('jobs-management');
  });

  return (
    <React.Fragment>
      <PageSectionHeader
        titleText="Jobs Management"
        breadcrumbText={['Home', 'Jobs']}
        breadcrumbPath={['/']}
      />
      <PageSection>
        <Card className="kogito-management-console__card-size">
          <JobsManagementContainer />
        </Card>
      </PageSection>
    </React.Fragment>
  );
};

export default JobsManagementPage;
