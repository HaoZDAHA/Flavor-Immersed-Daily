package com.fidtest.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record CowpeaSpinData(float spinSpeed, int remainingTicks, int comboCount) {

    public static final CowpeaSpinData EMPTY = new CowpeaSpinData(0.0F, 0, 0);

    public static final Codec<CowpeaSpinData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("spinSpeed").forGetter(CowpeaSpinData::spinSpeed),
                    Codec.INT.fieldOf("remainingTicks").forGetter(CowpeaSpinData::remainingTicks),
                    Codec.INT.fieldOf("comboCount").forGetter(CowpeaSpinData::comboCount)
            ).apply(instance, CowpeaSpinData::new)
    );

    public boolean isActive() {
        return remainingTicks > 0 && spinSpeed > 0.0F;
    }
}
