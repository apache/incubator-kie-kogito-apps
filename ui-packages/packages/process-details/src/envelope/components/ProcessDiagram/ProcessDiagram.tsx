/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from 'react';
import { UncontrolledReactSVGPanZoom } from 'react-svg-pan-zoom';
import { OUIAProps, componentOuiaProps } from '@kogito-apps/components-common';
import {
  ReactSvgPanZoomLoader,
  SvgLoaderSelectElement
} from 'react-svg-pan-zoom-loader';
import { Title, Card, CardHeader, CardBody } from '@patternfly/react-core';

interface svgType {
  src: string;
}
interface svgProp {
  props: svgType;
}
interface IOwnProps {
  svg: svgProp;
}

const ProcessDiagram: React.FC<IOwnProps & OUIAProps> = ({
  svg,
  ouiaId,
  ouiaSafe
}) => {
  return (
    <>
      <Card {...componentOuiaProps(ouiaId, 'process-diagram', ouiaSafe)}>
        <CardHeader>
          <Title headingLevel="h3" size="xl">
            Process Diagram
          </Title>
        </CardHeader>
        <CardBody>
          <ReactSvgPanZoomLoader
            src={svg.props.src}
            width={1000}
            height={400}
            proxy={
              <>
                <SvgLoaderSelectElement />
              </>
            }
            render={() => (
              <UncontrolledReactSVGPanZoom
                width={1000}
                height={400}
                detectAutoPan={false}
                background="#fff"
              >
                <svg width={1000} height={400}>
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
