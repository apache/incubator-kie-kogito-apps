import React from 'react';
import { UncontrolledReactSVGPanZoom } from 'react-svg-pan-zoom';
import evaluationSvg from '../../../static/evaluationSvg.svg';
import {
  ReactSvgPanZoomLoader,
  SvgLoaderSelectElement
} from 'react-svg-pan-zoom-loader';
import { Title, Card, CardHeader, CardBody } from '@patternfly/react-core';
import axios from 'axios';
import SVG from 'react-inlinesvg';

const ProcessDetailsVisualization = ({ data, id }) => {
  const [svg, setSvg] = React.useState(null);
  const getSvg = async () => {
    try {
      await axios
        .get(
          `${data.ProcessInstances[0].endpoint}/process/definition/svg/${id}`
        )
        .then(res => {
          const temp = <SVG src={res.data} />;
          setSvg(temp);
        });
    } catch (error) {
      return error;
    }
  };
  React.useEffect(() => {
    // tslint:disable-next-line: no-floating-promises
    getSvg();
  }, []);

  return (
    <>
      {svg !== null && svg.props.src && (
        <Card>
          <CardHeader>
            <Title headingLevel="h3" size="xl">
              Process Visualization
            </Title>
          </CardHeader>
          <CardBody>
            <ReactSvgPanZoomLoader
              src={evaluationSvg}
              width={800}
              height={400}
              proxy={
                <>
                  <SvgLoaderSelectElement
                    selector="#tree"
                    onClick={() => {
                      return null;
                    }}
                  />
                </>
              }
              render={content => (
                <UncontrolledReactSVGPanZoom
                  width={800}
                  height={400}
                  detectWheel={false}
                  detectAutoPan={false}
                  background="#fff"
                >
                  <svg
                    width={800}
                    height={400}
                    style={{ alignContent: 'center' }}
                  >
                    {svg}
                  </svg>
                </UncontrolledReactSVGPanZoom>
              )}
            />
          </CardBody>
        </Card>
      )}
    </>
  );
};

export default ProcessDetailsVisualization;
