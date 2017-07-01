package com.touchGestureProcessor;

import android.graphics.PointF;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

import java.util.ArrayList;

public class TouchGestureProcessor
{
  public TouchGestureProcessor()
  {}

  public void addTouchGestureListener(TouchGestureListener touchGestureListener)
  {
      this.touchGestureListener = touchGestureListener;
  }

  public void touchEventHandle(MotionEvent e)
  {
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
        moveHandle(e);
        break;
    }
  }
  private void dropFinger(MotionEvent e)
  {
    Finger droppedFinger = new Finger(e);
    fingers.remove(droppedFinger);

    boolean ffDropped = ff.equals(droppedFinger)
          , sfDropped = sf.equals(droppedFinger);

    if (ffDropped || sfDropped)
    {
      if (sfDropped)
      {
        sf = new Finger();
      }

      updateFingers();
    }
  }
  private void addFinger(MotionEvent e)
  {
    fingers.add( new Finger(e) );
    if (fingers.size() < 3)
    {
      updateFingers();
    }
  }
  private void updateFingers()
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
        initialPinchDistance = Finger.distance(ff, sf);
        break;
    }

    previousPinchChanges = currentPinchChange;
  }

  private void moveHandle(MotionEvent e)
  {
    switch ( fingers.size() )
    {
      case 1:
        swipeHandle(e);
        break;
      case 2:
        pinchHandle(e);
        break;
      default:
        //-- for more than 3 fingers there is no real handle
        //-- trace changes finger positions and still update
        //-- initialPinchDistance, this allow to avoid strange
        //-- behavior of manipulated object
        for (Finger finger : fingers)
        {
          finger.update(e);
        }
        initialPinchDistance = Finger.distance(ff, sf);
        break;
    }
  }

    private void swipeHandle(MotionEvent e)
    {
      //-- get info about current ff position
      Finger actualFf = new Finger(e);

      //-- estimate shift between old and actual position
      PointF shift = Finger.shift(ff, actualFf);

      //-- update ff
      ff.update(e);

      if ( touchGestureListener != null )
        touchGestureListener.onMoveTouch(shift.x, shift.y);
    }
    private void pinchHandle(MotionEvent e)
    {
      //-- update first and second finger
      ff.update(e);
      sf.update(e);

      //-- estimate distance between them
      float currentPinchDistance = Finger.distance(ff, sf);

      //-- current pinch change is value of single pinch session
      //-- single pinch session will start when two fingers touch screen
      //-- and end will when only one finger touch screen
      currentPinchChange = previousPinchChanges
              - initialPinchDistance
              + currentPinchDistance;

//      System.out.println("1:\t" + currentPinchChange);

      if ( touchGestureListener != null )
        touchGestureListener.onScaleTouch(currentPinchChange);
    }

  private TouchGestureListener  touchGestureListener = null;
  private ArrayList<Finger>     fingers = new ArrayList<>(0);

  private Finger  ff = new Finger()
                , sf = new Finger();
  private float   initialPinchDistance  = 0
                , currentPinchChange    = 0
                , previousPinchChanges  = 0;
}
