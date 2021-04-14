import {
  CFGoal,
  CFSearchDomain,
  CFSearchInput,
  CFStatus
} from './Counterfactual';

export interface CFState {
  goals: CFGoal[];
  searchDomains: CFSearchInput[];
  status: CFStatus;
  results: number[][];
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
      name: 'Monthly Insurance Payment',
      typeRef: 'number',
      value: 0.15,
      isFixed: true
    }
  ],
  status: {
    isDisabled: false,
    isRunning: false
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
      type: 'setInputNumericDomain';
      payload: {
        inputIndex: number;
        range: {
          min?: number;
          max?: number;
        };
      };
    }
  | {
      type: 'setStatus';
      payload: {
        isDisabled?: boolean;
        isRunning?: boolean;
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
    case 'setInputNumericDomain':
      return {
        ...state,
        searchDomains: state.searchDomains.map((input, index) =>
          index === action.payload.inputIndex
            ? {
                ...input,
                domain: {
                  type: 'numerical',
                  lowerBound: action.payload.range.min,
                  upperBound: action.payload.range.max
                } as CFSearchDomain['domain']
              }
            : input
        )
      };
    case 'setStatus':
      return {
        ...state,
        status: { ...state.status, ...action.payload }
      };
    default:
      throw new Error();
  }
};
