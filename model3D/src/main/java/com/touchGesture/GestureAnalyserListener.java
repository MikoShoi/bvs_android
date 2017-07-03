package com.touchGesture;

public interface GestureAnalyserListener
{
  void onDrag    (float dx, float dy);
  void onScale   (float ds);
  void onZRotate (float da);
}
