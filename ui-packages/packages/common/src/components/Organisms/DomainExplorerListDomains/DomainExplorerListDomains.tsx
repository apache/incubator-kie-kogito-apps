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
import {
  TextContent,
  Text,
  TextVariants,
  Title,
  Button,
  EmptyState,
  EmptyStateVariant,
  EmptyStateIcon,
  EmptyStateBody,
  EmptyStateSecondaryActions
} from '@patternfly/react-core';
import { CubesIcon } from '@patternfly/react-icons';
import { Link } from 'react-router-dom';
import { GraphQL } from '../../../graphql/types';
import useGetQueryFieldsQuery = GraphQL.useGetQueryFieldsQuery;
import { OUIAProps, componentOuiaProps } from '../../../utils/OuiaUtils';

const DomainExplorerListDomains: React.FC<OUIAProps> = ({
  ouiaId,
  ouiaSafe
}) => {
  const getQuery = useGetQueryFieldsQuery();
  let availableDomains =
    !getQuery.loading && getQuery.data.__type.fields.slice(2);

  availableDomains =
    availableDomains &&
    availableDomains.filter(item => {
      /* istanbul ignore else */
      if (item.name !== 'Jobs') {
        return item;
      }
    });
  return (
    <EmptyState
      variant={EmptyStateVariant.full}
      {...componentOuiaProps(ouiaId, 'domain-explorer-list-domains', ouiaSafe)}
    >
      <EmptyStateIcon icon={CubesIcon} />

      {availableDomains.length > 0 ? (
        <>
          <Title headingLevel="h5" size="lg">
            Domains List
          </Title>
          <EmptyStateBody>Select a domain below</EmptyStateBody>
          <EmptyStateSecondaryActions>
            {!getQuery.loading &&
              availableDomains.map((item, index) => {
                return (
                  <Link to={`/DomainExplorer/${item.name}`} key={index}>
                    <Button variant="link">{item.name}</Button>
                  </Link>
                );
              })}
          </EmptyStateSecondaryActions>
        </>
      ) : (
        <TextContent>
          <Text component={TextVariants.h2}>No domains available</Text>
        </TextContent>
      )}
    </EmptyState>
  );
};

export default DomainExplorerListDomains;
