import React, { useCallback, useMemo } from 'react';
import { FeatureScores } from '../../../types';
import { maxBy } from 'lodash';
import {
  Chart,
  ChartAxis,
  ChartBar,
  ChartGroup,
  ChartLabel,
  ChartLegend
} from '@patternfly/react-charts';
import { Split, SplitItem } from '@patternfly/react-core';

type FeaturesScoreChartAlternativeProps = {
  featuresScore: FeatureScores[];
  large?: boolean;
};

const FeaturesScoreChartAlternative = (
  props: FeaturesScoreChartAlternativeProps
) => {
  const { featuresScore, large = false } = props;
  const width = large ? 750 : 480;
  const height = large ? 50 * featuresScore.length : 500;
  const positiveScores = useMemo(
    () => featuresScore.filter(feature => feature.featureScore > 0),
    [featuresScore]
  );
  const negativeScores = useMemo(
    () => featuresScore.filter(feature => feature.featureScore < 0),
    [featuresScore]
  );

  const maxValue = useMemo(() => {
    const max = maxBy(featuresScore, item => {
      return Math.abs(item.featureScore);
    });
    return max ? max.featureScore : 1;
  }, [featuresScore]);

  const computeOpacity = useCallback(
    data => {
      const computedOpacity = Math.abs(
        Math.floor((data.datum.featureScore / maxValue) * 100) / 100
      );
      return computedOpacity < 0.25 ? 0.25 : computedOpacity;
    },
    [maxValue]
  );

  const computeColor = useCallback(data => {
    return data.datum.featureScore >= 0
      ? 'var(--pf-global--info-color--100)'
      : 'var(--pf-global--palette--orange-300)';
  }, []);

  return (
    <Split>
      <SplitItem isFilled>
        <Chart
          ariaDesc="Importance of different features on the decision"
          width={width}
          height={height}
          domainPadding={{ x: [40, 40], y: 20 }}
          domain={{ y: [-maxValue, 0] }}
          horizontal
          padding={{ top: 60, right: 30, bottom: 30, left: 90 }}
          animate={{
            duration: 400,
            onLoad: { duration: 400 }
          }}
        >
          <ChartAxis tickFormat={() => ''} />

          <ChartBar
            data={positiveScores}
            x="featureName"
            y="featureScore"
            alignment="middle"
            barWidth={50}
            style={{
              data: {
                fill: computeColor,
                opacity: computeOpacity
              }
            }}
          />
          <ChartGroup>
            {positiveScores.length > 0 &&
              positiveScores.map((item, index) => {
                return (
                  <ChartLabel
                    className={'feature-chart-axis-label'}
                    datum={{ x: index + 1, y: 0 }}
                    text={item.featureName.split(' ')}
                    direction="rtl"
                    textAnchor={item.featureScore >= 0 ? 'start' : 'end'}
                    dx={-10 * Math.sign(item.featureScore) || -10}
                    key={item.featureName}
                  />
                );
              })}
          </ChartGroup>

          <ChartLegend
            data={[{ name: 'Positive Impact' }]}
            colorScale={['var(--pf-global--info-color--100)']}
            x={width / 2 - 75}
            y={10}
          />
        </Chart>
      </SplitItem>
      <SplitItem isFilled>
        <Chart
          ariaDesc="Importance of different features on the decision"
          width={width}
          height={height}
          domainPadding={{ x: [40, 40], y: 20 }}
          domain={{ y: [0, maxValue] }}
          horizontal
          padding={{ top: 60, right: 90, bottom: 30, left: 30 }}
          animate={{
            duration: 400,
            onLoad: { duration: 400 }
          }}
        >
          <ChartAxis tickFormat={() => ''} />

          <ChartBar
            data={negativeScores}
            x="featureName"
            y="featureScore"
            alignment="middle"
            barWidth={50}
            style={{
              data: {
                fill: computeColor,
                opacity: computeOpacity
              }
            }}
          />
          <ChartGroup>
            {negativeScores.length > 0 &&
              negativeScores.map((item, index) => {
                return (
                  <ChartLabel
                    className={'feature-chart-axis-label'}
                    datum={{ x: index + 1, y: 0 }}
                    text={item.featureName.split(' ')}
                    direction="rtl"
                    textAnchor={item.featureScore >= 0 ? 'start' : 'end'}
                    dx={-10 * Math.sign(item.featureScore) || -10}
                    key={item.featureName}
                  />
                );
              })}
          </ChartGroup>

          <ChartLegend
            data={[{ name: 'Negative Impact' }]}
            colorScale={['var(--pf-global--palette--orange-300)']}
            x={width / 2 - 75}
            y={10}
          />
        </Chart>
      </SplitItem>
    </Split>
  );
};

export default FeaturesScoreChartAlternative;
