/**
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import React, { useEffect } from 'react';
import {
  PageSection,
  Breadcrumb,
  BreadcrumbItem,
  Card,
  CardBody
} from '@patternfly/react-core';
import {
  componentOuiaProps,
  DomainExplorerListDomains,
  ouiaPageTypeAndObjectId,
  OUIAProps
} from '@kogito-apps/common';
import { Link } from 'react-router-dom';
import PageTitle from '../../Molecules/PageTitle/PageTitle';

const DomainExplorerLandingPage: React.FC<OUIAProps> = ({
  ouiaId,
  ouiaSafe
}) => {
  useEffect(() => {
    return ouiaPageTypeAndObjectId('domain-explorer');
  });
  return (
    <div {...componentOuiaProps(ouiaId, 'DomainExplorerLandingPage', ouiaSafe)}>
      <PageSection variant="light">
        <PageTitle title="Domain Explorer" />
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
            <DomainExplorerListDomains />
          </CardBody>
        </Card>
      </PageSection>
    </div>
  );
};

export default DomainExplorerLandingPage;
