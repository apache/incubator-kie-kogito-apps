import React, { useContext } from 'react';
import {
  Breadcrumb,
  BreadcrumbItem,
  Card,
  CardBody,
  Grid,
  GridItem,
  PageSection,
  withOuiaContext
} from '@patternfly/react-core';
import TaskConsoleContext, {
  IContext
} from '../../../context/TaskConsoleContext/TaskConsoleContext';
import { Link } from 'react-router-dom';
import { TaskInfo } from '../../../model/TaskInfo';
import TaskForm from '../../Organisms/TaskForm/TaskForm';
import PageTitle from '../../Molecules/PageTitle/PageTitle';

const UserTaskInstanceDetailsPage = props => {
  const context: IContext<TaskInfo> = useContext(TaskConsoleContext);

  const taskInfo = context.getActiveItem();

  return (
    <React.Fragment>
      <PageSection variant="light">
        <PageTitle title="Task Details" />
        <Breadcrumb>
          <BreadcrumbItem>
            <Link to={'/'}>Home</Link>
          </BreadcrumbItem>
        </Breadcrumb>
      </PageSection>
      <PageSection>
        <Grid gutter="md" className="pf-u-h-100">
          <GridItem span={12} className="pf-u-h-100">
            <Card className="pf-u-h-100">
              <CardBody className="pf-u-h-100">
                <TaskForm
                  taskInfo={taskInfo}
                  successCallback={() => props.history.goBack()}
                  errorCallback={() => props.history.goBack()}
                />
              </CardBody>
            </Card>
          </GridItem>
        </Grid>
      </PageSection>
    </React.Fragment>
  );
};

export default withOuiaContext(UserTaskInstanceDetailsPage);
