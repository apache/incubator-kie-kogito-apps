import React from 'react';
import { UncontrolledReactSVGPanZoom } from 'react-svg-pan-zoom';
import { componentOuiaProps, OUIAProps } from '@kogito-apps/ouia-tools';
import {
  ReactSvgPanZoomLoader,
  SvgLoaderSelectElement
} from 'react-svg-pan-zoom-loader';
import { Title, Card, CardHeader, CardBody } from '@patternfly/react-core';

interface SvgType {
  src: string;
}
interface SvgProp {
  props: SvgType;
}
interface IOwnProps {
  svg: SvgProp;
  width?: number;
  height?: number;
}

const ProcessDiagram: React.FC<IOwnProps & OUIAProps> = ({
  svg,
  width,
  height,
  ouiaId,
  ouiaSafe
}) => {
  return (
    <>
      <Card {...componentOuiaProps(ouiaId, 'process-diagram', ouiaSafe)}>
        <CardHeader>
          <Title headingLevel="h3" size="xl">
            Diagram
          </Title>
        </CardHeader>
        <CardBody>
          <ReactSvgPanZoomLoader
            src={svg.props.src}
            width={width ?? 1000}
            height={height ?? 400}
            proxy={
              <>
                <SvgLoaderSelectElement />
              </>
            }
            render={() => (
              <UncontrolledReactSVGPanZoom
                width={width ?? 1000}
                height={height ?? 400}
                detectAutoPan={false}
                background="#fff"
              >
                <svg width={width ?? 1000} height={height ?? 400}>
                  {svg}
                </svg>
              </UncontrolledReactSVGPanZoom>
            )}
          />
        </CardBody>
      </Card>
    </>
  );
};

export default ProcessDiagram;
