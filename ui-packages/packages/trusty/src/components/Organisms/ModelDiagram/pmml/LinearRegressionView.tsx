import React from 'react';
import {
  Chart,
  ChartLabel,
  ChartAxis,
  ChartGroup,
  ChartLine,
  ChartVoronoiContainer
} from '@patternfly/react-charts';

class Line {
  // y=mx+c
  readonly m: number;
  readonly c: number;
  readonly title: string;

  constructor(m: number, c: number, title: string) {
    this.m = m;
    this.c = c;
    this.title = title;
  }
}

// tslint:disable-next-line:max-classes-per-file
class Range {
  readonly min: number;
  readonly max: number;

  constructor(min: number, max: number) {
    this.min = min;
    this.max = max;
  }
}

type Props = {
  modelName: string;
  independentAxisTitle: string;
  dependentAxisTitle: string;
  width?: number;
  height?: number;
  lines: Line[];
  rangeX: Range;
  rangeY: Range;
};

const LinearRegressionView = (props: Props) => {
  function roundedToFixed(_float: number, _digits: number): string {
    const rounded = Math.pow(10, _digits);
    return (Math.round(_float * rounded) / rounded).toFixed(_digits);
  }

  function getTicks(range: Range, count: number): number[] {
    const start: number = range.min;
    const end: number = range.max;
    const step: number = (end - start) / count;
    const ticks: number[] = new Array<number>();
    let v: number = start;
    while (v <= end) {
      ticks.push(v);
      v = v + step;
    }
    if (ticks[ticks.length - 1] !== end) {
      ticks.push(end);
    }
    return ticks;
  }

  const legendData: any = [];
  props.lines.forEach(line => {
    legendData.push({ name: line.title });
  });

  const { modelName = 'undefined', width = 500, height = 500 } = props;

  return (
    <div style={{ height, width }}>
      <Chart
        ariaTitle={modelName}
        containerComponent={
          <ChartVoronoiContainer
            labels={({ datum }) =>
              `${roundedToFixed(datum._x, 2)}, ${roundedToFixed(datum._y, 2)}`
            }
            constrainToVisibleArea
          />
        }
        legendData={legendData}
        legendOrientation="horizontal"
        legendPosition="bottom"
        padding={{
          bottom: 100,
          left: 50,
          right: 50,
          top: 50
        }}
        height={height}
        width={width}
      >
        <ChartLabel text={modelName} x={width / 2} y={30} textAnchor="middle" />
        <ChartAxis
          label={props.independentAxisTitle}
          showGrid
          tickValues={getTicks(props.rangeX, 8)}
          tickFormat={x => roundedToFixed(x, 2)}
        />
        <ChartAxis
          label={props.dependentAxisTitle}
          dependentAxis
          showGrid
          tickValues={getTicks(props.rangeY, 8)}
          tickFormat={x => roundedToFixed(x, 2)}
        />
        <ChartGroup>
          {props.lines.map(line => {
            return (
              <ChartLine
                key={line.title}
                samples={100}
                domain={{
                  x: [props.rangeX.min, props.rangeX.max],
                  y: [props.rangeY.min, props.rangeY.max]
                }}
                y={(datum: any) => line.m * datum.x + line.c}
              />
            );
          })}
        </ChartGroup>
      </Chart>
    </div>
  );
};

export { LinearRegressionView, Line, Range };
