import React from 'react';
import { PageSection } from '@patternfly/react-core';
import useModelData from './useModelData';
import { useParams } from 'react-router-dom';
import { ExecutionRouteParams } from '../../../types';

const ModelLookup = () => {
  const { executionId } = useParams<ExecutionRouteParams>();
  const modelData = useModelData(executionId);
  return (
    <PageSection>
      {modelData.status === 'SUCCESS' && <span>model diagram here</span>}
    </PageSection>
  );
};

export default ModelLookup;
