import React, { useEffect } from 'react';
import {
  PageSection,
  Breadcrumb,
  BreadcrumbItem,
  Card,
  CardBody,
  InjectedOuiaProps,
  withOuiaContext
} from '@patternfly/react-core';
import { DomainExplorer, ouiaPageTypeAndObjectId } from '@kogito-apps/common';
import { Link } from 'react-router-dom';
import PageTitleComponent from '../../Molecules/PageTitleComponent/PageTitleComponent';

const DomainExplorerLandingPage: React.FC<InjectedOuiaProps> = ({
  ouiaContext
}) => {
  useEffect(() => {
    return ouiaPageTypeAndObjectId(ouiaContext, 'domain-explorer');
  });
  return (
    <>
      <PageSection variant="light">
        <PageTitleComponent title="Domain Explorer" />
        <Breadcrumb>
          <BreadcrumbItem>
            <Link to={'/'}>Home</Link>
          </BreadcrumbItem>
          <BreadcrumbItem isActive>Domain Explorer</BreadcrumbItem>
        </Breadcrumb>
      </PageSection>
      <PageSection>
        <Card>
          <CardBody>
            <DomainExplorer />
          </CardBody>
        </Card>
      </PageSection>
    </>
  );
};

export default withOuiaContext(DomainExplorerLandingPage);
