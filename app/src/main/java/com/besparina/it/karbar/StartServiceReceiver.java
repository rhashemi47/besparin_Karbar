package com.besparina.it.karbar;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Created by hashemi on 10/15/2018.
 */

public class StartServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//
            //*****************************************ServiceGetLocation******************************************
            ComponentName serviceComponent_SchaduleServiceGetLocation = new ComponentName(context, ServiceGetServiceSaved.class);
            JobInfo.Builder builder_SchaduleServiceGetLocation = null;
            builder_SchaduleServiceGetLocation = new JobInfo.Builder(0, serviceComponent_SchaduleServiceGetLocation);
            builder_SchaduleServiceGetLocation.setMinimumLatency(5 * 1000); // wait at least
            builder_SchaduleServiceGetLocation.setOverrideDeadline(50 * 1000); // maximum delay
            builder_SchaduleServiceGetLocation.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
            builder_SchaduleServiceGetLocation.setRequiresDeviceIdle(false); // device should be idle
            builder_SchaduleServiceGetLocation.setRequiresCharging(false); // we don't care if the device is charging or not
            JobScheduler jobScheduler_SchaduleServiceGetLocation = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                jobScheduler_SchaduleServiceGetLocation = context.getSystemService(JobScheduler.class);
            }
            jobScheduler_SchaduleServiceGetLocation.schedule(builder_SchaduleServiceGetLocation.build());
            //*****************************************ServiceGetServiceSaved******************************************
            ComponentName serviceComponent_SchaduleServiceGetServiceSaved = new ComponentName(context, SchaduleServiceGetServiceSaved.class);
            JobInfo.Builder builder_SchaduleServiceGetServiceSaved = null;
            builder_SchaduleServiceGetServiceSaved = new JobInfo.Builder(1, serviceComponent_SchaduleServiceGetServiceSaved);
            builder_SchaduleServiceGetServiceSaved.setMinimumLatency(5 * 1000); // wait at least
            builder_SchaduleServiceGetServiceSaved.setOverrideDeadline(50 * 1000); // maximum delay
            builder_SchaduleServiceGetServiceSaved.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
            builder_SchaduleServiceGetServiceSaved.setRequiresDeviceIdle(false); // device should be idle
            builder_SchaduleServiceGetServiceSaved.setRequiresCharging(false); // we don't care if the device is charging or not
            JobScheduler jobScheduler_SchaduleServiceGetServiceSaved = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                jobScheduler_SchaduleServiceGetServiceSaved = context.getSystemService(JobScheduler.class);
            }
            jobScheduler_SchaduleServiceGetServiceSaved.schedule(builder_SchaduleServiceGetServiceSaved.build());

            //*****************************************SchaduleServiceGetPerFactor************************************************
            ComponentName serviceComponent_SchaduleServiceGetPerFactor = new ComponentName(context, SchaduleServiceGetPerFactor.class);
            JobInfo.Builder builder_SchaduleServiceGetPerFactor = null;
            builder_SchaduleServiceGetPerFactor = new JobInfo.Builder(2, serviceComponent_SchaduleServiceGetPerFactor);
            builder_SchaduleServiceGetPerFactor.setMinimumLatency(5 * 1000); // wait at least
            builder_SchaduleServiceGetPerFactor.setOverrideDeadline(50 * 1000); // maximum delay
            builder_SchaduleServiceGetPerFactor.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
            builder_SchaduleServiceGetPerFactor.setRequiresDeviceIdle(false); // device should be idle
            builder_SchaduleServiceGetPerFactor.setRequiresCharging(false); // we don't care if the device is charging or not
            JobScheduler jobScheduler_SchaduleServiceGetPerFactor = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                jobScheduler_SchaduleServiceGetPerFactor = context.getSystemService(JobScheduler.class);
            }
            jobScheduler_SchaduleServiceGetPerFactor.schedule(builder_SchaduleServiceGetPerFactor.build());

            //*****************************************SchaduleServiceGetServicesAndServiceDetails******************************************
            ComponentName serviceComponent_SchaduleServiceGetServicesAndServiceDetails = new ComponentName(context, SchaduleServiceGetServicesAndServiceDetails.class);
            JobInfo.Builder builder_SchaduleServiceGetServicesAndServiceDetails = null;
            builder_SchaduleServiceGetServicesAndServiceDetails = new JobInfo.Builder(3, serviceComponent_SchaduleServiceGetServicesAndServiceDetails);
            builder_SchaduleServiceGetServicesAndServiceDetails.setMinimumLatency(5 * 1000); // wait at least
            builder_SchaduleServiceGetServicesAndServiceDetails.setOverrideDeadline(50 * 1000); // maximum delay
            builder_SchaduleServiceGetServicesAndServiceDetails.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
            builder_SchaduleServiceGetServicesAndServiceDetails.setRequiresDeviceIdle(false); // device should be idle
            builder_SchaduleServiceGetServicesAndServiceDetails.setRequiresCharging(false); // we don't care if the device is charging or not
            JobScheduler jobScheduler_SchaduleServiceGetServicesAndServiceDetails = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                jobScheduler_SchaduleServiceGetServicesAndServiceDetails = context.getSystemService(JobScheduler.class);
            }
            jobScheduler_SchaduleServiceGetServicesAndServiceDetails.schedule(builder_SchaduleServiceGetServicesAndServiceDetails.build());

            //*****************************************SchaduleServiceGetServiceVisit******************************************
            ComponentName serviceComponent_SchaduleServiceGetServiceVisit = new ComponentName(context, SchaduleServiceGetServiceVisit.class);
            JobInfo.Builder builder_SchaduleServiceGetServiceVisit = null;
            builder_SchaduleServiceGetServiceVisit = new JobInfo.Builder(4, serviceComponent_SchaduleServiceGetServiceVisit);
            builder_SchaduleServiceGetServiceVisit.setMinimumLatency(5 * 1000); // wait at least
            builder_SchaduleServiceGetServiceVisit.setOverrideDeadline(50 * 1000); // maximum delay
            builder_SchaduleServiceGetServiceVisit.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
            builder_SchaduleServiceGetServiceVisit.setRequiresDeviceIdle(false); // device should be idle
            builder_SchaduleServiceGetServiceVisit.setRequiresCharging(false); // we don't care if the device is charging or not
            JobScheduler jobScheduler_SchaduleServiceGetServiceVisit = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                jobScheduler_SchaduleServiceGetServiceVisit = context.getSystemService(JobScheduler.class);
            }
            jobScheduler_SchaduleServiceGetServiceVisit.schedule(builder_SchaduleServiceGetServiceVisit.build());

            //*****************************************SchaduleServiceGetSliderPic******************************************
            ComponentName serviceComponent_SchaduleServiceGetSliderPic = new ComponentName(context, SchaduleServiceGetSliderPic.class);
            JobInfo.Builder builder_SchaduleServiceGetSliderPic = null;
            builder_SchaduleServiceGetSliderPic = new JobInfo.Builder(5, serviceComponent_SchaduleServiceGetSliderPic);
            builder_SchaduleServiceGetSliderPic.setMinimumLatency(5 * 1000); // wait at least
            builder_SchaduleServiceGetSliderPic.setOverrideDeadline(50 * 1000); // maximum delay
            builder_SchaduleServiceGetSliderPic.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
            builder_SchaduleServiceGetSliderPic.setRequiresDeviceIdle(false); // device should be idle
            builder_SchaduleServiceGetSliderPic.setRequiresCharging(false); // we don't care if the device is charging or not
            JobScheduler jobScheduler_SchaduleServiceGetSliderPic = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                jobScheduler_SchaduleServiceGetSliderPic = context.getSystemService(JobScheduler.class);
            }
            jobScheduler_SchaduleServiceGetSliderPic.schedule(builder_SchaduleServiceGetSliderPic.build());

            //*****************************************SchaduleServiceGetStateAndCity******************************************
            ComponentName serviceComponent_SchaduleServiceGetStateAndCity = new ComponentName(context, SchaduleServiceGetStateAndCity.class);
            JobInfo.Builder builder_SchaduleServiceGetStateAndCity = null;
            builder_SchaduleServiceGetStateAndCity = new JobInfo.Builder(6, serviceComponent_SchaduleServiceGetStateAndCity);
            builder_SchaduleServiceGetStateAndCity.setMinimumLatency(5 * 1000); // wait at least
            builder_SchaduleServiceGetStateAndCity.setOverrideDeadline(50 * 1000); // maximum delay
            builder_SchaduleServiceGetStateAndCity.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
            builder_SchaduleServiceGetStateAndCity.setRequiresDeviceIdle(false); // device should be idle
            builder_SchaduleServiceGetStateAndCity.setRequiresCharging(false); // we don't care if the device is charging or not
            JobScheduler jobScheduler_SchaduleServiceGetStateAndCity = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                jobScheduler_SchaduleServiceGetStateAndCity = context.getSystemService(JobScheduler.class);
            }
            jobScheduler_SchaduleServiceGetStateAndCity.schedule(builder_SchaduleServiceGetStateAndCity.build());

            //*****************************************SchaduleServiceGetStateAndCity******************************************
            ComponentName serviceComponent_SchaduleServiceGetUserServiceStartDate = new ComponentName(context, SchaduleServiceGetUserServiceStartDate.class);
            JobInfo.Builder builder_SchaduleServiceGetUserServiceStartDate = null;
            builder_SchaduleServiceGetUserServiceStartDate = new JobInfo.Builder(7, serviceComponent_SchaduleServiceGetUserServiceStartDate);
            builder_SchaduleServiceGetUserServiceStartDate.setMinimumLatency(5 * 1000); // wait at least
            builder_SchaduleServiceGetUserServiceStartDate.setOverrideDeadline(50 * 1000); // maximum delay
            builder_SchaduleServiceGetUserServiceStartDate.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
            builder_SchaduleServiceGetUserServiceStartDate.setRequiresDeviceIdle(false); // device should be idle
            builder_SchaduleServiceGetUserServiceStartDate.setRequiresCharging(false); // we don't care if the device is charging or not
            JobScheduler jobScheduler_SchaduleServiceGetUserServiceStartDate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                jobScheduler_SchaduleServiceGetUserServiceStartDate = context.getSystemService(JobScheduler.class);
            }
            jobScheduler_SchaduleServiceGetUserServiceStartDate.schedule(builder_SchaduleServiceGetUserServiceStartDate.build());

            //*****************************************SchaduleServiceSyncMessage******************************************
            ComponentName serviceComponent_SchaduleServiceSyncMessage = new ComponentName(context, SchaduleServiceSyncMessage.class);
            JobInfo.Builder builder_SchaduleServiceSyncMessage = null;
            builder_SchaduleServiceSyncMessage = new JobInfo.Builder(7, serviceComponent_SchaduleServiceSyncMessage);
            builder_SchaduleServiceSyncMessage.setMinimumLatency(5 * 1000); // wait at least
            builder_SchaduleServiceSyncMessage.setOverrideDeadline(50 * 1000); // maximum delay
            builder_SchaduleServiceSyncMessage.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
            builder_SchaduleServiceSyncMessage.setRequiresDeviceIdle(false); // device should be idle
            builder_SchaduleServiceSyncMessage.setRequiresCharging(false); // we don't care if the device is charging or not
            JobScheduler jobScheduler_SchaduleServiceSyncMessage = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                jobScheduler_SchaduleServiceSyncMessage = context.getSystemService(JobScheduler.class);
            }
            jobScheduler_SchaduleServiceSyncMessage.schedule(builder_SchaduleServiceSyncMessage.build());

        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                context.startForegroundService(new Intent(context, ServiceGetServiceSaved.class));
