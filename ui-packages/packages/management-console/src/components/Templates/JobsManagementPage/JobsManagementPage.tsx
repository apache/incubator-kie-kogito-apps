import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import {
  componentOuiaProps,
  ouiaPageTypeAndObjectId,
  OUIAProps
} from '@kogito-apps/common';
import PageTitle from '../../Molecules/PageTitle/PageTitle';
import {
  Breadcrumb,
  BreadcrumbItem,
  Card,
  CardBody,
  PageSection
} from '@patternfly/react-core';

const JobsManagementPage: React.FC<OUIAProps> = ({ ouiaId, ouiaSafe }) => {
  useEffect(() => {
    return ouiaPageTypeAndObjectId('jobs-management');
  });
  return (
    <div {...componentOuiaProps(ouiaId, 'JobsManagementPage', ouiaSafe)}>
      <PageSection variant="light">
        <PageTitle title="Jobs Management" />
        <Breadcrumb>
          <BreadcrumbItem>
            <Link to={'/'}>Home</Link>
          </BreadcrumbItem>
          <BreadcrumbItem isActive>Jobs</BreadcrumbItem>
        </Breadcrumb>
      </PageSection>
      <PageSection>
        <Card>
          <CardBody>
            <p> Jobs Management section</p>
          </CardBody>
        </Card>
      </PageSection>
    </div>
  );
};

export default JobsManagementPage;
