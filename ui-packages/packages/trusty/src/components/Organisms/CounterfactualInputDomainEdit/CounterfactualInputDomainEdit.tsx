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
  Form,
  FormGroup,
  Split,
  SplitItem,
  Stack,
  StackItem,
  TextInput,
  Title
} from '@patternfly/react-core';
import {
  CFDispatch,
  CFSearchInput
} from '../../Templates/Counterfactual/Counterfactual';

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
  const [min, setMin] = useState(
    input.domain && input.domain.type === 'numerical'
      ? input.domain.lowerBound
      : undefined
  );
  const [max, setMax] = useState(
    input.domain && input.domain.type === 'numerical'
      ? input.domain.upperBound
      : undefined
  );

  const handleMinChange = value => {
    setMin(Number(value));
  };
  const handleMaxChange = value => {
    setMax(Number(value));
  };

  const handleApply = () => {
    dispatch({
      type: 'setInputNumericDomain',
      payload: { inputIndex, range: { min, max } }
    });
    onClose();
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
              Counterfactuals
            </Title>
          </StackItem>
          <StackItem>
            <Form>
              <Split hasGutter={true}>
                <SplitItem>
                  <FormGroup label="Minimum Value" isRequired fieldId="min">
                    <TextInput
                      isRequired
                      type="number"
                      id="min"
                      name="min"
                      value={min || ''}
                      onChange={handleMinChange}
                    />
                  </FormGroup>
                </SplitItem>
                <SplitItem>
                  <FormGroup label="Maximum Value" isRequired fieldId="max">
                    <TextInput
                      isRequired
                      type="number"
                      id="max"
                      name="max"
                      value={max || ''}
                      onChange={handleMaxChange}
                    />
                  </FormGroup>
                </SplitItem>
              </Split>
            </Form>
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
