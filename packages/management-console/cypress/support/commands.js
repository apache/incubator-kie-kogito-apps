// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })

Cypress.Commands.add("byOuiaType", { prevSubject: 'optional' }, (subject, type, criteria = '', options = {}) => {
    if (subject) {
        cy.wrap(subject).get('*[data-ouia-component-type="' + type + '"]' + ((criteria) ? ':' + criteria : ''), options)
    } else {
        cy.get('*[data-ouia-component-type="' + type + '"]' + ((criteria) ? ':' + criteria : ''), options)
    }
})


Cypress.Commands.add("byOuiaId", { prevSubject: 'optional' }, (subject, id, criteria = '', options = {}) => {
    if (subject) {
        cy.wrap(subject).get('*[data-ouia-component-id="' + id + '"]' + ((criteria) ? ':' + criteria : ''), options)
    } else {
        cy.get('*[data-ouia-component-id="' + id + '"]' + ((criteria) ? ':' + criteria : ''), options)
    }
})