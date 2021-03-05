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
import axios from 'axios';
import { InfoCircleIcon } from '@patternfly/react-icons';
import { Title, TitleSizes } from '@patternfly/react-core';

export const setTitle = (
  titleStatus: string,
  titleText: string
): JSX.Element => {
  let icon = null;

  switch (titleStatus) {
    case 'success':
      icon = (
        <InfoCircleIcon
          className="pf-u-mr-sm"
          color="var(--pf-global--info-color--100)"
        />
      );
      break;
    case 'failure':
      icon = (
        <InfoCircleIcon
          className="pf-u-mr-sm"
          color="var(--pf-global--danger-color--100)"
        />
      );
      break;
  }

  return (
    <Title headingLevel="h1" size={TitleSizes['2xl']}>
      {icon}
      <span>{titleText}</span>
    </Title>
  );
};

export const handleJobReschedule = async (
  job,
  repeatInterval: number | string,
  repeatLimit: number | string,
  rescheduleClicked: boolean,
  setErrorMessage: (errorMessage: string) => void,
  setRescheduleClicked: (rescheduleClicked: boolean) => void,
  scheduleDate: Date,
  doQuery
): Promise<void> => {
  let parameter = {};
  if (repeatInterval === null && repeatLimit === null) {
    parameter = {
      expirationTime: new Date(scheduleDate)
    };
  } else {
    parameter = {
      expirationTime: new Date(scheduleDate),
      repeatInterval,
      repeatLimit
    };
  }
  try {
    await axios.patch(`${job.endpoint}/${job.id}`, parameter).then(res => {
      setRescheduleClicked(!rescheduleClicked);
      doQuery(0, 10);
    });
  } catch (error) {
    setRescheduleClicked(!rescheduleClicked);
    setErrorMessage(error.message);
    doQuery(0, 10);
  }
};

// function adds new property to existing object
export const constructObject = (obj, path, val) => {
  const keys = path.split(',');
  const lastKey = keys.pop();
  // tslint:disable-next-line: no-shadowed-variable
  const lastObj = keys.reduce(
    // tslint:disable-next-line: no-shadowed-variable
    (_obj, key) => (_obj[key] = obj[key] || {}),
    obj
  );
  lastObj[lastKey] = val;
};
