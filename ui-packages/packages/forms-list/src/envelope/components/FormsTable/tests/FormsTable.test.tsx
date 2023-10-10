import React from 'react';
import { mount } from 'enzyme';
import FormsTable from '../FormsTable';
import {
  formList,
  MockedFormsListDriver
} from '../../../tests/mocks/MockedFormsListDriver';

Date.now = jest.fn(() => 1487076708000); //14.02.2017

const MockedComponent = (): React.ReactElement => {
  return <></>;
};

jest.mock('@kogito-apps/components-common/dist/components/DataTable', () =>
  Object.assign({}, jest.requireActual('@kogito-apps/components-common'), {
    DataTable: () => {
      return <MockedComponent />;
    }
  })
);

describe('forms table test', () => {
  const driver = new MockedFormsListDriver();
  it('renders table', () => {
    const wrapper = mount(
      <FormsTable
        driver={driver}
        isLoading={false}
        formsData={formList}
        setFormsData={jest.fn()}
      />
    );
    const dataTable = wrapper.find('DataTable');
    expect(dataTable.exists()).toBeTruthy();
  });
});
