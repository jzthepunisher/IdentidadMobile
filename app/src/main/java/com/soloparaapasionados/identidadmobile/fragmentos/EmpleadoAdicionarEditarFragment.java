package com.soloparaapasionados.identidadmobile.fragmentos;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.ServicioLocal.EmpleadoServicioLocal;
import com.soloparaapasionados.identidadmobile.aplicacion.Constantes;
import com.soloparaapasionados.identidadmobile.modelo.Empleado;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Empleados;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Cargos;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmpleadoAdicionarEditarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmpleadoAdicionarEditarFragment extends Fragment
implements AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor>,View.OnLongClickListener {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imageViewFotoEmpleado;
    // Lista TextInputEditText
    private TextInputLayout textInputLayoutIdEmpleado;
    private TextInputLayout textInputLayoutNombresEmpleado;
    private TextInputLayout textInputLayoutApellidoPaternoEmpleado;
    private TextInputLayout textInputLayoutApellidoMaternoEmpleado;
    private TextInputLayout textInputLayoutDireccionEmpleado;
    private TextInputLayout textInputLayoutDniEmpleado;
    private TextInputLayout textInputLayoutCelular;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutFechaNacimiento;
    private TextInputLayout textInputLayoutFechaIngreso;
    private TextInputLayout textInputLayoutFechaBaja;
    // Lista EditText
    private EditText editTextIdEmpleado;
    private EditText editTextNombresEmpleado;
    private EditText editTextApellidoPaternoEmpleado;
    private EditText editTextApellidoMaternoEmpleado;
    private EditText editTextDireccionEmpleado;
    private EditText editTextDniEmpleado;
    private EditText editTextCelular;
    private EditText editTextEmail;
    private EditText editTextFechaNacimiento;
    private EditText editTextFechaIngreso;
    private EditText editTextFechaBaja;
    //Lista Spinner
    private Spinner spinnerCargos;

    FloatingActionButton floatingActionButtonGuardar;

    final static String DATE_FORMAT = "dd/MM/yyyy";

    private static final String ARGUMENTO_ID_EMPLEADO = "argumento_id_empleado";

    String codigoCargoSeleccionado;

    private String mIdEmpleado;
    private int idCursor;
    String idCargoEmpleado="";

    /* Adaptadores para los Spinners*/
    SimpleCursorAdapter cargoSpinnerAdapter;

    public EmpleadoAdicionarEditarFragment() {
        // Required empty public constructor
    }

    public static EmpleadoAdicionarEditarFragment newInstance(String idEmpleado) {
        EmpleadoAdicionarEditarFragment fragment = new EmpleadoAdicionarEditarFragment();

        Bundle args = new Bundle();
        args.putString(ARGUMENTO_ID_EMPLEADO, idEmpleado);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIdEmpleado = getArguments().getString(ARGUMENTO_ID_EMPLEADO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_empleado_adicionar_editar, container, false);

        //Referencias Spiner de la UI
        spinnerCargos=(Spinner)root.findViewById(R.id.spinnerCargos);
        // Iniciar loader
        getActivity().getSupportLoaderManager().restartLoader(1,null,this);

        collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapser);
        imageViewFotoEmpleado = (ImageView) getActivity().findViewById(R.id.image_paralax);

        //Referencias TextInputLayout de la UI
        textInputLayoutIdEmpleado =(TextInputLayout) root.findViewById(R.id.textInputLayoutIdEmpleado);
        textInputLayoutNombresEmpleado=(TextInputLayout) root.findViewById(R.id.textInputLayoutNombresEmpleado);
        textInputLayoutApellidoPaternoEmpleado=(TextInputLayout) root.findViewById(R.id.textInputLayoutApellidoPaternoEmpleado);
        textInputLayoutApellidoMaternoEmpleado=(TextInputLayout) root.findViewById(R.id.textInputLayoutApellidoMaternoEmpleado);
        textInputLayoutDireccionEmpleado=(TextInputLayout) root.findViewById(R.id.textInputLayoutDireccionEmpleado);
        textInputLayoutDniEmpleado=(TextInputLayout) root.findViewById(R.id.textInputLayoutDniEmpleado);
        textInputLayoutCelular=(TextInputLayout) root.findViewById(R.id.textInputLayoutCelular);
        textInputLayoutEmail=(TextInputLayout) root.findViewById(R.id.textInputLayoutEmail);
        textInputLayoutFechaNacimiento=(TextInputLayout) root.findViewById(R.id.textInputLayoutFechaNacimiento);
        textInputLayoutFechaIngreso=(TextInputLayout) root.findViewById(R.id.textInputLayoutFechaIngreso);
        textInputLayoutFechaBaja=(TextInputLayout) root.findViewById(R.id.textInputLayoutFechaBaja);
        //Referencias EditText de la UI
        editTextIdEmpleado=(EditText) root.findViewById(R.id.editTextIdEmpleado);
        editTextNombresEmpleado=(EditText) root.findViewById(R.id.editTextNombresEmpleado);
        editTextApellidoPaternoEmpleado=(EditText) root.findViewById(R.id.editTextApellidoPaternoEmpleado);
        editTextApellidoMaternoEmpleado=(EditText) root.findViewById(R.id.editTextApellidoMaternoEmpleado);
        editTextDireccionEmpleado=(EditText) root.findViewById(R.id.editTextDireccionEmpleado);
        editTextDniEmpleado=(EditText) root.findViewById(R.id.editTextDniEmpleado);
        editTextCelular=(EditText) root.findViewById(R.id.editTextCelular);
        editTextEmail=(EditText) root.findViewById(R.id.editTextEmail);
        editTextFechaNacimiento=(EditText) root.findViewById(R.id.editTextFechaNacimiento);
        editTextFechaIngreso=(EditText) root.findViewById(R.id.editTextFechaIngreso);
        editTextFechaBaja=(EditText) root.findViewById(R.id.editTextFechaBaja);

        //Establecer eventos TextWatcher.
        editTextIdEmpleado.addTextChangedListener(new MiTextWatcher(editTextIdEmpleado));
        editTextNombresEmpleado.addTextChangedListener(new MiTextWatcher(editTextNombresEmpleado));
        editTextApellidoPaternoEmpleado.addTextChangedListener(new MiTextWatcher(editTextApellidoPaternoEmpleado));
        editTextApellidoMaternoEmpleado.addTextChangedListener(new MiTextWatcher(editTextApellidoMaternoEmpleado));
        editTextDireccionEmpleado.addTextChangedListener(new MiTextWatcher(editTextDireccionEmpleado));
        editTextDniEmpleado.addTextChangedListener(new MiTextWatcher(editTextDniEmpleado));
        editTextCelular.addTextChangedListener(new MiTextWatcher(editTextCelular));
        editTextEmail.addTextChangedListener(new MiTextWatcher(editTextEmail));
        editTextFechaNacimiento.addTextChangedListener(new MiTextWatcher(editTextFechaNacimiento));
        editTextFechaIngreso.addTextChangedListener(new MiTextWatcher(editTextFechaIngreso));
        editTextFechaBaja.addTextChangedListener(new MiTextWatcher(editTextFechaBaja));
        //Eventos del Spinner de la UI
        spinnerCargos.setOnItemSelectedListener(this);
        //Eventos setOnLongClickListener del EditText Fechas de la UI
        editTextFechaNacimiento.setOnLongClickListener(this);
        editTextFechaIngreso.setOnLongClickListener(this);
        editTextFechaBaja.setOnLongClickListener(this);

        floatingActionButtonGuardar = (FloatingActionButton) getActivity().findViewById(R.id.floatingActionButtonGuardar);

        // Eventos
        floatingActionButtonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarDatosEmpleado();
            }
        });

        this.editTextIdEmpleado.setEnabled(true);
        // Carga de datos
        if (mIdEmpleado != null) {
            cargaEmpleado();
        }

        return root;
    }

    private void cargaEmpleado()
    {
        getActivity().getSupportLoaderManager().restartLoader(2,null,this);
        this.editTextIdEmpleado.setEnabled(false);
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
                return new CursorLoader(getActivity(), Cargos.crearUriCargoLista(), null, null, null, null);
            case 2:
                //cursorCargos=getContentResolver().query(ContratoCotizacion.Cargos.crearUriCargoLista(), null, null, null, null);
                return new CursorLoader(getActivity(), Empleados.crearUriEmpleado(mIdEmpleado), null, null, null, null);

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
                cargoSpinnerAdapter = new SimpleCursorAdapter(getActivity(),
                    android.R.layout.simple_selectable_list_item,
                    data,
                    new String[]{Cargos.DESCRIPCION},
                    new int[]{android.R.id.text1},
                    SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

                    spinnerCargos.setAdapter(cargoSpinnerAdapter);

                    spinnerCargos.setSelection(getIndex(spinnerCargos,Cargos.ID_CARGO,idCargoEmpleado));
            }
            break;
        case 2:

            if (data != null && data.moveToLast())
            {
                muestraEmpleado(data);
                //spinnerCargos.setSelection(getIndex(data,Cargos.ID_CARGO,idCargoEmpleado));
                spinnerCargos.setSelection(getIndex(spinnerCargos,Cargos.ID_CARGO,idCargoEmpleado));
            }
            else
            {
                muestraErrorCarga();
            }

            break;
        }

    }

    private int getIndex(Spinner spinner, String columnName, String searchString) {

        //Log.d(LOG_TAG, "getIndex(" + searchString + ")");

        if (searchString == null || spinner.getCount() == 0) {
            return -1; // Not found
        }
        else {
            try{
                Cursor cursor = (Cursor)spinner.getItemAtPosition(0);

                for (int i = 0; i < spinner.getCount(); i++) {

                    cursor.moveToPosition(i);
                    String itemText = cursor.getString(cursor.getColumnIndex(columnName));

                    if (itemText.equals(searchString)) {
                        return i;
                    }
                }
                return -1; // Not found
            }catch (ClassCastException e){
                return -1;
            }


        }
    }

    private int getIndex(Cursor cursor, String columnName, String searchString) {

        //Log.d(LOG_TAG, "getIndex(" + searchString + ")");

        if (searchString == null || cursor.getCount() == 0) {
            return -1; // Not found
        }
        else {

            for (int i = 0; i < cursor.getCount(); i++) {

                cursor.moveToPosition(i);
                String itemText = cursor.getString(cursor.getColumnIndex(columnName));

                if (itemText.equals(searchString)) {
                    return i;
                }
            }
            return -1; // Not found
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
    ////////////////////////////////////////////////////////////////////////////////////////////

    //Validar datos del empleado
    private void validarDatosEmpleado() {
        boolean error = false;

        if (!esIdEmpleadoValido()) {
            error = true;
        }

        if (!esNombreEmpleadoValido()) {
            error = true;
        }

        if (!esApelllidoPaternoValido()) {
            error = true;
        }

        if (!esApelllidoMaternoValido()) {
            error = true;
        }

        if (!esDireccionValido()) {
            error = true;
        }

        if (!esDNIValido()) {
            error = true;
        }

        if (!esCelularValido()) {
            error = true;
        }

        if (!esEmailValido()) {
            error = true;
        }

        if (!esFechaNacimientoValido()) {
            error = true;
        }

        if (!esFechaIngresoValido()) {
            error = true;
        }

        if (!esFechaBajaValido()) {
            error = true;
        }

        if (error) {
            return;
        }

        Toast.makeText(getActivity(), "Thank You!", Toast.LENGTH_SHORT).show();

        if (mIdEmpleado != null) {
            actualizarEmpleadoLocalmente();
            muestraPantallaDetalleEmpleados();
        }else {
            insertarEmpleadoLocalmente();
            muestraPantallaDetalleEmpleados();
        }
    }

    private void insertarEmpleadoLocalmente()
    {
        Intent intent = new Intent(getActivity(), EmpleadoServicioLocal.class);
        intent.setAction(EmpleadoServicioLocal.ACCION_INSERTAR_EMPLEADO_ISERVICE);
        Empleado empleado = generarEntidadEmpleado();
        intent.putExtra(EmpleadoServicioLocal.EXTRA_MI_EMPLEADO, empleado);
        getActivity().startService(intent);
    }

    private void actualizarEmpleadoLocalmente(){
        Intent intent = new Intent(getActivity(), EmpleadoServicioLocal.class);
        intent.setAction(EmpleadoServicioLocal.ACCION_ACTUALIZAR_EMPLEADO_ISERVICE);
        Empleado empleado=generarEntidadEmpleado();
        intent.putExtra(EmpleadoServicioLocal.EXTRA_MI_EMPLEADO, empleado);
        getActivity().startService(intent);
    }

    //Generar entidad empleado
    private Empleado generarEntidadEmpleado()
    {
        Empleado empleado =new Empleado();

        empleado.setIdEmpleado(this.editTextIdEmpleado.getText().toString().trim());
        empleado.setNombres(this.editTextNombresEmpleado.getText().toString().trim());
        empleado.setApellidoPaterno(this.editTextApellidoPaternoEmpleado.getText().toString().trim());
        empleado.setApellidoMaterno(this.editTextApellidoMaternoEmpleado.getText().toString().trim());
        empleado.setDireccion(this.editTextDireccionEmpleado.getText().toString().trim());
        empleado.setDNI(this.editTextDniEmpleado.getText().toString().trim());
        empleado.setCelular(this.editTextCelular.getText().toString().trim());
        empleado.setEmail(this.editTextEmail.getText().toString().trim());
        empleado.setFechaNacimiento(this.editTextFechaNacimiento.getText().toString().trim());
        empleado.setIdCargo(this.codigoCargoSeleccionado);
        empleado.setFechaIngreso(this.editTextFechaIngreso.getText().toString().trim());
        empleado.setFechaBaja(this.editTextFechaBaja.getText().toString().trim());

        return empleado;
    }

    //Validar datos del empleado online
    private class MiTextWatcher implements TextWatcher {

        private View view;

        private MiTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            int identificadorVista=view.getId();
            switch (identificadorVista)
            {
                case R.id.editTextIdEmpleado:
                    esIdEmpleadoValido();
                    break;
                case R.id.editTextNombresEmpleado:
                    esNombreEmpleadoValido();
                    break;
                case R.id.editTextApellidoPaternoEmpleado:
                    esApelllidoPaternoValido();
                    break;
                case R.id.editTextApellidoMaternoEmpleado:
                    esApelllidoMaternoValido();
                    break;
                case R.id.editTextDireccionEmpleado:
                    esDireccionValido();
                    break;
                case R.id.editTextDniEmpleado:
                    esDNIValido();
                    break;
                case R.id.editTextCelular:
                    esCelularValido();
                    break;
                case R.id.editTextEmail:
                    esEmailValido();
                    break;
                case R.id.editTextFechaNacimiento:
                    esFechaNacimientoValido();
                    break;
                case R.id.editTextFechaIngreso:
                    esFechaIngresoValido();
                    break;
                case R.id.editTextFechaBaja:
                    esFechaBajaValido();
                    break;
            }
        }
    }

    //IdEmpleado
    private boolean esIdEmpleadoValido() {
        String idEmpleado=editTextIdEmpleado.getText().toString().trim();

        if (idEmpleado.isEmpty()) {
            textInputLayoutIdEmpleado.setError(getString(R.string.error_campo_vacio));
            textInputLayoutIdEmpleado.requestFocus();
            return false;
        }

        if (idEmpleado.length() > 20) {
            textInputLayoutIdEmpleado.setError(getString(R.string.error_campo_grande));
            textInputLayoutIdEmpleado.requestFocus();
            return false;
        }

        textInputLayoutIdEmpleado.setErrorEnabled(false);

        return true;
    }

    //Nombres del Empleado
    private boolean esNombreEmpleadoValido() {
        String nombreEmpleado=editTextNombresEmpleado.getText().toString().trim();

        if (nombreEmpleado.isEmpty()) {
            textInputLayoutNombresEmpleado.setError(getString(R.string.error_campo_vacio));
            textInputLayoutNombresEmpleado.requestFocus();
            return false;
        }

        //Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        Pattern patron = Pattern.compile("^[a-zA-ZñÑáéíóúÁÉÍÓÚ ]+$");
        if (!patron.matcher(nombreEmpleado).matches()) {
            textInputLayoutNombresEmpleado.setError(getString(R.string.error_campo_invalido_nombre_empleado));
            textInputLayoutNombresEmpleado.requestFocus();
            return false;
        }

        if (nombreEmpleado.length() > 50) {
            textInputLayoutNombresEmpleado.setError(getString(R.string.error_campo_grande));
            textInputLayoutNombresEmpleado.requestFocus();
            return false;
        }

        textInputLayoutNombresEmpleado.setErrorEnabled(false);

        return true;
    }

    //Apellido Paterno del Empleado
    private boolean esApelllidoPaternoValido() {
        String apelllidoPaterno=editTextApellidoPaternoEmpleado.getText().toString().trim();

        Pattern patron = Pattern.compile("^[a-zA-ZñÑáéíóúÁÉÍÓÚ ]+$");
        if (apelllidoPaterno.length() > 0  ){
            if (!patron.matcher(apelllidoPaterno).matches()) {
                textInputLayoutApellidoPaternoEmpleado.setError(getString(R.string.error_campo_invalido_apellido_paterno_empleado));
                textInputLayoutApellidoPaternoEmpleado.requestFocus();
                return false;
            }
        }

        if (apelllidoPaterno.length() > 30) {
            textInputLayoutApellidoPaternoEmpleado.setError(getString(R.string.error_campo_grande));
            textInputLayoutApellidoPaternoEmpleado.requestFocus();
            return false;
        }

        textInputLayoutApellidoPaternoEmpleado.setErrorEnabled(false);

        return true;
    }

    //Apellido Materno del Empleado
    private boolean esApelllidoMaternoValido() {
        String apelllidoMaterno=editTextApellidoMaternoEmpleado.getText().toString().trim();

        Pattern patron = Pattern.compile("^[a-zA-ZñÑáéíóúÁÉÍÓÚ ]+$");

        if (apelllidoMaterno.length() > 0  ){
            if (!patron.matcher(apelllidoMaterno).matches()) {
                textInputLayoutApellidoMaternoEmpleado.setError(getString(R.string.error_campo_invalido_apellido_materno_empleado));
                textInputLayoutApellidoMaternoEmpleado.requestFocus();
                return false;
            }
        }

        if (apelllidoMaterno.length() > 30) {
            textInputLayoutApellidoMaternoEmpleado.setError(getString(R.string.error_campo_grande));
            textInputLayoutApellidoMaternoEmpleado.requestFocus();
            return false;
        }

        textInputLayoutApellidoMaternoEmpleado.setErrorEnabled(false);

        return true;
    }

    //Dirección del Empleado
    private boolean esDireccionValido() {
        String direccion=editTextDireccionEmpleado.getText().toString().trim();

        if (direccion.length() > 50) {
            textInputLayoutDireccionEmpleado.setError(getString(R.string.error_campo_grande));
            textInputLayoutDireccionEmpleado.requestFocus();
            return false;
        }

        textInputLayoutDireccionEmpleado.setErrorEnabled(false);

        return true;
    }

    //DNI del Empleado
    private boolean esDNIValido() {
        String DNI=editTextDniEmpleado.getText().toString().trim();

        if (DNI.length() > 8) {
            textInputLayoutDniEmpleado.setError(getString(R.string.error_campo_grande));
            textInputLayoutDniEmpleado.requestFocus();
            return false;
        }

        textInputLayoutDniEmpleado.setErrorEnabled(false);

        return true;
    }

    //Celular del Empleado
    private boolean esCelularValido() {
        String celular=editTextCelular.getText().toString().trim();

        if (celular.length() > 15) {
            textInputLayoutCelular.setError(getString(R.string.error_campo_grande));
            textInputLayoutCelular.requestFocus();
            return false;
        }

        textInputLayoutCelular.setErrorEnabled(false);

        return true;
    }

    //Email del Empleado
    private boolean esEmailValido() {
        String email=editTextEmail.getText().toString().trim();

        if (email.length() > 30) {
            textInputLayoutEmail.setError(getString(R.string.error_campo_grande));
            textInputLayoutEmail.requestFocus();
            return false;
        }

        if (email.length() > 0) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                textInputLayoutEmail.setError(getString(R.string.error_campo_invalido_mail));
                return false;
            }
        }

        textInputLayoutEmail.setErrorEnabled(false);

        return true;
    }

    //Fecha de Nacimiento del Empleado
    private boolean esFechaNacimientoValido() {
        String fechaNacimiento=editTextFechaNacimiento.getText().toString().trim();

        if (fechaNacimiento.length() > 0) {
            try {
                DateFormat df = new SimpleDateFormat(DATE_FORMAT);
                df.setLenient(false);
                df.parse(fechaNacimiento);
                textInputLayoutFechaNacimiento.setErrorEnabled(false);
                return true;
            } catch (ParseException e) {
                textInputLayoutFechaNacimiento.setError(getString(R.string.error_campo_invalido_fecha));
                return false;
            }
        }

        textInputLayoutFechaNacimiento.setErrorEnabled(false);
        return true;
    }

    //Fecha de Ingreso del Empleado
    private boolean esFechaIngresoValido() {
        String fechaIngreso=editTextFechaIngreso.getText().toString().trim();

        if (fechaIngreso.length() > 0) {
            try {
                DateFormat df = new SimpleDateFormat(DATE_FORMAT);
                df.setLenient(false);
                df.parse(fechaIngreso);
                textInputLayoutFechaIngreso.setErrorEnabled(false);
                return true;
            } catch (ParseException e) {
                textInputLayoutFechaIngreso.setError(getString(R.string.error_campo_invalido_fecha));
                return false;
            }
        }

        textInputLayoutFechaIngreso.setErrorEnabled(false);
        return true;
    }

    //Fecha de Ingreso del Empleado
    private boolean esFechaBajaValido() {
        String fechaBaja=editTextFechaBaja.getText().toString().trim();

        if (fechaBaja.length() > 0) {
            try {
                DateFormat df = new SimpleDateFormat(DATE_FORMAT);
                df.setLenient(false);
                df.parse(fechaBaja);
                textInputLayoutFechaBaja.setErrorEnabled(false);
                return true;
            } catch (ParseException e) {
                textInputLayoutFechaBaja.setError(getString(R.string.error_campo_invalido_fecha));
                return false;
            }
        }

        textInputLayoutFechaBaja.setErrorEnabled(false);
        return true;
    }

    public void actualizarFecha(int idViewSeleccionadora ,int ano, int mes, int dia) {

        switch (idViewSeleccionadora)
        {
            case R.id.editTextFechaNacimiento:
                editTextFechaNacimiento.setText( dia + "/" + (mes + 1) + "/" +  ano );
                break;
            case R.id.editTextFechaIngreso:
                editTextFechaIngreso.setText( dia + "/" + (mes + 1) + "/" +  ano );
                break;
            case R.id.editTextFechaBaja:
                editTextFechaBaja.setText( dia + "/" + (mes + 1) + "/" +  ano );
                break;
        }
        // Setear en el textview la fecha
        //editTextFechaNacimiento.setText(ano + "-" + (mes + 1) + "-" + dia);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Obteniendo el id del Spinner que recibió el evento
        int idSpinner = parent.getId();

        switch(idSpinner) {
            case R.id.spinnerCargos:
                //Obteniendo el id del género seleccionado
                Cursor c1 = (Cursor) parent.getItemAtPosition(position);

                codigoCargoSeleccionado = c1.getString(
                        c1.getColumnIndex(Cargos.ID_CARGO));

                String codigoDescripcionCargoSeleccionado = c1.getString(
                        c1.getColumnIndex(Cargos.DESCRIPCION));

                Toast.makeText(getActivity(),codigoCargoSeleccionado+ " " + codigoDescripcionCargoSeleccionado ,Toast.LENGTH_SHORT).show();

                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        /*
        Nada por hacer
         */

    }

    @Override
    public boolean onLongClick(View view) {

        switch (view.getId()){
            case R.id.editTextFechaNacimiento:
                DialogFragment picker = new DatePickerFragment();
                Bundle bundle=new Bundle();
                bundle.putInt (DatePickerFragment.ARGUMENTO_ID_VIEW,editTextFechaNacimiento.getId());
                picker.setArguments(bundle);
                picker.show(getFragmentManager(), "datePicker");
                break;

            case R.id.editTextFechaIngreso:
                DialogFragment picker2 = new DatePickerFragment();
                Bundle bundle2=new Bundle();
                bundle2.putInt (DatePickerFragment.ARGUMENTO_ID_VIEW,editTextFechaIngreso.getId());
                picker2.setArguments(bundle2);
                picker2.show(getFragmentManager(), "datePicker");
                break;
            case R.id.editTextFechaBaja:
                DialogFragment picker3 = new DatePickerFragment();
                Bundle bundle3=new Bundle();
                bundle3.putInt (DatePickerFragment.ARGUMENTO_ID_VIEW,editTextFechaBaja.getId());
                picker3.setArguments(bundle3);
                picker3.show(getFragmentManager(), "datePicker");
        }

        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        return true;
    }

    private void muestraErrorCarga() {
        Toast.makeText(getActivity(),
                "No se ha cargado información", Toast.LENGTH_SHORT).show();
    }

    private void muestraEmpleado(Cursor cursorEmpleado)
    {
        idCargoEmpleado="";
        String nombresCompletos="";
        nombresCompletos+=cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.NOMBRES));
        nombresCompletos+=" " + cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.APELLIDO_PATERNO));
        nombresCompletos+=" " + cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.APELLIDO_MAERNO));

        collapsingToolbarLayout.setTitle(nombresCompletos);
        Glide.with(this)
                .load(Uri.parse("file:///android_asset/" + cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.FOTO))))
                .centerCrop()
                .into(imageViewFotoEmpleado);

        editTextIdEmpleado.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.ID_EMPLEADO)));
        editTextNombresEmpleado.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.NOMBRES)));
        editTextApellidoPaternoEmpleado.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.APELLIDO_PATERNO)));
        editTextApellidoMaternoEmpleado.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.APELLIDO_MAERNO)));
        editTextDireccionEmpleado.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.DIRECCION)));
        editTextDniEmpleado.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.DNI)));
        editTextCelular.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.CELULAR)));
        editTextEmail.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.EMAIL)));
        editTextFechaNacimiento.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.FECHA_NACIMIENTO)));

        idCargoEmpleado=cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.ID_CARGO));
        //spinnerCargos.setSelection(getIndex(spinnerCargos,Cargos.ID_CARGO,idCargoEmpleado));

        editTextFechaIngreso.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.FECHA_INGRESO)));
        editTextFechaBaja.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.FECHA_BAJA)));
    }

    private void muestraPantallaDetalleEmpleados() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EmpleadoListadoFragment.REQUEST_ACTUALIZAR_ELIMINAR_EMPLEADO) {
            if (resultCode == Activity.RESULT_OK) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        }
    }

}
