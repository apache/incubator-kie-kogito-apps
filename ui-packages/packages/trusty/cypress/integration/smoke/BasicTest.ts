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

describe('Basic Elements', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('Menu button is visible', () => {
    cy.menuButton().should('be.visible');
  });

  it('Menu button shows/hides options', () => {
    cy.auditInvestigationItem().then($item => {
      //toggle menu side bar - both directions
      if ($item.is(':visible')) {
        cy.menuButton().click();
        cy.auditInvestigationItem().should('not.be.visible');
        cy.menuButton().click();
        cy.auditInvestigationItem().should('be.visible');
      } else {
        cy.menuButton().click();
        cy.auditInvestigationItem().should('be.visible');
        cy.menuButton().click();
        cy.auditInvestigationItem().should('not.be.visible');
      }
    });
  });

  it('Search is visible', () => {
    cy.searchInput().should('be.visible');
    cy.searchInput().type('someId');
    cy.searchButton().should('be.visible');
  });

  it('Refresh is visible', () => {
    cy.refreshButton().should('be.visible');
  });

  it('Date inputs are visible', () => {
    cy.fromInput().should('be.visible');
    cy.toInput().should('be.visible');
  });

  it('Top paging is visible', () => {
    cy.paginationTop().should('be.visible');
  });

  it('Bottom paging is visible', () => {
    cy.get("main").scrollTo("bottom");
    cy.paginationBottom().should('be.visible');
  });

  it('Decision result is available', () => {
    cy.pageContent().find("tr>td>a:contains('1003')", { timeout: 5000 });
  });
});
