package com.example.firstlaunch;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.firstlaunch.databinding.InstructionBinding;

public class Instruction extends Fragment
{
  public Instruction()
  {

  }

  public static Instruction newInstance(int sectionNumber)
  {
      Instruction fragment = new Instruction();
      Bundle      args     = new Bundle();
      args.putInt(ARG_SECTION_NUMBER, sectionNumber);
      fragment.setArguments(args);
      return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater
          ,ViewGroup container
          ,Bundle savedInstanceState)
  {
      //--inflate
      InstructionBinding Instruction =
              DataBindingUtil.inflate(inflater, R.layout.instruction, container, false);

      //--get number of page, which you will customize
      int pageNumber = this.getArguments().getInt(ARG_SECTION_NUMBER);

      Instruction.textViewTitle.setText           ( getTitle      (pageNumber) );
      Instruction.textViewDescription.setText     ( getDescription(pageNumber) );
      Instruction.layout.setBackgroundColor       ( getColor      (pageNumber) );
      Instruction.imageViewIcon.setImageDrawable  ( getIcon       (pageNumber) );

      return Instruction.getRoot();
  }
  @Override
  public void onAttach(Context context)
  {
    super.onAttach(context);

    this.context = context;
  }
  @Override
  public void onDetach()
  {
      super.onDetach();
  }

  private String      getDescription  (int pageNumber)
  {
      int descriptionId;

      switch (pageNumber)
      {
          case 0:
              descriptionId = R.string.welcome_description;
              break;
          case 1:
              descriptionId = R.string.help_description;
              break;
          case 2:
              descriptionId = R.string.capture_images_description;
              break;
          case 3:
              descriptionId = R.string.enjoy_description;
              break;
          default:
              descriptionId = R.string.default_description;
              break;
      }

      return getResources().getString(descriptionId);
  }
  private String      getTitle        (int pageNumber)
  {
      int titleId;

      switch (pageNumber)
      {
          case 0:
              titleId = R.string.welcome_title;
              break;
          case 1:
              titleId = R.string.help_title;
              break;
          case 2:
              titleId = R.string.capture_images_title;
              break;
          case 3:
              titleId = R.string.enjoy_title;
              break;
          default:
              titleId = R.string.default_title;
              break;
      }

      return getResources().getString(titleId);
  }
  private int         getColor        (int pageNumber)
  {
      //--color variable
      int colorId;

      //--assign get proper color for each page
      switch (pageNumber)
      {
          case 0:
              colorId = R.color.bg_screen1;
              break;
          case 1:
              colorId = R.color.bg_screen2;
              break;
          case 2:
              colorId = R.color.bg_screen3;
              break;
          case 3:
              colorId = R.color.bg_screen4;
              break;
          default:
              colorId = R.color.default_background;
              break;
      }

      //--return this color
      return ContextCompat.getColor(context, colorId);
  }
  private Drawable    getIcon         (int pageNumber)
  {
      int iconId;

      switch (pageNumber)
      {
          case 0:
              iconId = R.drawable.happy;
              break;
          case 1:
              iconId = R.drawable.info_squared;
              break;
          case 2:
              iconId = R.drawable.camera;
              break;
          case 3:
              iconId = R.drawable.mesh;
              break;
          default:
              iconId = R.drawable.default_icon;
              break;
      }

      return ContextCompat.getDrawable(context, iconId);
  }

  private Context context = null;
  private static final String ARG_SECTION_NUMBER = "section_number";
}
