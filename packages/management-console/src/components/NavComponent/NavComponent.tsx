import React, {useState} from 'react';
import { Nav, NavItem, NavList } from '@patternfly/react-core';
export interface IOwnProps {}
export interface IStateProps {
  activeItem: number;
}

const NavComponent: React.FunctionComponent<IOwnProps> = (props) => {
  const [activeItem, setActiveItem] = useState(0);
  const onNavSelect = result => {
    setActiveItem(result.itemId);
  };
    const navItems = ['Services', 'Policy', 'Authentication', 'Network Services', 'Server'];
    return (
      <Nav onSelect={onNavSelect} aria-label="Nav">
        <NavList>
          {navItems.map((navItem, index) => (
            <NavItem itemId={index} isActive={activeItem === index}>
              {navItem}
            </NavItem>
          ))}
        </NavList>
      </Nav>
    );
}

export default NavComponent;