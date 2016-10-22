package ch.epfl.sweng.project.engine3d;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Display;
import android.view.Surface;

import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;

import ch.epfl.sweng.project.BuildConfig;


public class RotSensorListener implements SensorEventListener {

    private static final String TAG = "RotSensorListener";

    private final PanoramaRenderer mRenderer;
    private final Display mDisplay;
    private final boolean isIsolatedFromApp;
    private float[] mRotationMatrixIn;
    private float[] mRotationMatrixOut;
    private Quaternion mDummyRotation;
    private int mScreenRotation;

    /**
     * A constructor for testing purposes
     */
    public RotSensorListener() {
        mRenderer = null;
        mDisplay = null;
        mScreenRotation = Surface.ROTATION_0;
        isIsolatedFromApp = true;
        mDummyRotation = new Quaternion();
        mRotationMatrixIn = new float[16];
        mRotationMatrixOut = new float[16];
    }

    public RotSensorListener(Display display, PanoramaRenderer renderer) {
        if (renderer == null) {
            throw new IllegalArgumentException("Renderer reference was null");
        }
        mDisplay = display;
        mRenderer = renderer;
        mRotationMatrixIn = new float[16];
        mRotationMatrixOut = new float[16];
        mDummyRotation = new Quaternion();
        isIsolatedFromApp = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_GAME_ROTATION_VECTOR) {
            return;
        } else {
            sensorChanged(event.values);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void sensorChanged(float[] values) {

        if (!isIsolatedFromApp) {
            mScreenRotation = mDisplay.getRotation();
        }

        values[3] = -values[3];

        if (BuildConfig.DEBUG)
            Log.d(TAG, String.format("Before: %1$.2f, %2$.2f, %3$.2f, %4$.2f",
                    values[3], values[0], values[1], values[2]));

        SensorManager.getRotationMatrixFromVector(mRotationMatrixIn, values);
        SensorManager.remapCoordinateSystem(mRotationMatrixIn, SensorManager.AXIS_X, SensorManager.AXIS_MINUS_Z,
                mRotationMatrixOut);

        Quaternion q = new Quaternion().fromMatrix(floatToDoubleArray(mRotationMatrixOut));
        if (BuildConfig.DEBUG)
            Log.d(TAG, String.format("After: %1$.2f, %2$.2f, %3$.2f, %4$.2f",
                    q.w, q.x, q.y, q.z));

        switch (mScreenRotation) {
            case Surface.ROTATION_0:
                break;
            case Surface.ROTATION_90:
                q.multiplyLeft(new Quaternion().fromAngleAxis(Vector3.Axis.Z, 90));
                break;
            case Surface.ROTATION_180:
                q.multiplyLeft(new Quaternion().fromAngleAxis(Vector3.Axis.Z, 180));
                break;
            case Surface.ROTATION_270:
                q.multiplyLeft(new Quaternion().fromAngleAxis(Vector3.Axis.Z, 270));
                break;
        }


        if (isIsolatedFromApp) {
            mDummyRotation = new Quaternion(q);
        } else {
            mRenderer.setSensorRotation(q);
        }
    }

    public Quaternion getDummyRotation() {
        return new Quaternion(mDummyRotation);
    }

    public void setScreenRotation(int r) {
        mScreenRotation = r;
    }

    public double[] floatToDoubleArray(float[] a) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = Double.valueOf(a[i]);
        }
        return ret;
    }

    public float[] doubleToFloatArray(double[] a) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = (float) a[i];
        }
        return ret;
    }
}