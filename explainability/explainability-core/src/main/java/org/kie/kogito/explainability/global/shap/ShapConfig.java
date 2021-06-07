/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.explainability.global.shap;

public class ShapConfig {
    public enum linkType {
        LOGIT,
        IDENTITY
    }

    private final linkType link;
    private final Integer nsamples;

    /**
     * Create a ShapConfig instance. This sets the global configuration of the SHAP explainer.
     *
     * @param link: enum, either LOGIT or IDENTITY.
     *        The link function is used as follows: link(modelOutput) = sum(shapValues)
     *        - If you want the shapValues to sum to the exact modelOutput, use IDENTITY
     *        - If your model outputs probabilities and you want the shap values to
     *        use log-odds units, use LOGIT
     * @param nsamples: int, the number of data samples to run when computing shap values
     * @return ShapConfig
     */
    public ShapConfig(linkType link, Integer nsamples) {
        this.link = link;
        this.nsamples = nsamples;
    }

    /**
     * Create a ShapConfig instance with the default nsamples, which will be computed automatically
     * from the data shape.
     *
     * @param link: enum, either LOGIT or IDENTITY. For models that output probabilities,
     *        use LOGIT to make the explanations use log-odds units. For other models, use IDENTITY
     * @return ShapConfig object
     */
    public ShapConfig(linkType link) {
        this.link = link;
        this.nsamples = null;
    }

    /**
     * getters and setters for the various attributes
     */
    public linkType getLink() {
        return this.link;
    }

    public Integer getNsamples() {
        return this.nsamples;
    }
}
