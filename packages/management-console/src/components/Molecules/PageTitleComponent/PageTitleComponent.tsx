import React from 'react';
import { Title, withOuiaContext, InjectedOuiaProps } from '@patternfly/react-core';
import { componentOuiaProps } from '@kogito-apps/common';

export interface IOwnProps {
  title: any;
}

const PageTitleComponent: React.FC<IOwnProps & InjectedOuiaProps> = ({
  title,
  ouiaContext,
  ouiaId
}) => {
  return (
    <React.Fragment>
      <Title headingLevel="h1" size="4xl"
        {...componentOuiaProps(ouiaContext, ouiaId, "PageTitle", true)}>
        {title}
      </Title>
    </React.Fragment>
  );
};

const PageTitleComponentWithContext = withOuiaContext(PageTitleComponent);
export default PageTitleComponentWithContext;
