import React from 'react';
import {
  TextContent,
  TextVariants,
  Text,
  Divider,
  TextList,
  TextListItem
} from '@patternfly/react-core';
import { ProcessDescriptor } from '@kogito-apps/common';

interface IOwnProps {
  requiredInstances: any;
  ignoredInstances: any;
  isSingleAbort: any;
  checkedArray: any;
  titleString: string;
}
const ProcessListBulkInstances: React.FC<IOwnProps> = ({
  requiredInstances,
  ignoredInstances,
  isSingleAbort,
  checkedArray,
  titleString
}) => {
  const processType = (title: string) => {
    /* istanbul ignore else */
    if (title === 'Skip operation') {
      return 'skipped';
    } else if (title === 'Retry operation') {
      return 'retried';
    } else if (title === 'Abort operation') {
      return 'aborted';
    }
  };

  return (
    <>
      {Object.keys(requiredInstances).length !== 0 &&
        Object.keys(ignoredInstances).length !== 0 &&
        !isSingleAbort && (
          <>
            <TextContent>
              <Text component={TextVariants.h2}>
                {` The following processes were ${processType(titleString)}:`}
              </Text>

              <TextList>
                {Object.entries(requiredInstances).map((process: any) => {
                  return (
                    <TextListItem key={process[0]}>
                      <ProcessDescriptor processInstanceData={process[1]} />
                    </TextListItem>
                  );
                })}
              </TextList>
            </TextContent>
            {!checkedArray.includes('ABORTED') &&
              requiredInstances !== undefined &&
              titleString === 'Abort operation' &&
              Object.keys(requiredInstances).length !== 0 && (
                <TextContent className="pf-u-mt-sm">
                  <Text>
                    Note: The process status has been updated. The list may
                    appear inconsistent until you refresh any applied filters.
                  </Text>
                </TextContent>
              )}
            <Divider component="div" className="pf-u-my-xl" />
            <TextContent>
              {titleString === 'Skip operation' ||
              titleString === 'Retry operation' ? (
                <Text component={TextVariants.h2}>
                  The following processes were ignored because they were not in
                  error state:
                </Text>
              ) : (
                <Text component={TextVariants.h2}>
                  The following processes were ignored because they were either
                  completed or aborted:
                </Text>
              )}
              <TextList>
                {Object.entries(ignoredInstances).map((process: any) => {
                  return (
                    <TextListItem key={process[0]}>
                      <ProcessDescriptor processInstanceData={process[1]} />
                    </TextListItem>
                  );
                })}
              </TextList>
            </TextContent>
          </>
        )}
      {Object.keys(requiredInstances).length === 0 &&
        Object.keys(ignoredInstances).length !== 0 &&
        !isSingleAbort && (
          <>
            <TextContent>
              <Text component={TextVariants.h2}>
                {' '}
                {`No processes were ${processType(titleString)}`}
              </Text>
            </TextContent>
            <Divider component="div" className="pf-u-my-xl" />
            <TextContent>
              {titleString === 'Skip operation' ||
              titleString === 'Retry operation' ? (
                <Text component={TextVariants.h2}>
                  The following processes were ignored because they were not in
                  error state:
                </Text>
              ) : (
                <Text component={TextVariants.h2}>
                  The following processes were ignored because they were either
                  completed or aborted:
                </Text>
              )}
              <TextList>
                {Object.entries(ignoredInstances).map((process: any) => {
                  return (
                    <TextListItem key={process[0]}>
                      <ProcessDescriptor processInstanceData={process[1]} />
                    </TextListItem>
                  );
                })}
              </TextList>
            </TextContent>
          </>
        )}
      {Object.keys(requiredInstances).length !== 0 &&
        Object.keys(ignoredInstances).length === 0 &&
        !isSingleAbort && (
          <>
            <TextContent>
              <Text component={TextVariants.h2}>
                {`The following processes were ${processType(titleString)}:`}
              </Text>
              <TextList>
                {Object.entries(requiredInstances).map((process: any) => {
                  return (
                    <TextListItem key={process[0]}>
                      <ProcessDescriptor processInstanceData={process[1]} />
                    </TextListItem>
                  );
                })}
              </TextList>
            </TextContent>
            {!checkedArray.includes('ABORTED') &&
              requiredInstances !== undefined &&
              titleString === 'Abort operation' &&
              Object.keys(requiredInstances).length !== 0 && (
                <TextContent className="pf-u-mt-sm">
                  <Text>
                    Note: The process status has been updated. The list may
                    appear inconsistent until you refresh any applied filters.
                  </Text>
                </TextContent>
              )}
          </>
        )}
      {Object.keys(requiredInstances).length !== 0 &&
        Object.keys(ignoredInstances).length === 0 &&
        isSingleAbort && (
          <>
            <TextContent>
              <Text component={TextVariants.h2}>
                {`The following process was ${processType(titleString)}:`}
              </Text>
              <TextList>
                {Object.entries(requiredInstances).map((process: any) => {
                  return (
                    <TextListItem key={process[0]}>
                      <ProcessDescriptor processInstanceData={process[1]} />
                    </TextListItem>
                  );
                })}
              </TextList>
            </TextContent>
            {!checkedArray.includes('ABORTED') &&
              requiredInstances !== undefined &&
              Object.keys(requiredInstances).length !== 0 && (
                <TextContent className="pf-u-mt-sm">
                  <Text>
                    Note: The process status has been updated. The list may
                    appear inconsistent until you refresh any applied filters.
                  </Text>
                </TextContent>
              )}
          </>
        )}
    </>
  );
};

export default ProcessListBulkInstances;
