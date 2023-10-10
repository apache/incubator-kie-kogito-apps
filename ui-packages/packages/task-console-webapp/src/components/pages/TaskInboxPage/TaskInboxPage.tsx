import React, { useEffect } from 'react';
import { Card, Grid, GridItem, PageSection } from '@patternfly/react-core';
import {
  OUIAProps,
  ouiaPageTypeAndObjectId,
  componentOuiaProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { PageTitle } from '@kogito-apps/consoles-common/dist/components/layout/PageTitle';
import TaskInboxContainer from './container/TaskInboxContainer/TaskInboxContainer';
import '../../styles.css';

const TaskInboxPage: React.FC<OUIAProps> = (ouiaId, ouiaSafe) => {
  useEffect(() => {
    return ouiaPageTypeAndObjectId('task-inbox-page');
  });

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
        <PageTitle title="Task Inbox" />
      </PageSection>
      <PageSection
        {...componentOuiaProps(
          'content' + (ouiaId ? '-' + ouiaId : ''),
          'task-inbox-page',
          ouiaSafe
        )}
      >
        <Grid hasGutter md={1} className={'kogito-task-console__full-size'}>
          <GridItem span={12} className={'kogito-task-console__full-size'}>
            <Card className={'kogito-task-console__full-size'}>
              <TaskInboxContainer />
            </Card>
          </GridItem>
        </Grid>
      </PageSection>
    </React.Fragment>
  );
};

export default TaskInboxPage;
