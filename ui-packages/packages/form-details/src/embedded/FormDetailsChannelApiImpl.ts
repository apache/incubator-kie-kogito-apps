/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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

import { FormDetailsDriver, FormDetailsChannelApi } from '../api';

/**
 * Implementation of the TaskInboxChannelApi delegating to a TaskInboxDriver
 */
export class FormDetailsChannelApiImpl implements FormDetailsChannelApi {
  // @ts-ignore
  constructor(private readonly driver: FormDetailsDriver) {}

  formDetails__getFormContent(formName: string): Promise<any[]> {
    return this.driver.getFormContent(formName);
  }
}
