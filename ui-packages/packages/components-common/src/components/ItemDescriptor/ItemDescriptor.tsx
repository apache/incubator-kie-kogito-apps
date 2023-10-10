import React from 'react';
import {
  TextContent,
  Text,
  TextVariants
} from '@patternfly/react-core/dist/js/components/Text';
import { Tooltip } from '@patternfly/react-core/dist/js/components/Tooltip';
import { Badge } from '@patternfly/react-core/dist/js/components/Badge';
import {
  OUIAProps,
  componentOuiaProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';

export interface ItemDescription {
  id: string;
  name: string;
  description?: string;
}

interface IOwnProps {
  itemDescription: ItemDescription;
}

export const ItemDescriptor: React.FC<IOwnProps & OUIAProps> = ({
  itemDescription,
  ouiaId,
  ouiaSafe
}) => {
  const tooltipContainerId = `kogito-consoles-tooltip-${
    itemDescription.id
  }-${Math.random()}`;

  const idStringModifier = (strId: string) => {
    return (
      <TextContent className="pf-u-display-inline">
        <Text component={TextVariants.small} className="pf-u-display-inline">
          {strId.substring(0, 5)}
        </Text>
      </TextContent>
    );
  };
  return (
    <>
      <Tooltip
        appendTo={() => document.getElementById(tooltipContainerId)}
        content={itemDescription.id}
        {...componentOuiaProps(ouiaId, 'item-descriptor', ouiaSafe)}
      >
        <span>
          {itemDescription.name}{' '}
          {itemDescription.description ? (
            <Badge>{itemDescription.description}</Badge>
          ) : (
            idStringModifier(itemDescription.id)
          )}
        </span>
      </Tooltip>
      <div id={tooltipContainerId}></div>
    </>
  );
};
