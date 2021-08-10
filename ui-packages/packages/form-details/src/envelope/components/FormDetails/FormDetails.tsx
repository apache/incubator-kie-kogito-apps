/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React, { useEffect, useState } from 'react';
import { OUIAProps, componentOuiaProps } from '@kogito-apps/ouia-tools';
import { FormDetailsDriver } from '../../../api/FormDetailsDriver';
import { FormInfo } from '@kogito-apps/forms-list';
import {
  Button,
  Drawer,
  DrawerContent,
  DrawerContentBody,
  DrawerHead,
  DrawerPanelContent,
  Tab,
  Tabs,
  TabTitleText
} from '@patternfly/react-core';
import FormView from '../FormView/FormView';
import _ from 'lodash';

export interface FormDetailsProps {
  isEnvelopeConnectedToChannel: boolean;
  driver: FormDetailsDriver;
  formData: FormInfo;
}

const FormDetails: React.FC<FormDetailsProps & OUIAProps> = ({
  isEnvelopeConnectedToChannel,
  driver,
  formData,
  ouiaId,
  ouiaSafe
}) => {
  const [activeTab, setActiveTab] = useState<number>(0);
  const [formContent, setFormContent] = useState({});
  useEffect(() => {
    if (isEnvelopeConnectedToChannel) {
      init();
    }
  }, [isEnvelopeConnectedToChannel]);

  const init = async () => {
    try {
      if (formData['formData']) {
        const response = await driver.getFormContent(formData['formData'].name);
        setFormContent(response);
      }
    } catch (error) {
      // handle error here
    }
  };
  const panelContent = (
    <DrawerPanelContent isResizable defaultSize={'750px'} minSize={'400px'}>
      <DrawerHead>
        <span>render displayer here</span>
      </DrawerHead>
    </DrawerPanelContent>
  );

  const onTabSelect = (event, tabIndex) => {
    setActiveTab(tabIndex);
  };

  return (
    <div {...componentOuiaProps(ouiaId, 'form-details', ouiaSafe)}>
      <Drawer isStatic>
        <DrawerContent panelContent={panelContent}>
          <Tabs isFilled activeKey={activeTab} onSelect={onTabSelect}>
            <Tab eventKey={0} title={<TabTitleText>Source</TabTitleText>}>
              <DrawerContentBody
                style={{
                  background: 'var(--pf-c-page__main-section--BackgroundColor)'
                }}
              >
                {/* <FormView
                code={!_.isEmpty(formContent) && formContent['source']['sourceContent']}
                  isSource={true}
                  isConfig={false}
                  formType={
                    formData &&
                    formData['formData'] &&
                    formData['formData'].type
                  }
                  formContent={formContent}
                  setFormContent={setFormContent}
                /> */}
                <FormView
                  code={
                    !_.isEmpty(formContent) && formContent['formConfiguration']
                  }
                  isSource={false}
                  isConfig={true}
                  formContent={formContent}
                  setFormContent={setFormContent}
                />
                <Button variant="primary">Refresh</Button>
              </DrawerContentBody>
            </Tab>
            <Tab eventKey={1} title={<TabTitleText>Connections</TabTitleText>}>
              <DrawerContentBody>
                <FormView
                  code={
                    !_.isEmpty(formContent) && formContent['formConfiguration']
                  }
                  isSource={false}
                  isConfig={true}
                  formContent={formContent}
                  setFormContent={setFormContent}
                />
                <Button variant="primary">Refresh</Button>
              </DrawerContentBody>
            </Tab>
          </Tabs>
        </DrawerContent>
      </Drawer>
    </div>
  );
};

export default FormDetails;
