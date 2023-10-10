import { SCHEMA_VERSION } from '@kogito-apps/components-common/dist/types';

export const ApplyForVisaForm = {
  $schema: SCHEMA_VERSION.DRAFT_2019_09,
  type: 'object',
  properties: {
    trip: {
      type: 'object',
      properties: {
        city: {
          type: 'string'
        },
        country: {
          type: 'string'
        },
        begin: {
          type: 'string',
          format: 'date-time'
        },
        end: {
          type: 'string',
          format: 'date-time'
        },
        visaRequired: {
          type: 'boolean'
        }
      },
      input: true
    },
    traveller: {
      type: 'object',
      properties: {
        firstName: { type: 'string' },
        lastName: { type: 'string' },
        email: { type: 'string', format: 'email' },
        nationality: { type: 'string' },
        address: {
          type: 'object',
          properties: {
            street: {
              type: 'string'
            },
            city: {
              type: 'string'
            },
            zipCode: {
              type: 'string'
            },
            country: {
              type: 'string'
            }
          }
        }
      },
      required: ['firstName', 'lastName'],
      input: true,
      output: true
    },
    visaApplication: {
      type: 'object',
      properties: {
        firstName: { type: 'string' },
        lastName: { type: 'string' },
        city: {
          type: 'string'
        },
        country: {
          type: 'string'
        },
        duration: {
          type: 'integer'
        },
        passportNumber: { type: 'string' },
        nationality: { type: 'string' }
      },
      input: true
    }
  },
  phases: ['complete', 'release']
};
