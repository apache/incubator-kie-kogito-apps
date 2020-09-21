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
import React from 'react';
import { Button } from '@patternfly/react-core';
import { ExternalLinkAltIcon } from '@patternfly/react-icons';
import { OUIAProps, componentOuiaProps } from '../../../utils/OuiaUtils';

interface IOwnProps {
  serviceUrl: string;
  isLinkShown: boolean;
  linkLabel?: string;
}

const EndpointLink: React.FC<IOwnProps & OUIAProps> = ({
  serviceUrl,
  isLinkShown,
  linkLabel,
  ouiaId,
  ouiaSafe
}) => {
  return (
    <>
      {serviceUrl !== null ? (
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
      ) : (
        ''
      )}
    </>
  );
};

export default EndpointLink;
