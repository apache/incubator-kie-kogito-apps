import React,{useState} from 'react';

import {
    PageSection,
    Bullseye,
    EmptyState,
    EmptyStateIcon,
    EmptyStateVariant,
    Button,
    EmptyStateBody,
    Title,
    Popover,
    ClipboardCopy,
    ClipboardCopyVariant
} from '@patternfly/react-core';
import {
    ExclamationCircleIcon
} from '@patternfly/react-icons';
import './ServerErrorsComponent.css';
import {useHistory} from 'react-router-dom';

const ServerErrorsComponent = (props) => {
    const [displayError, setDisplayError] = useState(false)
    const history = useHistory();
    
    return (
        <PageSection variant="light">
            <Bullseye>
                <EmptyState variant={EmptyStateVariant.full}>
                    <EmptyStateIcon
                        icon={ExclamationCircleIcon}
                        size="md"
                        color="var(--pf-global--danger-color--100)" />
                    <Title headingLevel="h1" size="4xl">Error fetching data</Title>
                    <EmptyStateBody>
                        An error occured while accessing data. <strong className="kogito-management-console--Server-Errors__text-color" onClick={() => setDisplayError(!displayError)}> See more details</strong>
                    </EmptyStateBody>
                    {displayError &&<EmptyStateBody>
                        <ClipboardCopy isCode variant={ClipboardCopyVariant.expansion} isExpanded={true}>{JSON.stringify(props.message)}</ClipboardCopy>             
                    </EmptyStateBody>}
                    <Button variant="primary" onClick={() => history.goBack()}>
                        Go back
              </Button>
                </EmptyState>
            </Bullseye>

        </PageSection>
    )
}

export default ServerErrorsComponent;