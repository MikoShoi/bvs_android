package com.touchGesture;

import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

import java.util.ArrayList;

public class TouchAnalyser
{
  public TouchAnalyser()
  {}

  public  void analyze                (MotionEvent e)
  {
    if ( listener == null )
      return;

    switch ( MotionEventCompat.getActionMasked(e) )
    {
      case MotionEventCompat.ACTION_POINTER_UP:
        dropFinger(e);
        break;
      case MotionEventCompat.ACTION_POINTER_DOWN:
        addFinger(e);
        break;
      case MotionEvent.ACTION_UP:
        dropFinger(e);
        break;
      case MotionEvent.ACTION_DOWN:
        addFinger(e);
        break;
      case MotionEvent.ACTION_MOVE:
        if ( fingers.isEmpty() )
        {
          //-- move action means that there is some finger, if
          //-- fingers is empty then you have to add this finger
          addFinger(e);
        }
        analyzeFingersMove(e);
        break;
    }
  }
  public  void addListener            (TouchAnalyserListener listener)
  {
    this.listener = listener;
  }

  private void dropFinger             (MotionEvent e)
  {
    Finger droppedFinger = new Finger(e);
    fingers.remove(droppedFinger);

    boolean ffDropped = ff.equals(droppedFinger)
          , sfDropped = sf.equals(droppedFinger);

    if (ffDropped || sfDropped)
    {
      if (sfDropped)
        sf = new Finger();

      updateMainFingers();
    }
  }
  private void addFinger              (MotionEvent e)
  {
    fingers.add( new Finger(e) );
    if (fingers.size() < 3)
    {
      updateMainFingers();
    }
  }
  private void analyzeFingersMove     (MotionEvent e)
  {
    updateFingerPositions(e);

    switch ( fingers.size() )
    {
      case 1:
        listener.onOneFingerMove(ff);
        break;
      case 2:
        listener.onTwoFingersMove(ff, sf, false);
        break;
      default:
        break;
    }
  }
  private void updateFingerPositions  (MotionEvent e)
  {
    for (Finger finger : fingers)
    {
      finger.update(e);
    }
  }
  private void updateMainFingers      ()
  {
    switch ( fingers.size() )
    {
      //-- if there are no fingers, skip switch
      case 0:
        break;
      //-- if there is only one finger update only this one
      case 1:
        ff = fingers.get(0);
        break;
      //-- but if there are more, then update first two
      default:
        ff = fingers.get(0);
        sf = fingers.get(1);
        listener.onTwoFingersMove(ff, sf, true);
        break;
    }
  }

  private TouchAnalyserListener listener  = null;
  private ArrayList<Finger>     fingers   = new ArrayList<>(0);
  private Finger                ff        = new Finger()
                              , sf        = new Finger();
}
