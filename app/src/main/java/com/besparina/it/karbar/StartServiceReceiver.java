package com.besparina.it.karbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by hashemi on 10/15/2018.
 */

public class StartServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, ServiceGetServiceSaved.class));
        context.startService(new Intent(context, ServiceGetLocation.class));
        context.startService(new Intent(context, ServiceGetSliderPic.class));
        context.startService(new Intent(context, ServiceSyncMessage.class));
        context.startService(new Intent(context, ServiceGetServicesAndServiceDetails.class));
        context.startService(new Intent(context, ServiceGetPerFactor.class));
        context.startService(new Intent(context, ServiceGetServiceVisit.class));
        context.startService(new Intent(context, ServiceGetStateAndCity.class));
        context.startService(new Intent(context, ServiceGetUserServiceStartDate.class));
    }
}
