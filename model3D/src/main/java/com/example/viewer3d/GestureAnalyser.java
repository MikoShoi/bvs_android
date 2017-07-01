package com.example.viewer3d;

import com.touchGestureProcessor.Finger;

public class GestureAnalyser
        implements TouchAnalyserListener
{
  @Override
  public void onOneFingerMove (Finger finger)
  {
    if ( listener != null )
      listener.onDrag( finger.shift() );
  }
  @Override
  public void onTwoFingersMove (Finger firstFinger, Finger secondFinger, boolean c)
  {
    if (c)
    {
      initPinchDistance = Finger.distance(firstFinger, secondFinger);
      previousPinchChange = currentPinchChange;
      return;
    }

    float pinchDistance = Finger.distance(firstFinger, secondFinger);

    currentPinchChange = previousPinchChange
                          - initPinchDistance
                          + pinchDistance;

//    System.out.println("2:\t" + currentPinchChange);

    if (listener != null)
      listener.onScale(currentPinchChange);
  }
  @Override
  public void onFingersUpdated ()
  {
    initPinchDistance   = 0;
    previousPinchChange = currentPinchChange;
  }

  public void addListener(GestureAnalyserListener listener)
  {
    this.listener = listener;
  }

  private GestureAnalyserListener listener = null;
  private float initPinchDistance
              , currentPinchChange
              , previousPinchChange;
}
