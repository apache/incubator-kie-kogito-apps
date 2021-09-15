import React, { useContext, useState } from 'react';
import {
  ActionList,
  ActionListItem,
  Button,
  DescriptionList,
  DescriptionListDescription,
  DescriptionListGroup,
  DescriptionListTerm,
  Divider,
  DrawerActions,
  DrawerCloseButton,
  DrawerHead,
  DrawerPanelBody,
  Stack,
  StackItem,
  Title
} from '@patternfly/react-core';
import {
  CFCategoricalDomain,
  CFDispatch,
  CFNumericalDomain,
  CFSearchInput
} from '../../Templates/Counterfactual/Counterfactual';
import CounterfactualCategoricalDomainEdit from '../CounterfactualCategoricalDomainEdit/CounterfactualCategoricalDomainEdit';
import CounterfactualNumericalDomainEdit from '../CounterfactualNumericalDomainEdit/CounterfactualNumericalDomainEdit';

type CounterfactualInputDomainEditProps = {
  input: CFSearchInput;
  inputIndex: number;
  onClose: () => void;
};

const CounterfactualInputDomainEdit = (
  props: CounterfactualInputDomainEditProps
) => {
  const dispatch = useContext(CFDispatch);
  const { input, inputIndex, onClose } = props;
  const [inputDomain, setInputDomain] = useState(input.domain);
  const [validation, setValidation] = useState({ isValid: true, message: '' });

  const handleApply = () => {
    const updatedValidation = validateDomain(inputDomain);
    if (updatedValidation.isValid) {
      dispatch({
        type: 'setInputDomain',
        payload: { inputIndex, domain: inputDomain }
      });
      onClose();
    }
    setValidation(updatedValidation);
  };

  const onNumericDomainUpdate = (
    min: number | undefined,
    max: number | undefined
  ) => {
    let updatedDomain = inputDomain
      ? ({ ...inputDomain } as CFNumericalDomain)
      : ({ type: 'numerical' } as CFNumericalDomain);
    if (typeof min === 'number') {
      updatedDomain = { ...updatedDomain, lowerBound: min };
    } else {
      delete updatedDomain.lowerBound;
    }
    if (typeof max === 'number') {
      updatedDomain = { ...updatedDomain, upperBound: max };
    } else {
      delete updatedDomain.upperBound;
    }
    if (typeof min !== 'number' && typeof max !== 'number') {
      updatedDomain = null;
    }
    setInputDomain(updatedDomain);
  };

  const onCategoricalDomainUpdate = (categories: string[]) => {
    let updatedDomain = inputDomain
      ? ({ ...inputDomain } as CFCategoricalDomain)
      : ({ type: 'categorical' } as CFCategoricalDomain);
    if (
      categories.filter(category => category === '').length ===
      categories.length
    ) {
      updatedDomain = null;
    } else {
      updatedDomain.categories = categories.filter(category => category !== '');
    }
    setInputDomain(updatedDomain);
  };

  const validateDomain = (domain: CFSearchInput['domain']) => {
    if (domain && domain.type === 'numerical') {
      return validateNumericDomain(domain);
    } else {
      return { isValid: true, message: '' };
    }
  };

  const validateNumericDomain = (numericDomain: CFNumericalDomain) => {
    const result = { isValid: true, message: '' };

    if (
      (numericDomain.lowerBound === undefined &&
        numericDomain.upperBound !== undefined) ||
      (numericDomain.upperBound === undefined &&
        numericDomain.lowerBound !== undefined)
    ) {
      result.isValid = false;
      result.message = 'Please provide both min and max values';
    }
    if (numericDomain.lowerBound > numericDomain.upperBound) {
      result.isValid = false;
      result.message = 'Minimum value cannot be higher than maximum value';
    }

    return result;
  };

  return (
    <>
      <DrawerHead>
        <Title headingLevel="h4" size="xl">
          {input.name}
        </Title>
        <DrawerActions>
          <DrawerCloseButton onClick={onClose} />
        </DrawerActions>
      </DrawerHead>
      <DrawerPanelBody>
        <Stack hasGutter={true}>
          <StackItem>
            <Divider />
          </StackItem>
          <StackItem>
            <DescriptionList>
              <DescriptionListGroup>
                <DescriptionListTerm>Data Type</DescriptionListTerm>
                <DescriptionListDescription>
                  {input.typeRef}
                </DescriptionListDescription>
              </DescriptionListGroup>
              <DescriptionListGroup>
                <DescriptionListTerm>Original Value</DescriptionListTerm>
                <DescriptionListDescription>
                  {input.value.toString()}
                </DescriptionListDescription>
              </DescriptionListGroup>
              {/* @kelvah: we do not have default constraints for now*/}
              {/*<DescriptionListGroup>*/}
              {/*  <DescriptionListTerm>Default Constraints</DescriptionListTerm>*/}
              {/*  <DescriptionListDescription>300-500</DescriptionListDescription>*/}
              {/*</DescriptionListGroup>*/}
            </DescriptionList>
          </StackItem>
          <StackItem>
            <Divider />
          </StackItem>
          <StackItem>
            <Title headingLevel="h5" size="md">
              Constraint
            </Title>
          </StackItem>
          <StackItem>
            {input.typeRef === 'number' && (
              <CounterfactualNumericalDomainEdit
                inputDomain={inputDomain as CFNumericalDomain}
                onUpdate={onNumericDomainUpdate}
                validation={validation}
              />
            )}
            {input.typeRef === 'string' && (
              <CounterfactualCategoricalDomainEdit
                inputDomain={input.domain as CFCategoricalDomain}
                onUpdate={onCategoricalDomainUpdate}
              />
            )}
          </StackItem>
          <StackItem style={{ marginTop: 'var(--pf-global--spacer--md)' }}>
            <ActionList>
              <ActionListItem>
                <Button
                  variant="primary"
                  id="save-button"
                  onClick={handleApply}
                >
                  Apply
                </Button>
              </ActionListItem>
              <ActionListItem>
                <Button variant="link" id="cancel-button" onClick={onClose}>
                  Cancel
                </Button>
              </ActionListItem>
            </ActionList>
          </StackItem>
        </Stack>
      </DrawerPanelBody>
    </>
  );
};

export default CounterfactualInputDomainEdit;

export interface CFConstraintValidation {
  isValid: boolean;
  message: string;
}
