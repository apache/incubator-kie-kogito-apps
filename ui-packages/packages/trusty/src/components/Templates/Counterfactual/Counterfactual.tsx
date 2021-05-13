import React from 'react';
import {
  Divider,
  PageSection,
  Stack,
  StackItem,
  Title
} from '@patternfly/react-core';
import { useParams } from 'react-router-dom';

import { ExecutionRouteParams, RemoteDataStatus } from '../../../types';
import CounterfactualAnalysis from '../../Organisms/CounterfactualAnalysis/CounterfactualAnalysis';
import useInputData from '../InputData/useInputData';
import useDecisionOutcomes from '../AuditDetail/useDecisionOutcomes';
import SkeletonDataList from '../../Molecules/SkeletonDataList/SkeletonDataList';
import SkeletonFlexStripes from '../../Molecules/SkeletonFlexStripes/SkeletonFlexStripes';
import './Counterfactual.scss';

const Counterfactual = () => {
  const { executionId } = useParams<ExecutionRouteParams>();
  const inputData = useInputData(executionId);
  const outcomesData = useDecisionOutcomes(executionId);

  return (
    <>
      <Divider className="counterfactual__divider" />
      {inputData.status === RemoteDataStatus.SUCCESS &&
      outcomesData.status === RemoteDataStatus.SUCCESS ? (
        <CounterfactualAnalysis
          inputs={inputData.data}
          outcomes={outcomesData.data}
          executionId={executionId}
        />
      ) : (
        <PageSection variant={'light'} isFilled={true}>
          <Stack hasGutter={true}>
            <StackItem>
              <Title headingLevel="h3" size="2xl">
                Counterfactual Analysis
              </Title>
            </StackItem>
            <StackItem>
              <SkeletonFlexStripes
                stripesNumber={3}
                stripesWidth={'100px'}
                stripesHeight={'1.5em'}
              />
            </StackItem>
            <StackItem>
              <SkeletonDataList rowsCount={5} colsCount={5} />
            </StackItem>
          </Stack>
        </PageSection>
      )}
    </>
  );
};

export default Counterfactual;
