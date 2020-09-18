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
import { OUIAProps, componentOuiaProps, GraphQL } from '@kogito-apps/common';
import {
  Modal,
  Title,
  TitleSizes,
  Button,
  TextContent,
  Flex,
  FlexItem,
  Split,
  SplitItem,
  Text,
  TextVariants
} from '@patternfly/react-core';
import Moment from 'react-moment';
interface JobsPanelDetailsModalProps {
  modalTitle: JSX.Element;
  isModalOpen: boolean;
  handleModalToggle: () => void;
  job: GraphQL.Job;
}
const JobsPanelDetailsModal: React.FC<JobsPanelDetailsModalProps &
  OUIAProps> = ({
  modalTitle,
  isModalOpen,
  handleModalToggle,
  job,
  ouiaId,
  ouiaSafe
}) => {
  return (
    <Modal
      variant="large"
      aria-labelledby="Job details modal"
      aria-label="Job details modal"
      title=""
      header={
        <Title headingLevel="h1" size={TitleSizes['2xl']}>
          {modalTitle}
        </Title>
      }
      isOpen={isModalOpen}
      onClose={handleModalToggle}
      actions={[
        <Button
          key="confirm-selection"
          variant="primary"
          onClick={handleModalToggle}
        >
          OK
        </Button>
      ]}
      {...componentOuiaProps(ouiaId, 'job-details-modal', ouiaSafe)}
    >
      <div style={{ padding: '30px' }}>
        <TextContent>
          <Flex direction={{ default: 'column' }}>
            <FlexItem>
              <Split hasGutter>
                <SplitItem>
                  <Text component={TextVariants.h6}>Process Id: </Text>{' '}
                </SplitItem>
                <SplitItem>{job.processId}</SplitItem>
              </Split>
            </FlexItem>
            <FlexItem>
              <Split hasGutter>
                <SplitItem>
                  {' '}
                  <Text component={TextVariants.h6}>
                    Process Instance Id:{' '}
                  </Text>{' '}
                </SplitItem>
                <SplitItem>{job.processInstanceId}</SplitItem>
              </Split>
            </FlexItem>
            <FlexItem>
              <Split hasGutter>
                <SplitItem>
                  <Text component={TextVariants.h6}>Status: </Text>{' '}
                </SplitItem>
                <SplitItem>{job.status}</SplitItem>
              </Split>
            </FlexItem>
            <FlexItem>
              <Split hasGutter>
                <SplitItem>
                  <Text component={TextVariants.h6}>Priority: </Text>{' '}
                </SplitItem>
                <SplitItem>{job.priority}</SplitItem>
              </Split>
            </FlexItem>
            {job.repeatInterval && (
              <FlexItem>
                <Split hasGutter>
                  <SplitItem>
                    <Text component={TextVariants.h6}>RepeatInterval: </Text>
                  </SplitItem>
                  <SplitItem>{job.repeatInterval}</SplitItem>
                </Split>
              </FlexItem>
            )}
            {job.repeatLimit && (
              <FlexItem>
                <Split hasGutter>
                  <SplitItem>
                    <Text component={TextVariants.h6}>RepeatLimit: </Text>
                  </SplitItem>
                  <SplitItem>{job.repeatLimit}</SplitItem>
                </Split>
              </FlexItem>
            )}
            <FlexItem>
              <Split hasGutter>
                <SplitItem>
                  <Text component={TextVariants.h6}>ScheduledId: </Text>
                </SplitItem>
                <SplitItem>{job.scheduledId}</SplitItem>
              </Split>
            </FlexItem>
            <FlexItem>
              <Split hasGutter>
                <SplitItem>
                  <Text component={TextVariants.h6}>Retries: </Text>
                </SplitItem>
                <SplitItem>{job.retries}</SplitItem>
              </Split>
            </FlexItem>
            <FlexItem>
              <Split hasGutter>
                <SplitItem>
                  <Text component={TextVariants.h6}>Last Updated: </Text>
                </SplitItem>
                <SplitItem>
                  <Moment fromNow>{new Date(`${job.lastUpdate}`)}</Moment>
                </SplitItem>
              </Split>
            </FlexItem>
            <FlexItem>
              <Split hasGutter>
                <SplitItem>
                  <Text
                    component={TextVariants.h6}
                    style={{ whiteSpace: 'nowrap' }}
                  >
                    Callback Endpoint:{' '}
                  </Text>
                </SplitItem>
                <SplitItem>{job.callbackEndpoint}</SplitItem>
              </Split>
            </FlexItem>
          </Flex>
        </TextContent>
      </div>
    </Modal>
  );
};

export default JobsPanelDetailsModal;
