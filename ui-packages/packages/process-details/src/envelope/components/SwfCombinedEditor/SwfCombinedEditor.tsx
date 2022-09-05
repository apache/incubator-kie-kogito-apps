/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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

import React, { useMemo } from 'react';
import { EmbeddedEditor } from "@kie-tools-core/editor/dist/embedded";
import { ChannelType, EditorEnvelopeLocator, EnvelopeMapping } from "@kie-tools-core/editor/dist/api";
import { Title, Card, CardHeader, CardBody } from '@patternfly/react-core';
import { componentOuiaProps, OUIAProps } from '@kogito-apps/ouia-tools';
import { EmbeddedEditorFile } from '@kie-tools-core/editor/dist/channel';

interface ISwfCombinedEditorProps {
  sourceString: string,
  width?: number;
  height?: number;
}

const SwfCombinedEditor: React.FC<ISwfCombinedEditorProps & OUIAProps> = ({
  sourceString,
  width,
  height,
  ouiaId,
  ouiaSafe
}) => {
  const embeddedFile: EmbeddedEditorFile = {
    getFileContents: async () => Promise.resolve(getFileContent()),
    isReadOnly: true,
    fileExtension: "sw.json",
    fileName: "*.sw.json",
  }
  const editorEnvelopeLocator = useMemo(
    () => new EditorEnvelopeLocator(window.location.origin, [
      new EnvelopeMapping({
        type: "swf",
        filePathGlob: "**/*.sw.+(json|yml|yaml)",
        resourcesPathPrefix: ".",
        envelopePath: "resources/serverless-workflow-combined-editor-envelope.html",
      }),
    ]), []);

  const getFileContent = () => {
    const arr = new Uint8Array(sourceString.length);
    for (let i = 0; i < sourceString.length; i++) {
      arr[i] = sourceString.charCodeAt(i);
    }
    const decoder = new TextDecoder("utf-8");
    return decoder.decode(arr);
  }

  return (
    <Card style={{ height: height, width: width }} {...componentOuiaProps(ouiaId, 'swf-diagram', ouiaSafe)} >
      <CardHeader>
        <Title headingLevel="h3" size="xl">
          Diagram
        </Title>
      </CardHeader>
      <CardBody>
        <EmbeddedEditor
          isReady={true}
          file={embeddedFile}
          channelType={ChannelType.ONLINE_MULTI_FILE}
          editorEnvelopeLocator={editorEnvelopeLocator}
          locale={"en"}
        />
      </CardBody>
    </Card>
  );
};

export default SwfCombinedEditor;