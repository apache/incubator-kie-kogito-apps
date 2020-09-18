/**
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import React from 'react';
import { mount } from 'enzyme';
import DomainExplorerListDomains from '../DomainExplorerListDomains';
import { GraphQL } from '../../../../graphql/types';
import { MemoryRouter as Router } from 'react-router-dom';
import useGetQueryFieldsQuery = GraphQL.useGetQueryFieldsQuery;

jest.mock('../../../../graphql/types');
jest.mock('@patternfly/react-icons');
describe('DomainExplorerListDomains Component test cases', () => {
  it('Snapshot with mock useGetQueryFieldsQuery', () => {
    // @ts-ignore
    useGetQueryFieldsQuery.mockReturnValue({
      loading: false,
      data: {
        __type: {
          fields: [
            {
              name: 'ProcessInstances',
              args: []
            },
            {
              name: 'UserTaskInstances',
              args: []
            },
            {
              name: 'Travels',
              args: []
            },
            {
              name: 'visaApplication',
              args: []
            },
            {
              name: 'Jobs',
              args: []
            }
          ]
        }
      }
    });
    const wrapper = mount(
      <Router keyLength={0}>
        <DomainExplorerListDomains />
      </Router>
    );
    expect(useGetQueryFieldsQuery).toHaveBeenCalled();
    expect(wrapper.find(DomainExplorerListDomains)).toMatchSnapshot();
  });
  it('Assertions', () => {
    // @ts-ignore
    useGetQueryFieldsQuery.mockReturnValue({
      loading: false,
      data: {
        __type: {
          fields: [
            {
              name: 'UserTaskInstances',
              args: []
            },
            {
              name: 'ProcessInstances',
              args: []
            },
            {
              name: 'Travels',
              args: [
                {
                  name: 'where',
                  type: {
                    kind: 'INPUT_OBJECT',
                    name: 'TravelsArgument'
                  }
                },
                {
                  name: 'orderBy',
                  type: {
                    kind: 'INPUT_OBJECT',
                    name: 'TravelsOrderBy'
                  }
                }
              ],
              type: { ofType: { name: 'Travels' } }
            },
            {
              name: 'visaApplication',
              args: [
                {
                  name: 'where',
                  type: {
                    kind: 'INPUT_OBJECT',
                    name: 'VisaApplicationsArgument'
                  }
                },
                {
                  name: 'orderBy',
                  type: {
                    kind: 'INPUT_OBJECT',
                    name: 'VisaApplicationsOrderBy'
                  }
                }
              ],
              type: { ofType: { name: 'VisaApplications' } }
            }
          ]
        }
      }
    });
    const wrapper = mount(
      <Router keyLength={0}>
        <DomainExplorerListDomains />
      </Router>
    );
    expect(useGetQueryFieldsQuery).toHaveBeenCalled();
    expect(wrapper.find(DomainExplorerListDomains)).toMatchSnapshot();
  });
});
