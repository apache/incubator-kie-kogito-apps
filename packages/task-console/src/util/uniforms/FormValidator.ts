import Ajv, { ValidateFunction } from 'ajv';

/**
 * Defines a basic Form Validator
 *
 * @interface
 */
export interface FormValidator {
  /**
   * Validates the given model
   * @param model The model to validate
   * @throws Should throw an error containing the validation errors
   */
  validate(model: any);
}

/**
 * Implementation of a validator using AJV
 */
export class DefaultFormValidator implements FormValidator {
  readonly schema: any;
  readonly validator: ValidateFunction;

  constructor(schema: any) {
    this.schema = schema;

    this.validator = new Ajv({ allErrors: true, useDefaults: true }).compile(
      schema
    );
  }

  validate(model: any) {
    this.validator(model);

    if (this.validator.errors && this.validator.errors.length) {
      throw { details: this.validator.errors };
    }
  }
}
