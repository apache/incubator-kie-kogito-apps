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
    EmptyStateSecondaryActions,
    Card,
    CardBody
} from '@patternfly/react-core';
import { CubesIcon } from '@patternfly/react-icons';
import { Link } from 'react-router-dom';
import { useGetQueryFieldsQuery } from '../../../graphql/types';

const DomainExplorer = () => {
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
        <Card>
            <CardBody>
                <EmptyState variant={EmptyStateVariant.full}>
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
            </CardBody>
        </Card>
    )
}

export default DomainExplorer;