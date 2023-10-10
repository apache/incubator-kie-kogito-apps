import React from 'react';
import { render } from '@testing-library/react';
import DevUINav from '../DevUINav';
import { MemoryRouter } from 'react-router-dom';
import DevUIAppContextProvider from '../../../contexts/DevUIAppContextProvider';

describe('DevUINav tests::Process and Tracing enabled', () => {
  it('Snapshot testing with processes props', () => {
    const { container } = render(
      <DevUIAppContextProvider
        users={[]}
        devUIUrl="http://devUIUrl"
        openApiPath="http://openApiPath"
        isProcessEnabled={true}
        isTracingEnabled={true}
        customLabels={{
          singularProcessLabel: 'Workflow',
          pluralProcessLabel: 'Workflows'
        }}
      >
        <MemoryRouter>
          <DevUINav pathname={'/Processes'} />
        </MemoryRouter>
      </DevUIAppContextProvider>
    );

    expect(container).toMatchSnapshot();

    const DevUINavWrapper = container.querySelector(
      '[data-ouia-navigation-name="processes-nav"]'
    );
    expect(DevUINavWrapper).toBeTruthy();
  });

  it('Snapshot testing with jobs management props', () => {
    const { container } = render(
      <DevUIAppContextProvider
        users={[]}
        devUIUrl="http://devUIUrl"
        openApiPath="http://openApiPath"
        isProcessEnabled={true}
        isTracingEnabled={true}
        customLabels={{
          singularProcessLabel: 'Workflow',
          pluralProcessLabel: 'Workflows'
        }}
      >
        <MemoryRouter>
          <DevUINav pathname={'/JobsManagement'} />
        </MemoryRouter>
      </DevUIAppContextProvider>
    );

    expect(container).toMatchSnapshot();

    const DevUINavWrapper = container.querySelector(
      '[data-ouia-navigation-name="jobs-management-nav"]'
    );
    expect(DevUINavWrapper).toBeTruthy();
  });

  it('Snapshot testing with forms list props', () => {
    const { container } = render(
      <DevUIAppContextProvider
        users={[]}
        devUIUrl="http://devUIUrl"
        openApiPath="http://openApiPath"
        isProcessEnabled={true}
        isTracingEnabled={true}
        customLabels={{
          singularProcessLabel: 'Workflow',
          pluralProcessLabel: 'Workflows'
        }}
      >
        <MemoryRouter>
          <DevUINav pathname={'/Forms'} />
        </MemoryRouter>
      </DevUIAppContextProvider>
    );

    expect(container).toMatchSnapshot();

    const DevUINavWrapper = container.querySelector(
      '[data-ouia-navigation-name="forms-list-nav"]'
    );
    expect(DevUINavWrapper).toBeTruthy();
  });

  it('Snapshot testing audit investigation link props', () => {
    const { container } = render(
      <DevUIAppContextProvider
        users={[]}
        devUIUrl="http://devUIUrl"
        openApiPath="http://openApiPath"
        isProcessEnabled={true}
        availablePages={['Processess']}
        isTracingEnabled={true}
        customLabels={{
          singularProcessLabel: 'Workflow',
          pluralProcessLabel: 'Workflows'
        }}
      >
        <MemoryRouter>
          <DevUINav pathname={'/Audit'} />
        </MemoryRouter>
      </DevUIAppContextProvider>
    );

    expect(container).toMatchSnapshot();

    const DevUINavWrapper = container.querySelector(
      '[data-ouia-navigation-name="audit-nav"]'
    );
    expect(DevUINavWrapper).toBeTruthy();
  });
});

