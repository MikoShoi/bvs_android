package com.instructions;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
      setAppearance(instruction.image, pageNumber);
    }
    else
    {
      MikoLogger.log("r id is invalid, did you use newInstance() ?");
    }

    return instruction.getRoot();
  }

  private void setAppearance (ImageView imageView, int pageNumber)
  {
    int imageId;

    switch (pageNumber)
    {
      case 0:
        imageId = R.drawable.guide_welcome;
        break;
      case 1:
        imageId = R.drawable.guide_take_photos;
        break;
      case 2:
        imageId = R.drawable.guide_have_fun;
        break;
      default:
        imageId = R.drawable.guide_welcome;
        MikoLogger.log("can not set instruction content");
        break;
    }

    Drawable image = ContextCompat.getDrawable(getContext(), imageId);
    imageView.setImageDrawable(image);
  }

  private static final String BUNDLE_PARAM_INSTRUCTION_PAGE = "INSTRUCTION_PAGE";
}
