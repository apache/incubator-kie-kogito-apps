import React, { useState } from 'react';
import {
  Button,
  Drawer,
  DrawerContent,
  DrawerContentBody,
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
import CounterfactualInputDomainEdit from '../../Organisms/CounterfactualInputDomainEdit/CounterfactualInputDomainEdit';
import './Counterfactual.scss';

const Counterfactual = () => {
  const [isSidePanelExpanded, setIsSidePanelExpanded] = useState(false);
  const [inputDomain, setInputDomain] = useState<CFSearchInput>();

  const panelContent = (
    <DrawerPanelContent widths={{ default: 'width_33' }}>
      <CounterfactualInputDomainEdit
        input={inputDomain}
        onClose={() => setIsSidePanelExpanded(false)}
      />
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
                    onOpenConstraints={input => {
                      setInputDomain(input);
                      setIsSidePanelExpanded(!isSidePanelExpanded);
                    }}
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

export interface CFSearchDomain {
  isFixed: boolean;
  name: string;
  typeRef: 'number' | 'string' | 'boolean';
  domain:
    | {
        type: 'numerical';
        lowerBound: 0;
        upperBound: 1000;
      }
    | {
        type: 'categorical';
        categories: string[];
      };
}

export interface CFSearchInput extends CFSearchDomain {
  value: number | string | boolean;
}
