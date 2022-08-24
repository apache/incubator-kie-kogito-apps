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
import { EmbeddedEditor, useEditorRef } from "@kie-tools-core/editor/dist/embedded";
import { ChannelType, EditorEnvelopeLocator, EnvelopeMapping } from "@kie-tools-core/editor/dist/api";
import { Title, Card, CardHeader, CardBody } from '@patternfly/react-core';
import { componentOuiaProps, OUIAProps } from '@kogito-apps/ouia-tools';

interface ISwfCombinedEditorProps {
  width?: number;
  height?: number;
}

const SwfCombinedEditor: React.FC<ISwfCombinedEditorProps & OUIAProps> = ({
  width,
  height,
  ouiaId,
  ouiaSafe
}) => {
  const swf_file = {
    "id": "jsongreet",
    "version": "1.0",
    "name": "Greeting workflow",
    "description": "JSON based greeting workflow",
    "start": "ChooseOnLanguage",
    "functions": [
      {
        "name": "greetFunction",
        "type": "custom",
        "operation": "sysout"
      }
    ],
    "states": [
      {
        "name": "ChooseOnLanguage",
        "type": "switch",
        "dataConditions": [
          {
            "condition": "${ .language == \"English\" }",
            "transition": "GreetInEnglish"
          },
          {
            "condition": "${ .language == \"Spanish\" }",
            "transition": "GreetInSpanish"
          }
        ],
        "defaultCondition": {
          "transition": "GreetInEnglish"
        }
      },
      {
        "name": "GreetInEnglish",
        "type": "inject",
        "data": {
          "greeting": "Hello from JSON Workflow, "
        },
        "transition": "GreetPerson"
      },
      {
        "name": "GreetInSpanish",
        "type": "inject",
        "data": {
          "greeting": "Saludos desde JSON Workflow, "
        },
        "transition": "GreetPerson"
      },
      {
        "name": "GreetPerson",
        "type": "operation",
        "actions": [
          {
            "name": "greetAction",
            "functionRef": {
              "refName": "greetFunction",
              "arguments": {
                "message": ".greeting+.name"
              }
            }
          }
        ],
        "end": {
          "terminate": "true"
        }
      }
    ]
  }
  const str = JSON.stringify(swf_file, null, 0);
  const ret = new Uint8Array(str.length);
  for (let i = 0; i < str.length; i++) {
    ret[i] = str.charCodeAt(i);
  }
  const decoder = new TextDecoder("utf-8");
  const content = decoder.decode(ret);
  const { editor } = useEditorRef();
  const isEditorReady = useMemo(() => editor?.isReady, [editor]);
  const embeddedFile = {
    getFileContents: async () => Promise.resolve(content),
    isReadOnly: true,
    fileExtension: "sw.json",
    fileName: "greetings.sw.json",
  }
  console.log(isEditorReady)
  const editorEnvelopeLocator = new EditorEnvelopeLocator(window.location.origin, [
    new EnvelopeMapping({
      type: "swf",
      filePathGlob: "**/*.sw.+(json|yml|yaml)",
      resourcesPathPrefix: ".",
      envelopePath: "resources/serverless-workflow-combined-editor-envelope.html",
    }),
  ]);
  return (
    <Card style={{height:height,width:width}} {...componentOuiaProps(ouiaId, 'swf-diagram', ouiaSafe)} >
      <CardHeader>
        <Title headingLevel="h3" size="xl">
          Diagram
        </Title>
      </CardHeader>
      <CardBody>
        <EmbeddedEditor
          // ref={editorRef}
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
