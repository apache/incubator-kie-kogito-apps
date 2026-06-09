/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.acme.travels.service;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Simple calculator service for testing recordArgs feature.
 * Used in process-level-true.bpmn, process-level-false.bpmn, and mixed-nodes.bpmn processes.
 */
@ApplicationScoped
public class CalculatorService {

    public int add(int a, int b) {
        return a + b;
    }

    public int multiply(int x, int y) {
        return x * y;
    }

    public double divide(double numerator, double denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return numerator / denominator;
    }

    public CalculationResult calculate(int num1, int num2, String operation) {
        CalculationResult result = new CalculationResult();
        result.setNum1(num1);
        result.setNum2(num2);
        result.setOperation(operation);

        switch (operation.toLowerCase()) {
            case "add":
                result.setResult(add(num1, num2));
                break;
            case "multiply":
                result.setResult(multiply(num1, num2));
                break;
            case "subtract":
                result.setResult(num1 - num2);
                break;
            default:
                throw new IllegalArgumentException("Unknown operation: " + operation);
        }

        return result;
    }

    public static class CalculationResult {
        private int num1;
        private int num2;
        private String operation;
        private int result;

        public int getNum1() {
            return num1;
        }

        public void setNum1(int num1) {
            this.num1 = num1;
        }

        public int getNum2() {
            return num2;
        }

        public void setNum2(int num2) {
            this.num2 = num2;
        }

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }
    }
}

// Made with Bob
