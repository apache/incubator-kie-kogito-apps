import React, { useState } from 'react';
import {
  Button,
  Drawer,
  DrawerActions,
  DrawerCloseButton,
  DrawerContent,
  DrawerContentBody,
  DrawerHead,
  DrawerPanelBody,
  DrawerPanelContent,
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
import './Counterfactual.scss';

const Counterfactual = () => {
  const [isSidePanelExpanded, setIsSidePanelExpanded] = useState(false);

  const panelContent = (
    <DrawerPanelContent widths={{ default: 'width_33' }}>
      <DrawerHead>
        <Title headingLevel="h4" size="xl">
          Constraint
        </Title>
        <DrawerActions>
          <DrawerCloseButton onClick={() => setIsSidePanelExpanded(false)} />
        </DrawerActions>
      </DrawerHead>
      <DrawerPanelBody>
        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Blanditiis,
        deleniti sint? Eius nobis quibusdam voluptates. Ad alias blanditiis
        consequatur dolore facilis ipsa iste itaque mollitia, necessitatibus
        omnis pariatur quo veritatis. Lorem ipsum dolor sit amet, consectetur
        adipisicing elit. A consectetur cumque deleniti dicta dolor, incidunt
        necessitatibus neque nostrum nulla omnis, quaerat quos temporibus
        voluptatem? Dolorum eum labore maiores quo! Dicta.
      </DrawerPanelBody>
    </DrawerPanelContent>
  );
  return (
    <Drawer isExpanded={isSidePanelExpanded} className="counterfactual__drawer">
      <DrawerContent panelContent={panelContent}>
        <DrawerContentBody>
          <PageSection variant="default" isFilled={true}>
            <section className="counterfactual__section">
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
                      Select a desired counterfactual Outcome; one or more Data
                      Types, and modify the input constraints.
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
                  <CounterfactualTable
                    onOpenConstraints={() =>
                      setIsSidePanelExpanded(!isSidePanelExpanded)
                    }
                  />
                </StackItem>
              </Stack>
            </section>
          </PageSection>
        </DrawerContentBody>
      </DrawerContent>
    </Drawer>
  );
};

export default Counterfactual;
