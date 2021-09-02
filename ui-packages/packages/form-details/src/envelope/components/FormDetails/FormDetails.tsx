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
import { ServerErrors } from '@kogito-apps/components-common';
import _ from 'lodash';
import { Form } from '../../../api';
import FormDisplayerContainer from '../../containers/FormDisplayerContainer/FormDisplayerContainer';

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
  const [formContent, setFormContent] = useState<Form>(null);
  const [error, setError] = useState<any>(null);
  useEffect(() => {
    /* istanbul ignore else */
    if (isEnvelopeConnectedToChannel) {
      init();
    }
  }, [isEnvelopeConnectedToChannel]);

  const init = async (): Promise<void> => {
    try {
      /* istanbul ignore else */
      if (formData) {
        const response = await driver.getFormContent(formData.name);
        setFormContent(response);
      }
    } catch (error) {
      setError(error);
    }
  };
  const panelContent: JSX.Element = (
    <DrawerPanelContent isResizable defaultSize={'800px'} minSize={'700px'}>
      <DrawerHead>
        {formContent && Object.keys(formContent)[0].length > 0 && (
          <span>
            <FormDisplayerContainer formContent={formContent} />
          </span>
        )}
      </DrawerHead>
    </DrawerPanelContent>
  );

  const onTabSelect = (event, tabIndex: number): void => {
    setActiveTab(tabIndex);
  };

  const getSource = (): string => {
    /* istanbul ignore else */
    if (!_.isEmpty(formContent)) {
      return formContent.source['source-content'];
    }
  };
  const getType = (): string => {
    /* istanbul ignore else */
    if (!_.isEmpty(formData)) {
      return formData.type;
    }
  };
  const getConfig = (): string => {
    /* istanbul ignore else */
    if (!_.isEmpty(formContent)) {
      return JSON.stringify(formContent.formConfiguration.resources);
    }
  };

  if (error) {
    return <ServerErrors error={error} variant={'large'} />;
  }

  return (
    <div {...componentOuiaProps(ouiaId, 'form-details', ouiaSafe)}>
      <Drawer isStatic>
        <DrawerContent panelContent={panelContent}>
          <Tabs isFilled activeKey={activeTab} onSelect={onTabSelect}>
            <Tab
              eventKey={0}
              title={<TabTitleText>Source</TabTitleText>}
              id="source-tab"
              aria-labelledby="source-tab"
            >
              <DrawerContentBody
                style={{
                  background: 'var(--pf-c-page__main-section--BackgroundColor)'
                }}
              >
                {activeTab === 0 && (
                  <FormView code={getSource()} isSource formType={getType()} />
                )}
                <Button variant="primary" className="pf-u-mt-md">
                  Refresh
                </Button>
              </DrawerContentBody>
            </Tab>
            <Tab
              eventKey={1}
              title={<TabTitleText>Connections</TabTitleText>}
              id="config-tab"
              aria-labelledby="config-tab"
            >
              <DrawerContentBody
                style={{
                  background: 'var(--pf-c-page__main-section--BackgroundColor)'
                }}
              >
                {activeTab === 1 && <FormView code={getConfig()} isConfig />}
                <Button variant="primary" className="pf-u-mt-md">
                  Refresh
                </Button>
              </DrawerContentBody>
            </Tab>
          </Tabs>
        </DrawerContent>
      </Drawer>
    </div>
  );
};

export default FormDetails;
