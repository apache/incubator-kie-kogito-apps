import { FormDetailsDriver } from '../../../api';

export const MockedFormDetailsDriver = jest.fn<FormDetailsDriver, []>(() => ({
  getFormContent: jest.fn(),
  saveFormContent: jest.fn()
}));
