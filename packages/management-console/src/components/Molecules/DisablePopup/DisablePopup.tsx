import React from 'react';
import { Tooltip, InjectedOuiaProps, withOuiaContext } from '@patternfly/react-core';
import { ProcessInstance } from '../../../graphql/types';
import { componentOuiaProps } from '@kogito-apps/common';

interface IOwnProps {
  processInstanceData: ProcessInstance;
  component: any;
}

const DisablePopup: React.FC<IOwnProps & InjectedOuiaProps> = ({
  processInstanceData,
  component,
  ouiaContext,
  ouiaId
}) => {
  let content = ''
  const distance = -15
  if (
    !processInstanceData.addons.includes('process-management') &&
    processInstanceData.serviceUrl === null
  ) {
  
    content = 'Management add-on capability not enabled & missing the kogito.service.url property. Contact your administrator to set up.'
  
  } else if (
    processInstanceData.serviceUrl === null &&
    processInstanceData.addons.includes('process-management')
  ) {
    
    content = 'This Kogito runtime is missing the kogito.service.url property. Contact your administrator to set up.'
  
  } else if (
    !processInstanceData.addons.includes('process-management') &&
    processInstanceData.serviceUrl !== null
  ) {
    
    content = 'Management add-on capability not enabled. Contact your administrator to set up'
  }
  return (
    <Tooltip
      {...componentOuiaProps(ouiaContext, ouiaId, 'DisabledPopup', true)} content={content} distance={distance}
    >
      {component}
    </Tooltip>
  );
  
};

const DisablePopupWithContext = withOuiaContext(DisablePopup);
export default DisablePopupWithContext;
