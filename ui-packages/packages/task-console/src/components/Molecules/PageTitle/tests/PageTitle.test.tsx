/**
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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
import React from 'react';
import PageTitle from '../PageTitle';
import { getWrapper } from '@kogito-apps/common';
import { Label } from '@patternfly/react-core';

describe('PageTitle test', () => {
  it('default snapshot testing', () => {
    const wrapper = getWrapper(<PageTitle title="Title" />, 'PageTitle');

    expect(wrapper).toMatchSnapshot();
  });

  it('snapshot testing with extra', () => {
    const wrapper = getWrapper(
      <PageTitle title="Title" extra={<Label>Label</Label>} />,
      'PageTitle'
    );

    expect(wrapper).toMatchSnapshot();

    const extra = wrapper.find(Label);

    expect(extra.exists()).toBeTruthy();
  });
});
