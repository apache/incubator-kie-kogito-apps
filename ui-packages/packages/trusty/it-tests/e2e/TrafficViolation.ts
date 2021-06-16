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

describe('Traffic Violation', () => {
  before(() => {
    cy.request({
      method: 'POST',
      url: 'http://localhost:8080/Traffic Violation',
      headers: {
        'Content-Type': 'application/json'
      },
      body: {
        Driver: { State: 'aa', City: 'bb', Age: '25', Points: '13' },
        Violation: { Type: 'speed', 'Actual Speed': '105', 'Speed Limit': '80' }
      }
    }).then(response => {
      // response.body is automatically serialized into JSON
      expect(response.body).to.not.be.null;
      expect(response.body).to.have.property(
        'Should the driver be suspended?',
        'No'
      );
      expect(response.body).to.have.property('Fine');
      expect(response.body.Fine).to.have.property('Points', 3);
      expect(response.body.Fine).to.have.property('Amount', 500);
      /*expect(response.body.data).to.have.length.of.at.least(4);
      {
        Violation: {
          Type: 'speed',
          'Speed Limit': 80,
          'Actual Speed': 105,
          Code: null,
          Date: null
        },
        Driver: { Points: 13, State: 'aa', City: 'bb', Age: 25, Name: null },
        Fine: { Points: 3, Amount: 500 },
        'Should the driver be suspended?': 'No'
      });*/
    });
  });

  beforeEach(() => {
    cy.visit('/');
  });

  it('verify results', () => {
    cy.ouiaId('refresh-button').click();
    cy.ouiaId('exec-table')
      .find("td[data-label='ID']:first>a")
      .click();
    cy.get('section.outcomes').should('be.visible');

    cy.get('div.pf-c-card__header:contains(Fine)').should('be.visible');
    cy.get(
      'div.outcome__property:contains(Points)>div.outcome__property__value'
    ).should($value => {
      expect($value).to.have.text('3');
    });
    cy.get(
      'div.outcome__property:contains(Amount)>div.outcome__property__value'
    ).should($value => {
      expect($value).to.have.text('500');
    });

    cy.get('div.pf-c-card__header:contains(suspended)').should('be.visible');
    cy.get(
      'div.pf-c-card__header:contains(suspended)+div.pf-c-card__body'
    ).should($value => {
      expect($value).to.have.text('No');
    });
  });

  it('verify inputs - Violation', () => {
    cy.ouiaId('refresh-button').click();
    cy.ouiaId('exec-table')
      .find("td[data-label='ID']:first>a")
      .click();

    cy.get('ul.pf-c-nav__list>li:contains(Input)').click();
    cy.get('div.input-browser button:contains(Violation)').click();
    cy.get(
      'ul.input-browser__data-list>li:contains(Type) div.pf-c-data-list__cell:nth-child(2)'
    ).should($value => {
      expect($value).to.have.text('speed');
    });
    cy.get(
      "ul.input-browser__data-list>li:contains('Speed Limit') div.pf-c-data-list__cell:nth-child(2)"
    ).should($value => {
      expect($value).to.have.text('80');
    });

    cy.get(
      "ul.input-browser__data-list>li:contains('Actual Speed') div.pf-c-data-list__cell:nth-child(2)"
    ).should($value => {
      expect($value).to.have.text('105');
    });

    cy.get(
      'ul.input-browser__data-list>li:contains(Code) div.pf-c-data-list__cell:nth-child(2)'
    ).should($value => {
      expect($value).to.have.text('Null');
    });

    cy.get(
      'ul.input-browser__data-list>li:contains(Date) div.pf-c-data-list__cell:nth-child(2)'
    ).should($value => {
      expect($value).to.have.text('Null');
    });
  });

  it('verify inputs - Driver', () => {
    cy.ouiaId('refresh-button').click();
    cy.ouiaId('exec-table')
      .find("td[data-label='ID']:first>a")
      .click();

    cy.get('ul.pf-c-nav__list>li:contains(Input)').click();
    cy.get('div.input-browser button:contains(Driver)').click();
    cy.get(
      'ul.input-browser__data-list>li:contains(Points) div.pf-c-data-list__cell:nth-child(2)'
    ).should($value => {
      expect($value).to.have.text('13');
    });
    cy.get(
      'ul.input-browser__data-list>li:contains(State) div.pf-c-data-list__cell:nth-child(2)'
    ).should($value => {
      expect($value).to.have.text('aa');
    });

    cy.get(
      'ul.input-browser__data-list>li:contains(City) div.pf-c-data-list__cell:nth-child(2)'
    ).should($value => {
      expect($value).to.have.text('bb');
    });

    cy.get(
      'ul.input-browser__data-list>li:contains(Age) div.pf-c-data-list__cell:nth-child(2)'
    ).should($value => {
      expect($value).to.have.text('25');
    });

    cy.get(
      'ul.input-browser__data-list>li:contains(Name) div.pf-c-data-list__cell:nth-child(2)'
    ).should($value => {
      expect($value).to.have.text('Null');
    });
  });
});
