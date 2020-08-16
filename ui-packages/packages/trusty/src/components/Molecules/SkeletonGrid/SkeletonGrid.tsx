import React from 'react';
import { Grid, GridItem, gridSpans } from '@patternfly/react-core';
import SkeletonStripe from '../../Atoms/SkeletonStripe/SkeletonStripe';

type SkeletonGridProps = {
  rowsNumber: number;
  colsNumber: number | number[];
};

/*
 * SkeletonGrid can be called passing as colsNumber a number of columns or an explicit list of columns sizes that
 * will be fed to the css grid component. This is intended for cases when an uneven number of columns
 * is needed or when specific columns size are wanted instead of columns with equally divided size
 * */
const SkeletonGrid = (props: SkeletonGridProps) => {
  const { rowsNumber, colsNumber } = props;
  let colsCount = 0;
  let colList: number[] = [];

  if (typeof colsNumber === 'number') {
    colsCount = colsNumber;
    colList.length = colsNumber;
    colList.fill(Math.floor(12 / colsNumber));
  }
  if (Array.isArray(colsNumber)) {
    colsCount = colsNumber.length;
    colList = colsNumber;
  }
  const gridRows = [];
  for (let i = 0; i < rowsNumber; i++) {
    for (let j = 0; j < colsCount; j++) {
      const size = (i + j) % 2 ? 'lg' : 'md';
      gridRows.push(
        <GridItem
          span={colList[j] as gridSpans}
          key={`skeleton-grid-${j}-${i}`}
        >
          <SkeletonStripe size={size} />
        </GridItem>
      );
    }
  }
  return <Grid hasGutter>{gridRows}</Grid>;
};

export default SkeletonGrid;
