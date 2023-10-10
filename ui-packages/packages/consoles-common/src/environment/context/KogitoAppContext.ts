import React, { useContext } from 'react';
import { User, UserContext } from '../auth';

export interface AppContext {
  getCurrentUser(): User;
  readonly userContext: UserContext;
}

export class AppContextImpl implements AppContext {
  public readonly userContext: UserContext;

  constructor(userSystem: UserContext) {
    this.userContext = userSystem;
  }

  getCurrentUser(): User {
    return this.userContext.getCurrentUser();
  }
}

const KogitoAppContext = React.createContext<AppContext>(null);

export default KogitoAppContext;

export const useKogitoAppContext = () =>
  useContext<AppContext>(KogitoAppContext);
