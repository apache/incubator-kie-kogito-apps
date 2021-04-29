import React, { useEffect, useReducer, useState } from 'react';
import {
  Divider,
  Drawer,
  DrawerContent,
  DrawerContentBody,
  DrawerPanelContent,
  Flex,
  FlexItem,
  PageSection,
  Stack,
  StackItem,
  Title
} from '@patternfly/react-core';
import { debounce } from 'lodash';
import CounterfactualTable from '../../Organisms/CounterfactualTable/CounterfactualTable';
import CounterfactualToolbar from '../../Organisms/CounterfactualToolbar/CounterfactualToolbar';
import CounterfactualInputDomainEdit from '../../Organisms/CounterfactualInputDomainEdit/CounterfactualInputDomainEdit';
import CounterfactualOutcomesSelected from '../../Molecules/CounterfactualsOutcomesSelected/CounterfactualOutcomesSelected';
import { cfActions, cfInitialState, cfReducer } from './counterfactualReducer';
import { ItemObject } from '../../../types';
import CounterfactualHint from '../../Molecules/CounterfactualHint/CounterfactualHint';
import CounterfactualExecutionInfo from '../../Molecules/CounterfactualExecutionInfo/CounterfactualExecutionInfo';
import CounterfactualSuccessMessage from '../../Molecules/CounterfactualSuccessMessage/CounterfactualSuccessMessage';
import './Counterfactual.scss';

const Counterfactual = () => {
  const [state, dispatch] = useReducer(cfReducer, cfInitialState);
  const [isSidePanelExpanded, setIsSidePanelExpanded] = useState(false);
  const [inputDomainEdit, setInputDomainEdit] = useState<{
    input: CFSearchInput;
    inputIndex: number;
  }>();
  const [containerHeight, setContainerHeight] = useState(0);

  useEffect(() => {
    const getHeight = debounce(() => {
      const size =
        window.innerHeight -
        document.querySelector('.pf-c-page__main-breadcrumb').clientHeight -
        document.querySelector('.pf-c-page__main-section').clientHeight -
        document.querySelector('.pf-c-page__header').clientHeight -
        25; // mind the extra space for native scrollbar size
      setContainerHeight(size);
    }, 150);
    getHeight();
    window.addEventListener('resize', getHeight);
    return () => window.removeEventListener('resize', getHeight);
  }, [containerHeight]);

  const handleInputDomainEdit = (input: CFSearchInput, inputIndex: number) => {
    setInputDomainEdit({ input, inputIndex });
    if (!isSidePanelExpanded) {
      setIsSidePanelExpanded(true);
    }
  };

  const areInputsSelected = (inputs: CFSearchInput[]) => {
    // filtering all non fixed inputs
    const selectedInputs = inputs.filter(domain => !domain.isFixed);
    // checking if all inputs have a domain specified (with the exception of
    // boolean, which do not require one)
    return (
      selectedInputs.length > 0 &&
      selectedInputs.every(input => input.domain || input.typeRef === 'boolean')
    );
  };

  useEffect(() => {
    if (
      areInputsSelected(state.searchDomains) &&
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

  useEffect(() => {
    if (state.status.executionStatus === 'RUNNING') {
      setTimeout(() => {
        dispatch({
          type: 'setResults',
          payload: {
            results: getCFResultsDemo(state.searchDomains, 15)
          }
        });
      }, 4000);
      setTimeout(() => {
        dispatch({
          type: 'setStatus',
          payload: {
            executionStatus: 'RUN'
          }
        });
      }, 10000);
    }
  }, [state.status.executionStatus, state.searchDomains]);

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
      <Divider className="counterfactual__divider" />
      {containerHeight > 0 && (
        <Drawer
          isExpanded={isSidePanelExpanded}
          className="counterfactual__drawer"
        >
          <DrawerContent
            panelContent={panelContent}
            style={{ height: containerHeight }}
          >
            <DrawerContentBody
              style={{
                display: 'flex',
                flexDirection: 'column',
                height: '100%'
              }}
            >
              <PageSection variant="light" isFilled={true}>
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
                        {state.status.executionStatus === 'RUN' && (
                          <CounterfactualExecutionInfo
                            results={state.results}
                          />
                        )}
                      </Flex>
                    </StackItem>
                    <CounterfactualHint
                      isVisible={state.status.executionStatus === 'NOT_STARTED'}
                    />
                    <CounterfactualSuccessMessage status={state.status} />
                    <StackItem isFilled={true} style={{ overflow: 'hidden' }}>
                      <CounterfactualToolbar
                        status={state.status}
                        goals={state.goals}
                      />
                      <CounterfactualTable
                        inputs={state.searchDomains}
                        results={state.results}
                        status={state.status}
                        onOpenInputDomainEdit={handleInputDomainEdit}
                      />
                    </StackItem>
                  </Stack>
                </section>
              </PageSection>
            </DrawerContentBody>
          </DrawerContent>
        </Drawer>
      )}
    </CFDispatch.Provider>
  );
};

export default Counterfactual;

export const CFDispatch = React.createContext<React.Dispatch<cfActions>>(null);

export interface CFSearchDomain {
  isFixed: boolean;
  name: string;
  typeRef: 'number' | 'string' | 'boolean';
  domain?: CFNumericalDomain | CFCategoricalDomain;
}

export interface CFNumericalDomain {
  type: 'numerical';
  lowerBound?: number;
  upperBound?: number;
}

export interface CFCategoricalDomain {
  type: 'categorical';
  categories: string[];
}

export interface CFSearchInput extends CFSearchDomain {
  value: number | string | boolean;
}

export type CFGoal = Pick<ItemObject, 'name' | 'typeRef' | 'value'> & {
  isFixed: boolean;
  originalValue: ItemObject['value'];
  id: string;
};

export type CFResult = Array<unknown>;

export interface CFStatus {
  isDisabled: boolean;
  executionStatus: 'RUN' | 'RUNNING' | 'NOT_STARTED';
  lastExecutionTime: null | string;
}

export type CFAnalysisResetType = 'NEW' | 'EDIT';

const getCFResultsDemo = (inputs: CFSearchInput[], count: number) => {
  const results = [];
  inputs.map(input => {
    const row = new Array(count > 0 ? count : 1);
    for (let i = 0; i < row.length; i++) {
      row[i] = input.isFixed ? input.value : generateCFValue(input);
    }
    results.push(row);
  });
  return results;
};

const generateCFValue = (input: CFSearchInput) => {
  switch (input.typeRef) {
    case 'boolean':
      return !input.value;
    case 'number':
      return (
        Math.floor(
          Math.random() *
            (Math.ceil((input.domain as CFNumericalDomain).upperBound) -
              Math.ceil((input.domain as CFNumericalDomain).lowerBound))
        ) + Math.ceil((input.domain as CFNumericalDomain).lowerBound)
      );
    case 'string':
      return (input.domain as CFCategoricalDomain).categories[
        Math.floor(
          Math.random() *
            (input.domain as CFCategoricalDomain).categories.length
        )
      ];
  }
};
