import React, { useState } from 'react';

import {
  PageSection,
  Bullseye,
  EmptyState,
  EmptyStateIcon,
  EmptyStateVariant,
  Button,
  EmptyStateBody,
  Title,
  ClipboardCopy,
  ClipboardCopyVariant,
  InjectedOuiaProps,
  withOuiaContext
} from '@patternfly/react-core';
import { ExclamationCircleIcon } from '@patternfly/react-icons';
import './ServerErrors.css';
import { withRouter, RouteComponentProps } from 'react-router-dom';
import { componentOuiaProps } from '../../../utils/OuiaUtils';

export interface IOwnProps {
  history: any,
  message?: any,
  error: any
}

const ServerErrors: React.FC<IOwnProps & RouteComponentProps & InjectedOuiaProps> = ({
  ouiaContext,
  ouiaId,
  ...props
}) => {
  const [displayError, setDisplayError] = useState(false);

  return (
    <PageSection variant="light"
      {...componentOuiaProps(ouiaContext, ouiaId, 'ServerErrors', true)}
    >
      <Bullseye>
        <EmptyState variant={EmptyStateVariant.full}>
          <EmptyStateIcon
            icon={ExclamationCircleIcon}
            size="md"
            color="var(--pf-global--danger-color--100)"
          />
          <Title headingLevel="h1" size="4xl">
            Error fetching data
          </Title>
          <EmptyStateBody>
            An error occurred while accessing data.{' '}
            <Button
              variant="link"
              isInline
              onClick={() => setDisplayError(!displayError)}
            >
              See more details
            </Button>
          </EmptyStateBody>
          {displayError && (
            <EmptyStateBody>
              <ClipboardCopy
                isCode
                variant={ClipboardCopyVariant.expansion}
                isExpanded={true}
                className="pf-u-text-align-left"
              >
                {JSON.stringify(props.error)}
              </ClipboardCopy>
            </EmptyStateBody>
          )}
          <Button variant="primary" onClick={() => props.history.goBack()}>
            Go back
          </Button>
        </EmptyState>
      </Bullseye>
    </PageSection>
  );
};

export default withRouter(withOuiaContext(ServerErrors));
