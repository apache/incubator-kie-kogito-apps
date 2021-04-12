import React, { useReducer, useState } from 'react';
import {
  Button,
  Drawer,
  DrawerContent,
  DrawerContentBody,
  DrawerPanelContent,
  Flex,
  FlexItem,
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
import { ItemObject } from '../../../types';
import './Counterfactual.scss';
import CounterfactualOutcomesSelected from '../../Molecules/CounterfactualsOutcomesSelected/CounterfactualOutcomesSelected';

const Counterfactual = () => {
  const [state, dispatch] = useReducer(cfReducer, cfInitialState);
  const [isSidePanelExpanded, setIsSidePanelExpanded] = useState(false);
  const [inputDomain, setInputDomain] = useState<CFSearchInput>();

  const handleConstraintEdit = (input: CFSearchInput) => {
    setInputDomain(input);
    if (!isSidePanelExpanded) {
      setIsSidePanelExpanded(true);
    }
  };

  const panelContent = (
    <DrawerPanelContent widths={{ default: 'width_33' }}>
      <CounterfactualInputDomainEdit
        input={inputDomain}
        onClose={() => setIsSidePanelExpanded(false)}
      />
    </DrawerPanelContent>
  );

  return (
    <CFDispatch.Provider value={dispatch}>
      <Drawer
        isExpanded={isSidePanelExpanded}
        className="counterfactual__drawer"
      >
        <DrawerContent panelContent={panelContent}>
          <DrawerContentBody>
            <PageSection variant="default" isFilled={true}>
              <section className="counterfactual__section">
                <Stack hasGutter>
                  <StackItem>
                    <Flex spaceItems={{ default: 'spaceItemsXl' }}>
                      <FlexItem>
                        <Title headingLevel="h3" size="2xl">
                          Counterfactual Analysis
                        </Title>
                      </FlexItem>
                      <FlexItem>
                        <CounterfactualOutcomesSelected goals={state.goals} />
                      </FlexItem>
                    </Flex>
                  </StackItem>
                  <StackItem>
                    <Hint>
                      <HintTitle>Create a counterfactual</HintTitle>
                      <HintBody>
                        Select a desired counterfactual Outcome; one or more
                        Data Types, and modify the input constraints.
                      </HintBody>
                      <HintFooter>
                        <Button variant="link" isInline>
                          Select Outcome
                        </Button>
                      </HintFooter>
                    </Hint>
                  </StackItem>
                  <StackItem>
                    <CounterfactualToolbar goals={state.goals} />
                    <CounterfactualTable
                      onOpenConstraints={handleConstraintEdit}
                    />
                  </StackItem>
                </Stack>
              </section>
            </PageSection>
          </DrawerContentBody>
        </DrawerContent>
      </Drawer>
    </CFDispatch.Provider>
  );
};

export default Counterfactual;

const cfInitialState: CFState = {
  goals: [
    {
      id: '_12268B68-94A1-4960-B4C8-0B6071AFDE58',
      name: 'Mortgage Approval',
      typeRef: 'boolean',
      value: true,
      originalValue: true,
      isFixed: true
    },
    {
      id: '_9CFF8C35-4EB3-451E-874C-DB27A5A424C0',
      name: 'Risk Score',
      typeRef: 'number',
      value: 150,
      originalValue: 150,
      isFixed: true
    }
  ],
  searchDomains: []
};

type cfActions =
  | { type: 'selectOutcomes'; payload: CFGoal[] }
  | { type: 'decrement'; payload: string };

const cfReducer = (state: typeof cfInitialState, action: cfActions) => {
  switch (action.type) {
    case 'selectOutcomes':
      return { ...state, goals: action.payload };
    case 'decrement':
      // return { count: state.count - Number(action.payload) };
      return { goals: [], searchDomains: [] };
    default:
      throw new Error();
  }
};

export const CFDispatch = React.createContext(null);

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

export type CFGoal = Pick<ItemObject, 'name' | 'typeRef' | 'value'> & {
  isFixed: boolean;
  originalValue: ItemObject['value'];
  id: string;
};

export interface CFState {
  goals: CFGoal[];
  searchDomains: CFSearchDomain[];
}
