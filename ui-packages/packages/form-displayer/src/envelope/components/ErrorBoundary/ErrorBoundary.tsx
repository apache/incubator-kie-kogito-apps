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
