package org.sherman.simd;

import com.google.common.base.Preconditions;
import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorShuffle;
import jdk.incubator.vector.VectorSpecies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArrayUtils {
    private static final Logger logger = LoggerFactory.getLogger(ArrayUtils.class);

    private static final VectorSpecies<Integer> SPECIES = IntVector.SPECIES_128;

    private ArrayUtils() {
    }

    public static boolean hasIntersectionScalar(int[] left, int[] right) {
        boolean hasIntersection = false;
        int i = 0;
        int j = 0;
        while (i < left.length && j < right.length) {
            if (left[i] == right[j]) {
                return true;
            } else if (left[i] > right[j]) {
                j++;
            } else {
                i++;
            }
        }

        return false;
    }

    public static boolean hasIntersectionVectorShuffling(int left[], int right[]) {
        Preconditions.checkArgument(left.length % SPECIES.length() == 0);
        Preconditions.checkArgument(right.length % SPECIES.length() == 0);

        var bound1 = SPECIES.loopBound(left.length);
        var bound2 = SPECIES.loopBound(right.length);

        int i = 0;
        int j = 0;
        while (i < bound1 && j < bound2) {
            var chunk = IntVector.fromArray(SPECIES, left, i);
            var rightVector = IntVector.fromArray(SPECIES, right, j);

            if (handleChunkByShuffling(chunk, rightVector)) {
                return true;
            }

            var chunkLast = chunk.lane(SPECIES.length() - 1);
            var rightLast = rightVector.lane(SPECIES.length() - 1);

            if (chunkLast > rightLast) {
                j += SPECIES.length();
            } else if (chunkLast < rightLast) {
                i += SPECIES.length();
            } else {
                j += SPECIES.length();
                i += SPECIES.length();
            }
        }

        return false;
    }

    private static final VectorShuffle<Integer> SHUFFLE = VectorShuffle.fromValues(SPECIES, 3, 0, 1, 2);

    private static boolean handleChunkByShuffling(IntVector chunk1, IntVector rightVector) {
        var v1 = rightVector.rearrange(SHUFFLE);
        var v2 = v1.rearrange(SHUFFLE);
        var v3 = v2.rearrange(SHUFFLE);
        var v4 = v3.rearrange(SHUFFLE);

        var r1 = chunk1.eq(v1);
        var r2 = chunk1.eq(v2);
        var r3 = chunk1.eq(v3);
        var r4 = chunk1.eq(v4);

        return r1.or(r2).or(r3).or(r4)
            .anyTrue();
    }

    public static boolean hasIntersectionVector(int[] left, int[] right) {
        Preconditions.checkArgument(left.length % SPECIES.length() == 0);
        Preconditions.checkArgument(right.length % SPECIES.length() == 0);

        var bound1 = SPECIES.loopBound(left.length);
        var bound2 = SPECIES.loopBound(right.length);

        int i = 0;
        int j = 0;
        while (i < bound1 && j < bound2) {
            var chunk = IntVector.fromArray(SPECIES, left, i);
            var rightVector = IntVector.fromArray(SPECIES, right, j);

            if (handleChunk(chunk, rightVector)) {
                return true;
            }

            var chunkLast = chunk.lane(SPECIES.length() - 1);
            var rightLast = rightVector.lane(SPECIES.length() - 1);

            if (chunkLast > rightLast) {
                j += SPECIES.length();
            } else if (chunkLast < rightLast) {
                i += SPECIES.length();
            } else {
                j += SPECIES.length();
                i += SPECIES.length();
            }
        }

        //logger.info("[{}]", counter);

        return false;
    }

    private static boolean handleChunk(IntVector chunk1, IntVector rightVector) {
        var v1 = rightVector.lane(0);
        var v2 = rightVector.lane(1);
        var v3 = rightVector.lane(2);
        var v4 = rightVector.lane(3);

        var r1 = chunk1.eq(v1);
        var r2 = chunk1.eq(v2);
        var r3 = chunk1.eq(v3);
        var r4 = chunk1.eq(v4);

        return r1.or(r2).or(r3).or(r4)
            .anyTrue();
    }
}
