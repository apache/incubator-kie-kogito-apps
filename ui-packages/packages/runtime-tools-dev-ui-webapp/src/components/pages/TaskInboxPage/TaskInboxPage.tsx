import React, { useEffect } from 'react';
import { Grid, GridItem } from '@patternfly/react-core/dist/js/layouts/Grid';
import { Card } from '@patternfly/react-core/dist/js/components/Card';
import { PageSection } from '@patternfly/react-core/dist/js/components/Page';
import {
  OUIAProps,
  ouiaPageTypeAndObjectId,
  componentOuiaProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { PageTitle } from '@kogito-apps/consoles-common/dist/components/layout/PageTitle';
import TaskInboxContainer from '../../containers/TaskInboxContainer/TaskInboxContainer';
import TaskInboxSwitchUser from './components/TaskInboxSwitchUser';
import { useDevUIAppContext } from '../../contexts/DevUIAppContext';
import '../../styles.css';

const TaskInboxPage: React.FC<OUIAProps> = (ouiaId, ouiaSafe) => {
  const appContext = useDevUIAppContext();
  const user: string = appContext.getCurrentUser().id;
  useEffect(() => {
    return ouiaPageTypeAndObjectId('task-inbox-page');
  });

  const renderTaskInbox = (): JSX.Element => {
    return <TaskInboxContainer />;
  };

  return (
    <React.Fragment>
      <PageSection
        variant="light"
        {...componentOuiaProps(
          'header' + (ouiaId ? '-' + ouiaId : ''),
          'task-inbox-page',
          ouiaSafe
        )}
      >
        <Grid>
          <GridItem span={10}>
            <PageTitle title="Task Inbox" />
          </GridItem>
          <GridItem span={2}>
            {user.length > 0 && <TaskInboxSwitchUser user={user} />}
          </GridItem>
        </Grid>
      </PageSection>
      <PageSection
        {...componentOuiaProps(
          'content' + (ouiaId ? '-' + ouiaId : ''),
          'task-inbox-page-section',
          ouiaSafe
        )}
      >
        <Card className="Dev-ui__card-size">{renderTaskInbox()}</Card>
      </PageSection>
    </React.Fragment>
  );
};

export default TaskInboxPage;
