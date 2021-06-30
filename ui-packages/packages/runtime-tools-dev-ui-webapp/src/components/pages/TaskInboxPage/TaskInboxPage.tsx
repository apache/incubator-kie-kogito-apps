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
  Grid,
  GridItem,
  PageSection,
  Dropdown,
  DropdownToggle,
  DropdownItem,
  Tooltip
} from '@patternfly/react-core';
import { ThIcon } from '@patternfly/react-icons';
import {
  OUIAProps,
  ouiaPageTypeAndObjectId,
  componentOuiaProps
} from '@kogito-apps/ouia-tools';
import { PageTitle } from '@kogito-apps/consoles-common';
import { TaskInboxGatewayApi } from '../../../channel/TaskInbox';
import { useTaskInboxGatewayApi } from '../../../channel/TaskInbox/TaskInboxContext';
import TaskInboxContainer from '../../containers/TaskInboxContainer/TaskInboxContainer';
import '../../styles.css';
import { useDevUIAppContext } from '../../contexts/DevUIAppContext';

const TaskInboxPage: React.FC<OUIAProps> = (ouiaId, ouiaSafe) => {
  const [isOpen, setIsOpen] = useState(false);
  const [currentUser, setCurrentUser] = useState({});
  const gatewayApi: TaskInboxGatewayApi = useTaskInboxGatewayApi();
  const appContext = useDevUIAppContext();
  const allUsers = appContext.getAllUsers();
  useEffect(() => {
    return ouiaPageTypeAndObjectId('task-inbox-page');
  });

  const frame = window.parent.frames;
  console.log('window', frame);

  const onSelect = (event): void => {
    const selectedUser = event.target.innerHTML;
    const obj = allUsers.find(user => user.id === selectedUser);
    gatewayApi.setUser(obj);
    gatewayApi.query(0, 10);
    setCurrentUser(obj);

    setIsOpen(!isOpen);
  };

  const dropdownItems = (): JSX.Element[] => {
    const userIds = [];
    allUsers.map(user => {
      userIds.push(<DropdownItem key={user.id}>{user.id}</DropdownItem>);
    });
    return userIds;
  };

  const onToggle = (isOpen): void => {
    setIsOpen(isOpen);
  };

  return (
    <React.Fragment>
      <PageSection
        variant="light"
        {...componentOuiaProps(
          'header' + (ouiaId ? '-' + ouiaId : ''),
          'task-inbox-page',
          ouiaSafe
        )}
      >
        <Grid>
          <GridItem span={11}>
            <PageTitle title="Task Inbox" />
          </GridItem>
          <GridItem span={1}>
            <Dropdown
              onSelect={onSelect}
              toggle={
                <DropdownToggle
                  toggleIndicator={null}
                  onToggle={onToggle}
                  aria-label="Applications"
                  id="toggle-id-7"
                >
                  <Tooltip position="top" content={<div>{currentUser}</div>}>
                    <ThIcon />
                  </Tooltip>
                </DropdownToggle>
              }
              isOpen={isOpen}
              isPlain
              dropdownItems={dropdownItems()}
            />
          </GridItem>
        </Grid>
      </PageSection>
      <PageSection
        {...componentOuiaProps(
          'content' + (ouiaId ? '-' + ouiaId : ''),
          'task-inbox-page',
          ouiaSafe
        )}
      >
        <Grid hasGutter md={1} className={'kogito-task-console__full-size'}>
          <GridItem span={12} className={'kogito-task-console__full-size'}>
            <Card className={'kogito-task-console__full-size'}>
              <TaskInboxContainer />
            </Card>
          </GridItem>
        </Grid>
      </PageSection>
    </React.Fragment>
  );
};

export default TaskInboxPage;
