import React from 'react';
import { render, screen } from '@testing-library/react';
import { ActionList } from '@patternfly/react-core/dist/js/components/ActionList';
import { Button } from '@patternfly/react-core/dist/js/components/Button';
import { FormFooter } from '../FormFooter';
import { ActionType, FormAction } from '../../utils';

const MockedComponent = (): React.ReactElement => {
  return <></>;
};

jest.mock('@patternfly/react-core/dist/js/components/Button', () =>
  Object.assign({}, jest.requireActual('@patternfly/react-core'), {
    Button: () => <MockedComponent />
  })
);

describe('Form Footer test', () => {
  it('showing actions', () => {
    const actions: FormAction[] = [
      {
        name: 'Abort',
        execute: jest.fn()
      },
      {
        name: 'Release',
        execute: jest.fn()
      },
      {
        name: 'Complete',
        execute: jest.fn()
      }
    ];

    const { container } = render(<FormFooter actions={actions} />);
    expect(container).toMatchSnapshot();

    const actionList = screen.getByTestId('action-list');
    expect(actionList).toBeTruthy();

    const actionListItem = screen.getAllByTestId('action-list-item');
    expect(actionListItem).toHaveLength(3);
  });

  it('showing disabled', () => {
    const actions: FormAction[] = [
      {
        name: 'Abort',
        execute: jest.fn()
      },
      {
        name: 'Release',
        execute: jest.fn()
      },
      {
        name: 'Complete',
        execute: jest.fn()
      }
    ];

    const { container } = render(
      <FormFooter actions={actions} enabled={false} />
    );
    expect(container).toMatchSnapshot();

    const actionList = screen.getByTestId('action-list');
    expect(actionList).toBeTruthy();

    const actionListItem = screen.getAllByTestId('action-list-item');
    expect(actionListItem).toHaveLength(3);
  });

  it('showing empty actions', () => {
    const props = {
      actions: [],
      actionType: ActionType.SUBMIT
    };

    const { container } = render(<FormFooter {...props} />);

    expect(container.querySelector('div')).toBe(null);
  });
});
