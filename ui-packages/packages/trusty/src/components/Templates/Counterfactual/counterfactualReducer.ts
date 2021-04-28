import {
  CFAnalysisResetType,
  CFGoal,
  CFResult,
  CFSearchInput,
  CFStatus
} from './Counterfactual';

export interface CFState {
  goals: CFGoal[];
  searchDomains: CFSearchInput[];
  status: CFStatus;
  results: CFResult[];
}

export const cfInitialState: CFState = {
  goals: [
    {
      id: '_12268B68-94A1-4960-B4C8-0B6071AFDE58',
      name: 'Mortgage Approval',
      typeRef: 'boolean',
      value: false,
      originalValue: false,
      isFixed: true
    },
    {
      id: '_9CFF8C35-4EB3-451E-874C-DB27A5A424C0',
      name: 'Risk Score',
      typeRef: 'number',
      value: 150,
      originalValue: 150,
      isFixed: true
    },
    {
      id: '_4CSS8C35-4EB3-451E-874C-DB27A5A346W1',
      name: 'Mortgage Category',
      typeRef: 'string',
      value: 'Product A',
      originalValue: 'Product A',
      isFixed: true
    }
  ],
  searchDomains: [
    {
      name: 'Credit Score',
      typeRef: 'number',
      value: 738,
      isFixed: true
    },
    {
      name: 'Down Payment',
      typeRef: 'number',
      value: 70000,
      isFixed: true
    },
    {
      name: 'Purchase Price',
      typeRef: 'number',
      value: 34000,
      isFixed: true
    },
    {
      name: 'Monthly Tax Payment',
      typeRef: 'number',
      value: 0.2,
      isFixed: true
    },
    {
      name: 'Loan Type',
      typeRef: 'string',
      value: 'ALFA',
      isFixed: true
    },
    {
      name: 'First Time Client',
      typeRef: 'boolean',
      value: true,
      isFixed: true
    },
    {
      name: 'Demo input with a long name',
      typeRef: 'number',
      value: 100,
      isFixed: true
    },
    {
      name: 'Demo input again',
      typeRef: 'number',
      value: 2,
      isFixed: true
    },
    {
      name: 'Demo input only',
      typeRef: 'number',
      value: 44,
      isFixed: true
    },
    {
      name: 'Demo input for fun',
      typeRef: 'number',
      value: 13,
      isFixed: true
    },
    {
      name: 'Demo input rate',
      typeRef: 'number',
      value: 90,
      isFixed: true
    },
    {
      name: 'Demo input w/ huge number',
      typeRef: 'number',
      value: 4110000,
      isFixed: true
    }
  ],
  status: {
    isDisabled: true,
    executionStatus: 'NOT_STARTED',
    lastExecutionTime: null
  },
  results: []
};

export type cfActions =
  | { type: 'setOutcomes'; payload: CFGoal[] }
  | {
      type: 'toggleInput';
      payload: {
        searchInputIndex: number;
      };
    }
  | {
      type: 'toggleAllInputs';
      payload: {
        selected: boolean;
      };
    }
  | {
      type: 'setInputDomain';
      payload: {
        inputIndex: number;
        domain: CFSearchInput['domain'];
      };
    }
  | {
      type: 'setStatus';
      payload: Partial<CFStatus>;
    }
  | {
      type: 'setResults';
      payload: {
        results: CFResult[];
      };
    }
  | {
      type: 'resetAnalysis';
      payload: {
        resetType: CFAnalysisResetType;
      };
    };

export const cfReducer = (state: typeof cfInitialState, action: cfActions) => {
  switch (action.type) {
    case 'setOutcomes':
      return { ...state, goals: action.payload };
    case 'toggleInput':
      return {
        ...state,
        searchDomains: state.searchDomains.map((input, index) =>
          index === action.payload.searchInputIndex
            ? { ...input, isFixed: !input.isFixed }
            : input
        )
      };
    case 'toggleAllInputs':
      return {
        ...state,
        searchDomains: state.searchDomains.map(input => ({
          ...input,
          isFixed: !action.payload.selected
        }))
      };
    case 'setInputDomain':
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
    case 'setResults':
      return {
        ...state,
        results: action.payload.results
      };
    case 'setStatus':
      return {
        ...state,
        status: { ...state.status, ...action.payload }
      };
    case 'resetAnalysis':
      switch (action.payload.resetType) {
        case 'NEW':
          return cfInitialState;
        case 'EDIT':
          return { ...state, status: cfInitialState.status, results: [] };
      }
      break;
    default:
      throw new Error();
  }
};
