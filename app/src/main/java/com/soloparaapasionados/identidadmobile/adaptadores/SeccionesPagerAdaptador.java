package com.soloparaapasionados.identidadmobile.adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USUARIO on 15/07/2017.
 */

public class SeccionesPagerAdaptador extends FragmentPagerAdapter {
    private final List<Fragment> fragmentos = new ArrayList<>();
    private final List<String> titulosFragmentos = new ArrayList<>();

    public SeccionesPagerAdaptador(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentos.get(position);
    }

    @Override
    public int getCount() {
        return fragmentos.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentos.add(fragment);
        titulosFragmentos.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titulosFragmentos.get(position);
    }
}

