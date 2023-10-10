import { FormsListDriver } from '../../../api';

export const MockedFormsListDriver = jest.fn<FormsListDriver, []>(() => ({
  getFormFilter: jest.fn(),
  applyFilter: jest.fn(),
  getFormsQuery: jest.fn(),
  openForm: jest.fn()
}));
