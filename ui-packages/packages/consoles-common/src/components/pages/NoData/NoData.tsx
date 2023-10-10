import React, { useState } from 'react';
import {
  EmptyState,
  EmptyStateIcon,
  EmptyStateVariant,
  EmptyStateBody
} from '@patternfly/react-core/dist/js/components/EmptyState';
import { Button } from '@patternfly/react-core/dist/js/components/Button';
import { Title } from '@patternfly/react-core/dist/js/components/Title';
import { PageSection } from '@patternfly/react-core/dist/js/components/Page';
import { Bullseye } from '@patternfly/react-core/dist/js/layouts/Bullseye';
import { SearchIcon } from '@patternfly/react-icons/dist/js/icons/search-icon';
import { Redirect } from 'react-router';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';

export interface IOwnProps {
  defaultPath: string;
  defaultButton: string;
  location: any;
}

export const NoData: React.FC<IOwnProps & OUIAProps> = ({
  ouiaId,
  ouiaSafe,
  ...props
}) => {
  let prevPath;
  if (props.location.state !== undefined) {
    prevPath = props.location.state.prev;
  } else {
    prevPath = props.defaultPath;
  }

  const tempPath = prevPath.split('/');
  prevPath = tempPath.filter((item) => item);

  const [isRedirect, setIsredirect] = useState(false);
  const redirectHandler = () => {
    setIsredirect(true);
  };
  return (
    <>
      {isRedirect && <Redirect to={`/${prevPath[0]}`} />}
      <PageSection
        isFilled={true}
        {...componentOuiaProps(
          ouiaId,
          'no-data',
          ouiaSafe ? ouiaSafe : !isRedirect
        )}
      >
        <Bullseye>
          <EmptyState variant={EmptyStateVariant.full}>
            <EmptyStateIcon icon={SearchIcon} />
            <Title headingLevel="h1" size="4xl">
              {props.location.state ? props.location.state.title : 'No matches'}
            </Title>
            <EmptyStateBody>
              {props.location.state
                ? props.location.state.description
                : 'No data to display'}
            </EmptyStateBody>
            <Button
              variant="primary"
              onClick={redirectHandler}
              data-testid="redirect-button"
            >
              {props.location.state
                ? props.location.state.buttonText
                : props.defaultButton}
            </Button>
          </EmptyState>
        </Bullseye>
      </PageSection>
    </>
  );
};
