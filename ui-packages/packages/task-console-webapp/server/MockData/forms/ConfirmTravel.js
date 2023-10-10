module.exports = ConfirmTravelForm = {
  $schema: 'https://json-schema.org/draft/2019-09/schema',
  type: 'object',
  properties: {
    flight: {
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
      },
      input: true
    },
    hotel: {
      type: 'object',
      properties: {
        name: {
          type: 'string'
        },
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
      },
      input: true,
      output: true
    }
  },
  phases: ['complete', 'release']
};
