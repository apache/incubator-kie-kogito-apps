import React, { useState } from 'react';
import {
  Alert,
  AlertActionCloseButton,
  AlertActionLink
} from '@patternfly/react-core';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';

export interface Notification {
  type: 'success' | 'error';
  message: string;
  details?: string;
  customAction?: Action;
  close: () => void;
}

export interface Action {
  label: string;
  onClick: () => void;
}

interface IOwnProps {
  notification: Notification;
}

const FormNotification: React.FC<IOwnProps & OUIAProps> = ({
  notification,
  ouiaId,
  ouiaSafe
}) => {
  const variant = notification.type === 'error' ? 'danger' : 'success';

  const [showDetails, setShowDetails] = useState<boolean>(false);

  return (
    <Alert
      isInline
      title={notification.message}
      variant={variant}
      actionLinks={
        <React.Fragment>
          {notification.details && (
            <AlertActionLink onClick={() => setShowDetails(!showDetails)}>
              View details
            </AlertActionLink>
          )}
          {notification.customAction && (
            <AlertActionLink onClick={notification.customAction.onClick}>
              {notification.customAction.label}
            </AlertActionLink>
          )}
        </React.Fragment>
      }
      actionClose={<AlertActionCloseButton onClose={notification.close} />}
      {...componentOuiaProps(ouiaId, 'form-notification-alert', ouiaSafe)}
    >
      {showDetails && notification.details && <p>{notification.details}</p>}
    </Alert>
  );
};

export default FormNotification;
