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


const SwfCombinedEditorContainer: React.FC = () => {
   
    const embeddedFile = {
        path: "greetings.sw.json",
        getFileContents: async () => Promise.resolve(undefined),
        isReadOnly: true,
        fileExtension: "sw.json",
        fileName: "greetings",
    }
    const { editorRef } = useEditorRef();
    
    const editorEnvelopeLocator = useMemo(
        () =>
          new EditorEnvelopeLocator(window.location.origin, [
            new EnvelopeMapping({
              type: "swf",
              filePathGlob: "**/*.sw.+(json|yml|yaml)",
              resourcesPathPrefix: "",
              envelopePath: "swf-combined-editor.html",
            }),
          ]),
        []
      );
      
    return (
        <EmbeddedEditor
            ref={editorRef}
            file={embeddedFile}
            channelType={ChannelType.ONLINE}
            editorEnvelopeLocator={editorEnvelopeLocator}
            locale={"en"}
        />
    );
};

export default SwfCombinedEditorContainer;
