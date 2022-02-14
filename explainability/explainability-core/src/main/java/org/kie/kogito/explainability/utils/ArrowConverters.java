/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.kogito.explainability.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.BitVector;
import org.apache.arrow.vector.FieldVector;
import org.apache.arrow.vector.Float4Vector;
import org.apache.arrow.vector.Float8Vector;
import org.apache.arrow.vector.IntVector;
import org.apache.arrow.vector.VarCharVector;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.ipc.ArrowFileReader;
import org.apache.arrow.vector.ipc.ArrowFileWriter;
import org.apache.arrow.vector.types.FloatingPointPrecision;
import org.apache.arrow.vector.types.Types;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.FieldType;
import org.apache.arrow.vector.types.pojo.Schema;
import org.apache.arrow.vector.util.ByteArrayReadableSeekableByteChannel;
import org.apache.arrow.vector.util.Text;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.Type;
import org.kie.kogito.explainability.model.Value;

public class ArrowConverters {
    private ArrowConverters() {
        throw new IllegalStateException("Utility class");
    }

    // Inbound Processing ==============================================================================================
    // convert a list of Arrow FieldVectors to a list of prediction outputs
    public static List<PredictionOutput> convertFieldVectorstoPO(List<FieldVector> fvs) {
        int colCount = fvs.size();
        int rowCount = fvs.get(0).getValueCount();

        Output[][] outputBuffer = new Output[rowCount][colCount];

        //grab the output values stored in even columns
        for (int col = 0; col < fvs.size(); col += 1) {
            FieldVector fv = fvs.get(col);
            int destinationCol = col;
            if (fv.getMinorType() == Types.MinorType.FLOAT8) {
                Float8Vector castv = (Float8Vector) fv;
                IntStream.range(0, rowCount).forEach(row -> outputBuffer[row][destinationCol] = new Output(
                        fv.getName(),
                        Type.NUMBER,
                        new Value(castv.get(row)),
                        1.0));
            } else if (fv.getMinorType() == Types.MinorType.FLOAT4) {
                Float4Vector castv = (Float4Vector) fv;
                IntStream.range(0, rowCount).forEach(row -> outputBuffer[row][destinationCol] = new Output(
                        fv.getName(),
                        Type.NUMBER,
                        new Value(castv.get(row)),
                        1.0));
            } else if (fv.getMinorType() == Types.MinorType.INT) {
                IntVector castv = (IntVector) fv;
                IntStream.range(0, rowCount).forEach(row -> outputBuffer[row][destinationCol] = new Output(
                        fv.getName(),
                        Type.NUMBER,
                        new Value(castv.get(row)),
                        1.0));
            } else if (fv.getMinorType() == Types.MinorType.BIT) {
                BitVector castv = (BitVector) fv;
                IntStream.range(0, rowCount).forEach(row -> outputBuffer[row][destinationCol] = new Output(
                        fv.getName(),
                        Type.BOOLEAN,
                        new Value(castv.get(row) == 1),
                        1.0));
            } else if (fv.getMinorType() == Types.MinorType.VARCHAR) {
                VarCharVector castv = (VarCharVector) fv;
                IntStream.range(0, rowCount).forEach(row -> outputBuffer[row][destinationCol] = new Output(
                        fv.getName(),
                        Type.TEXT,
                        new Value(castv.get(row)),
                        1.0));
            } else {
                throw new IllegalArgumentException(String.format("FieldVector Type %s currently unsupported",
                        fv.getMinorType()));
            }
        }
        List<PredictionOutput> converted = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            converted.add(new PredictionOutput(Arrays.asList(outputBuffer[i])));
        }
        return converted;
    }

    //Outbound Processing ==============================================================================================
    //generate the schema that all predictioninputs for this explanation will take
    public static Schema generatePrototypePISchema(PredictionInput paradigm) {
        List<Field> fields = new ArrayList<>();
        for (Feature f : paradigm.getFeatures()) {
            if (f.getType() == Type.NUMBER) {
                Field doubleField = new Field(f.getName(), FieldType.nullable(new ArrowType.FloatingPoint(FloatingPointPrecision.DOUBLE)), null);
                fields.add(doubleField);
            } else if (f.getType() == Type.BOOLEAN) {
                Field boolField = new Field(f.getName(), FieldType.nullable(new ArrowType.Bool()), null);
                fields.add(boolField);
            } else if (f.getType() == Type.TEXT) {
                Field textField = new Field(f.getName(), FieldType.nullable(new ArrowType.List()), null);
                fields.add(textField);
            } else {
                throw new IllegalArgumentException(String.format("Output type %s currently unsupported",
                        f.getType()));
            }
        }
        return new Schema(fields, null);
    }

    // convert a list of PIs to VectorSchemaRoot as per the prototype Schema
    public static VectorSchemaRoot convertPItoVSR(List<PredictionInput> inputs, Schema sourceSchema, RootAllocator allocator) {
        int nrows = inputs.size();
        VectorSchemaRoot sourceRoot = VectorSchemaRoot.create(sourceSchema, allocator);

        List<FieldVector> fvs = sourceRoot.getFieldVectors();
        for (int col = 0; col < fvs.size(); col++) {
            FieldVector fv = fvs.get(col);
            int finalCol = col;
            if (fv.getMinorType() == Types.MinorType.FLOAT8) {
                Float8Vector castv = (Float8Vector) fv;
                castv.allocateNew(nrows);
                IntStream.range(0, nrows).forEach(row -> castv.set(row, inputs.get(row).getFeatures().get(finalCol).getValue().asNumber()));
                castv.setValueCount(nrows);
            } else if (fv.getMinorType() == Types.MinorType.BIT) {
                BitVector castv = (BitVector) fv;
                castv.allocateNew(nrows);
                IntStream.range(0, nrows).forEach(row -> castv.set(row, (int) inputs.get(row).getFeatures().get(finalCol).getValue().asNumber()));
                castv.setValueCount(nrows);
            } else if (fv.getMinorType() == Types.MinorType.VARCHAR) {
                VarCharVector castv = (VarCharVector) fv;
                castv.allocateNew(nrows);
                IntStream.range(0, nrows).forEach(row -> castv.set(row, new Text(inputs.get(row).getFeatures().get(finalCol).getValue().asString())));
                castv.setValueCount(nrows);
            } else {
                throw new IllegalArgumentException(String.format("Output type %s currently unsupported, but this error should never arise",
                        fv.getMinorType()));
            }
        }
        sourceRoot.setRowCount(nrows);
        return sourceRoot;
    }

    // IO ==============================================================================================================
    // read an inbound bytearray
    public static List<PredictionOutput> read(byte[] resultBuffer, RootAllocator allocator) {
        byte[] byteArray = resultBuffer;
        try (ArrowFileReader reader = new ArrowFileReader(
                new ByteArrayReadableSeekableByteChannel(byteArray), allocator)) {
            reader.loadRecordBatch(reader.getRecordBlocks().get(0));
            VectorSchemaRoot vsr = reader.getVectorSchemaRoot();
            return ArrowConverters.convertFieldVectorstoPO(vsr.getFieldVectors());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //write an outbound byte array
    public static byte[] write(VectorSchemaRoot vsr) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ArrowFileWriter writer = new ArrowFileWriter(vsr, null, Channels.newChannel(out));
            writer.start();
            writer.writeBatch();
            writer.end();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
