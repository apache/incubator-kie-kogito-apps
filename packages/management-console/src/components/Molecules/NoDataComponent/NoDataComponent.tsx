import React, { useState, useEffect } from 'react';
import {
  PageSection,
  Bullseye,
  EmptyState,
  EmptyStateIcon,
  EmptyStateVariant,
  Button,
  EmptyStateBody,
  Title,
  InjectedOuiaProps,
  withOuiaContext
} from '@patternfly/react-core';
import { SearchIcon } from '@patternfly/react-icons';
import { Redirect, RouteComponentProps } from 'react-router';
import { ouiaPageTypeAndObjectId } from '@kogito-apps/common';

interface LocationProps {
  prev: any,
  rememberedData: any,
  title: string,
  description: string,
  buttonText: string
}

const NoDataComponent: React.FC<RouteComponentProps<{}, {}, LocationProps> &InjectedOuiaProps> = ({
  ouiaContext,
  ...props
}) => {
  let prevPath;
  if (props.location.state !== undefined) {
    prevPath = props.location.state.prev;
  } else {
    prevPath = '/ProcessInstances';
  }

  const tempPath = prevPath.split('/');
  prevPath = tempPath.filter(item => item);

  const [isRedirect, setIsredirect] = useState(false);
  const redirectHandler = () => {
    setIsredirect(true);
  };

  useEffect(() => {
    window.onpopstate = () => {
      props.history.push({ state: { ...props.location.state.rememberedData } });
    };
  });

  useEffect(() => { return ouiaPageTypeAndObjectId(ouiaContext, "no-data") })

  let finalPath = '';
  prevPath.map(item => (finalPath = finalPath + `/${item}`));

  return (
    <>
      {isRedirect && (
        <Redirect
          to={{
            pathname: finalPath,
            state: { ...props.location.state.rememberedData }
          }}
        />
      )}
      <PageSection isFilled={true}>
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
            <Button variant="primary" onClick={redirectHandler}>
              {props.location.state
                ? props.location.state.buttonText
                : 'Go to process instances'}
                            
            </Button>
          </EmptyState>
        </Bullseye>
      </PageSection>
    </>
  );
};

export default withOuiaContext(NoDataComponent);
