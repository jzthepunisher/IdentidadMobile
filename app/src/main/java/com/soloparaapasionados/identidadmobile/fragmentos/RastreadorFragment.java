package com.soloparaapasionados.identidadmobile.fragmentos;

import android.Manifest;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
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
import com.soloparaapasionados.identidadmobile.alarmas.ReminderAlarmService;
import com.soloparaapasionados.identidadmobile.modelo.Grupo;
import com.soloparaapasionados.identidadmobile.modelo.ProgramacionRastreoGpsDetalle;
import com.soloparaapasionados.identidadmobile.serviciotransporttracker.TrackerService;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.ProgramacionesRastregoGpsDetalleTabla;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.GruposTabla;

/**
 * Created by USUARIO on 10/02/2018.
 */

public class RastreadorFragment  extends Fragment implements  LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String TAG = RastreadorFragment.class.getSimpleName();
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
    private SwitchCompat switchCompatActivaAlarma;
    private Snackbar mSnackbarPermissions;
    private Snackbar mSnackbarGps;

    private SwitchCompat switchCompatRastregoGps;
    private TextView textViewRastreoGpsResumen;
    private SwitchCompat  switchCompatEstadoRastreoGps;
    private TextView textViewEstadoRastreoGpsResumen;
    private TextView  textViewTipoRastreoGpsResumen;

    private SwitchCompat switchCompatProgramacionLunes;
    private TextView  textViewProgramacionLunesResumen;
    private TextView textViewRangoLunesResumen;
    private TextView  textViewIntervaloLunes;

    private SwitchCompat switchCompatProgramacionMartes;
    private TextView  textViewProgramacionMartesResumen;
    private TextView textViewRangoMartesResumen;
    private TextView  textViewIntervaloMartes;

    private SwitchCompat switchCompatProgramacionMiercoles;
    private TextView  textViewProgramacionMiercolesResumen;
    private TextView textViewRangoMiercolesResumen;
    private TextView  textViewIntervaloMiercoles;

    private SwitchCompat switchCompatProgramacionJueves;
    private TextView  textViewProgramacionJuevesResumen;
    private TextView textViewRangoJuevesResumen;
    private TextView  textViewIntervaloJueves;

    private SwitchCompat switchCompatProgramacionViernes;
    private TextView  textViewProgramacionViernesResumen;
    private TextView textViewRangoViernesResumen;
    private TextView  textViewIntervaloViernes;

    private SwitchCompat switchCompatProgramacionSabado;
    private TextView  textViewProgramacionSabadoResumen;
    private TextView textViewRangoSabadoResumen;
    private TextView  textViewIntervaloSabado;

    private SwitchCompat switchCompatProgramacionDomingo;
    private TextView  textViewProgramacionDomingoResumen;
    private TextView textViewRangoDomingoResumen;
    private TextView  textViewIntervaloDomingo;

    private int idCursor;
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

        inicialiarControles(root);

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

        switchCompatActivaAlarma = (SwitchCompat) root.findViewById(R.id.switchCompatActivaAlarma);
        switchCompatActivaAlarma.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (((SwitchCompat) v).isChecked())
                {
                    Intent action = new Intent(getActivity(), ReminderAlarmService.class);
                    ///////action.setData(uri);
                    action.putExtra(ProgramacionRastreadosGpsServicioLocal.EXTRA_ACTIVAR_DESACTIVAR_SERVICIO_RASTREO,ProgramacionRastreadosGpsServicioLocal.ACTIVAR);
                    //return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
                    getActivity().startService(action);

                    /*Intent intentProgrmacionRastreoGps = new Intent(getActivity(), ProgramacionRastreadosGpsServicioLocal.class);
                    intentProgrmacionRastreoGps.setAction(ProgramacionRastreadosGpsServicioLocal.ACCION_REACTIVAR_PROGRAMACION_RASTREO_GPS_ISERVICE);
                    getActivity().startService(intentProgrmacionRastreoGps);*/
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

        muestraGrupoInformacion();
        //muestraProgramacionRastreGpsDetalleInformacion();

        // Iniciar loader
        getActivity().getSupportLoaderManager().restartLoader(1,null,this);

        return root;
    }

    private  void inicialiarControles(View root)
    {
        switchCompatRastregoGps = (SwitchCompat) root.findViewById(R.id.switchCompatRastregoGps);
        textViewRastreoGpsResumen= (TextView) root.findViewById(R.id.textViewRastreoGpsResumen);
        switchCompatEstadoRastreoGps = (SwitchCompat) root.findViewById(R.id.switchCompatEstadoRastreoGps);
        textViewEstadoRastreoGpsResumen = (TextView) root.findViewById(R.id.textViewEstadoRastreoGpsResumen);
        textViewTipoRastreoGpsResumen= (TextView) root.findViewById(R.id.textViewTipoRastreoGpsResumen);


        switchCompatProgramacionLunes=(SwitchCompat) root.findViewById(R.id.switchCompatProgramacionLunes);
        textViewProgramacionLunesResumen= (TextView) root.findViewById(R.id.textViewProgramacionLunesResumen);
        textViewRangoLunesResumen= (TextView) root.findViewById(R.id.textViewRangoLunesResumen);
        textViewIntervaloLunes= (TextView) root.findViewById(R.id.textViewIntervaloLunes);

        switchCompatProgramacionMartes=(SwitchCompat) root.findViewById(R.id.switchCompatProgramacionMartes);
        textViewProgramacionMartesResumen= (TextView) root.findViewById(R.id.textViewProgramacionMartesResumen);
        textViewRangoMartesResumen = (TextView) root.findViewById(R.id.textViewRangoLunesResumen);
        textViewIntervaloMartes = (TextView) root.findViewById(R.id.textViewIntervaloMartes);

        switchCompatProgramacionMiercoles=(SwitchCompat) root.findViewById(R.id.switchCompatProgramacionMiercoles);
        textViewProgramacionMiercolesResumen= (TextView) root.findViewById(R.id.textViewProgramacionMiercolesResumen);
        textViewRangoMiercolesResumen = (TextView) root.findViewById(R.id.textViewRangoMiercolesResumen);
        textViewIntervaloMiercoles = (TextView) root.findViewById(R.id.textViewIntervaloMiercoles);

        switchCompatProgramacionJueves=(SwitchCompat) root.findViewById(R.id.switchCompatProgramacionJueves);
        textViewProgramacionJuevesResumen= (TextView) root.findViewById(R.id.textViewProgramacionJuevesResumen);
        textViewRangoJuevesResumen = (TextView) root.findViewById(R.id.textViewRangoJuevesResumen);
        textViewIntervaloJueves = (TextView) root.findViewById(R.id.textViewIntervaloJueves);

        switchCompatProgramacionViernes=(SwitchCompat) root.findViewById(R.id.switchCompatProgramacionViernes);
        textViewProgramacionViernesResumen= (TextView) root.findViewById(R.id.textViewProgramacionViernesResumen);
        textViewRangoViernesResumen = (TextView) root.findViewById(R.id.textViewRangoViernesResumen);
        textViewIntervaloViernes = (TextView) root.findViewById(R.id.textViewIntervaloViernes);

        switchCompatProgramacionSabado=(SwitchCompat) root.findViewById(R.id.switchCompatProgramacionSabado);
        textViewProgramacionSabadoResumen= (TextView) root.findViewById(R.id.textViewProgramacionSabadoResumen);
        textViewRangoSabadoResumen = (TextView) root.findViewById(R.id.textViewRangoSabadoResumen);
        textViewIntervaloSabado = (TextView) root.findViewById(R.id.textViewIntervaloSabado);

        switchCompatProgramacionDomingo=(SwitchCompat) root.findViewById(R.id.switchCompatProgramacionDomingo);
        textViewProgramacionDomingoResumen= (TextView) root.findViewById(R.id.textViewProgramacionDomingoResumen);
        textViewRangoDomingoResumen = (TextView) root.findViewById(R.id.textViewRangoDomingoResumen);
        textViewIntervaloDomingo = (TextView) root.findViewById(R.id.textViewIntervaloDomingo);
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

        if (tracking)
        {
            switchCompatEstadoRastreoGps.setChecked(true);
            textViewEstadoRastreoGpsResumen.setText("Actualmente sí está enviando su ubicación el dispositivo");
        }
        else
        {
            switchCompatEstadoRastreoGps.setChecked(false);
            textViewEstadoRastreoGpsResumen.setText("Actualmente nó está enviando su ubicación el dispositivo");
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
        switchCompatEstadoRastreoGps.setChecked(false);
        textViewEstadoRastreoGpsResumen.setText("Actualmente nó está enviando su ubicación el dispositivo");


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

    private void muestraGrupoInformacion()
    {
        try
        {
            Boolean rastreoGpsDia=false;

            ContentResolver resolver = getActivity().getContentResolver();

            Uri uri =  GruposTabla.crearUriGrupoTablaLista();

            Cursor cursorGrupo = resolver.query(uri, null, null, null, null);
            assert cursorGrupo != null;

            Log.i(TAG, "Se encontraron " + cursorGrupo.getCount() + " registros locales.");
            // Encontrar datos obsoletos
            if (cursorGrupo.moveToNext()== true)
            {
                Grupo grupo = new Grupo(cursorGrupo);

                // Rastreo Gps
                switchCompatRastregoGps.setEnabled(false);
                if (grupo.getRastreoGps() == true)
                {
                    switchCompatRastregoGps.setChecked(true);
                    textViewRastreoGpsResumen.setText("Este dispositivo sí envía su ubicación a la central");
                }
                else
                {
                    switchCompatRastregoGps.setChecked(false);
                    textViewRastreoGpsResumen.setText("Este dispositivo nó envía su ubicación a la central");
                }

                textViewTipoRastreoGpsResumen.setText("La programación de rastreo GPS asignado es : " + grupo.getIdProgramacionRastreoGps());

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    /*private void muestraProgramacionRastreGpsDetalleInformacion()
    {
        try
        {
            ContentResolver resolver = getActivity().getContentResolver();

            Uri uri =  ProgramacionesRastregoGpsDetalleTabla.crearUriProgramacionRastreoGpsDetalleTablaLista();

            Cursor cursorProgramacionRastreoGpsDetalle = resolver.query(uri, null, null, null, null);
            assert cursorProgramacionRastreoGpsDetalle != null;

            Log.i(TAG, "Se encontraron " + cursorProgramacionRastreoGpsDetalle.getCount() + " registros locales d.");



                    // Encontrar datos obsoletos
                    while (cursorProgramacionRastreoGpsDetalle.moveToNext())
                    {
                        ProgramacionRastreoGpsDetalle programacionRastreoGpsDetalle = new ProgramacionRastreoGpsDetalle(cursorProgramacionRastreoGpsDetalle);

                        if (programacionRastreoGpsDetalle.getDia().equals("Lunes"))
                        {
                            switchCompatProgramacionLunes.setEnabled(false);
                            if (programacionRastreoGpsDetalle.getRastreoGps()==true)
                            {
                                switchCompatProgramacionLunes.setChecked(true);
                                textViewProgramacionLunesResumen.setText("Este día sí será rastreado el dispostivo");
                            }
                            else
                            {
                                switchCompatProgramacionLunes.setChecked(false);
                                textViewProgramacionLunesResumen.setText("Este día nó será rastreado el dispostivo");
                            }

                            if(programacionRastreoGpsDetalle.getRangoHoraInicio() !=null && programacionRastreoGpsDetalle.getRangoHoraFinal() != null)
                            {
                                textViewRangoLunesResumen.setText("Desde " + programacionRastreoGpsDetalle.getRangoHoraInicio() + " hasta " +
                                        programacionRastreoGpsDetalle.getRangoHoraFinal() + " el dispositivo será rastreado");
                            }
                            else
                            {
                                textViewRangoLunesResumen.setText("Desde 0 hasta 0 el dispositivo será rastreado");
                            }


                            textViewIntervaloLunes.setText("Cada " + programacionRastreoGpsDetalle.getIntervaloHoraCantidad() + " hora(s) " +
                                    programacionRastreoGpsDetalle.getIntervaloMinutoCantidad() + " minuto(s) " +
                                    programacionRastreoGpsDetalle.getIntervaloSegundoCantidad() + " segundo(s) enviará la ubicación el dispositivo");
                        }

                        if (programacionRastreoGpsDetalle.getDia().equals("Martes"))
                        {
                            switchCompatProgramacionMartes.setEnabled(false);
                            if (programacionRastreoGpsDetalle.getRastreoGps()==true)
                            {
                                switchCompatProgramacionMartes.setChecked(true);
                                textViewProgramacionMartesResumen.setText("Este día sí será rastreado el dispostivo");
                            }
                            else
                            {
                                switchCompatProgramacionMartes.setChecked(false);
                                textViewProgramacionMartesResumen.setText("Este día nó será rastreado el dispostivo");
                            }

                            if(programacionRastreoGpsDetalle.getRangoHoraInicio() !=null && programacionRastreoGpsDetalle.getRangoHoraFinal() != null)
                            {
                                textViewRangoMartesResumen.setText("Desde " + programacionRastreoGpsDetalle.getRangoHoraInicio() + " hasta " +
                                        programacionRastreoGpsDetalle.getRangoHoraFinal() + " el dispositivo será rastreado");
                            }
                            else
                            {
                                textViewRangoMartesResumen.setText("Desde 0 hasta 0 el dispositivo será rastreado");
                            }


                            textViewIntervaloMartes.setText("Cada " + programacionRastreoGpsDetalle.getIntervaloHoraCantidad() + " hora(s) " +
                                    programacionRastreoGpsDetalle.getIntervaloMinutoCantidad() + " minuto(s) " +
                                    programacionRastreoGpsDetalle.getIntervaloSegundoCantidad() + " segundo(s) enviará la ubicación el dispositivo");
                        }

                        if (programacionRastreoGpsDetalle.getDia().equals("Miercoles"))
                        {
                            switchCompatProgramacionMiercoles.setEnabled(false);
                            if (programacionRastreoGpsDetalle.getRastreoGps()==true)
                            {
                                switchCompatProgramacionMiercoles.setChecked(true);
                                textViewProgramacionMiercolesResumen.setText("Este día sí será rastreado el dispostivo");
                            }
                            else
                            {
                                switchCompatProgramacionMiercoles.setChecked(false);
                                textViewProgramacionMiercolesResumen.setText("Este día nó será rastreado el dispostivo");
                            }

                            if(programacionRastreoGpsDetalle.getRangoHoraInicio() !=null && programacionRastreoGpsDetalle.getRangoHoraFinal() != null)
                            {
                                textViewRangoMiercolesResumen.setText("Desde " + programacionRastreoGpsDetalle.getRangoHoraInicio() + " hasta " +
                                        programacionRastreoGpsDetalle.getRangoHoraFinal() + " el dispositivo será rastreado");
                            }
                            else
                            {
                                textViewRangoMiercolesResumen.setText("Desde 0 hasta 0 el dispositivo será rastreado");
                            }


                            textViewIntervaloMiercoles.setText("Cada " + programacionRastreoGpsDetalle.getIntervaloHoraCantidad() + " hora(s) " +
                                    programacionRastreoGpsDetalle.getIntervaloMinutoCantidad() + " minuto(s) " +
                                    programacionRastreoGpsDetalle.getIntervaloSegundoCantidad() + " segundo(s) enviará la ubicación el dispositivo");
                        }

                        if (programacionRastreoGpsDetalle.getDia().equals("Jueves"))
                        {
                            switchCompatProgramacionJueves.setEnabled(false);
                            if (programacionRastreoGpsDetalle.getRastreoGps()==true)
                            {
                                switchCompatProgramacionJueves.setChecked(true);
                                textViewProgramacionJuevesResumen.setText("Este día sí será rastreado el dispostivo");
                            }
                            else
                            {
                                switchCompatProgramacionJueves.setChecked(false);
                                textViewProgramacionJuevesResumen.setText("Este día nó será rastreado el dispostivo");
                            }

                            if(programacionRastreoGpsDetalle.getRangoHoraInicio() !=null && programacionRastreoGpsDetalle.getRangoHoraFinal() != null)
                            {
                                textViewRangoJuevesResumen.setText("Desde " + programacionRastreoGpsDetalle.getRangoHoraInicio() + " hasta " +
                                        programacionRastreoGpsDetalle.getRangoHoraFinal() + " el dispositivo será rastreado");
                            }
                            else
                            {
                                textViewRangoJuevesResumen.setText("Desde 0 hasta 0 el dispositivo será rastreado");
                            }


                            textViewIntervaloJueves.setText("Cada " + programacionRastreoGpsDetalle.getIntervaloHoraCantidad() + " hora(s) " +
                                    programacionRastreoGpsDetalle.getIntervaloMinutoCantidad() + " minuto(s) " +
                                    programacionRastreoGpsDetalle.getIntervaloSegundoCantidad() + " segundo(s) enviará la ubicación el dispositivo");
                        }

                        if (programacionRastreoGpsDetalle.getDia().equals("Viernes"))
                        {
                            switchCompatProgramacionViernes.setEnabled(false);
                            if (programacionRastreoGpsDetalle.getRastreoGps()==true)
                            {
                                switchCompatProgramacionViernes.setChecked(true);
                                textViewProgramacionViernesResumen.setText("Este día sí será rastreado el dispostivo");
                            }
                            else
                            {
                                switchCompatProgramacionViernes.setChecked(false);
                                textViewProgramacionViernesResumen.setText("Este día nó será rastreado el dispostivo");
                            }

                            if(programacionRastreoGpsDetalle.getRangoHoraInicio() !=null && programacionRastreoGpsDetalle.getRangoHoraFinal() != null)
                            {
                                textViewRangoViernesResumen.setText("Desde " + programacionRastreoGpsDetalle.getRangoHoraInicio() + " hasta " +
                                        programacionRastreoGpsDetalle.getRangoHoraFinal() + " el dispositivo será rastreado");
                            }
                            else
                            {
                                textViewRangoViernesResumen.setText("Desde 0 hasta 0 el dispositivo será rastreado");
                            }


                            textViewIntervaloViernes.setText("Cada " + programacionRastreoGpsDetalle.getIntervaloHoraCantidad() + " hora(s) " +
                                    programacionRastreoGpsDetalle.getIntervaloMinutoCantidad() + " minuto(s) " +
                                    programacionRastreoGpsDetalle.getIntervaloSegundoCantidad() + " segundo(s) enviará la ubicación el dispositivo");
                        }

                        if (programacionRastreoGpsDetalle.getDia().equals("Sabado"))
                        {
                            switchCompatProgramacionSabado.setEnabled(false);
                            if (programacionRastreoGpsDetalle.getRastreoGps()==true)
                            {
                                switchCompatProgramacionSabado.setChecked(true);
                                textViewProgramacionSabadoResumen.setText("Este día sí será rastreado el dispostivo");
                            }
                            else
                            {
                                switchCompatProgramacionSabado.setChecked(false);
                                textViewProgramacionSabadoResumen.setText("Este día nó será rastreado el dispostivo");
                            }

                            if(programacionRastreoGpsDetalle.getRangoHoraInicio() !=null && programacionRastreoGpsDetalle.getRangoHoraFinal() != null)
                            {
                                textViewRangoSabadoResumen.setText("Desde " + programacionRastreoGpsDetalle.getRangoHoraInicio() + " hasta " +
                                        programacionRastreoGpsDetalle.getRangoHoraFinal() + " el dispositivo será rastreado");
                            }
                            else
                            {
                                textViewRangoSabadoResumen.setText("Desde 0 hasta 0 el dispositivo será rastreado");
                            }

                            textViewIntervaloSabado.setText("Cada " + programacionRastreoGpsDetalle.getIntervaloHoraCantidad() + " hora(s) " +
                                    programacionRastreoGpsDetalle.getIntervaloMinutoCantidad() + " minuto(s) " +
                                    programacionRastreoGpsDetalle.getIntervaloSegundoCantidad() + " segundo(s) enviará la ubicación el dispositivo");
                        }

                        if (programacionRastreoGpsDetalle.getDia().equals("Domingo"))
                        {
                            switchCompatProgramacionDomingo.setEnabled(false);
                            if (programacionRastreoGpsDetalle.getRastreoGps()==true)
                            {
                                switchCompatProgramacionDomingo.setChecked(true);
                                textViewProgramacionDomingoResumen.setText("Este día sí será rastreado el dispostivo");
                            }
                            else
                            {
                                switchCompatProgramacionDomingo.setChecked(false);
                                textViewProgramacionDomingoResumen.setText("Este día nó será rastreado el dispostivo");
                            }

                            if(programacionRastreoGpsDetalle.getRangoHoraInicio() !=null && programacionRastreoGpsDetalle.getRangoHoraFinal() != null)
                            {
                                textViewRangoDomingoResumen.setText("Desde " + programacionRastreoGpsDetalle.getRangoHoraInicio() + " hasta " +
                                        programacionRastreoGpsDetalle.getRangoHoraFinal() + " el dispositivo será rastreado");
                            }
                            else
                            {
                                textViewRangoDomingoResumen.setText("Desde 0 hasta 0 el dispositivo será rastreado");
                            }

                            textViewIntervaloDomingo.setText("Cada " + programacionRastreoGpsDetalle.getIntervaloHoraCantidad() + " hora(s) " +
                                    programacionRastreoGpsDetalle.getIntervaloMinutoCantidad() + " minuto(s) " +
                                    programacionRastreoGpsDetalle.getIntervaloSegundoCantidad() + " segundo(s) enviará la ubicación el dispositivo");
                        }

                    }



            cursorProgramacionRastreoGpsDetalle.close();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }*/

    private void muestraProgramacionRastreGpsDetalleInformacion(Cursor cursorProgramacionRastreoGpsDetalle)
    {
        try
        {


            assert cursorProgramacionRastreoGpsDetalle != null;

            Log.i(TAG, "Se encontraron " + cursorProgramacionRastreoGpsDetalle.getCount() + " registros locales d.");

           /* if (cursorProgramacionRastreoGpsDetalle.getCount()>=1)
            {
                cursorProgramacionRastreoGpsDetalle.moveToPosition(-1);
*/

            // Encontrar datos obsoletos
            while (cursorProgramacionRastreoGpsDetalle.moveToNext())
            {
                Log.i(TAG, "cursorProgramacionRastreoGpsDetalle Posicion " + cursorProgramacionRastreoGpsDetalle.getPosition());

                ProgramacionRastreoGpsDetalle programacionRastreoGpsDetalle = new ProgramacionRastreoGpsDetalle(cursorProgramacionRastreoGpsDetalle);

                if (programacionRastreoGpsDetalle.getDia().equals("Lunes"))
                {
                    switchCompatProgramacionLunes.setEnabled(false);
                    if (programacionRastreoGpsDetalle.getRastreoGps()==true)
                    {
                        switchCompatProgramacionLunes.setChecked(true);
                        textViewProgramacionLunesResumen.setText("Este día sí será rastreado el dispostivo");
                    }
                    else
                    {
                        switchCompatProgramacionLunes.setChecked(false);
                        textViewProgramacionLunesResumen.setText("Este día nó será rastreado el dispostivo");
                    }

                    if(programacionRastreoGpsDetalle.getRangoHoraInicio() !=null && programacionRastreoGpsDetalle.getRangoHoraFinal() != null)
                    {
                        textViewRangoLunesResumen.setText("Desde " + programacionRastreoGpsDetalle.getRangoHoraInicio() + " hasta " +
                                programacionRastreoGpsDetalle.getRangoHoraFinal() + " el dispositivo será rastreado");
                    }
                    else
                    {
                        textViewRangoLunesResumen.setText("Desde 0 hasta 0 el dispositivo será rastreado");
                    }


                    textViewIntervaloLunes.setText("Cada " + programacionRastreoGpsDetalle.getIntervaloHoraCantidad() + " hora(s) " +
                            programacionRastreoGpsDetalle.getIntervaloMinutoCantidad() + " minuto(s) " +
                            programacionRastreoGpsDetalle.getIntervaloSegundoCantidad() + " segundo(s) enviará la ubicación el dispositivo");
                }

                if (programacionRastreoGpsDetalle.getDia().equals("Martes"))
                {
                    switchCompatProgramacionMartes.setEnabled(false);
                    if (programacionRastreoGpsDetalle.getRastreoGps()==true)
                    {
                        switchCompatProgramacionMartes.setChecked(true);
                        textViewProgramacionMartesResumen.setText("Este día sí será rastreado el dispostivo");
                    }
                    else
                    {
                        switchCompatProgramacionMartes.setChecked(false);
                        textViewProgramacionMartesResumen.setText("Este día nó será rastreado el dispostivo");
                    }

                    if(programacionRastreoGpsDetalle.getRangoHoraInicio() !=null && programacionRastreoGpsDetalle.getRangoHoraFinal() != null)
                    {
                        textViewRangoMartesResumen.setText("Desde " + programacionRastreoGpsDetalle.getRangoHoraInicio() + " hasta " +
                                programacionRastreoGpsDetalle.getRangoHoraFinal() + " el dispositivo será rastreado");
                    }
                    else
                    {
                        textViewRangoMartesResumen.setText("Desde 0 hasta 0 el dispositivo será rastreado");
                    }


                    textViewIntervaloMartes.setText("Cada " + programacionRastreoGpsDetalle.getIntervaloHoraCantidad() + " hora(s) " +
                            programacionRastreoGpsDetalle.getIntervaloMinutoCantidad() + " minuto(s) " +
                            programacionRastreoGpsDetalle.getIntervaloSegundoCantidad() + " segundo(s) enviará la ubicación el dispositivo");
                }

                if (programacionRastreoGpsDetalle.getDia().equals("Miercoles"))
                {
                    switchCompatProgramacionMiercoles.setEnabled(false);
                    if (programacionRastreoGpsDetalle.getRastreoGps()==true)
                    {
                        switchCompatProgramacionMiercoles.setChecked(true);
                        textViewProgramacionMiercolesResumen.setText("Este día sí será rastreado el dispostivo");
                    }
                    else
                    {
                        switchCompatProgramacionMiercoles.setChecked(false);
                        textViewProgramacionMiercolesResumen.setText("Este día nó será rastreado el dispostivo");
                    }

                    if(programacionRastreoGpsDetalle.getRangoHoraInicio() !=null && programacionRastreoGpsDetalle.getRangoHoraFinal() != null)
                    {
                        textViewRangoMiercolesResumen.setText("Desde " + programacionRastreoGpsDetalle.getRangoHoraInicio() + " hasta " +
                                programacionRastreoGpsDetalle.getRangoHoraFinal() + " el dispositivo será rastreado");
                    }
                    else
                    {
                        textViewRangoMiercolesResumen.setText("Desde 0 hasta 0 el dispositivo será rastreado");
                    }


                    textViewIntervaloMiercoles.setText("Cada " + programacionRastreoGpsDetalle.getIntervaloHoraCantidad() + " hora(s) " +
                            programacionRastreoGpsDetalle.getIntervaloMinutoCantidad() + " minuto(s) " +
                            programacionRastreoGpsDetalle.getIntervaloSegundoCantidad() + " segundo(s) enviará la ubicación el dispositivo");
                }

                if (programacionRastreoGpsDetalle.getDia().equals("Jueves"))
                {
                    switchCompatProgramacionJueves.setEnabled(false);
                    if (programacionRastreoGpsDetalle.getRastreoGps()==true)
                    {
                        switchCompatProgramacionJueves.setChecked(true);
                        textViewProgramacionJuevesResumen.setText("Este día sí será rastreado el dispostivo");
                    }
                    else
                    {
                        switchCompatProgramacionJueves.setChecked(false);
                        textViewProgramacionJuevesResumen.setText("Este día nó será rastreado el dispostivo");
                    }

                    if(programacionRastreoGpsDetalle.getRangoHoraInicio() !=null && programacionRastreoGpsDetalle.getRangoHoraFinal() != null)
                    {
                        textViewRangoJuevesResumen.setText("Desde " + programacionRastreoGpsDetalle.getRangoHoraInicio() + " hasta " +
                                programacionRastreoGpsDetalle.getRangoHoraFinal() + " el dispositivo será rastreado");
                    }
                    else
                    {
                        textViewRangoJuevesResumen.setText("Desde 0 hasta 0 el dispositivo será rastreado");
                    }


                    textViewIntervaloJueves.setText("Cada " + programacionRastreoGpsDetalle.getIntervaloHoraCantidad() + " hora(s) " +
                            programacionRastreoGpsDetalle.getIntervaloMinutoCantidad() + " minuto(s) " +
                            programacionRastreoGpsDetalle.getIntervaloSegundoCantidad() + " segundo(s) enviará la ubicación el dispositivo");
                }

                if (programacionRastreoGpsDetalle.getDia().equals("Viernes"))
                {
                    switchCompatProgramacionViernes.setEnabled(false);
                    if (programacionRastreoGpsDetalle.getRastreoGps()==true)
                    {
                        switchCompatProgramacionViernes.setChecked(true);
                        textViewProgramacionViernesResumen.setText("Este día sí será rastreado el dispostivo");
                    }
                    else
                    {
                        switchCompatProgramacionViernes.setChecked(false);
                        textViewProgramacionViernesResumen.setText("Este día nó será rastreado el dispostivo");
                    }

                    if(programacionRastreoGpsDetalle.getRangoHoraInicio() !=null && programacionRastreoGpsDetalle.getRangoHoraFinal() != null)
                    {
                        textViewRangoViernesResumen.setText("Desde " + programacionRastreoGpsDetalle.getRangoHoraInicio() + " hasta " +
                                programacionRastreoGpsDetalle.getRangoHoraFinal() + " el dispositivo será rastreado");
                    }
                    else
                    {
                        textViewRangoViernesResumen.setText("Desde 0 hasta 0 el dispositivo será rastreado");
                    }


                    textViewIntervaloViernes.setText("Cada " + programacionRastreoGpsDetalle.getIntervaloHoraCantidad() + " hora(s) " +
                            programacionRastreoGpsDetalle.getIntervaloMinutoCantidad() + " minuto(s) " +
                            programacionRastreoGpsDetalle.getIntervaloSegundoCantidad() + " segundo(s) enviará la ubicación el dispositivo");
                }

                if (programacionRastreoGpsDetalle.getDia().equals("Sabado"))
                {
                    switchCompatProgramacionSabado.setEnabled(false);
                    if (programacionRastreoGpsDetalle.getRastreoGps()==true)
                    {
                        switchCompatProgramacionSabado.setChecked(true);
                        textViewProgramacionSabadoResumen.setText("Este día sí será rastreado el dispostivo");
                    }
                    else
                    {
                        switchCompatProgramacionSabado.setChecked(false);
                        textViewProgramacionSabadoResumen.setText("Este día nó será rastreado el dispostivo");
                    }

                    if(programacionRastreoGpsDetalle.getRangoHoraInicio() !=null && programacionRastreoGpsDetalle.getRangoHoraFinal() != null)
                    {
                        textViewRangoSabadoResumen.setText("Desde " + programacionRastreoGpsDetalle.getRangoHoraInicio() + " hasta " +
                                programacionRastreoGpsDetalle.getRangoHoraFinal() + " el dispositivo será rastreado");
                    }
                    else
                    {
                        textViewRangoSabadoResumen.setText("Desde 0 hasta 0 el dispositivo será rastreado");
                    }

                    textViewIntervaloSabado.setText("Cada " + programacionRastreoGpsDetalle.getIntervaloHoraCantidad() + " hora(s) " +
                            programacionRastreoGpsDetalle.getIntervaloMinutoCantidad() + " minuto(s) " +
                            programacionRastreoGpsDetalle.getIntervaloSegundoCantidad() + " segundo(s) enviará la ubicación el dispositivo");
                }

                if (programacionRastreoGpsDetalle.getDia().equals("Domingo"))
                {
                    switchCompatProgramacionDomingo.setEnabled(false);
                    if (programacionRastreoGpsDetalle.getRastreoGps()==true)
                    {
                        switchCompatProgramacionDomingo.setChecked(true);
                        textViewProgramacionDomingoResumen.setText("Este día sí será rastreado el dispostivo");
                    }
                    else
                    {
                        switchCompatProgramacionDomingo.setChecked(false);
                        textViewProgramacionDomingoResumen.setText("Este día nó será rastreado el dispostivo");
                    }

                    if(programacionRastreoGpsDetalle.getRangoHoraInicio() !=null && programacionRastreoGpsDetalle.getRangoHoraFinal() != null)
                    {
                        textViewRangoDomingoResumen.setText("Desde " + programacionRastreoGpsDetalle.getRangoHoraInicio() + " hasta " +
                                programacionRastreoGpsDetalle.getRangoHoraFinal() + " el dispositivo será rastreado");
                    }
                    else
                    {
                        textViewRangoDomingoResumen.setText("Desde 0 hasta 0 el dispositivo será rastreado");
                    }

                    textViewIntervaloDomingo.setText("Cada " + programacionRastreoGpsDetalle.getIntervaloHoraCantidad() + " hora(s) " +
                            programacionRastreoGpsDetalle.getIntervaloMinutoCantidad() + " minuto(s) " +
                            programacionRastreoGpsDetalle.getIntervaloSegundoCantidad() + " segundo(s) enviará la ubicación el dispositivo");
                }

                Log.i(TAG, "cursorProgramacionRastreoGpsDetalle Posicion fin " + cursorProgramacionRastreoGpsDetalle.getPosition());


            }

            //}
            Log.i(TAG, "cursorProgramacionRastreoGpsDetalle Posicion fin 2 " + cursorProgramacionRastreoGpsDetalle.getPosition());

            cursorProgramacionRastreoGpsDetalle.close();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    //Métodos implementados de la interface de comunicación LoaderManager.LoaderCallbacks<Cursor>
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        idCursor=id;
        switch (id)
        {
            case 1:
                //cursorCargos=getContentResolver().query(ContratoCotizacion.Cargos.crearUriCargoLista(), null, null, null, null);
                return new CursorLoader(getActivity(), ProgramacionesRastregoGpsDetalleTabla.crearUriProgramacionRastreoGpsDetalleTablaLista(), null, null, null, null);

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        //Creando Adaptador para CargoSpinner
        switch (loader.getId())
        {
            case 1:
                if(data!=null)
                {
                    muestraProgramacionRastreGpsDetalleInformacion(data);
                }
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
    ////////////////////////////////////////////////////////////////////////////////////////////

}
