import React from 'react';
import { mount } from 'enzyme';
import TaskConsole from '../TaskConsole';
import { ApolloClient } from 'apollo-client';
import { TestUserContext } from '@kogito-apps/consoles-common/dist/environment/context';
import TaskConsoleRoutes from '../../TaskConsoleRoutes/TaskConsoleRoutes';

const MockedComponent = (): React.ReactElement => {
  return <></>;
};

jest.mock('../../TaskConsoleNav/TaskConsoleNav');
jest.mock('../../TaskConsoleRoutes/TaskConsoleRoutes');

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

describe('TaskConsole tests', () => {
  it('Snapshot', () => {
    const client = jest
      .fn()
      .mockImplementation() as unknown as ApolloClient<any>;
    const testContext = new TestUserContext();
    const props = {
      apolloClient: client,
      userContext: testContext
    };
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore
    const wrapper = mount(
      <TaskConsole {...props}>
        <TaskConsoleRoutes />
      </TaskConsole>
    ).find('TaskConsole');

    expect(wrapper).toMatchSnapshot();
  });
});
