package com.touchGesture;

interface TouchAnalyserListener
{
  void onOneFingerMove   (Finger  finger);
  void onTwoFingersMove  (Finger  firstFinger
                        , Finger  secondFinger
                        , boolean fingersChanged);
}
