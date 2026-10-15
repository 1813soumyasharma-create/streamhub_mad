package com.example.streamhub.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import com.example.streamhub.accessibility.HapticManager;
import com.example.streamhub.accessibility.TtsManager;

public class OfflineReceiver extends BroadcastReceiver {

    public interface OnConnectivityChangedListener {
        void onConnectivityChanged(boolean isConnected);
    }

    private final OnConnectivityChangedListener listener;

    public OfflineReceiver(OnConnectivityChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = false;
        if (cm != null) {
            Network activeNetwork = cm.getActiveNetwork();
            NetworkCapabilities caps = cm.getNetworkCapabilities(activeNetwork);
            if (caps != null) {
                isConnected = caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
            }
        }

        if (!isConnected) {
            HapticManager haptic = new HapticManager(context);
            haptic.playAlertPulse();
            TtsManager ttsManager = new TtsManager(context, null);
            ttsManager.speak("Warning. Network connection lost. Offline mode active.");
        }
        if (listener != null) {
            listener.onConnectivityChanged(isConnected);
        }
    }
}
