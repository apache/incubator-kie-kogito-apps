import { TaskFormDriver } from '../../api';
import { EmbeddedTaskFormChannelApiImpl } from '../EmbeddedTaskFormChannelApiImpl';
import { MockedTaskFormDriver } from './mocks/Mocks';

let driver: TaskFormDriver;
let api: EmbeddedTaskFormChannelApiImpl;

describe('EmbeddedTaskFormChannelApiImpl tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    driver = new MockedTaskFormDriver();
    api = new EmbeddedTaskFormChannelApiImpl(driver);
  });

  it('taskForm__getTaskFormSchema', () => {
    api.taskForm__getTaskFormSchema();

    expect(driver.getTaskFormSchema).toHaveBeenCalled();
  });

  it('taskForm__getCustomForm', () => {
    api.taskForm__getCustomForm();

    expect(driver.getCustomForm).toHaveBeenCalled();
  });

  it('taskForm__doSubmit', () => {
    api.taskForm__doSubmit('complete', {});

    expect(driver.doSubmit).toHaveBeenCalledWith('complete', {});
  });
});
