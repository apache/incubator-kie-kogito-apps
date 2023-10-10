import React from 'react';
import { Link } from 'react-router-dom';
import { PageSection } from '@patternfly/react-core/dist/js/components/Page';
import {
  Breadcrumb,
  BreadcrumbItem
} from '@patternfly/react-core/dist/js/components/Breadcrumb';
import { PageTitle } from '../PageTitle';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import * as H from 'history';

type pathType = Pick<H.Location, 'pathname' | 'state'>;
interface PageSectionHeaderProps {
  titleText: string;
  breadcrumbText?: string[];
  breadcrumbPath?: Array<pathType | string>;
}
export const PageSectionHeader: React.FC<
  PageSectionHeaderProps & OUIAProps
> = ({ titleText, breadcrumbText, breadcrumbPath, ouiaId, ouiaSafe }) => {
  const renderBreadcrumb = (): JSX.Element[] => {
    const items: JSX.Element[] = [];
    breadcrumbText.forEach((text, index) => {
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
    <PageSection
      variant="light"
      {...componentOuiaProps(ouiaId, 'page-section-header', ouiaSafe)}
    >
      {breadcrumbText && breadcrumbPath && (
        <Breadcrumb>{renderBreadcrumb()}</Breadcrumb>
      )}
      <PageTitle title={titleText} />
    </PageSection>
  );
};
