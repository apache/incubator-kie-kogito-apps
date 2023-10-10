import React, { useState } from 'react';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import {
  EmptyState,
  EmptyStateBody,
  EmptyStateIcon,
  EmptyStateVariant
} from '@patternfly/react-core/dist/js/components/EmptyState';
import { Title } from '@patternfly/react-core/dist/js/components/Title';
import { Button } from '@patternfly/react-core/dist/js/components/Button';
import {
  ClipboardCopy,
  ClipboardCopyVariant
} from '@patternfly/react-core/dist/js/components/ClipboardCopy';
import { Bullseye } from '@patternfly/react-core/dist/js/layouts/Bullseye';
import { ExclamationCircleIcon } from '@patternfly/react-icons/dist/js/icons/exclamation-circle-icon';

import '@patternfly/patternfly/patternfly.css';

interface FormErrorsWrapperProps {
  error: Error;
}

const FormErrorsWrapper: React.FC<FormErrorsWrapperProps & OUIAProps> = ({
  error,
  ouiaId,
  ouiaSafe
}) => {
  const [expanded, setExpanded] = useState<boolean>(false);

  const buildErrorTitle = () => {
    if (error.name && error.message) {
      return `${error.name}: ${error.message}`;
    }
    return error.toString();
  };

  const buildErrorInfo = () => {
    if (error.stack) {
      return error.stack;
    }
    return error.toString();
  };

  return (
    <Bullseye {...componentOuiaProps(ouiaId, 'form-errors-wrapper', ouiaSafe)}>
      <EmptyState variant={EmptyStateVariant.full}>
        <EmptyStateIcon icon={ExclamationCircleIcon} />
        <Title headingLevel="h4" size="lg">
          Error displaying form:
        </Title>
        <EmptyStateBody>
          {buildErrorTitle()}.{' '}
          <Button
            variant="link"
            isInline
            onClick={() => setExpanded(!expanded)}
          >
            See more details
          </Button>
        </EmptyStateBody>
        {expanded && (
          <EmptyStateBody>
            <ClipboardCopy
              isCode
              variant={ClipboardCopyVariant.expansion}
              isExpanded={true}
            >
              <div className="kogito-form-displayer-error-wrapper__align-left">
                {buildErrorInfo()}
              </div>
            </ClipboardCopy>
          </EmptyStateBody>
        )}
      </EmptyState>
    </Bullseye>
  );
};

export default FormErrorsWrapper;
