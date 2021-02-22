import React, { useEffect } from 'react';
import { ModelData } from '../../../types';
import { StandaloneEditorApi } from '@kogito-tooling/kie-editors-standalone/dist/common/Editor';
import * as DmnEditor from '@kogito-tooling/kie-editors-standalone/dist/dmn';

const DMN1_2: string = 'http://www.omg.org/spec/DMN/20151101/dmn.xsd';

type ModelDiagramProps = {
  model: ModelData;
};

const ModelDiagram = (props: ModelDiagramProps) => {
  const { model } = props;
  const type: string = model.type;

  useEffect(() => {
    let editor: StandaloneEditorApi | undefined = undefined;
    if (type === DMN1_2) {
      editor = DmnEditor.open({
        container: document.getElementById('dmn-editor-container'),
        initialContent: Promise.resolve(model.model),
        readOnly: true
      });
      return () => {
        editor.close();
      };
    }
  }, [model]);

  return type === DMN1_2 ? makeDMNEditor() : DEFAULT;
};

function makeUnknownModel(): JSX.Element {
  return <div>Unknown model type</div>;
}

function makeDMNEditor(): JSX.Element {
  return <div id="dmn-editor-container" style={{ height: '100%' }} />;
}

const DEFAULT: JSX.Element = makeUnknownModel();

export default ModelDiagram;
