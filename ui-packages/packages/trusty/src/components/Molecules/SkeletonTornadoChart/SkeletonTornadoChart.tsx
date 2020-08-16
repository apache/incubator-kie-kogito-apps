import React from 'react';
import { v4 as uuid } from 'uuid';
import SkeletonStripe from '../../Atoms/SkeletonStripe/SkeletonStripe';
import './SkeletonTornadoChart.scss';

type SkeletonTornadoChartProps = {
  valuesCount: number;
  width?: number;
  height: number;
};

const SkeletonTornadoChart = (props: SkeletonTornadoChartProps) => {
  const { valuesCount = 10, width = '100%', height = 500 } = props;
  const stripes = [];

  for (let i = 0; i < valuesCount; i++) {
    const stripeWidth = 45 - (40 / valuesCount) * i;
    const stripeLeft = i % 2 ? 50 : 50 - stripeWidth;
    const stripeTop = i * 10 + 1;
    stripes.push(
      <SkeletonStripe
        isInline={true}
        key={uuid()}
        customStyle={{
          width: stripeWidth + '%',
          left: stripeLeft + '%',
          top: stripeTop + '%'
        }}
      />
    );
  }

  return (
    <div className="skeleton-tornado" style={{ width, height }}>
      <div className="skeleton-tornado__legend">
        <SkeletonStripe isInline={true} />
        <SkeletonStripe isInline={true} />
      </div>
      <div className="skeleton-tornado__chart">{stripes}</div>
    </div>
  );
};

export default SkeletonTornadoChart;
