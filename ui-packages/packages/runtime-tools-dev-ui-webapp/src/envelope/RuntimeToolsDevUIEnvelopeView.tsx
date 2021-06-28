/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import * as React from 'react';
import '@patternfly/patternfly/patternfly.css';
import { RuntimeToolsDevUIEnvelopeViewApi } from './RuntimeToolsDevUIEnvelopeViewApi';
import { useImperativeHandle } from 'react';
import RuntimeTools from '../components/console/RuntimeTools/RuntimeTools';
import { UserContext } from '@kogito-apps/consoles-common';

export interface Props {
  userContext: UserContext;
}

export const RuntimeToolsDevUIEnvelopeView = React.forwardRef<
  RuntimeToolsDevUIEnvelopeViewApi,
  Props
>((props: Props, forwardingRef) => {
  const [dataIndex, setDataIndex] = React.useState('');
  // @ts-ignore
  const [users, setUsers] = React.useState([]);

  //const history = useHistory();

  useImperativeHandle(
    forwardingRef,
    () => {
      return {
        setDataIndexUrl: dataIndexUrl => {
          setDataIndex(dataIndexUrl);
        },
        setUsers: users => {
          console.log('setUsers: ' + users);
          setUsers(users);
        },
        navigateTo: page => {
          console.log('navigateTo: ' + page);
          //history.push('/' + page);
        }
      };
    },
    []
  );

  return (
    <>
      {dataIndex.length > 0 && (
        <RuntimeTools userContext={props.userContext} dataIndex={dataIndex} />
      )}
    </>
  );
});
