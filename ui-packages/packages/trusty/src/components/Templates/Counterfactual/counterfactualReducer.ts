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
    case 'CF_SET_OUTCOMES': {
      const newState = { ...state, goals: action.payload };
      return updateCFStatus(newState);
    }

    case 'CF_TOGGLE_INPUT': {
      const newState = {
        ...state,
        searchDomains: state.searchDomains.map((input, index) =>
          index === action.payload.searchInputIndex
            ? { ...input, isFixed: !input.isFixed }
            : input
        )
      };
      return updateCFStatus(newState);
    }

    case 'CF_TOGGLE_ALL_INPUTS': {
      const newState = {
        ...state,
        searchDomains: state.searchDomains.map(input => ({
          ...input,
          isFixed: !action.payload.selected
        }))
      };
      return updateCFStatus(newState);
    }

    case 'CF_SET_INPUT_DOMAIN': {
      const newState = {
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
      return updateCFStatus(newState);
    }

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
              isDisabled: false,
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
      isFixed: true,
      kind: outcome.outcomeResult.kind
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

const updateCFStatus = (state: CFState): CFState => {
  if (areRequiredParametersSet(state)) {
    if (state.status.isDisabled) {
      return { ...state, status: { ...state.status, isDisabled: false } };
    }
  } else {
    if (!state.status.isDisabled) {
      return { ...state, status: { ...state.status, isDisabled: true } };
    }
  }
  return state;
};

const areRequiredParametersSet = (state: CFState): boolean => {
  return (
    areInputsSelected(state.searchDomains) &&
    state.goals.filter(goal => !goal.isFixed).length > 0
  );
};

const areInputsSelected = (inputs: CFSearchInput[]) => {
  // filtering all non fixed inputs
  const selectedInputs = inputs.filter(domain => domain.isFixed === false);
  // checking if all inputs have a domain specified, with the exception of
  // boolean (do not require one) and structured inputs (not yet supported)
  return (
    selectedInputs.length > 0 &&
    inputs.every(
      input =>
        input.domain ||
        input.components !== null ||
        typeof input.value === 'boolean'
    )
  );
};
