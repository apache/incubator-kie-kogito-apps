import React from 'react';
import _ from 'lodash';
import {
  ActionList,
  ActionListItem
} from '@patternfly/react-core/dist/js/components/ActionList';
import { convertActionsToButton, FormAction } from '../utils';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import '../styles.css';

interface IOwnProps {
  actions?: FormAction[];
  enabled?: boolean;
  onSubmitForm?: () => void;
}

export const FormFooter: React.FC<IOwnProps & OUIAProps> = ({
  actions,
  enabled = true,
  onSubmitForm,
  ouiaId,
  ouiaSafe
}) => {
  if (_.isEmpty(actions)) {
    return null;
  }

  const actionItems = convertActionsToButton(
    actions,
    enabled,
    onSubmitForm
  ).map((button, index) => {
    return (
      <ActionListItem
        key={`form-action-${index}`}
        data-testid="action-list-item"
      >
        {button}
      </ActionListItem>
    );
  });

  return (
    <div
      className="kogito-components-common__form-footer-padding-top"
      {...componentOuiaProps(ouiaId, 'form-footer', ouiaSafe)}
    >
      <ActionList data-testid="action-list">{actionItems}</ActionList>
    </div>
  );
};
