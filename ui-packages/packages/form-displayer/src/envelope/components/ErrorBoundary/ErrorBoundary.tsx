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

import React, { ErrorInfo } from 'react';
import FormErrorsWrapper from '../FormErrorsWrapper/FormErrorsWrapper';
import { FormOpened, FormOpenedState } from '../../../api';

interface ErrorBoundaryProps {
  children: React.ReactElement;
  notifyOnError: (opened: FormOpened) => void;
}

interface ErrorBoundaryState {
  error: any;
  errorInfo: any;
}

class ErrorBoundary extends React.Component<
  ErrorBoundaryProps,
  ErrorBoundaryState
> {
  constructor(props) {
    super(props);
    this.state = { error: undefined, errorInfo: undefined };
  }

  componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    this.setState({
      error: error,
      errorInfo: errorInfo
    });
  }
  render() {
    if (this.state.error) {
      setTimeout(() => {
        this.props.notifyOnError({
          state: FormOpenedState.ERROR,
          size: {
            height: document.body.scrollHeight,
            width: document.body.scrollWidth
          }
        });
      }, 500);
      return <FormErrorsWrapper error={this.state.error} />;
    }
    return this.props.children;
  }
}

export default ErrorBoundary;
