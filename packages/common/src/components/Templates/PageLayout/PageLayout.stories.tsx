import React from 'react';
import PageLayout from './PageLayout';
import { action } from '@storybook/addon-actions';
import { PageSection, Nav, NavList, NavItem } from '@patternfly/react-core';
import managementConsoleLogo from '../../../static/managementConsoleLogo.svg'

export default {
    title: 'Page layout',
};

export const defaultView = () => {

    const PageNav = (
        <Nav aria-label="Nav" theme="dark">
            <NavList>
                <NavItem onClick={action('button-click')} >
                   Process Instances
                </NavItem>
                <NavItem onClick={action('button-click')}>
                    Domain Explorer
                </NavItem>
            </NavList>
        </Nav>
    );
    return (
        <div style={{ height: '100vh' }}>
            <PageLayout
                PageNav={PageNav}
                BrandSrc={managementConsoleLogo}
                BrandAltText="Management Console Logo"
                BrandClick={action('button-click')}>
                <PageSection variant="light" />
            </PageLayout>
        </div>
    )
}