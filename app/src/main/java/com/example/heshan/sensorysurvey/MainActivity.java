package com.example.heshan.sensorysurvey;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    // System sensor manager instance.
    private SensorManager mSensorManager;

    // Proximity and light sensors, as retrieved from the sensor manager.
    private Sensor mSensorProximity;
    private Sensor mSensorLight;
    private Sensor mAccelerometer;
    private Sensor mGyroscope;
    private Sensor mOrientation;
    private Sensor mMagneticField;

    // TextViews to display current sensor values.
    private TextView mTextSensorLight;
    private TextView mTextSensorProximity;
    private TextView mTextAccelerometer;
    private TextView mTextGyroscope;
    private TextView mTextOrientation;
    private TextView mTextMagneticField;

    // toggle indicator button
    private boolean buttonPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize all view variables.
        mTextSensorLight = findViewById(R.id.label_light);
        mTextSensorProximity = findViewById(R.id.label_proximity);
        mTextAccelerometer = findViewById(R.id.label_accelerometer);
        mTextGyroscope = findViewById(R.id.label_gyroscope);
        mTextOrientation = findViewById(R.id.label_orientation);
        mTextMagneticField = findViewById(R.id.label_magnetic_field);

        // Get an instance of the sensor manager.
        mSensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);

        // Get light and proximity sensors from the sensor manager.
        // The getDefaultSensor() method returns null if the sensor
        // is not available on the device.
        mSensorProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Get the error message from string resources.
        String err_light = getResources().getString(R.string.error_no_light_sensor);
        String err_proximity = getResources().getString(R.string.error_no_proximity_sensor);
        String err_accelerometer = getResources().getString(R.string.error_no_accelerometer);
        String err_gyroscope = getResources().getString(R.string.error_no_gyroscope);
        String err_orientation = getResources().getString(R.string.error_no_orientation);
        String err_magnetic_field = getResources().getString(R.string.error_no_magnetic_field);

        // If either mSensorLight or mSensorProximity are null, those sensors
        // are not available in the device.  Set the text to the error message
        if (mSensorLight == null) {
            mTextSensorLight.setText(err_light);
        } else {
            mTextSensorLight.setText(getResources().getString(R.string.label_light, 0.0));
        }
        if (mSensorProximity == null) {
            mTextSensorProximity.setText(err_proximity);
        } else {
            mTextSensorProximity.setText(getResources().getString(R.string.label_proximity, 0.0));
        }
        if (mAccelerometer == null) {
            mTextAccelerometer.setText(err_accelerometer);
        } else {
            mTextAccelerometer.setText(getResources().getString(R.string.label_accelerometer, 0.0, 0.0, 0.0));
        }
        if (mGyroscope == null) {
            mTextGyroscope.setText(err_gyroscope);
        } else {
            mTextGyroscope.setText(getResources().getString(R.string.label_gyroscope, 0.0, 0.0, 0.0));
        }
        if (mOrientation == null) {
            mTextOrientation.setText(err_orientation);
        } else {
            mTextOrientation.setText(getResources().getString(R.string.label_orientation, 0.0, 0.0, 0.0));
        }
        if (mMagneticField == null) {
            mTextMagneticField.setText(err_magnetic_field);
        } else {
            mTextMagneticField.setText(getResources().getString(R.string.label_magnetic_field, 0.0, 0.0, 0.0));
        }

        buttonPressed = false;

        final Button button = findViewById(R.id.button_start);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!buttonPressed) {
                    getSensorReadings();
                    buttonPressed = true;
                    button.setText("STOP TAKING READINGS");
                } else {
                    stopReadings();
                    buttonPressed = false;
                    button.setText("START TAKING READINGS");
                }
            }
        });
    }

//    @Override
    protected void getSensorReadings() {
//        super.onStart();

        // Listeners for the sensors are registered in this callback and
        // can be unregistered in onPause().
        //
        // Check to ensure sensors are available before registering listeners.
        // Both listeners are registered with a "normal" amount of delay
        // (SENSOR_DELAY_NORMAL)
        if (mSensorProximity != null) {
            mSensorManager.registerListener(this, mSensorProximity,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mSensorLight != null) {
            mSensorManager.registerListener(this, mSensorLight,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mAccelerometer != null) {
            mSensorManager.registerListener(this, mAccelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mGyroscope != null) {
            mSensorManager.registerListener(this, mGyroscope,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mOrientation != null) {
            mSensorManager.registerListener(this, mOrientation,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mMagneticField != null) {
            mSensorManager.registerListener(this, mMagneticField,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

//    @Override
    protected void stopReadings() {
//        super.onStop();

        // Unregister all sensor listeners in this callback so they don't
        // continue to use resources when the app is paused.
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        // The sensor type (as defined in the Sensor class).
        int sensorType = sensorEvent.sensor.getType();

        // The new data value of the sensor.  Both the light and proximity
        // sensors report one value at a time, which is always the first
        // element in the values array.
        float[] currentValue = sensorEvent.values;

        switch (sensorType) {
            // Event came from the light sensor.
            case Sensor.TYPE_LIGHT:
                // Set the light sensor text view to the light sensor string
                // from the resources, with the placeholder filled in.
                mTextSensorLight.setText(getResources().getString(
                        R.string.label_light, currentValue[0]));
                break;
            case Sensor.TYPE_PROXIMITY:
                // Set the proximity sensor text view to the light sensor
                // string from the resources, with the placeholder filled in.
                mTextSensorProximity.setText(getResources().getString(
                        R.string.label_proximity, currentValue[0]));
                break;
            case Sensor.TYPE_ACCELEROMETER:
                // Set the proximity sensor text view to the light sensor
                // string from the resources, with the placeholder filled in.
                mTextAccelerometer.setText(getResources().getString(
                        R.string.label_accelerometer, currentValue[0], currentValue[1], currentValue[2]));
                break;
            case Sensor.TYPE_GYROSCOPE:
                // Set the proximity sensor text view to the light sensor
                // string from the resources, with the placeholder filled in.
                mTextGyroscope.setText(getResources().getString(
                        R.string.label_gyroscope, currentValue[0], currentValue[1], currentValue[2]));
                break;
            case Sensor.TYPE_ORIENTATION:
                // Set the proximity sensor text view to the light sensor
                // string from the resources, with the placeholder filled in.
                mTextOrientation.setText(getResources().getString(
                        R.string.label_orientation, currentValue[0], currentValue[1], currentValue[2]));
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                // Set the proximity sensor text view to the light sensor
                // string from the resources, with the placeholder filled in.
                mTextMagneticField.setText(getResources().getString(
                        R.string.label_magnetic_field, currentValue[0], currentValue[1], currentValue[2]));
                break;
            default:
                // do nothing
        }
    }

    /**
     * Abstract method in SensorEventListener.  It must be implemented, but is
     * unused in this app.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
