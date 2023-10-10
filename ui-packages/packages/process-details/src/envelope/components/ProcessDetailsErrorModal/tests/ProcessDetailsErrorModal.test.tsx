import React from 'react';
import ProcessDetailsErrorModal from '../ProcessDetailsErrorModal';
import { Button } from '@patternfly/react-core/dist/js/components/Button';
import { setTitle } from '@kogito-apps/management-console-shared/dist/utils/Utils';
import { render } from '@testing-library/react';

const MockedIcon = (): React.ReactElement => {
  return <></>;
};
jest.mock('@patternfly/react-icons/dist/js/icons/info-circle-icon', () =>
  Object.assign({}, jest.requireActual('@patternfly/react-icons'), {
    InfoCircleIcon: () => {
      return <MockedIcon />;
    }
  })
);

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
    const container = render(<ProcessDetailsErrorModal {...props} />).container;
    expect(container).toMatchSnapshot();
  });
});
