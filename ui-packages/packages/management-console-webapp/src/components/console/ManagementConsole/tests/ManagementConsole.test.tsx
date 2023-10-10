import React from 'react';
import { mount } from 'enzyme';
import ManagementConsole from '../ManagementConsole';
import ManagementConsoleRoutes from '../../ManagementConsoleRoutes/ManagementConsoleRoutes';
import { ApolloClient } from 'apollo-client';
import { act } from 'react-dom/test-utils';

const MockedComponent = (): React.ReactElement => {
  return <></>;
};

jest.mock(
  '@kogito-apps/consoles-common/dist/components/layout/PageLayout',
  () =>
    Object.assign({}, jest.requireActual('@kogito-apps/consoles-common'), {
      PageLayout: () => {
        return <MockedComponent />;
      }
    })
);

jest.mock('apollo-client');

describe('ManagementConsole tests', () => {
  it('Snapshot test with default props', () => {
    const client = jest
      .fn()
      .mockImplementation() as unknown as ApolloClient<any>;
    const props = {
      apolloClient: client,
      userContext: { getCurrentUser: jest.fn() }
    };
    const wrapper = mount(
      <ManagementConsole {...props}>
        <ManagementConsoleRoutes />
      </ManagementConsole>
    ).find('ManagementConsole');
    expect(wrapper).toMatchSnapshot();
  });

  it('test brandClick prop on PageLayout', async () => {
    const client = jest
      .fn()
      .mockImplementation() as unknown as ApolloClient<any>;
    const props = {
      apolloClient: client,
      userContext: { getCurrentUser: jest.fn() }
    };
    const wrapper = mount(
      <ManagementConsole {...props}>
        <ManagementConsoleRoutes />
      </ManagementConsole>
    ).find('ManagementConsole');
    await act(async () => {
      wrapper.find('PageLayout').props()['BrandClick']();
    });
  });
});
