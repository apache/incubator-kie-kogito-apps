import React from 'react';
// import {getWrapperAsync} from '../../../../utils/OuiaUtils';
import DomainExplorerFilterOptions from '../DomainExplorerFilterOptions';
import reactApollo from 'react-apollo';
import { GraphQL } from '../../../../graphql/types';
import useGetInputFieldsFromQueryQuery = GraphQL.useGetInputFieldsFromQueryQuery;
import useGetInputFieldsFromTypeQuery = GraphQL.useGetInputFieldsFromTypeQuery;
import { mount } from 'enzyme';
// import { MockedProvider } from '@apollo/react-testing';

jest.mock('apollo-client');
jest.mock('gql-query-builder');
jest.mock('react-apollo', () => {
  const ApolloClient = { query: jest.fn() };
  return { useApolloClient: jest.fn(() => ApolloClient) };
});

jest.mock('../../../../graphql/types');
// tslint:disable: no-string-literal
// tslint:disable: no-unexpected-multiline
describe('Domain explorer filter options component tests', () => {
  afterEach(() => {
    jest.clearAllMocks();
  });
  let client;
  let useApolloClient;
  let useEffect;

  const mockUseEffect = () => {
    useEffect.mockImplementationOnce(f => f());
  };

  const mockuseApolloClient = () => {
    // tslint:disable-next-line: react-hooks-nesting
    client = useApolloClient();
  };

  beforeEach(() => {
    useApolloClient = jest.spyOn(reactApollo, 'useApolloClient');
    mockuseApolloClient();
    useEffect = jest.spyOn(React, 'useEffect');
    mockUseEffect();
    mockUseEffect();
  });
  it('Snapshot test with default props', async () => {
    const props = {
      currentDomain: 'Travels',
      getQuery: {
        loading: false,
        data: {
          __type: {
            name: 'Query',
            fields: [
              {
                name: 'Travels',
                args: [
                  {
                    name: 'where',
                    type: { kind: 'INPUT_OBJECT', name: 'TravelsArgument' }
                  },
                  {
                    name: 'orderBy',
                    type: { kind: 'INPUT_OBJECT', name: 'TravelsOrderBy' }
                  },
                  {
                    name: 'pagination',
                    type: { kind: 'INPUT_OBJECT', name: 'Pagination' }
                  }
                ],
                type: {
                  ofType: { name: 'Travels' }
                }
              }
            ]
          }
        }
      },
      parameters: [
        {
          metadata: {
            processInstances: [
              'id',
              'processName',
              'state',
              'start',
              'lastUpdate',
              'businessKey'
            ]
          }
        }
      ],
      setColumnFilters: jest.fn(),
      setTableLoading: jest.fn(),
      setDisplayTable: jest.fn(),
      setDisplayEmptyState: jest.fn(),
      queryAttributes: {},
      setQueryAttributes: jest.fn(),
      enableFilter: false,
      setEnableFilter: jest.fn(),
      setError: jest.fn(),
      getQueryTypes: {
        loading: false,
        data: {
          __schema: {
            queryType: [
              {
                name: 'AddressArgument',
                kind: 'INPUT_OBJECT',
                inputFields: [
                  {
                    name: 'city',
                    type: { name: 'StringArgument', kind: 'INPUT_OBJECT' }
                  },
                  {
                    name: 'country',
                    type: { name: 'StringArgument', kind: 'INPUT_OBJECT' }
                  },
                  {
                    name: 'street',
                    type: { name: 'StringArgument', kind: 'INPUT_OBJECT' }
                  },
                  {
                    name: 'zipCode',
                    type: { name: 'StringArgument', kind: 'INPUT_OBJECT' }
                  }
                ]
              },
              {
                name: 'IdArgument',
                kind: 'INPUT_OBJECT',
                inputFields: [
                  { name: 'id', type: { name: null, kind: 'LIST' } },
                  { name: 'equal', type: { name: 'String', kind: 'SCALAR' } },
                  { name: 'isNull', type: { name: 'String', kind: 'SCALAR' } }
                ]
              }
            ]
          }
        }
      },
      filterChips: [],
      setFilterChips: jest.fn(),
      runFilter: false,
      setRunFilter: jest.fn(),
      finalFilters: {},
      setFinalFilters: jest.fn()
    };
    // @ts-ignore
    useGetInputFieldsFromTypeQuery.mockReturnValue({
      loading: false,
      data: {
        __type: {
          name: 'IdArgument',
          inputFields: [
            { name: 'in', type: { name: null, kind: 'LIST' } },
            { name: 'equal', type: { name: 'String', kind: 'SCALAR' } },
            { name: 'isNull', type: { name: 'String', kind: 'SCALAR' } }
          ],
          kind: 'INPUT_OBJECT'
        }
      }
    });
    // @ts-ignore
    useGetInputFieldsFromQueryQuery.mockReturnValue({
      loading: false,
      data: {
        __type: {
          name: 'TravelsArgument',
          inputFields: [
            {
              name: 'and',
              type: {
                name: null,
                kind: 'LIST',
                inputFields: null
              }
            },
            {
              name: 'or',
              type: {
                name: null,
                kind: 'LIST',
                inputFields: null
              }
            },
            {
              name: 'flight',
              type: {
                name: 'FlightArgument',
                kind: 'INPUT_OBJECT',
                inputFields: [
                  {
                    name: 'arrival',
                    type: {
                      name: 'StringArgument'
                    }
                  },
                  {
                    name: 'departure',
                    type: {
                      name: 'StringArgument'
                    }
                  },
                  {
                    name: 'flightNumber',
                    type: {
                      name: 'StringArgument'
                    }
                  },
                  {
                    name: 'gate',
                    type: {
                      name: 'StringArgument'
                    }
                  },
                  {
                    name: 'seat',
                    type: {
                      name: 'StringArgument'
                    }
                  }
                ]
              }
            },
            {
              name: 'hotel',
              type: {
                name: 'HotelArgument',
                kind: 'INPUT_OBJECT',
                inputFields: [
                  {
                    name: 'address',
                    type: {
                      name: 'AddressArgument'
                    }
                  },
                  {
                    name: 'bookingNumber',
                    type: {
                      name: 'StringArgument'
                    }
                  },
                  {
                    name: 'name',
                    type: {
                      name: 'StringArgument'
                    }
                  },
                  {
                    name: 'phone',
                    type: {
                      name: 'StringArgument'
                    }
                  },
                  {
                    name: 'room',
                    type: {
                      name: 'StringArgument'
                    }
                  }
                ]
              }
            }
          ]
        }
      }
    });
    const wrapper = mount(<DomainExplorerFilterOptions {...props} />);
    wrapper.update();
    wrapper.setProps({});
    const mGraphQLResponse = {};
    client.query.mockReturnValueOnce(mGraphQLResponse);
    await Promise.resolve();
    expect(wrapper).toMatchSnapshot();
  });
  it('Trigger onselect function on field select', () => {
    const props = {
      currentDomain: 'Travels',
      getQuery: {
        loading: false,
        data: {
          __type: {
            name: 'Query',
            fields: [
              {
                name: 'Travels',
                args: [
                  {
                    name: 'where',
                    type: { kind: 'INPUT_OBJECT', name: 'TravelsArgument' }
                  },
                  {
                    name: 'orderBy',
                    type: { kind: 'INPUT_OBJECT', name: 'TravelsOrderBy' }
                  },
                  {
                    name: 'pagination',
                    type: { kind: 'INPUT_OBJECT', name: 'Pagination' }
                  }
                ],
                type: {
                  ofType: { name: 'Travels' }
                }
              }
            ]
          }
        }
      },
      parameters: [
        {
          metadata: {
            processInstances: [
              'id',
              'processName',
              'state',
              'start',
              'lastUpdate',
              'businessKey'
            ]
          }
        }
      ],
      setColumnFilters: jest.fn(),
      setTableLoading: jest.fn(),
      setDisplayTable: jest.fn(),
      setDisplayEmptyState: jest.fn(),
      queryAttributes: {},
      setQueryAttributes: jest.fn(),
      enableFilter: false,
      setEnableFilter: jest.fn(),
      setError: jest.fn(),
      getQueryTypes: {
        loading: false,
        data: {
          __schema: {
            queryType: [
              {
                name: 'AddressArgument',
                kind: 'INPUT_OBJECT',
                inputFields: [
                  {
                    name: 'city',
                    type: { name: 'StringArgument', kind: 'INPUT_OBJECT' }
                  },
                  {
                    name: 'country',
                    type: { name: 'StringArgument', kind: 'INPUT_OBJECT' }
                  },
                  {
                    name: 'street',
                    type: { name: 'StringArgument', kind: 'INPUT_OBJECT' }
                  },
                  {
                    name: 'zipCode',
                    type: { name: 'StringArgument', kind: 'INPUT_OBJECT' }
                  }
                ]
              },
              {
                name: 'IdArgument',
                kind: 'INPUT_OBJECT',
                inputFields: [
                  { name: 'id', type: { name: null, kind: 'LIST' } },
                  { name: 'equal', type: { name: 'String', kind: 'SCALAR' } },
                  { name: 'isNull', type: { name: 'String', kind: 'SCALAR' } }
                ]
              }
            ]
          }
        }
      },
      filterChips: [],
      setFilterChips: jest.fn(),
      runFilter: false,
      setRunFilter: jest.fn(),
      finalFilters: {},
      setFinalFilters: jest.fn()
    };
    const wrapper = mount(<DomainExplorerFilterOptions {...props} />);
    wrapper.update();
    wrapper.setProps({});
    // @ts-ignore
    useGetInputFieldsFromQueryQuery.mockReturnValue({
      loading: false,
      data: {}
    });
    // @ts-ignore
    useGetInputFieldsFromTypeQuery.mockReturnValue({
      loading: false,
      data: {}
    });
    const obj = {
      nativeEvent: {
        target: {
          parentElement: {
            parentElement: {
              getAttribute: jest.fn(() => 'id')
            }
          }
        }
      },
      target: {}
    } as any;
    const obj2 = {
      target: {
        innerText: 'equal'
      }
    } as any;

    wrapper
      .find('#select-field')
      .first()
      .props()
      ['onSelect'](obj);
    wrapper
      .find('#select-field')
      .first()
      .props()
      ['onClear']();
    wrapper
      .find('#select-operator')
      .first()
      .props()
      ['onSelect'](obj2);
  });
});
