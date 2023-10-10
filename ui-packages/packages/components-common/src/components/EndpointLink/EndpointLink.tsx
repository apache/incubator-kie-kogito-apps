import React from 'react';
import { Button } from '@patternfly/react-core/dist/js/components/Button';
import { ExternalLinkAltIcon } from '@patternfly/react-icons/dist/js/icons/external-link-alt-icon';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
interface IOwnProps {
  serviceUrl: string;
  isLinkShown: boolean;
  linkLabel?: string;
}

export const EndpointLink: React.FC<IOwnProps & OUIAProps> = ({
  serviceUrl,
  isLinkShown,
  linkLabel,
  ouiaId,
  ouiaSafe
}) => {
  if (serviceUrl) {
    return (
      <Button
        component={'a'}
        variant={'link'}
        target={'_blank'}
        href={`${serviceUrl}`}
        isInline={true}
        {...componentOuiaProps(ouiaId, 'endpoint-link', ouiaSafe)}
      >
        {isLinkShown ? serviceUrl : linkLabel || 'Endpoint'}
        {<ExternalLinkAltIcon className="pf-u-ml-xs" />}
      </Button>
    );
  } else {
    return <></>;
  }
};
