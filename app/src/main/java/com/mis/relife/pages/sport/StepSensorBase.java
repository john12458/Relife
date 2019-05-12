//package com.mis.relife.pages.sport;
//
//import android.content.Context;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//
//public abstract class StepSensorBase implements SensorEventListener {
//    private Context context;
//    public StepCallBack stepCallBack;
//    public SensorManager sensorManager;
//    public static int CURRENT_SETP = 0;
//    public boolean isAvailable = false;
//    public StepSensorBase(Context context, StepCallBack stepCallBack) {
//        this.context = context;
//        this.stepCallBack = stepCallBack;
//    }
//    /**
//     * 開啟計步
//     */
//    public boolean registerStep() {
//        if (sensorManager != null) {
//            sensorManager.unregisterListener(this);
//            sensorManager = null;
//        }
//        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
//        registerStepListener();
//        return isAvailable;
//    }
//    /**
//     * 註冊計步監聽器
//     */
//    protected abstract void registerStepListener();
//    /**
//     * 登出計步監聽器
//     */
//    public abstract void unregisterStep();
//}
