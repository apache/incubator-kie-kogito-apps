import { Form, FormContent, FormDetailsDriver } from '../../../api';

export const formContent: Form = {
  formInfo: {
    name: 'form1',
    type: 'HTML' as any,
    lastModified: new Date('2020-07-11T18:30:00.000Z')
  },
  configuration: {
    schema:
      '{"$schema":"http://json-schema.org/draft-07/schema#","type":"object","properties":{"approve":{"type":"boolean","output":true},"candidate":{"type":"object","properties":{"email":{"type":"string"},"name":{"type":"string"},"salary":{"type":"integer"},"skills":{"type":"string"}},"input":true}}}',
    resources: {
      scripts: {},
      styles: {}
    }
  },
  source: 'html source code'
};
export class MockedFormDetailsDriver implements FormDetailsDriver {
  getFormContent(): Promise<Form> {
    return Promise.resolve(formContent);
  }

  saveFormContent(formName: string, content: FormContent) {
    return;
  }
}
