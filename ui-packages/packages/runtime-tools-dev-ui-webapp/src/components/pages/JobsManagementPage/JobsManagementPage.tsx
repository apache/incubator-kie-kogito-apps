import React, { useEffect } from 'react';
import { Card } from '@patternfly/react-core/dist/js/components/Card';
import { PageSection } from '@patternfly/react-core/dist/js/components/Page';
import {
  OUIAProps,
  ouiaPageTypeAndObjectId,
  componentOuiaProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { PageSectionHeader } from '@kogito-apps/consoles-common/dist/components/layout/PageSectionHeader';
import JobsManagementContainer from '../../containers/JobsManagementContainer/JobsManagementContainer';
import '../../styles.css';

const JobsManagementPage: React.FC<OUIAProps> = ({ ouiaId, ouiaSafe }) => {
  useEffect(() => {
    return ouiaPageTypeAndObjectId('jobs-management');
  });

  return (
    <React.Fragment>
      <PageSectionHeader titleText="Jobs Management" ouiaId={ouiaId} />
      <PageSection
        {...componentOuiaProps(
          ouiaId,
          'jobs-management-page-section',
          ouiaSafe
        )}
      >
        <Card className="Dev-ui__card-size">
          <JobsManagementContainer />
        </Card>
      </PageSection>
    </React.Fragment>
  );
};

export default JobsManagementPage;
