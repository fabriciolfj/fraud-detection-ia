package com.github.fabriciolfj.fraud.configuration;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.NoBatchifyTranslator;
import ai.djl.translate.TranslatorContext;
import com.github.fabriciolfj.fraud.models.TransactionDetails;

public class TransactionTransformer implements NoBatchifyTranslator<TransactionDetails, Boolean> {

    private final float threshold;

    public TransactionTransformer(float threshold) {
        this.threshold = threshold;
    }

    @Override
    public Boolean processOutput(TranslatorContext ctx, NDList list) throws Exception {
        NDArray result = list.getFirst();
        float prediction = result.toFloatArray()[0];
        IO.println("Prediction: " + prediction);

        return prediction > threshold;
    }

    @Override
    public NDList processInput(TranslatorContext ctx, TransactionDetails input) throws Exception {
        NDArray array = ctx.getNDManager().create(toFloatRepresentation(input), new Shape(1,5)); //forma do array, no caso 5 parametros
        return new NDList(array);
    }

    private static float[] toFloatRepresentation(final TransactionDetails td) {
        return new float[] {
                td.distanceFomLastTransaction(),
                td.ratioToMedianPrice(),
                booleanAsFloat(td.usedChip()),
                booleanAsFloat(td.usePinNumber()),
                booleanAsFloat(td.onlineOrder())
        };
    }

    private static float booleanAsFloat(boolean flag) {
        return flag? 1.0f :0.0f;
    }
}
