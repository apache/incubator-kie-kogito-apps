import isPlainObject from 'lodash/isPlainObject';
import { CloudEventRequest } from '../../../api';

export interface FormValidations {
  isValid(): boolean;

  getFieldValidation(fieldId: string): undefined | string;
}

class FormValidationsImpl implements FormValidations {
  constructor(private readonly validations: Record<string, string>) {}

  getFieldValidation(fieldId: string): string | undefined {
    return this.validations[fieldId];
  }

  isValid(): boolean {
    return Object.keys(this.validations).length == 0;
  }
}

export function validateCloudEventRequest(
  eventRequest: CloudEventRequest
): FormValidations {
  const validations = {};

  if (!eventRequest.endpoint) {
    validations['endpoint'] = 'The Cloud Event endpoint cannot be empty.';
  }

  if (!eventRequest.headers.type) {
    validations['eventType'] = 'The Cloud Event type cannot be empty.';
  }

  if (eventRequest.data) {
    try {
      const json = JSON.parse(eventRequest.data);
      if (!isPlainObject(json)) {
        throw new Error('not an object');
      }
    } catch (err) {
      validations['eventData'] =
        'The Cloud Event data should have a JSON format.';
    }
  }

  return new FormValidationsImpl(validations);
}
