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
  ClipboardCopyVariant
} from '@patternfly/react-core';
import { ExclamationCircleIcon } from '@patternfly/react-icons';
import '../../styles.css';
import { withRouter, RouteComponentProps } from 'react-router-dom';
import { OUIAProps, componentOuiaProps } from '../../../utils/OuiaUtils';

interface IOwnProps {
  error: any;
  variant: string;
}
const ServerErrors: React.FC<IOwnProps & RouteComponentProps & OUIAProps> = ({
  ouiaId,
  ouiaSafe,
  ...props
}) => {
  const [displayError, setDisplayError] = useState(false);

  const renderContent = () => (
    <>
      <EmptyStateIcon
        icon={ExclamationCircleIcon}
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
          id="display-error"
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
    </>
  );

  const renderBullseye = (renderButton: boolean) => (
    <Bullseye {...componentOuiaProps(ouiaId, 'server-errors', ouiaSafe)}>
      <EmptyState variant={EmptyStateVariant.full}>
        {renderContent()}
        {renderButton && (
          <Button
            variant="primary"
            id="goback-button"
            onClick={() => props.history.goBack()}
          >
            Go back
          </Button>
        )}
      </EmptyState>
    </Bullseye>
  );

  return (
    <>
      {props.variant === 'large' && (
        <PageSection variant="light">{renderBullseye(true)}</PageSection>
      )}
      {props.variant === 'small' && renderBullseye(false)}
    </>
  );
};

export default withRouter(ServerErrors);
