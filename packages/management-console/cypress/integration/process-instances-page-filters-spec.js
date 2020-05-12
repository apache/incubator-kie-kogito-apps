/// <reference types="Cypress" />
beforeEach(() => {
  cy.visit('/ProcessInstances')

  cy.byOuiaType('empty-state', 'contains("Loading process instances")', { timeout: 5000 }).should('not.be.visible')
  // Remove any filters
  cy.byOuiaType('data-toolbar').within(() => {
    cy.byOuiaType('toggle-filter').find('button').then(($button) => {
      if ($button.is(':visible')) {
        $button.click()
      }
    })
    cy.get('button[aria-label="close"]').each(($button) => {
      cy.wrap($button).click()
    })
  });
  cy.byOuiaType('empty-state').should('contain.text', 'No status is selected')
})

describe('Process Instances filters test', function () {
  it('Reset to default', () => {
    cy.byOuiaId('empty-state-no-filters-selected').find('button').should('be.visible').should('contain.text', 'Reset to default').click();
    cy.byOuiaType('empty-state').find('h5').should('not.contain.text', 'No status is selected')
    cy.byOuiaType('empty-state', 'contains("Loading process instances")', { timeout: 5000 }).should('not.be.visible') // wait for loading to disappear
    cy.byOuiaId('filter-process-status').click().byOuiaType('select-option').byOuiaId('active').find('input').should('be.checked')
    cy.byOuiaId('list-process-instances').byOuiaType('data-list-item').should('have.length', 10)
  })
})
