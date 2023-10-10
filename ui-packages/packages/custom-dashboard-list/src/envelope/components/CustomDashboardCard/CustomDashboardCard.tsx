import React from 'react';
import {
  OUIAProps,
  componentOuiaProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import {
  TextVariants,
  Text
} from '@patternfly/react-core/dist/js/components/Text';
import {
  Card,
  CardBody,
  CardHeaderMain,
  CardHeader
} from '@patternfly/react-core/dist/js/components/Card';
import {
  FormGroup,
  Form
} from '@patternfly/react-core/dist/js/components/Form';
import { CustomDashboardInfo } from '../../../api/CustomDashboardListEnvelopeApi';
import { CustomDashboardListDriver } from '../../../api/CustomDashboardListDriver';
import Moment from 'react-moment';
export interface CustomDashboardCardProps {
  customDashboardData: CustomDashboardInfo;
  driver: CustomDashboardListDriver;
}

const CustomDashboardCard: React.FC<CustomDashboardCardProps & OUIAProps> = ({
  customDashboardData,
  driver,
  ouiaId,
  ouiaSafe
}) => {
  const handleCardClick = (): void => {
    driver.openDashboard(customDashboardData);
  };

  return (
    <Card
      {...componentOuiaProps(ouiaId, 'customDashboard-card', ouiaSafe)}
      isSelectable
      onClick={handleCardClick}
      data-testid="card"
    >
      <CardHeader>
        <CardHeaderMain>Empty</CardHeaderMain>
      </CardHeader>
      <CardHeader>
        <Text component={TextVariants.h1} className="pf-u-font-weight-bold">
          {customDashboardData.name}
        </Text>
      </CardHeader>
      <CardBody>
        <div className="pf-u-mt-md">
          <Form>
            <FormGroup label="Path" fieldId="path">
              <Text component={TextVariants.p}>{customDashboardData.path}</Text>
            </FormGroup>
            <FormGroup label="LastModified" fieldId="lastModified">
              <Text component={TextVariants.p}>
                <Moment fromNow>{customDashboardData.lastModified}</Moment>
              </Text>
            </FormGroup>
          </Form>
        </div>
      </CardBody>
    </Card>
  );
};

export default CustomDashboardCard;
