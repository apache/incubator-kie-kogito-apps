import React from 'react';
import ProcessDetailsErrorModal from '../ProcessDetailsErrorModal';
import { Button } from '@patternfly/react-core';
import { setTitle } from '@kogito-apps/management-console-shared';
import { getWrapper } from '@kogito-apps/components-common';

const MockedIcon = (): React.ReactElement => {
  return <></>;
};
jest.mock('@patternfly/react-icons', () => ({
  ...jest.requireActual('@patternfly/react-icons'),
  InfoCircleIcon: () => {
    return <MockedIcon />;
  }
}));

const errorModalAction: JSX.Element[] = [
  <Button key="confirm-selection" variant="primary">
    OK
  </Button>
];
describe('Process details error modal tests', () => {
  const props = {
    errorString: '404-not found',
    errorModalOpen: true,
    errorModalAction: errorModalAction,
    handleErrorModal: jest.fn(),
    label: 'Error modal',
    title: setTitle('failure', 'Process Visualization')
  };

  it('Snapshot test with default props', () => {
    const wrapper = getWrapper(
      <ProcessDetailsErrorModal {...props} />,
      'ProcessDetailsErrorModal'
    );
    expect(wrapper).toMatchSnapshot();
    expect(
      wrapper
        .find('Text')
        .children()
        .text()
    ).toEqual('404-not found');
  });
});
