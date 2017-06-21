package com.example.camera;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.camera.databinding.CameraPageBinding;

import static android.databinding.DataBindingUtil.setContentView;

public class CameraPage extends Fragment
{
    private Camera              cameraDevice;
    private CameraPreview       cameraPreview;
    private CameraPageBinding   cameraPage;

    private CameraInterface     parentObject;

    public CameraPage()
    {
        // Required empty public constructor
    }

    public static CameraPage newInstance(String photoDirPath)
    {
        CameraPage fragment = new CameraPage();

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
        cameraPage = DataBindingUtil.inflate(   inflater
                                                ,R.layout.camera_page
                                                ,container
                                                ,false );

        init();

        setUiButtons();
//        EventBus eventBus = EventBus.getDefault();

        return cameraPage.getRoot();
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if( context instanceof CameraInterface )
            parentObject = (CameraInterface) context;
        else
            throw new Error("\n\n---Error source:\tCameraPage:onAttach");
    }
    @Override
    public void onDetach()
    {
        super.onDetach();

        parentObject = null;
    }

    void init()
    {
        cameraPreview   = cameraPage.cameraPreviewWindow;
        cameraDevice    = new Camera( cameraPreview );

        cameraPreview.setPreviewSource(cameraDevice);
    }

    void setUiButtons                       ()
    {
        setButtonCaptureOnClickListener();
        setButtonDoneOnClickListener();
        setButtonInfoOnClickListener();
    }
    void setButtonCaptureOnClickListener    ()
    {
        View.OnClickListener l = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.i(  "\n\n------ CameraPage"
                        ,"Button capture: onClickListener");

                cameraDevice.takePicture();
            }
        };

        cameraPage.controls.capture.setOnClickListener(l);
    }
    void setButtonDoneOnClickListener       ()
    {
        View.OnClickListener l = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //--let parent object handle done event
                parentObject.cameraDoneEventHandle();

                Log.i(  "\n\n------ CameraPage"
                        ,"Button done: onClickListener");
            }
        };

        cameraPage.controls.done.setOnClickListener(l);
    }
    void setButtonInfoOnClickListener       ()
    {
        View.OnClickListener l = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int timeDuration = 15000;   //milli seconds
                String message = "Select object you would like to reconstruct and take " +
                        "a several photos of it from different perspective." +
                        "\nIn order to take photo, touch camera button." +
                        "When you decided that's enough, click done button.";

                Snackbar s  = Snackbar.make(v, message, timeDuration);
                View     sv = s.getView();
                TextView tv = (TextView) sv.findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(3);

                s.show();
            }
        };

        cameraPage.info.setOnClickListener(l);
    }
}


