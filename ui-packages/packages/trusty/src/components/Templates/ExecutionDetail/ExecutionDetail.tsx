import React, { useCallback } from 'react';
import { useHistory } from 'react-router-dom';
import { PageSection, Stack, StackItem, Title } from '@patternfly/react-core';
import Outcomes from '../../Organisms/Outcomes/Outcomes';
import SkeletonCards from '../../Molecules/SkeletonCards/SkeletonCards';
import { RemoteData, IOutcome } from '../../../types';
import './ExecutionDetail.scss';

type ExecutionDetailProps = {
  outcome: RemoteData<Error, IOutcome[]>;
};

const ExecutionDetail = (props: ExecutionDetailProps) => {
  const { outcome } = props;
  const history = useHistory();
  const goToExplanation = useCallback(
    (outcomeId: string) => {
      history.push({
        pathname: 'outcomes-details',
        search: `?outcomeId=${outcomeId}`
      });
    },
    [history]
  );

  return (
    <section className="execution-detail">
      <PageSection variant="default">
        <Stack hasGutter>
          <StackItem>
            <Title headingLevel="h3" size="2xl">
              Outcomes
            </Title>
          </StackItem>
          <StackItem>
            {outcome.status === 'LOADING' && <SkeletonCards quantity={2} />}
            {outcome.status === 'SUCCESS' && (
              <Outcomes
                outcomes={outcome.data}
                onExplanationClick={goToExplanation}
                listView
              />
            )}
          </StackItem>
        </Stack>
      </PageSection>
    </section>
  );
};

export default ExecutionDetail;
