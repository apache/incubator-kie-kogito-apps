import React from 'react';
import './SkeletonStripe.scss';

type SkeletonStripeProps = {
  customStyle?: React.CSSProperties;
  isInline?: boolean;
  size?: 'sm' | 'md' | 'lg';
};

const SkeletonStripe = (props: SkeletonStripeProps) => {
  const { isInline = false, size = 'sm', customStyle } = props;
  const stripeDefaultStyle = {};
  const stripeStyle = customStyle
    ? Object.assign(customStyle, stripeDefaultStyle)
    : stripeDefaultStyle;
  let cssClasses = 'skeleton__stripe';
  if (isInline) {
    cssClasses += ' skeleton__stripe--inline';
  }
  if (size !== 'sm') {
    cssClasses += ` skeleton__stripe--${size}`;
  }

  return <span className={cssClasses} style={stripeStyle} />;
};

export default SkeletonStripe;
