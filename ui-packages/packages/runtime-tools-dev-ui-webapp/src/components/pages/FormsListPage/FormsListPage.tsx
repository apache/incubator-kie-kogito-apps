import React, { useEffect } from 'react';
import { Card } from '@patternfly/react-core/dist/js/components/Card';
import { PageSection } from '@patternfly/react-core/dist/js/components/Page';
import {
  OUIAProps,
  ouiaPageTypeAndObjectId
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { PageSectionHeader } from '@kogito-apps/consoles-common/dist/components/layout/PageSectionHeader';
import FormsListContainer from '../../containers/FormsListContainer/FormsListContainer';
import '../../styles.css';

const FormsListPage: React.FC<OUIAProps> = () => {
  useEffect(() => {
    return ouiaPageTypeAndObjectId('forms-list');
  });

  return (
    <React.Fragment>
      <PageSectionHeader titleText="Forms" />
      <PageSection>
        <Card className="Dev-ui__card-size">
          <FormsListContainer />
        </Card>
      </PageSection>
    </React.Fragment>
  );
};

export default FormsListPage;
