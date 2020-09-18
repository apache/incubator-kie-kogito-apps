/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React, { useContext } from 'react';
import { User, UserSystem } from '../auth/Auth';

export interface AppContext {
  getCurrentUser(): User;
  readonly userSystem: UserSystem;
  readonly environment: Environment;
}

export class AppContextImpl implements AppContext {
  public readonly userSystem: UserSystem;
  public readonly environment: Environment;

  constructor(userSystem: UserSystem, environment: Environment) {
    this.userSystem = userSystem;
    this.environment = environment;
  }

  getCurrentUser(): User {
    return this.userSystem.getCurrentUser();
  }
}

export enum EnvironmentMode {
  TEST,
  PROD
}

export interface Environment {
  mode: EnvironmentMode;
}

const KogitoAppContext = React.createContext<AppContext>(null);

export default KogitoAppContext;

export const useKogitoAppContext = () =>
  useContext<AppContext>(KogitoAppContext);

export const isContextInTestMode = (context: AppContext): boolean => {
  if (context && context.environment) {
    return context.environment.mode === EnvironmentMode.TEST;
  }
  return false;
};
