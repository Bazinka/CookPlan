/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cookplan.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.widget.Toast;

public abstract class PermissionUtils {

    private static final String CONFIRMATION_DIALOG = "ConfirmationDialog";
    private static final String TAG = PermissionUtils.class.getSimpleName();


    public static void requestPermissions(Activity activity, int requestCode, String[] permissions) {

        for (String permission : permissions) {
            if (!isPermissionGranted(activity, permission)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {

                    ConfirmationDialog.newInstance(requestCode, permission).
                            show(activity.getFragmentManager(), CONFIRMATION_DIALOG);
                } else {
                    ActivityCompat.requestPermissions(activity, permissions,
                                                      requestCode);
                }
            }
        }
    }

    /**
     * Requests the fine location permission. If a rationale with an additional explanation should
     * be shown to the user, displays a dialog that triggers the request.
     */
    public static void requestPermissionFromFragment(Fragment fragment, int requestCode,
                                                     String permission, boolean finishActivity) {
        if (fragment.shouldShowRequestPermissionRationale(permission)) {
            // Display a dialog with rationale.
            ConfirmationDialog.newInstance(requestCode, permission)
                    .show(fragment.getActivity().getFragmentManager(), CONFIRMATION_DIALOG);
        } else {
            // Location permission has not been granted yet, request it.
            fragment.requestPermissions(new String[]{permission}, requestCode);

        }
    }

    public static boolean isPermissionsGranted(Context context, String[] permissions) {

        for (String permission : permissions) {
            if (!isPermissionGranted(context, permission)) {
                return false;
            }
        }
        return true;
    }


    public static boolean isPermissionGranted(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context,
                                                  permission)
                == PackageManager.PERMISSION_GRANTED;
    }
    /**
     * Checks if the result contains a {@link PackageManager#PERMISSION_GRANTED} result for a
     * permission from a runtime permissions request.
     *
     * @see androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
     */
    public static boolean isPermissionGranted(String[] grantPermissions, int[] grantResults,
                                              String permission) {
        for (int i = 0; i < grantPermissions.length; i++) {
            if (permission.equals(grantPermissions[i])) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }


    /**
     * Shows OK/Cancel confirmation dialog about permission.
     */
    public static class ConfirmationDialog extends DialogFragment {

        private static final String ARG_PERMISSION = "permission";
        private static final String ARG_REQUEST_CODE = "request_code";

        public static ConfirmationDialog newInstance(int permissionKey, String permissionValue) {
            ConfirmationDialog dialog = new ConfirmationDialog();
            Bundle bundle = new Bundle();
            bundle.putString(ARG_PERMISSION, permissionValue);
            bundle.putInt(ARG_REQUEST_CODE, permissionKey);
            dialog.setArguments(bundle);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            return new AlertDialog.Builder(getActivity())
                    .setMessage("Please allow permission")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ActivityCompat.requestPermissions(getActivity(),
                                                              new String[]{getArguments().getString(ARG_PERMISSION)},
                                                              getArguments().getInt(ARG_REQUEST_CODE));
                        }
                    })
                    .setNegativeButton(android.R.string.cancel,
                                       new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {
                                               Toast.makeText(getActivity(), "Not available", Toast.LENGTH_SHORT).show();
                                           }
                                       })
                    .create();
        }
    }

    /**
     * Shows an error message dialog.
     */
    public static class ErrorDialog extends DialogFragment {

        private static final String ARG_MESSAGE = "message";

        public static ErrorDialog newInstance(String message) {
            ErrorDialog dialog = new ErrorDialog();
            Bundle args = new Bundle();
            args.putString(ARG_MESSAGE, message);
            dialog.setArguments(args);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Activity activity = getActivity();
            return new AlertDialog.Builder(activity)
                    .setMessage(getArguments().getString(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //nothing
                        }
                    })
                    .create();
        }

    }
}
