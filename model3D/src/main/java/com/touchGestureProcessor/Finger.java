package com.touchGestureProcessor;

import android.graphics.PointF;
import android.view.MotionEvent;

public class Finger
{
  public Finger()
  {
    id = -1;
    pos = new PointF(0, 0);
  }
  public Finger(MotionEvent e)
  {
    id  = fingerId(e);
    pos = fingerPosition(e, id);
  }

  @Override
  public boolean  equals (Object obj)
  {
    return ( obj instanceof Finger && ((Finger) obj).id() == id );
  }
  public void     update  (MotionEvent e)
  {
    pos = fingerPosition(e, id);
  }
  public int      id      ()
  {
      return id;
  }

  public static float   distance(Finger a, Finger b)
  {
    float   x = a.pos.x - b.pos.x
            , y = a.pos.y - b.pos.y;

    return new PointF(x, y).length();
  }
  public static PointF  shift   (Finger a, Finger b)
  {
    float   x = b.pos.x - a.pos.x
          , y = b.pos.y - a.pos.y;

  return new PointF(x, y);
}

  private int     fingerId      (MotionEvent e)
  {
      int pointerIndex  = e.getActionIndex();

      return e.getPointerId(pointerIndex);
  }
  private PointF  fingerPosition(MotionEvent e, int pointerId)
  {
      float x = e.getX(e.findPointerIndex(pointerId));
      float y = e.getY(e.findPointerIndex(pointerId));

      return new PointF(x, y);
  }

  private int     id;
  private PointF  pos;
}
