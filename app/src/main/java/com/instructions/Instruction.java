package com.instructions;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bruce.bvs.R;
import com.example.bruce.bvs.databinding.InstructionBinding;
import com.example.mikotools.MikoLogger;

public class Instruction extends Fragment
{
  public static Instruction newInstance(int sectionNumber)
  {
      Instruction fragment = new Instruction();

      Bundle bundle = new Bundle();
      bundle.putInt(BUNDLE_PARAM_INSTRUCTION_PAGE, sectionNumber);
      fragment.setArguments(bundle);

      return fragment;
  }

  @Override
  public View onCreateView  (LayoutInflater inflater
                            ,ViewGroup      container
                            ,Bundle         savedInstanceState)
  {
    InstructionBinding instruction
            = DataBindingUtil.inflate ( inflater
                                      , R.layout.instruction
                                      , container
                                      , false);

    Bundle bundle = getArguments();
    if(bundle != null)
    {
      int pageNumber = bundle.getInt(BUNDLE_PARAM_INSTRUCTION_PAGE);
      setAppearance(instruction, pageNumber);
    }
    else
    {
      MikoLogger.log("r id is invalid, did you use newInstance() ?");
    }

    return instruction.getRoot();
  }

  private void setAppearance (InstructionBinding instruction, int pageNumber)
  {
    int titleId, descriptionId, colorId, iconId;

    switch (pageNumber)
    {
      case 0:
        iconId        = R.drawable.happy;
        colorId       = R.color.bg_screen1;
        titleId       = R.string.welcome_title;
        descriptionId = R.string.welcome_description;
        break;
      case 1:
        iconId        = R.drawable.info_squared;
        colorId       = R.color.bg_screen2;
        titleId       = R.string.help_title;
        descriptionId = R.string.help_description;
        break;
      case 2:
        iconId        = R.drawable.camera;
        colorId       = R.color.bg_screen3;
        titleId       = R.string.capture_images_title;
        descriptionId = R.string.capture_images_description;
        break;
      case 3:
        iconId        = R.drawable.mesh;
        colorId       = R.color.bg_screen4;
        titleId       = R.string.enjoy_title;
        descriptionId = R.string.enjoy_description;
        break;
      default:
        iconId        = R.drawable.default_icon;
        colorId       = R.color.default_background;
        titleId       = R.string.default_title;
        descriptionId = R.string.default_description;

        MikoLogger.log("can not set instruction content");
        break;
    }

    Drawable  icon  = ContextCompat.getDrawable(getContext(), iconId);
    int       color = ContextCompat.getColor(getContext(), colorId);

    instruction.header.setText(titleId);
    instruction.description.setText(descriptionId);
    instruction.icon.setImageDrawable(icon);
    instruction.layout.setBackgroundColor(color);
  }

  private static final String BUNDLE_PARAM_INSTRUCTION_PAGE = "INSTRUCTION_PAGE";
}
