/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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
import { Card, CardBody, PageSection } from '@patternfly/react-core';
import {
  OUIAProps,
  ouiaPageTypeAndObjectId,
  componentOuiaProps
} from '@kogito-apps/ouia-tools';
import WorkflowFormContainer from '../../containers/WorkflowFormContainer/WorkflowFormContainer';
import '../../styles.css';
import { PageTitle } from '@kogito-apps/consoles-common';
import { FormNotification, Notification } from '@kogito-apps/components-common';
import { useHistory } from 'react-router-dom';
import { WorkflowDefinition } from '@kogito-apps/workflow-form';

const WorkflowFormPage: React.FC<OUIAProps> = ({ ouiaId, ouiaSafe }) => {
  const [notification, setNotification] = useState<Notification>();

  const history = useHistory();

  const workflowDefinition: WorkflowDefinition =
    history.location.state['workflowDefinition'];

  useEffect(() => {
    return ouiaPageTypeAndObjectId('workflow-form');
  });

  const goToWorkflowList = () => {
    history.push('/Processes');
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
      customActions: [
        {
          label: 'Go to workflow list',
          onClick: () => {
            setNotification(null);
            goToWorkflowList();
          }
        }
      ],
      close: () => {
        setNotification(null);
      }
    });
  };

  const onSubmitSuccess = (id: string): void => {
    const message = `The workflow with id: ${id} has started successfully.`;
    showNotification('success', message);
  };

  const onSubmitError = (details?: string) => {
    const message = 'Failed to trigger event.';
    showNotification('error', message, details);
  };

  return (
    <React.Fragment>
      <PageSection
        {...componentOuiaProps(
          `title${ouiaId ? '-' + ouiaId : ''}`,
          'workflow-form-page-section',
          ouiaSafe
        )}
        variant="light"
      >
        <PageTitle title={`Trigger cloud event`} />
        {notification && (
          <div>
            <FormNotification notification={notification} />
          </div>
        )}
      </PageSection>
      <PageSection
        {...componentOuiaProps(
          `content${ouiaId ? '-' + ouiaId : ''}`,
          'workflow-form-page-section',
          ouiaSafe
        )}
      >
        <Card className="Dev-ui__card-size">
          <CardBody className="pf-u-h-100">
            <WorkflowFormContainer
              workflowDefinitionData={workflowDefinition}
              onSubmitSuccess={onSubmitSuccess}
              onSubmitError={onSubmitError}
            />
          </CardBody>
        </Card>
      </PageSection>
    </React.Fragment>
  );
};

export default WorkflowFormPage;
