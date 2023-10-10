import React from 'react';
import { Flex, FlexItem } from '@patternfly/react-core/dist/js/layouts/Flex';
import { Title } from '@patternfly/react-core/dist/js/components/Title';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';

export interface IOwnProps {
  title: any;
  extra?: JSX.Element;
}

export const PageTitle: React.FC<IOwnProps & OUIAProps> = ({
  title,
  extra,
  ouiaId,
  ouiaSafe
}) => {
  return (
    <Flex {...componentOuiaProps(ouiaId, 'page-title', ouiaSafe)}>
      <FlexItem spacer={{ default: 'spacerSm' }}>
        <Title headingLevel="h1" size="4xl">
          {title}
        </Title>
      </FlexItem>
      {extra ? (
        <FlexItem spacer={{ default: 'spacerSm' }}>{extra}</FlexItem>
      ) : null}
    </Flex>
  );
};
