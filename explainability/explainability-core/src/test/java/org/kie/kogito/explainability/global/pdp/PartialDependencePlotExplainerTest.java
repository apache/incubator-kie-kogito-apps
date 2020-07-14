package org.kie.kogito.explainability.global.pdp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.DoubleStream;

import org.kie.kogito.explainability.model.Model;
import org.kie.kogito.explainability.model.DataSeries;
import org.kie.kogito.explainability.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PartialDependencePlotExplainerTest {

    @Test
    public void testPdpTextClassifier() throws FileNotFoundException {
        PartialDependencePlotExplainer partialDependencePlotProvider = new PartialDependencePlotExplainer();
        Model modelInfo = TestUtils.getDummyTextClassifier();
        Collection<DataSeries> pdps = partialDependencePlotProvider.explain(modelInfo);
        assertNotNull(pdps);
        for (DataSeries dataSeries : pdps) {
            writeAsciiGraph(dataSeries, new PrintWriter(new File("target/pdp" + dataSeries.getFeature().getName() + ".txt")));
        }
    }

    private void writeAsciiGraph(DataSeries dataSeries, PrintWriter out) {
        double[] outputs = dataSeries.getY();
        double max = DoubleStream.of(outputs).max().getAsDouble();
        double min = DoubleStream.of(outputs).min().getAsDouble();
        outputs = Arrays.stream(outputs).map(d -> d * max / min).toArray();
        double curMax = 1 + DoubleStream.of(outputs).max().getAsDouble();
        ;
        int tempIdx = -1;
        for (int k = 0; k < dataSeries.getX().length; k++) {
            double tempMax = -Integer.MAX_VALUE;
            for (int j = 0; j < outputs.length; j++) {
                double v = outputs[j];
                if ((int) v < (int) curMax && (int) v > (int) tempMax && tempIdx != j) {
                    tempMax = v;
                    tempIdx = j;
                }
            }
            writeDot(dataSeries, tempIdx, out);
            curMax = tempMax;
        }
        out.flush();
        out.close();
    }

    private void writeDot(DataSeries data, int i, PrintWriter out) {
        for (int j = 0; j < data.getX()[i]; j++) {
            out.print(" ");
        }
        out.println("*");
    }
}