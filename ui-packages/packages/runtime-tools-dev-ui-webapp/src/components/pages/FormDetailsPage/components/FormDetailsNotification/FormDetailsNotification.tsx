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
  close: () => void;
}

interface IOwnProps {
  notification: Notification;
}

const FormDetailsNotification: React.FC<IOwnProps & OUIAProps> = ({
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
        </React.Fragment>
      }
      actionClose={<AlertActionCloseButton onClose={notification.close} />}
      {...componentOuiaProps(ouiaId, 'notification-alert', ouiaSafe)}
    >
      {showDetails && notification.details && <p>{notification.details}</p>}
    </Alert>
  );
};

export default FormDetailsNotification;
