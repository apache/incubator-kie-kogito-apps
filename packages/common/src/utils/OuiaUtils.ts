import { ReactElement } from 'react';
import { mount, ReactWrapper } from 'enzyme';
import wait from 'waait';
import { act } from 'react-dom/test-utils';

/**
 * Wrapper used in snapshot testing to get rid of unnecessary wrappers of components.
 * Not only OUIA wrappers, but also Routers, MockedProvider,...
 * @param component The actual component with wrappers, e.g. <Router><MyGreatComponent /></Router>
 * @param name name of the component to be extracted as string, e.g. 'MyGreatComponent'
 */
export const getWrapperAsync = async (component: ReactElement, name: string): Promise<ReactWrapper> => {
  const wrapper = mount(component);
  await act(async () => {
    await wait(0)
  })
  const promise: Promise<ReactWrapper> = new Promise(resolve => { resolve(wrapper.update().find(name)) })
  return promise
}

/**
 * Wrapper used in snapshot testing to get rid of unnecessary wrappers of components.
 * Not only OUIA wrappers, but also Routers, MockedProvider,...
 * @param component The actual component with wrappers, e.g. <Router><MyGreatComponent /></Router>
 * @param name name of the component to be extracted as string, e.g. 'MyGreatComponent'
 */
export const getWrapper = (component: ReactElement, name: string): ReactWrapper => {
  const wrapper = mount(component);
  return wrapper.update().find(name)
}

/**
 * Function to set OUIA:Page Page Type and Page Object Id attributes in page body.
 * @param ouiaContext OUIAContext of the component wrapped with patternfly's withOuiaContext function.
 * @param type string value to be set as Page Type
 * @param objectId string value to be set as Page Object Id
 */
export const ouiaPageTypeAndObjectId = (
  ouiaContext,
  type: string,
  objectId?: string
): (() => void) => {
  if (!ouiaContext.isOuia) return;
  document.body.setAttribute('data-ouia-page-type', type)
  if (objectId) document.body.setAttribute('data-ouia-page-object-id', objectId)
  return () => {
    document.body.removeAttribute('data-ouia-page-type')
    if (objectId) document.body.removeAttribute('data-ouia-page-object-id')
  }
};

/**
 * Function to set ouia attribute - only when OUIA is enabled.
 * Usage:
 * <div
 *   {...ouiaAttribute(ouiaContext,'name','value')}
 * @param ouiaContext OUIAContext of the component wrapped with patternfly's withOuiaContext function.
 * @param name name of the attribute
 * @param value value of the attribute
 */
export const ouiaAttribute = (ouiaContext, name: string, value: string) => {
  return (ouiaContext.isOuia && {[name]:value})
}

export const componentOuiaProps = (
  ouiaContext,
  ouiaId,
  ouiaType,
  isSafe?
) => {
  return ouiaContext.isOuia && {
    'data-ouia-component-type': ouiaType,
    'data-ouia-component-id': ouiaId || ouiaContext.ouiaId,
    'data-ouia-safe': (isSafe)?true:false
  }
};

export const attributeOuiaId = (
  ouiaContext,
  ouiaId:string
) => {
  return ouiaAttribute(ouiaContext, 'ouiaId', ouiaId)
}