import React from 'react';
import {mount} from 'enzyme';
import ServerErrors from '../ServerErrors';
import { MemoryRouter as Router } from 'react-router-dom';

describe('Server errors component tests', () => {
    it('Snapshot testing', () => {
        const wrapper = mount(
            <Router keyLength={0}>
                <ServerErrors />
            </Router>);
        wrapper.find('#More-details').first().simulate('click');
        wrapper.find('#GoBack-Button').first().simulate('click');
        expect(wrapper).toMatchSnapshot();
    })
})