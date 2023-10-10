import React from 'react';
import { render } from '@testing-library/react';
import CloudEventFieldLabelIcon from '../CloudEventFieldLabelIcon';

const MockedComponent = (): React.ReactElement => {
  return <></>;
};

jest.mock('@patternfly/react-icons/dist/esm/icons/help-icon', () =>
  Object.assign({}, jest.requireActual('@patternfly/react-icons'), {
    HelpIcon: () => {
      return <MockedComponent />;
    }
  })
);

jest.mock('@patternfly/react-core/dist/js/components/Popover', () =>
  Object.assign({}, jest.requireActual('@patternfly/react-icons'), {
    Popover: () => {
      return <MockedComponent />;
    }
  })
);

describe('CloudEventFieldLabelIcon tests', () => {
  it('default snapshot test', () => {
    const container = render(
      <CloudEventFieldLabelIcon
        fieldId="endpoint"
        helpMessage="Sets the endpoint and method where the CloudEvent should be triggered."
      />
    ).container;

    expect(container).toMatchSnapshot();
  });

  it('default snapshot test - with header', () => {
    const container = render(
      <CloudEventFieldLabelIcon
        fieldId="eventType"
        helpMessage="Sets the type of the cloud event."
        cloudEventHeader="type"
      />
    ).container;

    expect(container).toMatchSnapshot();
  });
});
