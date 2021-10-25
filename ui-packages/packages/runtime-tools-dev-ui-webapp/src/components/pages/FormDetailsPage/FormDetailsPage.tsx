/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React, { useEffect, useState } from 'react';
import {
  Card,
  Label,
  PageSection,
  Text,
  TextVariants
} from '@patternfly/react-core';
import { OUIAProps, ouiaPageTypeAndObjectId } from '@kogito-apps/ouia-tools';
import FormDetailsContainer from '../../containers/FormDetailsContainer/FormDetailsContainer';
import '../../styles.css';
import { useHistory } from 'react-router-dom';
import { FormInfo } from '@kogito-apps/forms-list';
import { PageTitle } from '@kogito-apps/consoles-common';
import Moment from 'react-moment';
import FormNotification, {
  Notification
} from '../TaskDetailsPage/components/FormNotification/FormNotification';

const FormDetailsPage: React.FC<OUIAProps> = () => {
  const [notification, setNotification] = useState<Notification>();

  useEffect(() => {
    return ouiaPageTypeAndObjectId('form-detail');
  });
  const history = useHistory();
  const formData: FormInfo = history.location.state['formData'];

  const onSuccess = () => {
    const message = `The form '${formData.name}.${formData.type}' has been successfully saved.`;

    showNotification('success', message);
  };

  const onError = (details?: string) => {
    const message = `The form '${formData.name}.${formData.type}' couldn't be saved.`;

    showNotification('error', message, details);
  };

  const showNotification = (
    notificationType: 'error' | 'success',
    submitMessage: string,
    notificationDetails?: string
  ) => {
    setNotification({
      type: notificationType,
      message: submitMessage,
      details: notificationDetails,
      close: () => {
        setNotification(null);
      }
    });
  };

  return (
    <React.Fragment>
      <PageSection variant="light">
        <PageTitle
          title={formData.name}
          extra={<Label variant="outline">{formData.type}</Label>}
        />
        <Text component={TextVariants.p} style={{ marginTop: '10px' }}>
          <span style={{ fontWeight: 'bold' }}>Last modified:</span>{' '}
          <Moment fromNow>{formData.lastModified}</Moment>
        </Text>
        {notification && (
          <div className="kogito-task-console__task-details-page">
            <FormNotification notification={notification} />
          </div>
        )}
      </PageSection>
      <PageSection>
        <Card className="Dev-ui__card-size">
          <FormDetailsContainer
            formData={formData}
            onSuccess={onSuccess}
            onError={onError}
          />
        </Card>
      </PageSection>
    </React.Fragment>
  );
};

export default FormDetailsPage;
