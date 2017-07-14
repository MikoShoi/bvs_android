package com.flowController;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;

import com.example.bruce.bvs.R;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class FlowController extends Fragment
        implements OnBackStackChangedListener
{
  public FlowController(FragmentManager fragmentManager)
  {
    this.fragmentManager = fragmentManager;
    this.fragmentManager.addOnBackStackChangedListener(this);

    setRetainInstance(true);
  }

  @Override
  public void     onBackStackChanged  ()
  {
    List<Fragment> fragments = fragmentManager.getFragments();

    if (fragments != null)
      updateBackStackInfo(fragments);
  }

  public void     moveTo              (Fragment fragment)
  {
    //-- get fragment info/"fingerprint"
    String fragmentMarker = fragment.getClass().getName();

    //-- if current fragment is the same fragment - do nothing
    //-- else update current fragment marker and show this fragment
    if ( currentFragmentMarker.equals(fragmentMarker) )
      return;
    else
      currentFragmentMarker = fragmentMarker;

    fragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, fragment, fragmentMarker)
                    .addToBackStack(fragmentMarker)
                    .commit();
  }
  public void     moveBack            ()
  {
    fragmentManager.popBackStack(backStackIndex, 0);
  }
  public boolean  canMoveBack         ()
  {
    return backStackIndex > 0;
  }
  public void     addFragmentsToSkip  (String ... fragmentClasses)
  {
    if (fragmentsToSkip == null)
      fragmentsToSkip = new Vector<>(fragmentClasses.length);

    Collections.addAll(fragmentsToSkip, fragmentClasses);
  }

  private boolean canBeShown          (Fragment fragment)
  {
    //-- fragment can be shown if there is no list of fragments to skip
    //-- or if list exist but the fragment is no in the list

    return  fragmentsToSkip == null
        || !fragmentsToSkip.contains( fragment.getClass().getName() );
  }
  private void    updateBackStackInfo (List<Fragment> backStack)
  {
//    System.out.println(" ");
//    for (Fragment fragment : backStack)
//    {
//      System.out.println( fragment == null ? "null" : fragment.getClass().getName());
//    }

    int nullFragmentNumber = Collections.frequency(backStack, null)
      , backStackSize = backStack.size() - nullFragmentNumber;

    if (backStackSize > 1)
    {
      //-- set it in case if back stack no contain any fragment to show;
      backStackIndex = 0;

      //-- iterating from the end of back stack try to find
      //-- position of first fragment which can be shown
      for (int i = backStackSize - 2; i >= 0; i--)
      {
        //-- if there is one, save position and break iteration
        if (canBeShown(backStack.get(i)))
        {
          backStackIndex = i;
          break;
        }
      }
    }
    else
    {
      backStackIndex = 0;
    }

    currentFragmentMarker = backStack.get(backStackSize - 1).getClass().getName();
  }

  private FragmentManager fragmentManager = null;
  private Vector<String> fragmentsToSkip = null;
  private String currentFragmentMarker = "";
  private int backStackIndex = 0;
}
