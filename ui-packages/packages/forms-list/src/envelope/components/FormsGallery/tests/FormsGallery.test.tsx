import React from 'react';
import FormsGallery from '../FormsGallery';
import { mount } from 'enzyme';
import {
  formList,
  MockedFormsListDriver
} from '../../../tests/mocks/MockedFormsListDriver';
import {
  KogitoEmptyState,
  KogitoSpinner
} from '@kogito-apps/components-common';

const MockedComponent = (): React.ReactElement => {
  return <></>;
};

jest.mock('@kogito-apps/components-common', () => ({
  ...jest.requireActual('@kogito-apps/components-common'),
  KogitoSpinner: () => {
    return <MockedComponent />;
  },
  KogitoEmptyState: () => {
    return <MockedComponent />;
  }
}));

describe('forms gallery tests', () => {
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
