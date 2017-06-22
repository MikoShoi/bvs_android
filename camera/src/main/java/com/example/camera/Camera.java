package com.example.camera;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.camera.databinding.CameraBinding;

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
            throw new Error("\n\n---Error source:\tCamera:onAttach");
    }
    @Override
    public void onDetach()
    {
        super.onDetach();

        parentObject = null;
    }

    void prepareCameraDevice()
    {
        cameraDevicePreview = camera.cameraPreviewWindow;
        cameraDevice        = new CameraDevice(cameraDevicePreview);
        cameraDevicePreview.setPreviewSource(cameraDevice);
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
                parentObject.onDoneHandle();
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
                int timeDuration = 10000;   //milli seconds
                String message = getResources().getString(R.string.fabText);

                Snackbar s  = Snackbar.make(v, message, timeDuration);
                View     sv = s.getView();
                TextView tv = (TextView) sv.findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(3);

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


