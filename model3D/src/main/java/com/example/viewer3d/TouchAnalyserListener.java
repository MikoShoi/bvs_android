package com.example.viewer3d;

import com.touchGestureProcessor.Finger;

public interface TouchAnalyserListener
{
  public void onOneFingerMove   (Finger finger);
  public void onTwoFingersMove  (Finger firstFinger, Finger secondFinger, boolean c);
  public void onFingersUpdated  ();
}
