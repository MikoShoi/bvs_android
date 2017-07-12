package com.flowController;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;

import com.documents.DocumentViewer;
import com.example.bruce.bvs.R;
import com.example.camera.Camera;
import com.example.mikotools.MikoLogger;
import com.infoScreens.InfoAnimation;
import com.infoScreens.InfoImage;
import com.instructions.InstructionViewer;

import java.util.Arrays;
import java.util.List;

public class FlowController
        implements OnBackStackChangedListener
{
  public FlowController(FragmentManager fm)
  {
    this.fm = fm;
    this.fm.addOnBackStackChangedListener(this);
  }

  @Override
  public void onBackStackChanged ()
  {
    List<Fragment> fragments = fm.getFragments();
    int indicesCount = getIndicesNumber(fragments);

    if (indicesCount < 3)
    {
      backStackIndex = 0;
    }
    else
    {
      int prevFIndex      = indicesCount  - 2
        , prevPrevFIndex  = prevFIndex    - 1;

      if ( !isFragmentToSkip(fragments.get(prevFIndex)) )
      {
        backStackIndex = prevFIndex;
      }
      else
      {
        for (int i = prevPrevFIndex; i >= 0; i--)
        {
          if ( !isFragmentToSkip(fragments.get(i)) )
          {
            backStackIndex = i;
          }
        }
      }
    }
  }

  public void     moveTo        (Tab tab)
  {
    switch (tab)
    {
      case WELCOME:
        moveTo( InfoImage.newInstance(
                R.drawable.welcome_h
              , R.drawable.welcome_v ) );
        break;
      case INTERNER_CONNECTION_UNAVAILABLE:
        moveTo( InfoImage.newInstance(
                R.drawable.internet_connection_unavailable_h
              , R.drawable.internet_connection_unavailable_v) );
        break;
      case LOADER:
        moveTo( InfoAnimation.newInstance(
                R.raw.loading
              , R.string.loaderDescription) );
        break;
      case INSTRUCTIONS:
        moveTo( new InstructionViewer() );
        break;
      case DOCUMENTS:
        moveTo( new DocumentViewer() );
        break;
      case CAMERA:
        moveTo( new Camera() );
        break;
      default:
        MikoLogger.log("Unsupported tab");
    }
  }
  public void     moveTo        (Fragment fragment)
  {
    if ( currentFragment.getClass().getName() == fragment.getClass().getName() )
      return;
    else
      currentFragment = fragment;

    String marker = fragment.getClass().getName();

    fm.beginTransaction()
      .replace(R.id.mainFrame, fragment, marker)
      .addToBackStack(marker)
      .commit();
  }
  public void     goBack        ()
  {
    fm.popBackStack(backStackIndex, 0);
  }
  public boolean  canGoBack     ()
  {
    return backStackIndex > 0;
  }
  public void     setFragmentsToSkip (String ... fragmentClasses)
  {
    fragmentsToSkip = Arrays.asList(fragmentClasses);
  }
  private int getIndicesNumber(List<Fragment> backStack)
  {
    if ( backStack != null && !backStack.contains(null) )
    {
      return backStack.size();
    }
    else if( backStack != null )
    {
      for (int i = backStack.size() - 1; i >= 0; i--)
      {
        if ( !isFragmentToSkip(backStack.get(i)) )
        {
          return i + 1;
        }
      }
    }

    return 0;
  }
  private boolean isFragmentToSkip (Fragment fragment)
  {
    return (fragment == null)
        || (fragmentsToSkip != null
        &&  fragmentsToSkip.contains( fragment.getClass().getName() ) );
  }

  private FragmentManager fm = null;
  private Fragment currentFragment = null;
  private List<String> fragmentsToSkip = null;
  private int backStackIndex = 1;
}
