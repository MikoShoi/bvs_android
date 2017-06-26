package com.infoScreens;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.handytools.R;

public class WelcomeScreen extends InfoScreen
{
    @Override
    public View onCreateView(LayoutInflater inflater
                            , ViewGroup container
                            , Bundle savedInstanceState)
    {
        screen = DataBindingUtil.inflate(   inflater
                , R.layout.info_screen
                , container
                , false);

        Drawable imageSource = ContextCompat.getDrawable(getContext(), R.drawable.welcome);
        screen.image.setImageDrawable(imageSource);

        scaleImage();

        return screen.getRoot();
    }
}
