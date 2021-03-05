import React from 'react';
import { Link } from 'react-router-dom';
import {
  PageSection,
  Breadcrumb,
  BreadcrumbItem
} from '@patternfly/react-core';
import PageTitle from '../PageTitle/PageTitle';

const PageSectionHeader = ({ titleText, breadcrumbText, breadcrumbPath }) => {
  const renderBreadcrumb = (): JSX.Element[] => {
    const items: JSX.Element[] = [];
    breadcrumbText.map((text, index) => {
      if (index === breadcrumbText.length - 1) {
        items.push(
          <BreadcrumbItem key={index} isActive>
            {text}
          </BreadcrumbItem>
        );
      } else {
        items.push(
          <BreadcrumbItem key={index}>
            <Link to={breadcrumbPath[index]}>{text}</Link>
          </BreadcrumbItem>
        );
      }
    });
    return items;
  };
  return (
    <PageSection variant="light">
      <PageTitle title={titleText} />
      <Breadcrumb>{renderBreadcrumb()}</Breadcrumb>
    </PageSection>
  );
};

export default PageSectionHeader;
