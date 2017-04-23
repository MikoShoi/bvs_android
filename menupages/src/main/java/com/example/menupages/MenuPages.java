package com.example.menupages;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.menupages.databinding.MenuPagesBinding;


public class MenuPages extends Fragment
{
    private MenuPagesInterface parentObject;

    private ViewPager            viewPager;
    private FloatingActionButton button;

    public MenuPages()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater
            , ViewGroup container
            , Bundle savedInstanceState)
    {
        MenuPagesBinding layout = DataBindingUtil.inflate( inflater
                ,R.layout.menu_pages
                ,container
                ,false);

        viewPager   = layout.viewPager;
        button      = layout.fab;

        setViewPager();

        setFabOnClickListener();

        return layout.getRoot();
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if (context instanceof MenuPagesInterface)
            parentObject = (MenuPagesInterface) context;
        else
            //--viewPager must implement FirstLaunchInterface
            throw new Error("\n\n-------\tError source:\tMenuPages:onAttach");
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
        parentObject = null;
    }

    //--view pager
    private void setViewPager()
    {
        MenuPagesAdapter adapter = new MenuPagesAdapter( getChildFragmentManager() );
        viewPager.setAdapter(adapter);
    }

    //--fab
    private void setFabOnClickListener()
    {
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                    closeActivity();
            }
        });
    }

    //--tell my why you are done | click Let's start begin
    private void closeActivity()
    {
        parentObject.menuPagesCompleted();
    }

}
