module.exports = ConfirmTravelFormDraft7 = {
  $schema: 'http://json-schema.org/draft-07/schema#',
  type: 'object',
  definitions: {
    Address: {
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
    },
    Flight: {
      type: 'object',
      properties: {
        flightNumber: {
          type: 'string'
        },
        seat: {
          type: 'string'
        },
        gate: {
          type: 'string'
        },
        departure: {
          type: 'string',
          format: 'date-time'
        },
        arrival: {
          type: 'string',
          format: 'date-time'
        }
      }
    },
    Hotel: {
      type: 'object',
      properties: {
        name: {
          type: 'string'
        },
        address: {
          $ref: '#/definitions/Address'
        },
        phone: {
          type: 'string'
        },
        bookingNumber: {
          type: 'string'
        },
        room: {
          type: 'string'
        }
      }
    }
  },
  properties: {
    flight: {
      allOf: [{ $ref: '#/definitions/Flight' }, { input: true }]
    },
    hotel: {
      allOf: [
        { $ref: '#/definitions/Hotel' },
        { input: true },
        { output: true }
      ]
    }
  },
  phases: ['complete', 'release']
};
