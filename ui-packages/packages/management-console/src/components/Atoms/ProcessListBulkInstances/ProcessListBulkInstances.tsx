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
  componentOuiaProps,
  GraphQL
} from '@kogito-apps/common';
import { IOperation } from '../../Molecules/ProcessListToolbar/ProcessListToolbar';
import {
  getProcessInstanceDescription,
  getJobsDescription
} from '../../../utils/Utils';

interface IOwnProps {
  operationResult: IOperation;
  type: string;
}

const ProcessListBulkInstances: React.FC<IOwnProps & OUIAProps> = ({
  operationResult,
  type,
  ouiaId,
  ouiaSafe
}) => {
  const getItemDescription = item => {
    if (type === 'processList') {
      return getProcessInstanceDescription(item);
    } else if (type === 'job') {
      return getJobsDescription(item);
    }
  };

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
                (item: [string, GraphQL.ProcessInstance | GraphQL.Job]) => {
                  return (
                    <TextListItem key={item[0]}>
                      <strong>
                        <ItemDescriptor
                          itemDescription={getItemDescription(item[1])}
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
                (item: [string, GraphQL.ProcessInstance | GraphQL.Job]) => {
                  return (
                    <TextListItem key={item[0]}>
                      <strong>
                        <ItemDescriptor
                          itemDescription={getItemDescription(item[1])}
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
                (item: [string, GraphQL.ProcessInstance | GraphQL.Job]) => {
                  return (
                    <TextListItem key={item[0]}>
                      <strong>
                        <ItemDescriptor
                          itemDescription={getItemDescription(item[1])}
                        />
                      </strong>{' '}
                      -{item[1]['errorMessage']}
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
