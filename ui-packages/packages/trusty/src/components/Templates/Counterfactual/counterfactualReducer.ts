import {
  CFAnalysisResetType,
  CFAnalysisResult,
  CFExecutionStatus,
  CFGoal,
  CFSearchInput,
  CFStatus,
  isItemObjectArray,
  isItemObjectMultiArray,
  ItemObject,
  Outcome
} from '../../../types';

export interface CFState {
  goals: CFGoal[];
  searchDomains: CFSearchInput[];
  status: CFStatus;
  results: CFAnalysisResult[];
}

export type cfActions =
  | { type: 'CF_SET_OUTCOMES'; payload: CFGoal[] }
  | {
      type: 'CF_TOGGLE_INPUT';
      payload: {
        searchInputIndex: number;
      };
    }
  | {
      type: 'CF_TOGGLE_ALL_INPUTS';
      payload: {
        selected: boolean;
      };
    }
  | {
      type: 'CF_SET_INPUT_DOMAIN';
      payload: {
        inputIndex: number;
        domain: CFSearchInput['domain'];
      };
    }
  | {
      type: 'CF_SET_STATUS';
      payload: Partial<CFStatus>;
    }
  | {
      type: 'CF_SET_RESULTS';
      payload: {
        results: CFAnalysisResult[];
      };
    }
  | {
      type: 'CF_RESET_ANALYSIS';
      payload: {
        resetType: CFAnalysisResetType;
        inputs: ItemObject[];
        outcomes: Outcome[];
      };
    };

export const cfReducer = (state: CFState, action: cfActions) => {
  switch (action.type) {
    case 'CF_SET_OUTCOMES':
      return { ...state, goals: action.payload };
    case 'CF_TOGGLE_INPUT':
      return {
        ...state,
        searchDomains: state.searchDomains.map((input, index) =>
          index === action.payload.searchInputIndex
            ? { ...input, isFixed: !input.isFixed }
            : input
        )
      };
    case 'CF_TOGGLE_ALL_INPUTS':
      return {
        ...state,
        searchDomains: state.searchDomains.map(input => ({
          ...input,
          isFixed: !action.payload.selected
        }))
      };
    case 'CF_SET_INPUT_DOMAIN':
      return {
        ...state,
        searchDomains: state.searchDomains.map((input, index) =>
          index === action.payload.inputIndex
            ? {
                ...input,
                domain: action.payload.domain
              }
            : input
        )
      };
    case 'CF_SET_RESULTS':
      return {
        ...state,
        results: action.payload.results
      };
    case 'CF_SET_STATUS':
      return {
        ...state,
        status: { ...state.status, ...action.payload }
      };
    case 'CF_RESET_ANALYSIS':
      switch (action.payload.resetType) {
        case 'NEW':
          return cfInitState({
            inputs: action.payload.inputs,
            outcomes: action.payload.outcomes
          });
        case 'EDIT':
          return {
            ...state,
            status: {
              isDisabled: true,
              executionStatus: CFExecutionStatus.NOT_STARTED,
              lastExecutionTime: null
            },
            results: []
          };
      }
      break;
    default:
      throw new Error();
  }
};

export const cfInitState = (parameters): CFState => {
  const { inputs, outcomes } = parameters;
  const initialState: CFState = {
    goals: [],
    searchDomains: [],
    status: {
      isDisabled: true,
      executionStatus: CFExecutionStatus.NOT_STARTED,
      lastExecutionTime: null
    },
    results: []
  };
  initialState.goals = outcomes.map(outcome => {
    return {
      id: outcome.outcomeId,
      name: outcome.outcomeName,
      typeRef: outcome.outcomeResult.typeRef,
      value: outcome.outcomeResult.value,
      originalValue: outcome.outcomeResult.value,
      isFixed: true
    };
  });

  initialState.searchDomains = convertInputToSearchDomain(inputs);

  return initialState;
};

const convertInputToSearchDomain = (inputs: ItemObject[]) => {
  const addIsFixed = (input: ItemObject): CFSearchInput => {
    if (input.components === null) {
      return { ...input, isFixed: true };
    } else {
      if (isItemObjectArray(input.components)) {
        const searchInput = input.components.map(item => addIsFixed(item));
        return { ...input, components: searchInput };
      } else if (isItemObjectMultiArray(input.components)) {
        const searchInput = input.components.map(list => {
          return list.map(item => addIsFixed(item));
        });
        return { ...input, components: searchInput };
      }
    }
  };
  return inputs.map(input => {
    return addIsFixed(input);
  });
};
