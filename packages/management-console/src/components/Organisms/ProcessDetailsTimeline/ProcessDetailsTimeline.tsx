import Moment from 'react-moment';
import {
  Card,
  CardBody,
  CardHeader,
  Title,
  Bullseye,
  Text,
  TextContent,
  TextVariants,
  Split,
  SplitItem,
  Stack,
  Dropdown,
  KebabToggle,
  DropdownItem,
  Tooltip
} from '@patternfly/react-core';
import {
  ServicesIcon,
  UserIcon,
  CheckCircleIcon,
  ErrorCircleOIcon,
  OnRunningIcon
} from '@patternfly/react-icons';
import React from 'react';
import './ProcessDetailsTimeline.css';
import SpinnerComponent from '../../Atoms/SpinnerComponent/SpinnerComponent';

export interface IOwnProps {
  loading: boolean;
  data: any;
}

// TODO: make the kebab menu for each timeline item work!
// const { isOpen } = this.state;
const dropdownItems = [
  <DropdownItem key="retry" component="button">
    Retry
  </DropdownItem>,
  <DropdownItem key="skip" component="button">
    Skip
  </DropdownItem>
];

const ProcessDetailsTimeline: React.FC<IOwnProps> = ({ loading, data }) => {
  return (
    <Card>
      <CardHeader>
        <Title headingLevel="h3" size="xl">
          Timeline
        </Title>
      </CardHeader>
      <CardBody>
        <Stack gutter="md" className="kogito-management-console--timeline">
          {!loading ? (
            data[0].nodes.map(content => {
              return (
                <Split
                  gutter={'sm'}
                  className={'kogito-management-console--timeline-item'}
                  key={content.id}
                >
                  <SplitItem>
                    {
                      <>
                        {/* TODO: put the correct icon in depending on the state */}
                        {/* <Tooltip content={'Active'}>
                            <OnRunningIcon className="kogito-management-console--timeline-status" />{' '}
                          </Tooltip> */}
                        <Tooltip content={'Completed'}>
                          <CheckCircleIcon
                            color="var(--pf-global--success-color--100)"
                            className="kogito-management-console--timeline-status"
                          />
                        </Tooltip>
                        {/* <Tooltip content={'Error'}>
                            <ErrorCircleOIcon
                              color="var(--pf-global--danger-color--100)"
                              className="kogito-management-console--timeline-status"
                            />
                          </Tooltip> */}
                      </>
                    }
                  </SplitItem>
                  <SplitItem isFilled>
                    <TextContent>
                      <Text component={TextVariants.p}>
                        {content.name}
                        <span>
                          {content.type === 'HumanTaskNode' ? (
                            <Tooltip content={'Human task'}>
                              <UserIcon
                                className="pf-u-ml-sm"
                                color="var(--pf-global--icon--Color--light)"
                              />
                            </Tooltip>
                          ) : (
                            <Tooltip content={'Service'}>
                              <ServicesIcon
                                className="pf-u-ml-sm"
                                color="var(--pf-global--icon--Color--light)"
                              />
                            </Tooltip>
                          )}
                        </span>
                        <Text component={TextVariants.small}>
                          {content.exit === null ? (
                            'Active'
                          ) : (
                            <Moment fromNow>
                              {new Date(`${content.exit}`)}
                            </Moment>
                          )}
                        </Text>
                      </Text>
                    </TextContent>
                  </SplitItem>
                  <SplitItem>
                    {
                      <>
                        {/* TODO: Make the dropdown work, with contents depending on the state */}
                        <Dropdown
                          // onSelect={this.onSelect}
                          toggle={
                            <KebabToggle
                            // onToggle={this.onToggle}
                            // id="toggle-id-6"
                            />
                          }
                          // isOpen={isOpen}
                          isPlain
                          // dropdownItems={dropdownItems}
                        />
                      </>
                    }
                  </SplitItem>{' '}
                </Split>
              );
            })
          ) : (
            <Bullseye>
              <SpinnerComponent spinnerText="Loading timeline" />
            </Bullseye>
          )}
        </Stack>
      </CardBody>
    </Card>
  );
};

export default ProcessDetailsTimeline;
