package ch.epfl.sweng.project.util;

public final class DoubleArrayConverter {

    public static double[] floatToDoubleArray(float[] a) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = Double.valueOf(a[i]);
        }
        return ret;
    }

    public static float[] doubleToFloatArray(double[] a) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = (float) a[i];
        }
        return ret;
    }
}
