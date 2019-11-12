import React from 'react';
import {
    Title,
    Button,
    EmptyState,
    EmptyStateVariant,
    EmptyStateIcon,
    EmptyStateBody,
    EmptyStateSecondaryActions,
    Bullseye
} from '@patternfly/react-core';
import { SearchIcon } from '@patternfly/react-icons';

interface IOwnProps { }
const NoMatchEmptyState: React.FC<IOwnProps> = () => {
    return (
        <Bullseye>
            <EmptyState>
                <EmptyStateIcon icon={SearchIcon} />
                <Title size="lg">No results found</Title>
            </EmptyState>
        </Bullseye>
    );
};

export default NoMatchEmptyState;