describe('DevUINav tests::Sections disabled', () => {
  it('Snapshot testing with processes props', () => {
    const { container } = render(
      <DevUIAppContextProvider
        users={[]}
        devUIUrl="http://devUIUrl"
        openApiPath="http://openApiPath"
        isProcessEnabled={true}
        isTracingEnabled={false}
        customLabels={{
          singularProcessLabel: 'Workflow',
          pluralProcessLabel: 'Workflows'
        }}
      >
        <MemoryRouter>
          <DevUINav pathname={'/Processes'} />
        </MemoryRouter>
      </DevUIAppContextProvider>
    );

    expect(container).toMatchSnapshot();

    expect(
      container.querySelector('[data-ouia-navigation-name="processes-nav"]')
    ).toBeTruthy();
    expect(
      container.querySelector(
        '[data-ouia-navigation-name="jobs-management-nav"]'
      )
    ).toBeTruthy();
    expect(
      container.querySelector('[data-ouia-navigation-name="task-inbox-nav"]')
    ).toBeTruthy();
    expect(
      container.querySelector('[data-ouia-navigation-name="forms-list-nav"]')
    ).toBeTruthy();
    expect(
      container.querySelector('[data-ouia-navigation-name="audit-nav"]')
    ).toBeFalsy();
  });

  it('Snapshot testing with jobs management props', () => {
    const { container } = render(
      <DevUIAppContextProvider
        users={[]}
        devUIUrl="http://devUIUrl"
        openApiPath="http://openApiPath"
        isProcessEnabled={true}
        isTracingEnabled={false}
        customLabels={{
          singularProcessLabel: 'Workflow',
          pluralProcessLabel: 'Workflows'
        }}
      >
        <MemoryRouter>
          <DevUINav pathname={'/JobsManagement'} />
        </MemoryRouter>
      </DevUIAppContextProvider>
    );

    expect(container).toMatchSnapshot();

    expect(
      container.querySelector('[data-ouia-navigation-name="processes-nav"]')
    ).toBeTruthy();
    expect(
      container.querySelector(
        '[data-ouia-navigation-name="jobs-management-nav"]'
      )
    ).toBeTruthy();
    expect(
      container.querySelector('[data-ouia-navigation-name="task-inbox-nav"]')
    ).toBeTruthy();
    expect(
      container.querySelector('[data-ouia-navigation-name="forms-list-nav"]')
    ).toBeTruthy();
    expect(
      container.querySelector('[data-ouia-navigation-name="audit-nav"]')
    ).toBeFalsy();
  });

  it('Snapshot testing with forms list props', () => {
    const { container } = render(
      <DevUIAppContextProvider
        users={[]}
        devUIUrl="http://devUIUrl"
        openApiPath="http://openApiPath"
        isProcessEnabled={true}
        isTracingEnabled={false}
        customLabels={{
          singularProcessLabel: 'Workflow',
          pluralProcessLabel: 'Workflows'
        }}
      >
        <MemoryRouter>
          <DevUINav pathname={'/Forms'} />
        </MemoryRouter>
      </DevUIAppContextProvider>
    );

    expect(container).toMatchSnapshot();

    expect(
      container.querySelector('[data-ouia-navigation-name="processes-nav"]')
    ).toBeTruthy();
    expect(
      container.querySelector(
        '[data-ouia-navigation-name="jobs-management-nav"]'
      )
    ).toBeTruthy();
    expect(
      container.querySelector('[data-ouia-navigation-name="task-inbox-nav"]')
    ).toBeTruthy();
    expect(
      container.querySelector('[data-ouia-navigation-name="forms-list-nav"]')
    ).toBeTruthy();
    expect(
      container.querySelector('[data-ouia-navigation-name="audit-nav"]')
    ).toBeFalsy();
  });

  it('Snapshot testing audit investigation link props', () => {
    const { container } = render(
      <DevUIAppContextProvider
        users={[]}
        devUIUrl="http://devUIUrl"
        openApiPath="http://openApiPath"
        isProcessEnabled={false}
        isTracingEnabled={true}
        customLabels={{
          singularProcessLabel: 'Workflow',
          pluralProcessLabel: 'Workflows'
        }}
      >
        <MemoryRouter>
          <DevUINav pathname={'/Audit'} />
        </MemoryRouter>
      </DevUIAppContextProvider>
    );

    expect(container).toMatchSnapshot();

    expect(
      container.querySelector('[data-ouia-navigation-name="processes-nav"]')
    ).toBeFalsy();
    expect(
      container.querySelector(
        '[data-ouia-navigation-name="jobs-management-nav"]'
      )
    ).toBeFalsy();
    expect(
      container.querySelector('[data-ouia-navigation-name="task-inbox-nav"]')
    ).toBeFalsy();
    expect(
      container.querySelector('[data-ouia-navigation-name="forms-list-nav"]')
    ).toBeFalsy();
    expect(
      container.querySelector('[data-ouia-navigation-name="audit-nav"]')
    ).toBeTruthy();
  });
});
