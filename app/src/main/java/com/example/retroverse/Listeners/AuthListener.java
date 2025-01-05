package com.example.retroverse.Listeners;

import android.content.Context;

public interface AuthListener {
    void onValidateLogin(final String token, final Context context);
}
