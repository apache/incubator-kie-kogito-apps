import isPlainObject from 'lodash/isPlainObject';

export function validateWorkflowData(data: string): boolean {
  if (data) {
    try {
      const json = JSON.parse(data);
      if (!isPlainObject(json)) {
        return false;
      }
    } catch (err) {
      return false;
    }
  }
  return true;
}
