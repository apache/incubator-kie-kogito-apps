import React from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';
import { TaskDetailsPage, TaskInboxPage } from '../../pages';

const TaskConsoleRoutes: React.FC = () => {
  return (
    <Switch>
      <Route exact path="/" render={() => <Redirect to="/TaskInbox" />} />
      <Route exact path="/TaskInbox" component={TaskInboxPage} />
      <Route
        exact
        path="/TaskDetails/:taskId"
        render={(routeProps) => <TaskDetailsPage {...routeProps} />}
      />
    </Switch>
  );
};

export default TaskConsoleRoutes;
