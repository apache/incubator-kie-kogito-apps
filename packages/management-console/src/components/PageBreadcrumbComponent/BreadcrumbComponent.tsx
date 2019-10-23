import React from 'react';
import { Breadcrumb, BreadcrumbItem } from '@patternfly/react-core';
import { RouteComponentProps } from 'react-router';

export interface IOwnProps {}
export interface IStateProps {}
const BreadcrumbComponent: React.FunctionComponent<IOwnProps> = () => {
  return (
    <Breadcrumb>
      <BreadcrumbItem to="#">Dashboard</BreadcrumbItem>
      <BreadcrumbItem to="#">Instances</BreadcrumbItem>
      <BreadcrumbItem to="#" isActive>
        Instance
      </BreadcrumbItem>
    </Breadcrumb>
  );
};

export default BreadcrumbComponent;
