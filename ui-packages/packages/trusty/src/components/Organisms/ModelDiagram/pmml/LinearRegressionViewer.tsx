import { Spinner } from '@patternfly/react-core';
import React, { useEffect, useState } from 'react';
import { document as PMMLDocument } from './generated/www.dmg.org/PMML-4_4';
import { LinearRegressionViewAdaptor } from './LinearRegressionViewAdaptor';
import { withCXML as unmarshal } from './unmarshall/unmarshaller';

type Props = {
  xml: string;
};

const LinearRegressionViewer = (props: Props) => {
  const { xml } = props;
  const [viewer, setViewer] = useState(<Spinner size="xl" />);

  useEffect(() => {
    let didMount = true;
    unmarshal(xml)
      .then((doc: PMMLDocument) => {
        if (didMount) {
          if (doc.PMML.RegressionModel !== undefined) {
            if (doc.PMML.RegressionModel[0] !== undefined) {
              setViewer(
                <LinearRegressionViewAdaptor
                  dictionary={doc.PMML.DataDictionary}
                  models={doc.PMML.RegressionModel}
                />
              );
            }
          }
        }
      })
      .catch(() => {
        setViewer(<span>unsupported model</span>);
      });
    return () => {
      didMount = false;
    };
  }, [xml]);

  return viewer;
};

export { LinearRegressionViewer };
