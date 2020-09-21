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
import React, { useContext } from 'react';
import {
  AboutModal,
  TextContent,
  Text,
  TextList,
  TextListItem
} from '@patternfly/react-core';
import '../../styles.css';
import { aboutLogoContext } from '../../contexts';
import aboutPageBackground from '../../../static/kogitoAbout.png';
import { OUIAProps, componentOuiaProps } from '../../../utils/OuiaUtils';

export interface IOwnProps {
  isOpenProp: boolean;
  handleModalToggleProp: any;
}
const AboutModalBox: React.FC<IOwnProps & OUIAProps> = ({
  isOpenProp,
  handleModalToggleProp,
  ouiaId,
  ouiaSafe
}) => {
  const dataIndexURL =
    // @ts-ignore
    window.DATA_INDEX_ENDPOINT || process.env.KOGITO_DATAINDEX_HTTP_URL;
  const logoSrc = useContext(aboutLogoContext);
  return (
    <AboutModal
      isOpen={isOpenProp}
      onClose={handleModalToggleProp}
      trademark={`${process.env.KOGITO_APP_NAME} is part of Kogito, an open source software released under the Apache Software License 2.0`}
      brandImageAlt="Kogito Logo"
      brandImageSrc={logoSrc}
      className="kogito-common--aboutModalBox"
      backgroundImageSrc={aboutPageBackground}
      {...componentOuiaProps(ouiaId, 'AboutModalBox', ouiaSafe)}
    >
      <TextContent>
        <Text component="h5" />
        <TextList component="dl">
          <TextListItem component="dt">Version: </TextListItem>
          <TextListItem component="dd">
            {process.env.KOGITO_APP_VERSION}
          </TextListItem>
          <TextListItem component="dt">License information: </TextListItem>
          <TextListItem component="dd">
            <a
              href="https://github.com/kiegroup/kogito-runtimes/blob/master/LICENSE"
              target="_blank"
            >
              https://github.com/kiegroup/kogito-runtimes/blob/master/LICENSE
            </a>
          </TextListItem>
          <TextListItem component="dt">Report a bug: </TextListItem>
          <TextListItem component="dd">
            <a href="https://issues.redhat.com/projects/KOGITO" target="_blank">
              https://issues.redhat.com/projects/KOGITO
            </a>
          </TextListItem>
          <TextListItem component="dt">Get involved/help/docs: </TextListItem>
          <TextListItem component="dd">
            <a
              href="https://docs.jboss.org/kogito/release/latest/html_single/"
              target="_blank"
            >
              https://docs.jboss.org/kogito/release/latest/html_single/
            </a>
          </TextListItem>
          <TextListItem component="dt">Kogito URL: </TextListItem>
          <TextListItem component="dd">
            <a href="http://kogito.kie.org" target="_blank">
              http://kogito.kie.org
            </a>
          </TextListItem>
          <TextListItem component="dt">Data-Index URL: </TextListItem>
          <TextListItem component="dd">{dataIndexURL} </TextListItem>
        </TextList>
      </TextContent>
    </AboutModal>
  );
};

export default AboutModalBox;
