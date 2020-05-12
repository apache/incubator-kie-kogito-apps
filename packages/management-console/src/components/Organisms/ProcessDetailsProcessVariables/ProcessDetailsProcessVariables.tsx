import {
  Card,
  CardBody,
  CardHeader,
  TextContent,
  Title,
  InjectedOuiaProps,
  withOuiaContext
} from '@patternfly/react-core';
import React from 'react';
import ReactJson from 'react-json-view';
import { componentOuiaProps } from '@kogito-apps/common';

export interface IOwnProps {
  data: any
}

const ProcessDetailsProcessVariables: React.FC<IOwnProps & InjectedOuiaProps> = ({
  ouiaContext,
  ouiaId,
  data
}) => {
  return (
    <Card
      {...componentOuiaProps(ouiaContext, ouiaId, 'ProcessVariables', true)}
    >
      <CardHeader>
        <Title headingLevel="h3" size="xl">
          Process Variables
        </Title>
      </CardHeader>
      <CardBody>
        <TextContent>
          {data.ProcessInstances.map((item, index) => {
            return (
              <div key={index}>
                <ReactJson src={JSON.parse(item.variables)} />
              </div>
            );
          })}
        </TextContent>
      </CardBody>
    </Card>
  );
};

const ProcessDetailsProcessVariablesWithContext = withOuiaContext(ProcessDetailsProcessVariables);
export default ProcessDetailsProcessVariablesWithContext;
