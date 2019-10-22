import React, { useState } from 'react';
import { Page, PageSection, Grid, GridItem } from '@patternfly/react-core';
import HeaderComponent from '../PageHeaderComponent/HeaderComponent';
import { useQuery } from '@apollo/react-hooks';
import gql from 'graphql-tag';
import { withApollo } from 'react-apollo';
import './ProcessDetailsPage.css';
import ProcessDetailsProcessDiagram from '../ProcessDetailsProcessDiagram/ProcessDetailsProcessDiagram';
import ProcessDetails from '../ProcessDetails/ProcessDetails';
import ProcessDetailsProcessVariables from '../ProcessDetailsProcessVariables/ProcessDetailsProcessVariables';
import ProcessDetailsTimeline from '../ProcessDetailsTimeline/ProcessDetailsTimeline';

const ProcessDetailsPage = ({ match }) => {
  const id = match.params['instanceID'];
  const GET_QUERY = gql`
    query getQuery($id: String!) {
      ProcessId(filter: { id: $id }) {
        id
        processId
        parentProcessInstanceId
        roles
        variables
        state
        nodes {
          id
          name
          type
          enter
          exit
        }
      }
    }
  `;
  const { data, loading, error } = useQuery(GET_QUERY, {
    variables: { id }
  });
  
  const PageSectionstyle = {
    height: '100%'
  };

  {
    if (loading) return <p>Loading..</p>;
  }
  return (
    <>
      <Page>
        <PageSection style={PageSectionstyle}>
          <Grid>
            <GridItem span={8}>
              <ProcessDetailsProcessDiagram />
            </GridItem>
            <GridItem span={4}>
              <ProcessDetails loading={loading} data={data} />
            </GridItem>
            <GridItem span={8}>
              <ProcessDetailsProcessVariables loading={loading} data={data} />
            </GridItem>
            <GridItem span={4} rowSpan={3}>
              <ProcessDetailsTimeline loading={loading} data={data.ProcessId} />
            </GridItem>
          </Grid>
        </PageSection>
      </Page>
    </>
  );
};

export default withApollo(ProcessDetailsPage as any);
