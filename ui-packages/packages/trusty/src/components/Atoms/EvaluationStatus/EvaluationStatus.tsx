import React from 'react';
import { evaluationStatus, evaluationStatusStrings } from '../../../types';
import {
  CheckCircleIcon,
  HourglassHalfIcon,
  MinusCircleIcon,
  ErrorCircleOIcon,
  FastForwardIcon
} from '@patternfly/react-icons';
import { Label } from '@patternfly/react-core';
import './EvaluationStatus.scss';

type EvaluationStatusProps = {
  status: evaluationStatusStrings;
};

const EvaluationStatus = (props: EvaluationStatusProps) => {
  const { status } = props;
  const label = evaluationStatus[status];
  switch (status) {
    case 'EVALUATING':
      return (
        <Label color="orange" icon={<HourglassHalfIcon />}>
          {label}
        </Label>
      );
    case 'FAILED':
      return (
        <Label color="red" icon={<ErrorCircleOIcon />}>
          {label}
        </Label>
      );
    case 'SKIPPED':
      return (
        <Label color="red" icon={<FastForwardIcon />}>
          {label}
        </Label>
      );
    case 'NOT_EVALUATED':
      return (
        <Label color="red" icon={<MinusCircleIcon />}>
          {label}
        </Label>
      );
    case 'SUCCEEDED':
      return (
        <Label color="green" icon={<CheckCircleIcon />}>
          {label}
        </Label>
      );
    default:
      return <span>{label}</span>;
  }
};

export default EvaluationStatus;
