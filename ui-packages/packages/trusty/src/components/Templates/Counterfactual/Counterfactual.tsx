import React, { useEffect, useReducer, useState } from 'react';
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
import CounterfactualOutcomesSelected from '../../Molecules/CounterfactualsOutcomesSelected/CounterfactualOutcomesSelected';
import { cfActions, cfInitialState, cfReducer } from './counterfactualReducer';
import { ItemObject } from '../../../types';
import './Counterfactual.scss';

const Counterfactual = () => {
  const [state, dispatch] = useReducer(cfReducer, cfInitialState);
  const [isSidePanelExpanded, setIsSidePanelExpanded] = useState(false);
  const [inputDomainEdit, setInputDomainEdit] = useState<{
    input: CFSearchInput;
    inputIndex: number;
  }>();

  const handleInputDomainEdit = (input: CFSearchInput, inputIndex: number) => {
    setInputDomainEdit({ input, inputIndex });
    if (!isSidePanelExpanded) {
      setIsSidePanelExpanded(true);
    }
  };

  useEffect(() => {
    if (
      state.searchDomains.filter(domain => !domain.isFixed).length > 0 &&
      state.goals.filter(goal => !goal.isFixed).length > 0
    ) {
      if (state.status.isDisabled) {
        dispatch({ type: 'setStatus', payload: { isDisabled: false } });
      }
    } else {
      if (!state.status.isDisabled) {
        dispatch({ type: 'setStatus', payload: { isDisabled: true } });
      }
    }
  }, [state.searchDomains, state.goals, state.status.isDisabled]);

  const panelContent = (
    <DrawerPanelContent widths={{ default: 'width_33' }}>
      {inputDomainEdit && (
        <CounterfactualInputDomainEdit
          input={inputDomainEdit.input}
          inputIndex={inputDomainEdit.inputIndex}
          onClose={() => setIsSidePanelExpanded(false)}
        />
      )}
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
                    <CounterfactualToolbar
                      status={state.status}
                      goals={state.goals}
                    />
                    <CounterfactualTable
                      inputs={state.searchDomains}
                      onOpenInputDomainEdit={handleInputDomainEdit}
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

export const CFDispatch = React.createContext<React.Dispatch<cfActions>>(null);

export interface CFSearchDomain {
  isFixed: boolean;
  name: string;
  typeRef: 'number' | 'string' | 'boolean';
  domain?:
    | {
        type: 'numerical';
        lowerBound?: number;
        upperBound?: number;
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

export interface CFStatus {
  isDisabled: boolean;
  isRunning: boolean;
}
