import React from 'react';
import { mount } from 'enzyme';
import { Helmet } from 'react-helmet';
import ResourcesContainer from '../ResourcesContainer';
import { FormResources } from '../../../../api';

const getWrapper = (resources: FormResources) => {
  return mount(<ResourcesContainer resources={resources} />);
};

describe('ResourcesContainer tests', () => {
  it('Resources Rendering', () => {
    const resoures: FormResources = {
      styles: {
        style1: 'style1-url',
        style2: 'style2-url'
      },
      scripts: {
        script1: 'script1-url',
        script2: 'script2-url'
      }
    };

    const wrapper = getWrapper(resoures);

    expect(wrapper).toMatchSnapshot();

    const helmet = wrapper.find(Helmet);

    expect(helmet.exists()).toBeTruthy();

    const sideEffect = helmet.childAt(0);

    expect(sideEffect.exists()).toBeTruthy();

    const links = sideEffect.props().link;
    expect(links).toHaveLength(2);

    const link1 = links[0];
    expect(link1.href).toStrictEqual('style1-url');
    expect(link1.rel).toStrictEqual('stylesheet');

    const link2 = links[1];
    expect(link2.href).toStrictEqual('style2-url');
    expect(link2.rel).toStrictEqual('stylesheet');

    const scripts = sideEffect.props().script;
    expect(scripts).toHaveLength(2);

    const script1 = scripts[0];
    expect(script1.src).toStrictEqual('script1-url');

    const script2 = scripts[1];
    expect(script2.src).toStrictEqual('script2-url');
  });

  it('Empty Resources Rendering', () => {
    const resoures: FormResources = {
      styles: {},
      scripts: {}
    };

    const wrapper = getWrapper(resoures);

    expect(wrapper).toMatchSnapshot();

    const helmet = wrapper.find(Helmet);

    expect(helmet.exists()).toBeTruthy();

    const sideEffect = helmet.childAt(0);

    expect(sideEffect.exists()).toBeTruthy();

    expect(sideEffect.props().link).toBeUndefined();

    expect(sideEffect.props().script).toBeUndefined();
  });
});
