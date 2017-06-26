package com.example.camera;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.camera.databinding.CameraBinding;
import com.example.handytools.MikoError;

public class Camera extends Fragment
{
    public Camera()
    {
        // Required empty public constructor
    }

    public static Camera newInstance(String photoDirPath)
    {
        Camera fragment = new Camera();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView    ( LayoutInflater inflater
                                ,ViewGroup container
                                ,Bundle savedInstanceState)
    {
        camera = DataBindingUtil.inflate( inflater
                                        , R.layout.camera
                                        , container
                                        , false );
        prepareCameraDevice();
        setButtonsListeners();

        return camera.getRoot();
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if( context instanceof CameraInterface)
            parentObject = (CameraInterface) context;
        else
            throw new MikoError(this
                                , "onAttach"
                                , "parent object does not implement needed interface");
    }
    @Override
    public void onDetach()
    {
        super.onDetach();

        parentObject = null;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    void prepareCameraDevice()
    {
        cameraDevicePreview = camera.cameraPreviewWindow;
        cameraDevice        = new CameraDevice(cameraDevicePreview);
        cameraDevicePreview.setPreviewSource(cameraDevice);

        cameraDevice.setParentObject(parentObject);
    }
    void setButtonsListeners()
    {
        setButtonDoneOnClickListener();
        setButtonInfoOnClickListener();
        setButtonCaptureOnClickListener();
    }
    void setButtonCaptureOnClickListener    ()
    {
        View.OnClickListener l = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cameraDevice.takePicture();
            }
        };

        camera.controls.capture.setOnClickListener(l);
    }
    void setButtonDoneOnClickListener       ()
    {
        View.OnClickListener l = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //--let parent object handle done event
                parentObject.onShootingFinishedHandle();
            }
        };

        camera.controls.done.setOnClickListener(l);
    }
    void setButtonInfoOnClickListener       ()
    {
        View.OnClickListener l = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final int timeDuration = 10000;   //milli seconds

                String message = Html.fromHtml( getResources().getString(R.string.fabText) ).toString();
//                String message = getResources().getString(R.string.fabText);

                Snackbar s  = Snackbar.make(v, message, timeDuration);
                TextView tv = (TextView) s.getView()
                        .findViewById(android.support.design.R.id.snackbar_text);

                tv.setMaxLines(5);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                s.show();
            }
        };

        camera.info.setOnClickListener(l);
    }

    private CameraBinding       camera;
    private CameraDevice        cameraDevice;
    private CameraDevicePreview cameraDevicePreview;
    private CameraInterface     parentObject;
}


