package com.soloparaapasionados.identidadmobile.fragmentos;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.ServicioLocal.ProgramacionRastreadosGpsServicioLocal;
import com.soloparaapasionados.identidadmobile.ServicioLocal.RastreadorServicio;
import com.soloparaapasionados.identidadmobile.serviciotransporttracker.TrackerService;

/**
 * Created by USUARIO on 10/02/2018.
 */

public class RastreadorFragment  extends Fragment
{
    private static final String ARGUMENTO_IMEI = "argumento_imei";

    private String mImei;

    private static final int PERMISSIONS_REQUEST = 1;
    private static String[] PERMISSIONS_REQUIRED = new String[]
            {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

    private SharedPreferences mPrefs;

    private TextView mTextViewTitle;
    private Button mStartButton;
    private EditText mTransportIdEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private SwitchCompat mSwitchCompat;
    private SwitchCompat switchCompatActivacionProgramacion;
    private Snackbar mSnackbarPermissions;
    private Snackbar mSnackbarGps;


    public RastreadorFragment()
    {
    }

    public static RastreadorFragment newInstance(String imei)
    {
        RastreadorFragment fragment = new RastreadorFragment();

        Bundle args = new Bundle();
        args.putString(ARGUMENTO_IMEI, imei);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImei = getArguments().getString(ARGUMENTO_IMEI);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_rastreador, container, false);

        mStartButton = (Button) root.findViewById(R.id.button_start);
        mStartButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                checkInputFields();
            }
        });

        mTextViewTitle= (TextView) root.findViewById(R.id.title);
        mTransportIdEditText = (EditText) root.findViewById(R.id.transport_id);
        mEmailEditText = (EditText) root.findViewById(R.id.email);
        mPasswordEditText = (EditText) root.findViewById(R.id.password);

        mPrefs = getActivity().getSharedPreferences(getString(R.string.prefs), getActivity().MODE_PRIVATE);
        String transportID = mPrefs.getString(getString(R.string.transport_id), "");
        String email = mPrefs.getString(getString(R.string.email), "");
        String password = mPrefs.getString(getString(R.string.password), "");

        mTransportIdEditText.setText(transportID);
        mEmailEditText.setText(email);
        mPasswordEditText.setText(password);

        switchCompatActivacionProgramacion = (SwitchCompat) root.findViewById(R.id.switchCompatActivacionProgramacion);
        switchCompatActivacionProgramacion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (((SwitchCompat) v).isChecked())
                {
                    Intent intentProgrmacionRastreoGps = new Intent(getActivity(), ProgramacionRastreadosGpsServicioLocal.class);
                    intentProgrmacionRastreoGps.setAction(ProgramacionRastreadosGpsServicioLocal.ACCION_REACTIVAR_PROGRAMACION_RASTREO_GPS_ISERVICE);
                    getActivity().startService(intentProgrmacionRastreoGps);
                }
                else
                {
                   /// confirmStop();
                }
            }
        });

        mSwitchCompat = (SwitchCompat) root.findViewById(R.id.switchCompat);
        mSwitchCompat.setEnabled(mTransportIdEditText.length() > 0 && mEmailEditText.length() > 0 && mPasswordEditText.length() > 0);
        mSwitchCompat.setChecked(mStartButton.getVisibility() != View.VISIBLE);
        mSwitchCompat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (((SwitchCompat) v).isChecked())
                {
                    checkInputFields();
                }
                else
                {
                    confirmStop();
                }
            }
        });

        if (isServiceRunning(RastreadorServicio.class))
        {
            // If service already running, simply update UI.
            setTrackingStatus(R.string.tracking);
        } else if (transportID.length() > 0 && email.length() > 0 && password.length() > 0)
        {
            // Inputs have previously been stored, start validation.
            checkLocationPermission();
        } else
        {
            // First time running - check for inputs pre-populated from build.
            mTransportIdEditText.setText(getString(R.string.build_transport_id));
            mEmailEditText.setText(getString(R.string.build_email));
            mPasswordEditText.setText(getString(R.string.build_password));
        }

        return root;
    }

    private boolean isServiceRunning(Class<?> serviceClass)
    {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if (serviceClass.getName().equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }

    private void setTrackingStatus(int status)
    {
        boolean tracking = status == R.string.tracking;

        mTransportIdEditText.setEnabled(!tracking);
        mEmailEditText.setEnabled(!tracking);
        mPasswordEditText.setEnabled(!tracking);
        mStartButton.setVisibility(tracking ? View.INVISIBLE : View.VISIBLE);

        if (mSwitchCompat != null)
        {
            // Initial broadcast may come before menu has been initialized.
            mSwitchCompat.setChecked(tracking);
        }

        mTextViewTitle.setText(getString(status));
    }

    /**
     * First validation check - ensures that required inputs have been
     * entered, and if so, store them and runs the next check.
     */
    private void checkInputFields()
    {
        if (mTransportIdEditText.length() == 0 || mEmailEditText.length() == 0 || mPasswordEditText.length() == 0)
        {
            Toast.makeText(getActivity(), R.string.missing_inputs, Toast.LENGTH_SHORT).show();
        }
        else
        {
            // Store values.
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString(getString(R.string.transport_id), mTransportIdEditText.getText().toString());
            editor.putString(getString(R.string.email), mEmailEditText.getText().toString());
            editor.putString(getString(R.string.password), mPasswordEditText.getText().toString());
            editor.apply();
            // Validate permissions.
            checkLocationPermission();
            mSwitchCompat.setEnabled(true);
        }
    }

    /**
     * Second validation check - ensures the app has location permissions, and
     * if not, requests them, otherwise runs the next check.
     */
    private void checkLocationPermission()
    {
        int locationPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        int storagePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (locationPermission != PackageManager.PERMISSION_GRANTED || storagePermission != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST);
        }
        else
        {
            checkGpsEnabled();
        }
    }

    /**
     * Third and final validation check - ensures GPS is enabled, and if not, prompts to
     * enable it, otherwise all checks pass so start the location tracking service.
     */
    private void checkGpsEnabled()
    {
        LocationManager lm = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            reportGpsError();
        }
        else
        {
            resolveGpsError();
            startLocationService();
        }
    }

    private void reportGpsError()
    {
        if (mSwitchCompat != null)
        {
            mSwitchCompat.setChecked(false);
        }

        Snackbar snackbar = Snackbar
                .make(getActivity().findViewById(R.id.rootView), getString(R.string.gps_required), Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.enable, new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });

        // Changing message action button text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void startLocationService()
    {
        // Before we start the service, confirm that we have extra power usage privileges.
        PowerManager pm = (PowerManager) getActivity().getSystemService(getActivity().POWER_SERVICE);
        Intent intent = new Intent();

        if (!pm.isIgnoringBatteryOptimizations(getActivity().getPackageName()))
        {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
            getActivity().startActivity(intent);
        }

        getActivity().startService(new Intent(getActivity(), RastreadorServicio.class));
    }

    private void resolveGpsError()
    {
        if (mSnackbarGps != null)
        {
            mSnackbarGps.dismiss();
            mSnackbarGps = null;
        }
    }

    private void confirmStop()
    {
        mSwitchCompat.setChecked(true);

        new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.confirm_stop))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        mSwitchCompat.setChecked(false);

                        mTransportIdEditText.setEnabled(true);
                        mEmailEditText.setEnabled(true);
                        mPasswordEditText.setEnabled(true);
                        mStartButton.setVisibility(View.VISIBLE);

                        stopLocationService();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void stopLocationService()
    {
        getActivity().stopService(new Intent(getActivity(), RastreadorServicio.class));
    }

    /**
     * Receives status messages from the tracking service.
     */
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            setTrackingStatus(intent.getIntExtra(getString(R.string.status), 0));
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter(TrackerService.STATUS_INTENT));
    }

    @Override
    public void onPause()
    {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

}
