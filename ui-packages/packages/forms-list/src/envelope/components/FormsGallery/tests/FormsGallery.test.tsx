import React from 'react';
import FormsGallery from '../FormsGallery';
import { mount } from 'enzyme';
import {
  formList,
  MockedFormsListDriver
} from '../../../tests/mocks/MockedFormsListDriver';
import { KogitoSpinner } from '@kogito-apps/components-common/dist/components/KogitoSpinner';
import { KogitoEmptyState } from '@kogito-apps/components-common/dist/components/KogitoEmptyState';

const MockedComponent = (): React.ReactElement => {
  return <></>;
};

jest.mock('@kogito-apps/components-common/dist/components/KogitoSpinner', () =>
  Object.assign({}, jest.requireActual('@kogito-apps/components-common'), {
    KogitoSpinner: () => {
      return <MockedComponent />;
    }
  })
);

jest.mock(
  '@kogito-apps/components-common/dist/components/KogitoEmptyState',
  () =>
    Object.assign({}, jest.requireActual('@kogito-apps/components-common'), {
      KogitoEmptyState: () => {
        return <MockedComponent />;
      }
    })
);

describe('forms gallery tests', () => {
  Date.now = jest.fn(() => 1487076708000);
  const driver = new MockedFormsListDriver();
  it('renders gallery of forms', () => {
    const wrapper = mount(
      <FormsGallery driver={driver} isLoading={false} formsData={formList} />
    );
    expect(wrapper).toMatchSnapshot();
  });

  it('renders loading component', () => {
    const wrapper = mount(
      <FormsGallery driver={driver} isLoading={true} formsData={formList} />
    );
    expect(wrapper).toMatchSnapshot();
    const loadingComponent = wrapper.find(KogitoSpinner);
    expect(loadingComponent.exists()).toBeTruthy();
  });

  it('renders empty state component', () => {
    const wrapper = mount(
      <FormsGallery driver={driver} isLoading={false} formsData={[]} />
    );
    expect(wrapper).toMatchSnapshot();
    const emptyStateComponent = wrapper.find(KogitoEmptyState);
    expect(emptyStateComponent.exists()).toBeTruthy();
  });
});
