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
import {
  Card,
  Grid,
  GridItem,
  PageSection,
  ExpandableSection
} from '@patternfly/react-core';
import React, { useState, useEffect } from 'react';
import UserTaskPageHeader from '../../Molecules/UserTaskPageHeader/UserTaskPageHeader';
import './DataListExpandable.css';
import TaskListByState from '../../Organisms/TaskListByState/TaskListByState';
import {
  componentOuiaProps,
  ouiaPageTypeAndObjectId,
  OUIAProps
} from '@kogito-apps/common';

const DataListContainerExpandable: React.FC<OUIAProps> = ({
  ouiaId,
  ouiaSafe
}) => {
  const [isExpanded, setIsExpanded] = useState(false);

  const onToggle = () => {
    setIsExpanded(!isExpanded);
  };

  useEffect(() => {
    return ouiaPageTypeAndObjectId('user-tasks', 'true');
  });

  return (
    <React.Fragment>
      <div
        {...componentOuiaProps(ouiaId, 'DataListContainerExpandable', ouiaSafe)}
      >
        <UserTaskPageHeader />
        <PageSection>
          <Grid hasGutter md={1}>
            <GridItem span={12}>
              <Card className="dataList">
                <ExpandableSection
                  toggleText={
                    isExpanded ? 'READY Show Less' : 'READY Show More'
                  }
                  onToggle={onToggle}
                  isExpanded={isExpanded}
                >
                  <TaskListByState currentState={'Ready'} />
                </ExpandableSection>
              </Card>
            </GridItem>
          </Grid>
        </PageSection>
      </div>
    </React.Fragment>
  );
};

export default DataListContainerExpandable;
