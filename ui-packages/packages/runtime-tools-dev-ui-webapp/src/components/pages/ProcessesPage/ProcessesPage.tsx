import React, { ReactText, useEffect, useState } from 'react';
import { Card } from '@patternfly/react-core/dist/js/components/Card';
import { PageSection } from '@patternfly/react-core/dist/js/components/Page';
import {
  Tab,
  Tabs,
  TabTitleText
} from '@patternfly/react-core/dist/js/components/Tabs';
import {
  OUIAProps,
  ouiaPageTypeAndObjectId,
  componentOuiaProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { RouteComponentProps } from 'react-router-dom';
import { StaticContext } from 'react-router';
import * as H from 'history';
import { PageSectionHeader } from '@kogito-apps/consoles-common/dist/components/layout/PageSectionHeader';
import ProcessListContainer from '../../containers/ProcessListContainer/ProcessListContainer';
import '../../styles.css';
import { ProcessListState } from '@kogito-apps/management-console-shared/dist/types';
import ProcessDefinitionListContainer from '../../containers/ProcessDefinitionListContainer/ProcessDefinitionListContainer';
import { useDevUIAppContext } from '../../contexts/DevUIAppContext';

interface MatchProps {
  instanceID: string;
}

const ProcessesPage: React.FC<
  RouteComponentProps<MatchProps, StaticContext, H.LocationState> & OUIAProps
> = ({ ouiaId, ouiaSafe, ...props }) => {
  const apiContext = useDevUIAppContext();

  const [activeTabKey, setActiveTabKey] = useState<ReactText>(0);
  useEffect(() => {
    return ouiaPageTypeAndObjectId('process-instances');
  });

  const initialState: ProcessListState =
    props.location && (props.location.state as ProcessListState);

  const handleTabClick = (event, tabIndex) => {
    setActiveTabKey(tabIndex);
  };
  return (
    <React.Fragment>
      {activeTabKey === 0 && (
        <PageSectionHeader
          titleText={`${apiContext.customLabels.singularProcessLabel} Instances`}
          ouiaId={ouiaId}
        />
      )}
      {activeTabKey === 1 && (
        <PageSectionHeader
          titleText={`${apiContext.customLabels.singularProcessLabel} Definitions`}
          ouiaId={ouiaId}
        />
      )}
      <div>
        <Tabs
          activeKey={activeTabKey}
          onSelect={handleTabClick}
          isBox
          variant="light300"
          style={{
            background: 'white'
          }}
        >
          <Tab
            id="process-list-tab"
            eventKey={0}
            title={
              <TabTitleText>
                {apiContext.customLabels.singularProcessLabel} Instances
              </TabTitleText>
            }
          >
            <PageSection
              {...componentOuiaProps(
                ouiaId,
                'process-list-page-section',
                ouiaSafe
              )}
            >
              <Card className="Dev-ui__card-size">
                <ProcessListContainer initialState={initialState} />
              </Card>
            </PageSection>
          </Tab>
          <Tab
            id="process-definitions-tab"
            eventKey={1}
            title={
              <TabTitleText>
                {apiContext.customLabels.singularProcessLabel} Definitions
              </TabTitleText>
            }
          >
            <PageSection
              {...componentOuiaProps(
                ouiaId,
                'process-definition-list-page-section',
                ouiaSafe
              )}
            >
              <Card className="Dev-ui__card-size">
                <ProcessDefinitionListContainer />
              </Card>
            </PageSection>
          </Tab>
        </Tabs>
      </div>
    </React.Fragment>
  );
};

export default ProcessesPage;
