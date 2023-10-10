import React from 'react';
import { render } from '@testing-library/react';
import FormDetailsContainer from '../FormDetailsContainer';
import * as FormDetailsContext from '../../../../channel/FormDetails/FormDetailsContext';
import { FormDetailsGatewayApiImpl } from '../../../../channel/FormDetails/FormDetailsGatewayApi';
import DevUIAppContextProvider from '../../../contexts/DevUIAppContextProvider';
import {
  DefaultUser,
  User
} from '@kogito-apps/consoles-common/dist/environment/auth';
import { EmbeddedFormDetails } from '@kogito-apps/form-details';
import { FormType } from '@kogito-apps/forms-list';

jest
  .spyOn(FormDetailsContext, 'useFormDetailsGatewayApi')
  .mockImplementation(() => new FormDetailsGatewayApiImpl());
const user: User = new DefaultUser('jon', []);
const appContextProps = {
  devUIUrl: 'http://localhost:9000',
  openApiPath: '/mocked',
  isProcessEnabled: false,
  isTracingEnabled: false,
  omittedProcessTimelineEvents: [],
  isStunnerEnabled: false,
  availablePages: [],
  customLabels: {
    singularProcessLabel: 'test-singular',
    pluralProcessLabel: 'test-plural'
  },
  diagramPreviewSize: { width: 100, height: 100 }
};

describe('FormDetailsContainer tests', () => {
  it('Snapshot', () => {
    const { container } = render(
      <DevUIAppContextProvider users={[user]} {...appContextProps}>
        <FormDetailsContainer
          formData={{
            name: 'form1',
            type: FormType.HTML,
            lastModified: new Date('2021-08-23T13:26:02.13Z')
          }}
          onSuccess={jest.fn}
          onError={jest.fn}
        />
      </DevUIAppContextProvider>
    );

    expect(container).toMatchSnapshot();

    expect(container.querySelector('div')).toBeTruthy();
  });
});
