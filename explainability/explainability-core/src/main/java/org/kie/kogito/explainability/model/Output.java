/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.explainability.model;

/**
 * A single output/decision value generated by a {@link PredictionProvider} and incorporated in a {@link PredictionOutput}.
 */
public class Output {

    private final Value value;
    private final Type type;
    private final double score;
    private final String name;

    public Output(String name, Type type, Value value, double score) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.score = score;
    }

    /**
     * Get the output name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the score (confidence) associated to this output
     * @return the score
     */
    public double getScore() {
        return score;
    }

    /**
     * Get the output type
     * @return the output type
     */
    public Type getType() {
        return type;
    }

    /**
     * Get the value associated to this output
     * @return the output value
     */
    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Output{" +
                "value=" + value +
                ", type=" + type +
                ", score=" + score +
                ", name='" + name + '\'' +
                '}';
    }
}
