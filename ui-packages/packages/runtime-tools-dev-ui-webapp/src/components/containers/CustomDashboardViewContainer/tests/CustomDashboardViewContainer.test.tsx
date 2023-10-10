import React from 'react';
import { render } from '@testing-library/react';
import CustomDashboardViewContainer from '../CustomDashboardViewContainer';
import { CustomDashboardViewGatewayApiImpl } from '../../../../channel/CustomDashboardView/CustomDashboardViewGatewayApi';
import * as CustomDashboardViewContext from '../../../../channel/CustomDashboardView/CustomDashboardViewContext';
import DevUIAppContextProvider from '../../../contexts/DevUIAppContextProvider';
import {
  DefaultUser,
  User
} from '@kogito-apps/consoles-common/dist/environment/auth';
import { EmbeddedCustomDashboardView } from '@kogito-apps/custom-dashboard-view';

jest
  .spyOn(CustomDashboardViewContext, 'useCustomDashboardViewGatewayApi')
  .mockImplementation(() => new CustomDashboardViewGatewayApiImpl());

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

describe('CustomDashboardViewContainer tests', () => {
  it('Snapshot', () => {
    const { container } = render(
      <DevUIAppContextProvider users={[user]} {...appContextProps}>
        <CustomDashboardViewContainer dashboardName="test-dashboard-name" />
      </DevUIAppContextProvider>
    );

    expect(container).toMatchSnapshot();

    expect(container.querySelector('div')).toBeTruthy();
  });
});
