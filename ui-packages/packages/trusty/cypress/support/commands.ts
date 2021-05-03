/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

declare namespace Cypress {
  interface Chainable {
    /**
     * Returns DOM Element for button which expands/collapses menu.
     */
    menuButton(): Chainable<JQuery<HTMLBodyElement>>;

    /**
     * Return DOM Element for item which opens audit investigation.
     */
    auditInvestigationItem(): Chainable<JQuery<HTMLBodyElement>>;

    /**
     * Return DOM Element for input for searching by ID.
     */
    searchInput(): Chainable<JQuery<HTMLBodyElement>>;

    /**
     * Return DOM Element for search button.
     */

    searchButton(): Chainable<JQuery<HTMLBodyElement>>;

    /**
     * Return DOM Element for "from date" input.
     */

    fromInput(): Chainable<JQuery<HTMLBodyElement>>;

    /**
     * Return DOM Element for "to date" input.
     */
    toInput(): Chainable<JQuery<HTMLBodyElement>>;

    /**
     * Return DOM Element for refresh button.
     */

    refreshButton(): Chainable<JQuery<HTMLBodyElement>>;

    /**
     * Return DOM Element for table page top.
     */
    paginationTop(): Chainable<JQuery<HTMLBodyElement>>;

    /**
     * Return DOM Element for table page bottom.
     */
    paginationBottom(): Chainable<JQuery<HTMLBodyElement>>;

    /**
     * Return DOM Element for table which represents page content.
     */
    pageContent(): Chainable<JQuery<HTMLBodyElement>>;
  }
}

Cypress.Commands.add('menuButton', () => {
  return cy.get('button#nav-toggle');
});

Cypress.Commands.add('auditInvestigationItem', () => {
  return cy.get("[data-ouia-component-id='auditItem']");
});

Cypress.Commands.add('searchInput', () => {
  return cy.get("[data-ouia-component-id='searchInput']");
});

Cypress.Commands.add('searchButton', () => {
  return cy.get("[data-ouia-component-id='searchButton']");
});

Cypress.Commands.add('fromInput', () => {
  return cy.get('div.flatpickr-wrapper>input#audit-from-date+input');
});

Cypress.Commands.add('toInput', () => {
  return cy.get('div.flatpickr-wrapper>input#audit-to-date+input');
});

Cypress.Commands.add('refreshButton', () => {
  return cy.get("[data-ouia-component-id='refreshButton']");
});

Cypress.Commands.add('paginationTop', () => {
  return cy.get("[data-ouia-component-id='OUIA-Generated-Pagination-top-1']");
});

Cypress.Commands.add('paginationBottom', () => {
  return cy.get(
    "[data-ouia-component-id='OUIA-Generated-Pagination-bottom-1']"
  );
});

Cypress.Commands.add('pageContent', () => {
  return cy.get("[data-ouia-component-id='execTable']");
});
