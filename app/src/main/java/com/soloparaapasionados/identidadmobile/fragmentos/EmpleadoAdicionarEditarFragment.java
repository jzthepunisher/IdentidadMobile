package com.soloparaapasionados.identidadmobile.fragmentos;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmpleadoAdicionarEditarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmpleadoAdicionarEditarFragment extends Fragment {
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

    FloatingActionButton floatingActionButtonGuardar;

    final static String DATE_FORMAT = "dd/MM/yyyy";

    private static final String ARGUMENTO_ID_EMPLEADO = "argumento_id_empleado";

    private String mIdEmpleado;

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

        floatingActionButtonGuardar = (FloatingActionButton) getActivity().findViewById(R.id.floatingActionButtonGuardar);

        // Eventos
        floatingActionButtonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarDatosEmpleado();
            }
        });

        editTextFechaNacimiento.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment picker = new DatePickerFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt (DatePickerFragment.ARGUMENTO_ID_VIEW,editTextFechaNacimiento.getId());
                    picker.setArguments(bundle);
                    picker.show(getFragmentManager(), "datePicker");

                }
            }
        );

        editTextFechaIngreso.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment picker = new DatePickerFragment();
                        Bundle bundle=new Bundle();
                        bundle.putInt (DatePickerFragment.ARGUMENTO_ID_VIEW,editTextFechaIngreso.getId());
                        picker.setArguments(bundle);
                        picker.show(getFragmentManager(), "datePicker");

                    }
                }
        );

        editTextFechaBaja.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment picker = new DatePickerFragment();
                        Bundle bundle=new Bundle();
                        bundle.putInt (DatePickerFragment.ARGUMENTO_ID_VIEW,editTextFechaBaja.getId());
                        picker.setArguments(bundle);
                        picker.show(getFragmentManager(), "datePicker");

                    }
                }
        );

        return root;
    }

    //Validar datos del empleado
    private void validarDatosEmpleado() {
        boolean error = false;

        String IdEmpleado = editTextIdEmpleado.getText().toString();
        String NombresEmpleado=editTextNombresEmpleado.getText().toString();
        String ApellidoPaternoEmpleado=editTextApellidoPaternoEmpleado.getText().toString();
        String ApellidoMaternoEmpleado=editTextApellidoMaternoEmpleado.getText().toString();
        String DireccionEmpleado=editTextDireccionEmpleado.getText().toString();
        String DniEmpleado=editTextDniEmpleado.getText().toString();
        String Celular=editTextCelular.getText().toString();
        String Email=editTextEmail.getText().toString();
        String FechaNacimiento=editTextFechaNacimiento.getText().toString();
        String FechaIngreso=editTextFechaIngreso.getText().toString();
        String FechaBaja=editTextFechaBaja.getText().toString();


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

        //boolean a = esNombreValido(nombre);
        //boolean b = esTelefonoValido(telefono);
        //boolean c = esCorreoValido(correo);

        //if (a && b && c) {
        //    // OK, se pasa a la siguiente acción
        //   Toast.makeText(this, "Se guarda el registro", Toast.LENGTH_LONG).show();
        //}

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

        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
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

        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
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

    //Apellido Paterno del Empleado
    private boolean esApelllidoMaternoValido() {
        String apelllidoMaterno=editTextApellidoMaternoEmpleado.getText().toString().trim();

        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");

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

    //Fecha de Ingreso del Empleado
    private boolean esFechaIngresoValido() {
        String fechaIngreso=editTextFechaIngreso.getText().toString().trim();

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

    //Fecha de Ingreso del Empleado
    private boolean esFechaBajaValido() {
        String fechaBaja=editTextFechaBaja.getText().toString().trim();

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
}
