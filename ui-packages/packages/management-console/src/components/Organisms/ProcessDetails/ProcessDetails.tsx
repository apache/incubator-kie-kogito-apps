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
import Moment from 'react-moment';
import {
  Button,
  Card,
  CardBody,
  CardHeader,
  Form,
  FormGroup,
  Text,
  TextVariants,
  Title,
  Tooltip
} from '@patternfly/react-core';
import React from 'react';
import { LevelDownAltIcon, LevelUpAltIcon } from '@patternfly/react-icons';
import { Link } from 'react-router-dom';
import {
  ItemDescriptor,
  GraphQL,
  EndpointLink,
  OUIAProps,
  componentOuiaProps
} from '@kogito-apps/common';
import {
  getProcessInstanceDescription,
  ProcessInstanceIconCreator
} from '../../../utils/Utils';
import ProcessInstance = GraphQL.ProcessInstance;

interface IOwnProps {
  data: {
    ProcessInstances?: Pick<
      ProcessInstance,
      | 'id'
      | 'processName'
      | 'businessKey'
      | 'serviceUrl'
      | 'state'
      | 'start'
      | 'end'
      | 'parentProcessInstance'
      | 'childProcessInstances'
      | 'lastUpdate'
    >[];
  };
  from: any;
}
const ProcessDetails: React.FC<IOwnProps & OUIAProps> = ({
  data,
  from,
  ouiaId,
  ouiaSafe
}) => {
  return (
    <Card
      {...componentOuiaProps(
        ouiaId ? ouiaId : data.ProcessInstances[0].id,
        'process-details',
        ouiaSafe
      )}
    >
      <CardHeader>
        <Title headingLevel="h3" size="xl">
          Details
        </Title>
      </CardHeader>
      <CardBody>
        <Form>
          <FormGroup label="Name" fieldId="name">
            <Text component={TextVariants.p}>
              {data.ProcessInstances[0].processName}
            </Text>
          </FormGroup>
          {data.ProcessInstances[0].businessKey && (
            <FormGroup label="Business key" fieldId="businessKey">
              <Text component={TextVariants.p}>
                {data.ProcessInstances[0].businessKey}
              </Text>
            </FormGroup>
          )}
          <FormGroup label="State" fieldId="state">
            <Text component={TextVariants.p}>
              {ProcessInstanceIconCreator(data.ProcessInstances[0].state)}
            </Text>
          </FormGroup>
          <FormGroup label="Id" fieldId="id">
            <Text
              component={TextVariants.p}
              className="kogito-management-console--u-WordBreak"
            >
              {data.ProcessInstances[0].id}
            </Text>
          </FormGroup>
          {data.ProcessInstances[0].serviceUrl ? (
            <FormGroup label="Endpoint" fieldId="endpoint">
              <Text
                component={TextVariants.p}
                className="kogito-management-console--u-WordBreak"
              >
                <EndpointLink
                  serviceUrl={data.ProcessInstances[0].serviceUrl}
                  isLinkShown={true}
                />
              </Text>
            </FormGroup>
          ) : (
            ''
          )}
          <FormGroup label="Start" fieldId="start">
            {data.ProcessInstances[0].start ? (
              <Text component={TextVariants.p}>
                <Moment fromNow>
                  {new Date(`${data.ProcessInstances[0].start}`)}
                </Moment>
              </Text>
            ) : (
              ''
            )}
          </FormGroup>

          {data.ProcessInstances[0].lastUpdate && (
            <FormGroup label="Last Updated" fieldId="lastUpdate">
              <Text component={TextVariants.p}>
                <Moment fromNow>
                  {new Date(`${data.ProcessInstances[0].lastUpdate}`)}
                </Moment>
              </Text>
            </FormGroup>
          )}

          {data.ProcessInstances[0].end && (
            <FormGroup label="End" fieldId="end">
              <Text component={TextVariants.p}>
                <Moment fromNow>
                  {new Date(`${data.ProcessInstances[0].end}`)}
                </Moment>
              </Text>
            </FormGroup>
          )}
          {data.ProcessInstances[0].parentProcessInstance !== null && (
            <FormGroup label="Parent Process" fieldId="parent">
              <div>
                <Link
                  to={{
                    pathname:
                      '/Process/' +
                      data.ProcessInstances[0].parentProcessInstance.id,
                    state: from
                  }}
                >
                  <Tooltip
                    content={data.ProcessInstances[0].parentProcessInstance.id}
                  >
                    <Button variant="link" icon={<LevelUpAltIcon />}>
                      <ItemDescriptor
                        itemDescription={getProcessInstanceDescription(
                          data.ProcessInstances[0].parentProcessInstance
                        )}
                      />
                    </Button>
                  </Tooltip>
                </Link>
              </div>
            </FormGroup>
          )}

          {data.ProcessInstances[0].childProcessInstances.length !== 0 && (
            <FormGroup label="Sub Processes" fieldId="child">
              {data.ProcessInstances[0].childProcessInstances.map(
                (child, index) => (
                  <div key={child.id}>
                    <Link
                      to={{ pathname: '/Process/' + child.id, state: from }}
                    >
                      <Tooltip content={child.id}>
                        <Button variant="link" icon={<LevelDownAltIcon />}>
                          <ItemDescriptor
                            itemDescription={getProcessInstanceDescription(
                              child
                            )}
                          />
                        </Button>
                      </Tooltip>
                    </Link>
                  </div>
                )
              )}
            </FormGroup>
          )}
        </Form>
      </CardBody>
    </Card>
  );
};

export default ProcessDetails;
