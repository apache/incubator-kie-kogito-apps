import React from 'react';
import { HttpLink } from 'apollo-link-http';
import { onError } from 'apollo-link-error';
import { ServerUnavailablePage } from '@kogito-apps/consoles-common/dist/components/pages/ServerUnavailablePage';
import { User } from '@kogito-apps/consoles-common/dist/environment/auth';
import { InMemoryCache, NormalizedCacheObject } from 'apollo-cache-inmemory';
import ApolloClient from 'apollo-client';
import DevUIRoutes from '../DevUIRoutes/DevUIRoutes';
import DevUILayout from '../DevUILayout/DevUILayout';
import ReactDOM from 'react-dom';
import { CustomLabels } from '../../../api/CustomLabels';
import { DiagramPreviewSize } from '@kogito-apps/process-details/dist/api';

interface IOwnProps {
  isProcessEnabled: boolean;
  isTracingEnabled: boolean;
  users: User[];
  dataIndexUrl: string;
  trustyServiceUrl: string;
  navigate: string;
  devUIUrl: string;
  openApiPath: string;
  availablePages: string[];
  customLabels: CustomLabels;
  omittedProcessTimelineEvents: string[];
  diagramPreviewSize?: DiagramPreviewSize;
  isStunnerEnabled: boolean;
}

const RuntimeTools: React.FC<IOwnProps> = ({
  users,
  dataIndexUrl,
  trustyServiceUrl,
  navigate,
  devUIUrl,
  openApiPath,
  isProcessEnabled,
  isTracingEnabled,
  availablePages,
  customLabels,
  omittedProcessTimelineEvents,
  diagramPreviewSize,
  isStunnerEnabled
}) => {
  const httpLink = new HttpLink({
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore
    uri: dataIndexUrl
  });

  const fallbackUI = onError(({ networkError }: any) => {
    if (networkError && networkError.stack === 'TypeError: Failed to fetch') {
      // eslint-disable-next-line react/no-render-return-value
      return ReactDOM.render(
        <DevUILayout
          apolloClient={client}
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
          <ServerUnavailablePage
            displayName={'Runtime Dev UI'}
            reload={() => window.location.reload()}
          />
        </DevUILayout>,
        document.getElementById('envelope-app')
      );
    }
  });

  const cache = new InMemoryCache();
  const client: ApolloClient<NormalizedCacheObject> = new ApolloClient({
    cache,
    link: fallbackUI.concat(httpLink)
  });

  return (
    <DevUILayout
      apolloClient={client}
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
      <DevUIRoutes
        navigate={navigate}
        trustyServiceUrl={trustyServiceUrl}
        dataIndexUrl={dataIndexUrl}
      />
    </DevUILayout>
  );
};

export default RuntimeTools;
