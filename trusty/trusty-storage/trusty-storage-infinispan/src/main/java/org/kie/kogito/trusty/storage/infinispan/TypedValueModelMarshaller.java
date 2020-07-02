/*
 *  Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.kie.kogito.trusty.storage.infinispan;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kie.kogito.trusty.storage.api.model.TypedValue;

public class TypedValueModelMarshaller extends AbstractModelMarshaller<TypedValue> {

    public static final String NAME_FIELD = "name";
    public static final String TYPE_REF_FIELD = "typeRef";
    public static final String VALUE_FIELD = "value";

    public TypedValueModelMarshaller(ObjectMapper mapper) {
        super(mapper, TypedValue.class);
    }

    @Override
    public TypedValue readFrom(ProtoStreamReader reader) throws IOException {
        return new TypedValue(
                reader.readString(NAME_FIELD),
                reader.readString(TYPE_REF_FIELD),
                jsonFromString(reader.readString(VALUE_FIELD))
        );
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, TypedValue input) throws IOException {
        writer.writeString(NAME_FIELD, input.getName());
        writer.writeString(TYPE_REF_FIELD, input.getTypeRef());
        writer.writeString(VALUE_FIELD, stringFromJson(input.getValue()));
    }

}
