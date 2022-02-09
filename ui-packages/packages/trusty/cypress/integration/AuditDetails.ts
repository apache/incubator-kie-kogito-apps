/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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

describe('AuditDetails - verify that ', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  [
    ['strings--4979-4e03-8692-0ec45cfca6ac', ['Outcomes', 'Outcomes details', 'Input data', 'Model lookup', 'Counterfactual analysis']],
    ['unsuppor-ted--all--stri-913ac955b90b', ['Outcome', 'Input data', 'Model lookup', 'Counterfactual analysis']]
  ].forEach(reqId => {
    it(`Audit Details header - ${reqId[0]}`, () => {
      cy.ouiaId('exec-table', 'PF4/Table')
        .ouiaId(`${reqId[0]}`, 'PF4/TableRow')
        .ouiaId('show-detail', 'link')
        .click();
      const title = 'Execution #' + String(reqId[0]).substring(0, 8);

      cy.ouiaType('PF4/Breadcrumb')
        .should('exist')
        .within($nav => {
          cy.wrap($nav)
            .find('li>a')
            .should('have.length', 3)
            .within($items => {
              expect($items.eq(0)).have.text('Audit investigation');
              expect($items.eq(1)).have.text(title);
              expect($items.eq(2)).have.text(reqId[1][0]);
            });
        });
      cy.ouiaId('execution-header').within(() => {
        cy.ouiaId('title')
          .should('be.visible')
          .should('have.text', title);
        cy.ouiaId('status')
          .should('be.visible')
          .should('have.text', 'Completed');
      });

      cy.ouiaId('nav-audit-detail')
      .should('exist')
      .within($nav => {
        cy.wrap($nav)
          .find('li>a')
          .should('have.length', reqId[1].length)
          .within($item => {
            for (let value = 0; value < reqId[1].length; value++) {
              expect($item.eq(value)).have.text(reqId[1][value]);
            }
          });
      });
      cy.get('section.outcomes').should('be.visible');
    });
  });
});
