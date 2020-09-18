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
import {
  TextContent,
  TextVariants,
  Text,
  Divider,
  TextList,
  TextListItem
} from '@patternfly/react-core';
import {
  ItemDescriptor,
  OUIAProps,
  componentOuiaProps
} from '@kogito-apps/common';
import { IOperation } from '../../Molecules/ProcessListToolbar/ProcessListToolbar';
import { getProcessInstanceDescription } from '../../../utils/Utils';

interface IOwnProps {
  operationResult: IOperation;
}
const ProcessListBulkInstances: React.FC<IOwnProps & OUIAProps> = ({
  operationResult,
  ouiaId,
  ouiaSafe
}) => {
  return (
    <div
      {...componentOuiaProps(ouiaId, 'process-list-bulk-instances', ouiaSafe)}
    >
      {Object.keys(operationResult.results.successInstances).length > 0 ? (
        <>
          <TextContent>
            <Text component={TextVariants.h2}>
              {operationResult.messages.successMessage}
            </Text>
            <TextList>
              {Object.entries(operationResult.results.successInstances).map(
                (process: any) => {
                  return (
                    <TextListItem key={process[0]}>
                      <strong>
                        <ItemDescriptor
                          itemDescription={getProcessInstanceDescription(
                            process[1]
                          )}
                        />
                      </strong>
                    </TextListItem>
                  );
                }
              )}
            </TextList>
          </TextContent>
          {Object.keys(operationResult.results.successInstances).length !== 0 &&
            operationResult.messages.warningMessage && (
              <TextContent className="pf-u-mt-sm">
                <Text component={TextVariants.small}>
                  {operationResult.messages.warningMessage}
                </Text>
              </TextContent>
            )}
        </>
      ) : (
        <TextContent>
          <Text component={TextVariants.h2}>
            {operationResult.messages.noProcessMessage}
          </Text>
        </TextContent>
      )}
      {Object.keys(operationResult.results.ignoredInstances).length !== 0 && (
        <>
          <Divider component="div" className="pf-u-my-xl" />
          <TextContent>
            <Text component={TextVariants.h2}>
              <span>Ignored processes:</span>
            </Text>
            <Text component={TextVariants.small} className="pf-u-mt-sm">
              <span>{operationResult.messages.ignoredMessage}</span>
            </Text>
            <TextList>
              {Object.entries(operationResult.results.ignoredInstances).map(
                (process: any) => {
                  return (
                    <TextListItem key={process[0]}>
                      <strong>
                        <ItemDescriptor
                          itemDescription={getProcessInstanceDescription(
                            process[1]
                          )}
                        />
                      </strong>
                    </TextListItem>
                  );
                }
              )}
            </TextList>
          </TextContent>
        </>
      )}
      {Object.keys(operationResult.results.failedInstances).length !== 0 && (
        <>
          <Divider component="div" className="pf-u-my-xl" />
          <TextContent>
            <Text component={TextVariants.h2}>Errors:</Text>
            <TextList>
              {Object.entries(operationResult.results.failedInstances).map(
                (process: any) => {
                  return (
                    <TextListItem key={process[0]}>
                      <strong>
                        <ItemDescriptor
                          itemDescription={getProcessInstanceDescription(
                            process[1]
                          )}
                        />
                      </strong>{' '}
                      -{process[1].errorMessage}
                    </TextListItem>
                  );
                }
              )}
            </TextList>
          </TextContent>
        </>
      )}
    </div>
  );
};

export default ProcessListBulkInstances;
