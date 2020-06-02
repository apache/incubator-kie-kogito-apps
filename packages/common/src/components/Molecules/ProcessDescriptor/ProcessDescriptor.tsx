import React from 'react';
import {
  Tooltip,
  Badge,
  TextContent,
  Text,
  TextVariants
} from '@patternfly/react-core';

interface IOwnProps {
  instanceData: any;
} 
const ProcessDescriptor: React.FC<IOwnProps> = ({ instanceData }) => {
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
      <Tooltip content={instanceData.id}>
        <span>
          {instanceData.processName ? instanceData.processName : instanceData.name}
          {instanceData.businessKey ? (
            <Badge>{instanceData.businessKey}</Badge>
          ) : (
            idStringModifier(instanceData.id)
          )}
        </span>
      </Tooltip>
    </>
  );
};

export default ProcessDescriptor;
