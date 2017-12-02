package com.teamcaffeine.hotswap.utility;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.teamcaffeine.hotswap.login.LoginActivity;
import com.teamcaffeine.hotswap.navigation.NavigationActivity;

/**
 * Safety class to route users away from activities if their login status is incorrect
 */
public final class SessionHandler {
    public static void shouldLogIn(Context context) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(context, "Login required.", Toast.LENGTH_SHORT).show();
            Intent login = new Intent(context, LoginActivity.class);
            context.startActivity(login);
        }
    }

    public static void alreadyLoggedIn(Context context) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Toast.makeText(context, "Already logged in.", Toast.LENGTH_SHORT).show();
            Intent nav = new Intent(context, NavigationActivity.class);
            context.startActivity(nav);
        }
    }
}
