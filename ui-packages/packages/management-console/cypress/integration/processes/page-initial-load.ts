describe('Process List Page test', () => {
  beforeEach('visit page', () => {
    cy.visit('http://localhost:9000/ProcessInstances');
  });
  describe('Check page structure', () => {
    it('Check page attributes', () => {
      cy.get("[data-ouia-page-type='process-instances']")
        .should('exist')
        .and('not.have.attr', 'data-ouia-page-object-id');
    });
    it('Check header', () => {
      cy.get('[data-ouia-header=true]')
        .should('exist')
        .within($header => {
          // eslint-disable-next-line cypress/require-data-selectors
          cy.get('img')
            .should('have.attr', 'alt')
            .should('contains', 'Management Console');
          cy.ouiaType('page-toolbar').should('exist');
        });
    });
    it('Check navigation panel', () => {
      cy.get('[data-ouia-navigation=true]')
        .ouiaType('PF4/Nav')
        .ouiaId('navigation-list')
        .should('exist')
        .within($navigation => {
          cy.ouiaNavigationName()
            .should('not.be.empty')
            .and('have.length', 3);
          cy.ouiaNavigationName('process-instances')
            .should('exist')
            .should('have.attr', 'class')
            .should('contains', 'current');
          cy.ouiaNavigationName('domain-explorer')
            .should('exist')
            .should('have.attr', 'class')
            .should('not.contains', 'current');
          cy.ouiaNavigationName('jobs-management')
            .should('exist')
            .should('have.attr', 'class')
            .should('not.contains', 'current');
        });
    });
    it('Check main content', () => {
      cy.get('[data-ouia-main=true]')
        .should('exist')
        .ouiaType('process-list-page')
        .should('exist')
        .within($page => {
          cy.ouiaType('page-title')
            .should('exist')
            .and('contain.text', 'Process Instances');
          cy.ouiaType('PF4/Breadcrumb')
            .should('exist')
            .within($nav => {
              // eslint-disable-next-line cypress/require-data-selectors
              cy.get('li')
                .should('have.length', 2)
                .within($items => {
                  expect($items.eq(0)).to.contain.text('Home');
                  expect($items.eq(1)).to.contain.text('Process instances');
                });
            });
          cy.ouiaType('process-list-toolbar').should('be.visible');
          cy.ouiaType('process-list-table').should('be.visible');
          cy.ouiaType('load-more')
            .scrollIntoView()
            .should('be.visible');
        });
    });
  });
  describe('Data presentation', () => {
    it('Table Layout', () => {
      cy.ouiaType('process-list-page').within($page => {
        cy.ouiaType('process-list-table')
          .ouiaSafe()
          .ouiaType('process-list-row')
          .should('have.length', 10)
          .ouiaId('8035b580-6ae4-4aa8-9ec0-e18e19809e0b1')
          .within($item => {
            cy.ouiaType('process-list-cell').then($cells => {
              cy.ouiaId('__toggle').should('be.visible');
              cy.ouiaId('__select')
                .should('be.visible')
                .find('input')
                .should('be.enabled');
              cy.ouiaId('id').should('be.visible');
              cy.wrap($cells)
                .ouiaId('status')
                .should('be.visible')
                .and('contain.text', 'Active');
              cy.wrap($cells)
                .ouiaId('created')
                .should('be.visible');
              cy.wrap($cells)
                .ouiaId('last update')
                .should('be.visible');
              cy.wrap($cells)
                .ouiaId('__actions')
                .should('be.visible');
            });
          });
      });
    });
    it('Process-list-item expanded.', () => {
      cy.ouiaType('process-list-page').within($page => {
        cy.ouiaType('load-more')
          .scrollIntoView()
          .should('be.visible')
          .ouiaType('PF4/Dropdown')
          .click();
        cy.ouiaType('process-list-table')
          .ouiaSafe()
          .within($table => {
            cy.ouiaId(
              '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
              'process-list-row-expanded'
            )
              .scrollIntoView()
              .should('not.be.visible');
            cy.ouiaId(
              '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
              'process-list-row'
            )
              .scrollIntoView()
              .should('be.visible')
              .within($row => {
                cy.ouiaId('__toggle', 'process-list-cell')
                  .scrollIntoView()
                  .should('be.visible')
                  .ouiaType('PF4/Button')
                  .click();
              });
            cy.ouiaId(
              '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
              'process-list-row-expanded'
            )
              .scrollIntoView()
              .should('be.visible')
              .within($expanded => {
                // 1 header and 4 items
                cy.ouiaType('PF4/TableRow').should('have.length', 5);
              });
          });
      });
    });
    it('Load More', () => {
      cy.ouiaType('process-list-page').within($page => {
        cy.ouiaType('process-list-table')
          .ouiaSafe()
          .ouiaType('process-list-row')
          .should('have.length', 10);
        cy.ouiaType('load-more')
          .scrollIntoView()
          .should('be.visible')
          .ouiaType('PF4/Dropdown')
          .click();
        cy.ouiaType('process-list-table')
          .ouiaType('process-list-row')
          .should('have.length', 14);
      });
    });
  });
});
