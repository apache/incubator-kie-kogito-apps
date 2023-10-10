describe('E2E - smoke test', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('menu button is visible', () => {
    cy.get('button#nav-toggle').should('be.visible');
  });

  it('Menu button shows/hides options', () => {
    cy.ouiaId('audit-item').then(($item) => {
      //toggle menu side bar - both directions
      if ($item.is(':visible')) {
        cy.get('button#nav-toggle').click();
        cy.ouiaId('audit-item').should('not.be.visible');
        cy.get('button#nav-toggle').click();
        cy.ouiaId('audit-item').should('be.visible');
      } else {
        cy.get('button#nav-toggle').click();
        cy.ouiaId('audit-item').should('be.visible');
        cy.get('button#nav-toggle').click();
        cy.ouiaId('audit-item').should('not.be.visible');
      }
    });
  });

  it('Search is visible', () => {
    cy.ouiaId('search-input').should('be.visible');
    cy.ouiaId('search-input').type('someId');
    cy.ouiaId('search-button').should('be.visible');
  });

  it('Refresh is visible', () => {
    cy.ouiaId('refresh-button').should('be.visible');
  });

  it('Date inputs are visible', () => {
    cy.get('#audit-from-date+input').should('be.visible');
    cy.get('#audit-to-date+input').should('be.visible');
  });

  it('Top paging is visible', () => {
    cy.ouiaId('top-pagination').should('be.visible');
  });
});
