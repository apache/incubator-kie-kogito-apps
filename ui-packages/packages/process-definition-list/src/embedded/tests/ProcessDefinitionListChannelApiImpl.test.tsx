import { ProcessDefinitionListDriver } from '../../api';
import { ProcessDefinitionListChannelApiImpl } from '../ProcessDefinitionListChannelApiImpl';
import { MockedProcessDefinitionListDriver } from './utils/Mocks';

let driver: ProcessDefinitionListDriver;
let api: ProcessDefinitionListChannelApiImpl;

describe('ProcessDefinitionListChannelApiImpl tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    driver = new MockedProcessDefinitionListDriver();
    api = new ProcessDefinitionListChannelApiImpl(driver);
  });

  it('ProcessDefinitionList__getProcessDefinitionsQuery', () => {
    api.processDefinitionList__getProcessDefinitionsQuery();
    expect(driver.getProcessDefinitionsQuery).toHaveBeenCalled();
  });
  it('ProcessDefinitionList__getProcessDefinitionsFilter', () => {
    api.processDefinitionList__getProcessDefinitionFilter();
    expect(driver.getProcessDefinitionFilter).toHaveBeenCalled();
  });

  it('ProcessDefinitionList__setProcessDefinitionFilter', () => {
    const filter = ['process1'];
    api.processDefinitionList__setProcessDefinitionFilter(filter);
    expect(driver.setProcessDefinitionFilter).toHaveBeenCalledWith(filter);
  });

  it('ProcessDefinitionList__getProcessDefinitionsQuery', () => {
    const processDefinition = {
      processName: 'process1',
      endpoint: 'http://localhost:4000'
    };
    api.processDefinitionList__openProcessForm(processDefinition);
    expect(driver.openProcessForm).toHaveBeenCalledWith(processDefinition);
  });
});