//                context.startForegroundService(new Intent(context, ServiceGetLocation.class));
//                context.startForegroundService(new Intent(context, ServiceGetSliderPic.class));
//                context.startForegroundService(new Intent(context, ServiceSyncMessage.class));
//                context.startForegroundService(new Intent(context, ServiceGetServicesAndServiceDetails.class));
//                context.startForegroundService(new Intent(context, ServiceGetPerFactor.class));
//                context.startForegroundService(new Intent(context, ServiceGetServiceVisit.class));
//                context.startForegroundService(new Intent(context, ServiceGetStateAndCity.class));
//                context.startForegroundService(new Intent(context, ServiceGetUserServiceStartDate.class));
//            } else {
//                context.startService(new Intent(context, ServiceGetServiceSaved.class));
//                context.startService(new Intent(context, ServiceGetLocation.class));
//                context.startService(new Intent(context, ServiceGetSliderPic.class));
//                context.startService(new Intent(context, ServiceSyncMessage.class));
//                context.startService(new Intent(context, ServiceGetServicesAndServiceDetails.class));
//                context.startService(new Intent(context, ServiceGetPerFactor.class));
//                context.startService(new Intent(context, ServiceGetServiceVisit.class));
//                context.startService(new Intent(context, ServiceGetStateAndCity.class));
//                context.startService(new Intent(context, ServiceGetUserServiceStartDate.class));
//            }
    }
}
