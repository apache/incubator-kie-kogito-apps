describe('Outcomes - verify mocked data', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('Simple outcome', () => {
    cy.ouiaId('exec-table', 'PF4/Table')
      .ouiaId('ac6d2f5f-4eba-4557-9d78-22b1661a876a', 'PF4/TableRow', {
        timeout: 20000
      })
      .ouiaId('show-detail', 'link')
      .click();
    cy.ouiaId('outcomes-gallery', 'outcomes').within(() => {
      cy.ouiaId('Mortgage Approval', 'PF4/Card').within(() => {
        cy.ouiaId('card-title', 'title').should(
          'has.text',
          'Mortgage Approval'
        );
        cy.ouiaId('Mortgage Approval', 'simple-property-value').should(
          'has.text',
          'Null'
        );
        cy.ouiaId('view-detail', 'PF4/Button').should('be.visible');
      });
      cy.ouiaId('Risk Score', 'PF4/Card').within(() => {
        cy.ouiaId('card-title', 'title').should('has.text', 'Risk Score');
        cy.ouiaId('Risk Score', 'simple-property-value').should(
          'has.text',
          '21.7031851958099'
        );
        cy.ouiaId('view-detail', 'PF4/Button').should('be.visible');
      });
    });
  });
});
