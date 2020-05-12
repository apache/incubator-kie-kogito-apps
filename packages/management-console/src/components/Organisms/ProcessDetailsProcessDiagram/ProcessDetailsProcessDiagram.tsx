import { Card, CardBody, CardHeader, Title, InjectedOuiaProps, withOuiaContext } from '@patternfly/react-core';
import React from 'react';
import { url } from './Url';
import { componentOuiaProps } from '@kogito-apps/common';

const ProcessDetailsProcessDiagram: React.FC<InjectedOuiaProps> = ({
  ouiaContext,
  ouiaId
}) => {
  return (
    <Card
      {...componentOuiaProps(ouiaContext, ouiaId, 'ProcessDiagram', true)}
    >
      <CardHeader>
        <Title headingLevel="h3" size="xl">
          Process Diagram
        </Title>
      </CardHeader>
      <CardBody>
        <img src={url} />
      </CardBody>
    </Card>
  );
};

const ProcessDetailsProcessDiagramWithContext = withOuiaContext(ProcessDetailsProcessDiagram);
export default ProcessDetailsProcessDiagramWithContext;
