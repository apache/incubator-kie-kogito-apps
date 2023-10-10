import { validateCloudEventRequest } from '../validateCloudEventRequest';
import { CloudEventMethod } from '../../../../api';

describe('validateCloudEventRequest tests', () => {
  it('Valid result', () => {
    const validation = validateCloudEventRequest({
      method: CloudEventMethod.POST,
      endpoint: '/',
      data: '{"name": "Jon Snow"}',
      headers: {
        type: 'any',
        source: 'any',
        extensions: {}
      }
    });

    expect(validation.isValid()).toBeTruthy();
  });

  it('Invalid result - endpoint', () => {
    const validation = validateCloudEventRequest({
      method: CloudEventMethod.POST,
      endpoint: '',
      data: '{"name": "Jon Snow"}',
      headers: {
        type: 'any',
        source: 'any',
        extensions: {}
      }
    });

    expect(validation.isValid()).toBeFalsy();
    expect(validation.getFieldValidation('endpoint')).not.toBeUndefined();
    expect(validation.getFieldValidation('eventType')).toBeUndefined();
    expect(validation.getFieldValidation('eventData')).toBeUndefined();
  });

  it('Invalid result - event type', () => {
    const validation = validateCloudEventRequest({
      method: CloudEventMethod.POST,
      endpoint: '/',
      data: '{"name": "Jon Snow"}',
      headers: {
        type: '',
        source: 'any',
        extensions: {}
      }
    });

    expect(validation.isValid()).toBeFalsy();
    expect(validation.getFieldValidation('endpoint')).toBeUndefined();
    expect(validation.getFieldValidation('eventType')).not.toBeUndefined();
    expect(validation.getFieldValidation('eventData')).toBeUndefined();
  });

  it('Invalid result - event data', () => {
    const eventRequest = {
      method: CloudEventMethod.POST,
      endpoint: '/',
      data: 'this should break because is not a json string',
      headers: {
        type: 'any',
        source: 'any',
        extensions: {}
      }
    };

    let validation = validateCloudEventRequest(eventRequest);

    expect(validation.isValid()).toBeFalsy();
    expect(validation.getFieldValidation('endpoint')).toBeUndefined();
    expect(validation.getFieldValidation('eventType')).toBeUndefined();
    expect(validation.getFieldValidation('eventData')).not.toBeUndefined();

    eventRequest.data =
      '"a string is a valid json value but not a valid payload"';

    validation = validateCloudEventRequest(eventRequest);

    expect(validation.isValid()).toBeFalsy();
    expect(validation.getFieldValidation('endpoint')).toBeUndefined();
    expect(validation.getFieldValidation('eventType')).toBeUndefined();
    expect(validation.getFieldValidation('eventData')).not.toBeUndefined();
  });
});
