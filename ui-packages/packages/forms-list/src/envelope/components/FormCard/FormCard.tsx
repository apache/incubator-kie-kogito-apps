import React from 'react';
import {
  OUIAProps,
  componentOuiaProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import {
  FormGroup,
  Form
} from '@patternfly/react-core/dist/js/components/Form';
import {
  Card,
  CardBody,
  CardHeaderMain,
  CardHeader
} from '@patternfly/react-core/dist/js/components/Card';
import {
  TextVariants,
  Text
} from '@patternfly/react-core/dist/js/components/Text';
import { Label } from '@patternfly/react-core/dist/js/components/Label';
import { FormInfo } from '../../../api/FormsListEnvelopeApi';
import { FormsListDriver } from '../../../api/FormsListDriver';
import Moment from 'react-moment';
export interface FormCardProps {
  formData: FormInfo;
  driver: FormsListDriver;
}

const FormCard: React.FC<FormCardProps & OUIAProps> = ({
  formData,
  driver,
  ouiaId,
  ouiaSafe
}) => {
  const getLabel = (): string | JSX.Element => {
    switch (formData.type) {
      case 'HTML':
        return <Label variant="outline">HTML</Label>;
      case 'TSX':
        return <Label variant="outline">REACT</Label>;
      /* istanbul ignore next */
      default:
        return '';
    }
  };

  const handleCardClick = (): void => {
    driver.openForm(formData);
  };

  return (
    <Card
      {...componentOuiaProps(ouiaId, 'forms-card', ouiaSafe)}
      isSelectable
      onClick={handleCardClick}
    >
      <CardHeader>
        <CardHeaderMain>{getLabel()}</CardHeaderMain>
      </CardHeader>
      <CardHeader>
        <Text component={TextVariants.h1} className="pf-u-font-weight-bold">
          {formData.name}
        </Text>
      </CardHeader>
      <CardBody>
        <div className="pf-u-mt-md">
          <Form>
            <FormGroup label="Type" fieldId="type">
              <Text component={TextVariants.p}>{formData.type}</Text>
            </FormGroup>
            <FormGroup label="LastModified" fieldId="lastModified">
              <Text component={TextVariants.p}>
                <Moment fromNow>{formData.lastModified}</Moment>
              </Text>
            </FormGroup>
          </Form>
        </div>
      </CardBody>
    </Card>
  );
};

export default FormCard;
