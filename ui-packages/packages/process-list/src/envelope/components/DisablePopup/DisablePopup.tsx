import React, { ReactElement } from 'react';
import { Tooltip } from '@patternfly/react-core/dist/js/components/Tooltip';
import { ProcessInstance } from '@kogito-apps/management-console-shared/dist/types';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';

interface DisablePopupProps {
  processInstanceData: ProcessInstance;
  component: ReactElement;
}

const DisablePopup: React.FC<DisablePopupProps & OUIAProps> = ({
  processInstanceData,
  component,
  ouiaId,
  ouiaSafe
}) => {
  let content = '';
  if (
    !processInstanceData.addons.includes('process-management') &&
    processInstanceData.serviceUrl === null
  ) {
    content =
      'Management add-on capability not enabled & missing the kogito.service.url property. Contact your administrator to set up.';
  } else if (
    processInstanceData.serviceUrl === null &&
    processInstanceData.addons.includes('process-management')
  ) {
    content =
      'This Kogito runtime is missing the kogito.service.url property. Contact your administrator to set up.';
  } else if (
    !processInstanceData.addons.includes('process-management') &&
    processInstanceData.serviceUrl !== null
  ) {
    content =
      'Management add-on capability not enabled. Contact your administrator to set up';
  }
  return (
    <Tooltip
      content={content}
      id="disable-popup-tooltip"
      {...componentOuiaProps(ouiaId, 'disable-popup', ouiaSafe)}
    >
      {component}
    </Tooltip>
  );
};

export default DisablePopup;
