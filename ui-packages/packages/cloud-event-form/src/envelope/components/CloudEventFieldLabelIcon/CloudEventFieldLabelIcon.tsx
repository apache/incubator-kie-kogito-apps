import React from 'react';
import { HelpIcon } from '@patternfly/react-icons/dist/esm/icons/help-icon';
import { Popover } from '@patternfly/react-core/dist/js/components/Popover';

export interface CloudEventFieldLabelIconProps {
  fieldId: string;
  helpMessage: string | JSX.Element;
  cloudEventHeader?: string;
}

const CloudEventFieldLabelIcon: React.FC<CloudEventFieldLabelIconProps> = ({
  fieldId,
  helpMessage,
  cloudEventHeader
}) => {
  return (
    <Popover
      id={`cloudEvent-form-${fieldId}-help`}
      bodyContent={
        <div>
          <div>{helpMessage}</div>
          {cloudEventHeader && (
            <div>
              The value will be set in the{' '}
              <span
                className={'pf-u-success-color-100'}
              >{`'${cloudEventHeader}'`}</span>{' '}
              header.
            </div>
          )}
        </div>
      }
    >
      <button
        type="button"
        aria-label={`More info for ${fieldId} field`}
        onClick={(e) => e.preventDefault()}
        className="pf-c-form__group-label-help"
      >
        <HelpIcon noVerticalAlign />
      </button>
    </Popover>
  );
};

export default CloudEventFieldLabelIcon;
