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

import React from 'react';
import { render, screen } from '@testing-library/react';
import PageToolbar from '../PageToolbar';
import {
  resetTestKogitoAppContext,
  testHandleLogoutMock
} from '../../../../environment/auth/tests/utils/KogitoAppContextTestingUtils';

jest.mock('../../AboutModalBox/AboutModalBox');

describe('PageToolbar component tests', () => {
  beforeEach(() => {
    testHandleLogoutMock.mockClear();
    resetTestKogitoAppContext(false);
  });

  it('Snapshot testing - auth disabled', () => {
    render(<PageToolbar />);

    expect(screen).toMatchSnapshot();
  });

  it('Snapshot testing - auth enabled', () => {
    resetTestKogitoAppContext(true);
    render(<PageToolbar />);

    expect(screen).toMatchSnapshot();
  });

  it('Testing dropdown items - auth enabled', () => {
    resetTestKogitoAppContext(true);

    const { container } = render(<PageToolbar />);

    expect(screen).toMatchSnapshot();

    const dropdown = screen.getByTestId('pageToolbar-dropdown');

    const dropdownItems = container.querySelectorAll('span');

    expect(dropdownItems.length).toStrictEqual(3);
  });

  it('Testing logout - auth enabled', () => {
    resetTestKogitoAppContext(true);

    const { container } = render(<PageToolbar />);

    const dropdown = screen.getByTestId('pageToolbar-dropdown');

    const dropdownItems = container.querySelectorAll('span');

    expect(dropdownItems.length).toStrictEqual(3);

    expect(screen).toMatchSnapshot();
  });

  it('Testing dropdown items - auth disabled', () => {
    render(<PageToolbar />);

    expect(screen).toMatchSnapshot();

    const anonymousProfile = screen.getByText('Anonymous');

    expect(anonymousProfile).toBeTruthy();
  });
});
