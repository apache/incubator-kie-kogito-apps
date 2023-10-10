import React from 'react';
import { Route, Switch } from 'react-router-dom';
import { ApolloProvider } from 'react-apollo';
import { ApolloClient } from 'apollo-client';
import { MemoryRouter } from 'react-router';
import PageLayout from '@kogito-apps/consoles-common/dist/components/layout/PageLayout/PageLayout';
import { User } from '@kogito-apps/consoles-common/dist/environment/auth';
import DevUINav from '../DevUINav/DevUINav';
import JobsManagementContextProvider from '../../../channel/JobsManagement/JobsManagementContextProvider';
import ProcessDetailsContextProvider from '../../../channel/ProcessDetails/ProcessDetailsContextProvider';
import ProcessListContextProvider from '../../../channel/ProcessList/ProcessListContextProvider';
import TaskConsoleContextsProvider from '../../../channel/TaskInbox/TaskInboxContextProvider';
import TaskFormContextProvider from '../../../channel/TaskForms/TaskFormContextProvider';
import FormsListContextProvider from '../../../channel/FormsList/FormsListContextProvider';
import FormDetailsContextProvider from '../../../channel/FormDetails/FormDetailsContextProvider';
import DevUIAppContextProvider from '../../contexts/DevUIAppContextProvider';
import ProcessDefinitionListContextProvider from '../../../channel/ProcessDefinitionList/ProcessDefinitionListContextProvider';
import ProcessFormContextProvider from '../../../channel/ProcessForm/ProcessFormContextProvider';
import { CustomLabels } from '../../../api/CustomLabels';
import { DiagramPreviewSize } from '@kogito-apps/process-details/dist/api';
import WorkflowFormContextProvider from '../../../channel/WorkflowForm/WorkflowFormContextProvider';
import CustomDashboardListContextProvider from '../../../channel/CustomDashboardList/CustomDashboardListContextProvider';
import { CustomDashboardViewContextProvider } from '../../../channel/CustomDashboardView';
import CloudEventFormContextProvider from '../../../channel/CloudEventForm/CloudEventFormContextProvider';

interface IOwnProps {
  apolloClient: ApolloClient<any>;
  isProcessEnabled: boolean;
  isTracingEnabled: boolean;
  users: User[];
  children: React.ReactElement;
  devUIUrl: string;
  openApiPath: string;
  availablePages?: string[];
  customLabels: CustomLabels;
  omittedProcessTimelineEvents?: string[];
  diagramPreviewSize?: DiagramPreviewSize;
  isStunnerEnabled: boolean;
}

const DevUILayout: React.FC<IOwnProps> = ({
  apolloClient,
  isProcessEnabled,
  isTracingEnabled,
  users,
  devUIUrl,
  openApiPath,
  availablePages,
  customLabels,
  omittedProcessTimelineEvents,
  diagramPreviewSize,
  isStunnerEnabled,
  children
}) => {
  const renderPage = (routeProps) => {
    return (
      <PageLayout
        pageNavOpen={true}
        withHeader={false}
        PageNav={<DevUINav pathname={routeProps.location.pathname} />}
      >
        {children}
      </PageLayout>
    );
  };

  return (
    <ApolloProvider client={apolloClient}>
      <DevUIAppContextProvider
        users={users}
        devUIUrl={devUIUrl}
        openApiPath={openApiPath}
        isProcessEnabled={isProcessEnabled}
        isTracingEnabled={isTracingEnabled}
        availablePages={availablePages}
        customLabels={customLabels}
        omittedProcessTimelineEvents={omittedProcessTimelineEvents}
        diagramPreviewSize={diagramPreviewSize}
        isStunnerEnabled={isStunnerEnabled}
      >
        <TaskConsoleContextsProvider apolloClient={apolloClient}>
          <TaskFormContextProvider>
            <ProcessListContextProvider apolloClient={apolloClient}>
              <ProcessDetailsContextProvider apolloClient={apolloClient}>
                <JobsManagementContextProvider apolloClient={apolloClient}>
                  <ProcessDefinitionListContextProvider>
                    <FormsListContextProvider>
                      <CustomDashboardListContextProvider>
                        <CustomDashboardViewContextProvider>
                          <FormDetailsContextProvider>
                            <ProcessFormContextProvider>
                              <WorkflowFormContextProvider>
                                <CloudEventFormContextProvider>
                                  <MemoryRouter>
                                    <Switch>
                                      <Route path="/" render={renderPage} />
                                    </Switch>
                                  </MemoryRouter>
                                </CloudEventFormContextProvider>
                              </WorkflowFormContextProvider>
                            </ProcessFormContextProvider>
                          </FormDetailsContextProvider>
                        </CustomDashboardViewContextProvider>
                      </CustomDashboardListContextProvider>
                    </FormsListContextProvider>
                  </ProcessDefinitionListContextProvider>
                </JobsManagementContextProvider>
              </ProcessDetailsContextProvider>
            </ProcessListContextProvider>
          </TaskFormContextProvider>
        </TaskConsoleContextsProvider>
      </DevUIAppContextProvider>
    </ApolloProvider>
  );
};

export default DevUILayout;
