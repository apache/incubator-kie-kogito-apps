import React, { useState } from 'react';
import {
  Alert,
  AlertActionCloseButton,
  AlertActionLink
} from '@patternfly/react-core/dist/js/components/Alert';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';

export interface Notification {
  type: 'success' | 'error';
  message: string;
  details?: string;
  customActions?: Action[];
  close: () => void;
}

export interface Action {
  label: string;
  onClick: () => void;
}

interface IOwnProps {
  notification: Notification;
}

export const FormNotification: React.FC<IOwnProps & OUIAProps> = ({
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
      data-testid={'alert-box'}
      actionLinks={
        <React.Fragment>
          {notification.details && (
            <AlertActionLink
              onClick={() => setShowDetails(!showDetails)}
              data-testid="view-details"
            >
              View details
            </AlertActionLink>
          )}
          {notification.customActions &&
            notification.customActions.length > 0 &&
            notification.customActions.map((customAction, index) => {
              return (
                <AlertActionLink
                  onClick={customAction.onClick}
                  key={index}
                  data-testid="custom-action"
                >
                  {customAction.label}
                </AlertActionLink>
              );
            })}
        </React.Fragment>
      }
      actionClose={
        <AlertActionCloseButton
          onClose={notification.close}
          data-testid={'close-button'}
        />
      }
      {...componentOuiaProps(ouiaId, 'form-notification-alert', ouiaSafe)}
    >
      {showDetails && notification.details && <p>{notification.details}</p>}
    </Alert>
  );
};
