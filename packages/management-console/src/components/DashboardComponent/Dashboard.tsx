import React from 'react';
import { Page, SkipToContent } from '@patternfly/react-core';
import { Route } from 'react-router-dom';
import './Dashboard.css';
import HeaderComponent from '../PageHeaderComponent/HeaderComponent';
import DataListComponent from '../ListDetailComponent/DataListComponent/DataListComponent';
import ProcesssDetailsPage from '../ProcessDetailsPage/ProcessDetailsPage';

interface IOwnProps {}

const Dashboard: React.FC<IOwnProps> = () => {
  const pageId = 'main-content-page-layout-default-nav';
  const PageSkipToContent = <SkipToContent href={`#${pageId}`}>Skip to Content</SkipToContent>;

  return (
    <React.Fragment>
      <Page header={<HeaderComponent />} skipToContent={PageSkipToContent} mainContainerId={pageId} className="page">
        <Route exact path="/" component={DataListComponent} />
        <Route exact path="/Details/:instanceID" component={ProcesssDetailsPage} />
      </Page>
    </React.Fragment>
  );
};

export default Dashboard;
