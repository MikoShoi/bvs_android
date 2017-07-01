package com.example.viewer3d;

import android.graphics.PointF;

public interface GestureAnalyserListener
{
  public void onDrag  (PointF shiftVector);
  public void onScale (float d);
}
