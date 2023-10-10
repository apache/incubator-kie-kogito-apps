import React, { useEffect } from 'react';
import { Card } from '@patternfly/react-core/dist/js/components/Card';
import { PageSection } from '@patternfly/react-core/dist/js/components/Page';
import {
  OUIAProps,
  ouiaPageTypeAndObjectId,
  componentOuiaProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { RouteComponentProps } from 'react-router-dom';
import { PageSectionHeader } from '@kogito-apps/consoles-common/dist/components/layout/PageSectionHeader';
import ProcessListContainer from '../../containers/ProcessListContainer/ProcessListContainer';
import { StaticContext } from 'react-router';
import * as H from 'history';
import '../../styles.css';
import { ProcessListState } from '@kogito-apps/management-console-shared/dist/types';

interface MatchProps {
  instanceID: string;
}

const ProcessListPage: React.FC<
  RouteComponentProps<MatchProps, StaticContext, H.LocationState> & OUIAProps
> = (props) => {
  useEffect(() => {
    return ouiaPageTypeAndObjectId('process-instances');
  });
  const initialState: ProcessListState =
    props.location && (props.location.state as ProcessListState);

  return (
    <React.Fragment>
      <PageSectionHeader
        titleText="Process Instances"
        breadcrumbText={['Home', 'Processes']}
        breadcrumbPath={['/']}
        ouiaId={props.ouiaId}
      />
      <PageSection
        {...componentOuiaProps(
          props.ouiaId,
          'page-section-content',
          props.ouiaSafe
        )}
      >
        <Card className="kogito-management-console__card-size">
          <ProcessListContainer initialState={initialState} />
        </Card>
      </PageSection>
    </React.Fragment>
  );
};

export default ProcessListPage;
