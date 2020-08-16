import React from 'react';
import { VictoryChart, VictoryAxis, VictoryBoxPlot } from 'victory';

const chartData = [
  { x: 'A', min: 10000, median: 55000, max: 100000, q1: 34000, q3: 88000 }
];

const FeatureDistributionBoxPlot = () => {
  const max = chartData[0].max;
  const min = chartData[0].min;
  return (
    <div
      style={{
        width: '190px',
        marginTop: '-18px',
        paddingTop: 0
      }}
    >
      <VictoryChart
        padding={{ left: 10, right: 10, bottom: 20, top: 0 }}
        domainPadding={5}
        domain={{ y: [min, max] }}
        horizontal
        width={190}
        height={60}
      >
        <VictoryBoxPlot
          data={chartData}
          labels={false}
          boxWidth={10}
          style={{
            min: { stroke: '#8BC1F7' },
            max: { stroke: '#519DE9' },
            q1: { fill: '#8BC1F7' },
            q3: { fill: '#519DE9' },
            median: { stroke: 'white', strokeWidth: 2 },
            minLabels: { fill: '#8BC1F7' },
            maxLabels: { fill: '#519DE9' }
          }}
        />
        <VictoryAxis
          dependentAxis
          tickCount={5}
          tickFormat={t => `${Math.round(t / 1000)}k`}
          style={{
            tickLabels: {
              fontSize: 12,
              padding: 5
            },
            axis: {
              stroke: '#c6c6c6'
            }
          }}
        />
      </VictoryChart>
    </div>
  );
};

export default FeatureDistributionBoxPlot;
