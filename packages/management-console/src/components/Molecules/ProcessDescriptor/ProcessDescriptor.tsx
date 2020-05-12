import React from 'react';
import {
  Tooltip,
  Badge,
  TextContent,
  Text,
  TextVariants,
  InjectedOuiaProps,
  withOuiaContext
} from '@patternfly/react-core';
import { ProcessInstance } from '../../../graphql/types';
import { componentOuiaProps } from '@kogito-apps/common';

interface IOwnProps {
  processInstanceData: Pick<
    ProcessInstance,
    'id' | 'processName' | 'businessKey'
  >;
}
const ProcessDescriptor: React.FC<IOwnProps & InjectedOuiaProps> = ({
  processInstanceData,
  ouiaContext,
  ouiaId
}) => {
  const idStringModifier = (strId: string) => {
    return (
      <TextContent className="pf-u-display-inline">
        <Text component={TextVariants.small} className="pf-u-display-inline">
          {strId.substring(0, 5)}
        </Text>
      </TextContent>
    );
  };
  return (
    <>
      <Tooltip content={processInstanceData.id}
        {...componentOuiaProps(ouiaContext, ouiaId, 'ProcessDescriptor', true)}
      >
        <span>
          {processInstanceData.processName}{' '}
          {processInstanceData.businessKey ? (
            <Badge>{processInstanceData.businessKey}</Badge>
          ) : (
            idStringModifier(processInstanceData.id)
          )}
        </span>
      </Tooltip>
    </>
  );
};

const ProcessDescriptorWithContext = withOuiaContext(ProcessDescriptor);
export default ProcessDescriptorWithContext;
