import React from 'react';
import { actions } from '@storybook/addon-actions';
import AboutModelBox from './AboutModalBox';
import { aboutLogoContext } from '../../contexts';
import managementConsoleLogo from '../../../static/managementConsoleLogo.svg';

const eventsFromObject = actions({
    onClick: 'clicked',
    onMouseOver: 'hovered'
});

export default {
    title: 'About modal box',
    component: AboutModelBox
};

export const defaultView = () => (
    <aboutLogoContext.Provider value={managementConsoleLogo}>
        <AboutModelBox
            isOpenProp={true}
            handleModalToggleProp={() => null}
            {...eventsFromObject}
        />
    </aboutLogoContext.Provider>
)
