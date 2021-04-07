import React from 'react';
import {
  Button,
  Hint,
  HintBody,
  HintFooter,
  HintTitle,
  PageSection,
  Stack,
  StackItem,
  Title
} from '@patternfly/react-core';
import CounterfactualTable from '../../Organisms/CounterfactualTable/CounterfactualTable';
import CounterfactualToolbar from '../../Organisms/CounterfactualToolbar/CounterfactualToolbar';

const Counterfactual = () => {
  return (
    <section className="execution-detail">
      <PageSection variant="default">
        <Stack hasGutter>
          <StackItem>
            <Title headingLevel="h3" size="2xl">
              Counterfactual Analysis
            </Title>
          </StackItem>
          <StackItem>
            <Hint>
              <HintTitle>Create a counterfactual</HintTitle>
              <HintBody>
                Select a desired counterfactual Outcome; one or more Data Types,
                and modify the input constraints.
              </HintBody>
              <HintFooter>
                <Button variant="link" isInline>
                  Select Outcome
                </Button>
              </HintFooter>
            </Hint>
          </StackItem>
          <StackItem>
            <CounterfactualToolbar />
            <CounterfactualTable />
          </StackItem>
        </Stack>
      </PageSection>
    </section>
  );
};

export default Counterfactual;
