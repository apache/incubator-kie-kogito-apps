import { ProcessFormDriver } from '../../api';
import { EmbeddedProcessFormChannelApiImpl } from '../EmbeddedProcessFormChannelApiImpl';
import { MockedProcessFormDriver } from './mocks/Mocks';

let driver: ProcessFormDriver;
let api: EmbeddedProcessFormChannelApiImpl;

describe('EmbeddedProcessFormChannelApiImpl tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    driver = new MockedProcessFormDriver();
    api = new EmbeddedProcessFormChannelApiImpl(driver);
  });

  it('processForm__getProcessFormSchema', () => {
    const processDefinitionData = {
      processName: 'process1',
      endpoint: 'http://localhost:4000'
    };
    api.processForm__getProcessFormSchema(processDefinitionData);

    expect(driver.getProcessFormSchema).toHaveBeenCalledWith(
      processDefinitionData
    );
  });

  it('ProcessForm__doSubmit', () => {
    const formJSON = {
      something: 'something'
    };
    api.processForm__startProcess(formJSON);

    expect(driver.startProcess).toHaveBeenCalledWith(formJSON);
  });
});
