import React from 'react';
import { Button, InjectedOuiaProps, withOuiaContext } from '@patternfly/react-core';
import { ExternalLinkAltIcon } from '@patternfly/react-icons';
import { componentOuiaProps } from '@kogito-apps/common';

interface IOwnProps {
  serviceUrl: string;
  isLinkShown: boolean;
}

const EndpointLink: React.FC<IOwnProps & InjectedOuiaProps> = ({
  serviceUrl,
  isLinkShown,
  ouiaContext,
  ouiaId
}) => {
  return (
    <>
      {serviceUrl !== null ? (
        <Button
          {...componentOuiaProps(ouiaContext, ouiaId, 'EndpointLink', true)}
          component={'a'}
          variant={'link'}
          target={'_blank'}
          href={`${serviceUrl}`}
          isInline={true}
        >
          {isLinkShown ? serviceUrl : 'Endpoint'}
          {<ExternalLinkAltIcon className="pf-u-ml-xs" />}
        </Button>
      ) : (
        ''
      )}
    </>
  );
};

const EndpointLinkWithContext = withOuiaContext(EndpointLink);
export default EndpointLinkWithContext;
