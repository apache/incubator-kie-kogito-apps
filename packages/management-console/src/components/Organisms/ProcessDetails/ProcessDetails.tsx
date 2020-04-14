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
import {
  LevelDownAltIcon,
  LevelUpAltIcon,
  OnRunningIcon,
  CheckCircleIcon,
  BanIcon,
  PausedIcon,
  ErrorCircleOIcon,
  ExternalLinkAltIcon
} from '@patternfly/react-icons';
import { Link } from 'react-router-dom';
import ProcessDescriptor from '../../Molecules/ProcessDescriptor/ProcessDescriptor';
import { stateIconCreator } from '../../../utils/Utils';

interface IOwnProps {
  data: any;
  from: any;
}
const ProcessDetails: React.FC<IOwnProps> = ({ data, from }) => {
  return (
    <Card>
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
              {stateIconCreator(data.ProcessInstances[0].state)}
            </Text>
          </FormGroup>
          <FormGroup label="Id" fieldId="id">
            <Text component={TextVariants.p}>
              {data.ProcessInstances[0].id}
            </Text>
          </FormGroup>
          <FormGroup label="Endpoint" fieldId="endpoint">
            {data.ProcessInstances[0].serviceUrl ? (
              <Text component={TextVariants.p}>
                <Button
                  component={'a'}
                  variant={'link'}
                  target={'_blank'}
                  href={`${data.ProcessInstances[0].serviceUrl}`}
                  isInline={true}
                >
                  {data.ProcessInstances[0].serviceUrl}
                  {<ExternalLinkAltIcon className="pf-u-ml-xs" />}
                </Button>
              </Text>
            ) : (
              ''
            )}
          </FormGroup>
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
                      <ProcessDescriptor
                        processInstanceData={
                          data.ProcessInstances[0].parentProcessInstance
                        }
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
                          <ProcessDescriptor processInstanceData={child} />
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
