import React from 'react';
import ReactDOM from 'react-dom';
import BaseLayout from './components/BaseComponent/BaseLayout';
import '@patternfly/patternfly/patternfly.css';
import ApolloClient from 'apollo-boost';
import { ApolloProvider } from 'react-apollo';

const client = new ApolloClient({
  uri: 'http://localhost:4000/graphql'
});
ReactDOM.render(
  <ApolloProvider client={client}>
    <BaseLayout />
  </ApolloProvider>,
  document.getElementById('root') as HTMLElement
);
