import React from 'react';
import { render, screen } from '@testing-library/react';
import CustomDashboardListContainer from '../CustomDashboardListContainer';
import { CustomDashboardListGatewayApiImpl } from '../../../../channel/CustomDashboardList/CustomDashboardListGatewayApi';
import * as CustomDashboardListContext from '../../../../channel/CustomDashboardList/CustomDashboardListContext';

jest
  .spyOn(CustomDashboardListContext, 'useCustomDashboardListGatewayApi')
  .mockImplementation(() => new CustomDashboardListGatewayApiImpl());

describe('CustomDashboardListContainer tests', () => {
  it('Snapshot', () => {
    const { container } = render(<CustomDashboardListContainer />);

    expect(container).toMatchSnapshot();
    const checkDiv = container.querySelector('div');
    expect(checkDiv).toBeTruthy();
  });
});
