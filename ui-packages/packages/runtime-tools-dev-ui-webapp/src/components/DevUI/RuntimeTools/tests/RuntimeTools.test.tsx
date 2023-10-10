import { render } from '@testing-library/react';
import React from 'react';
import { MemoryRouter } from 'react-router-dom';
import RuntimeTools from '../RuntimeTools';

jest.mock('apollo-link-http');
jest.mock('../../DevUILayout/DevUILayout');
describe('Runtime Tools tests', () => {
  it('Snapshot tests with default props', () => {
    const { container } = render(
      <MemoryRouter initialEntries={['/']} keyLength={0}>
        <RuntimeTools
          users={[{ id: 'John snow', groups: ['admin'] }]}
          dataIndexUrl="http:localhost:4000"
          trustyServiceUrl="http://localhost:1336"
          navigate="JobsManagement"
          devUIUrl="http://localhost:8080"
          openApiPath="/docs/openapi.json"
          isProcessEnabled={true}
          isTracingEnabled={true}
        />
      </MemoryRouter>
    );

    expect(container).toMatchSnapshot();
  });
});
