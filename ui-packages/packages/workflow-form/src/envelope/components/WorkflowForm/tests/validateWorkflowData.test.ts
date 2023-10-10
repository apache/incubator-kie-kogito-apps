import { validateWorkflowData } from '../validateWorkflowData';

describe('validateWorkflowData tests', function () {
  it('Validate data', () => {
    expect(validateWorkflowData(undefined)).toBeTruthy();
    expect(validateWorkflowData('this should fail')).toBeFalsy();
    expect(validateWorkflowData('""')).toBeFalsy();
    expect(
      validateWorkflowData(
        JSON.stringify({
          name: 'Donatello',
          hobby: 'Eat Pizza',
          age: 15
        })
      )
    );
  });
});
