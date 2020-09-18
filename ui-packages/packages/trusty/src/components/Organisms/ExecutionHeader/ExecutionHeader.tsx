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
import React from 'react';
import { Flex, FlexItem, Title, Tooltip } from '@patternfly/react-core';
import SkeletonStripe from '../../Atoms/SkeletonStripe/SkeletonStripe';
import ExecutionStatus from '../../Atoms/ExecutionStatus/ExecutionStatus';
import FormattedDate from '../../Atoms/FormattedDate/FormattedDate';
import { RemoteData, Execution } from '../../../types';
import './ExecutionHeader.scss';

type ExecutionHeaderProps = {
  execution: RemoteData<Error, Execution>;
};

const ExecutionHeader = (props: ExecutionHeaderProps) => {
  const { execution } = props;

  return (
    <section className="execution-header">
      <Flex>
        <FlexItem>
          {execution.status === 'LOADING' && (
            <SkeletonStripe
              isInline={true}
              customStyle={{
                height: '1.8em',
                width: 500,
                verticalAlign: 'baseline',
                margin: 0
              }}
            />
          )}
          {execution.status === 'SUCCESS' && (
            <Title size="3xl" headingLevel="h2">
              <span className="execution-header__uuid">
                ID# {execution.data.executionId}
              </span>
            </Title>
          )}
        </FlexItem>
        <FlexItem className="execution-header__property">
          {execution.status === 'SUCCESS' && (
            <Tooltip
              entryDelay={23}
              exitDelay={23}
              distance={5}
              position="bottom"
              content={
                <div>
                  <span>
                    Created on{' '}
                    <FormattedDate
                      date={execution.data.executionDate}
                      fullDateAndTime={true}
                    />
                  </span>
                  <br />
                  <span>Executed by {execution.data.executorName}</span>
                </div>
              }
            >
              <div>
                <ExecutionStatus
                  result={
                    execution.data.executionSucceeded ? 'success' : 'failure'
                  }
                />
              </div>
            </Tooltip>
          )}
        </FlexItem>
      </Flex>
    </section>
  );
};

export default ExecutionHeader;
