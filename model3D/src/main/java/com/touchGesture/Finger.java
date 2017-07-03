package com.touchGesture;

import android.view.MotionEvent;

import org.joml.Vector2f;

public class Finger
{
  public Finger()
  {

  }
  public Finger(MotionEvent e)
  {
    id      = fingerId(e);
    pos     = fingerPosition(e);
    prevPos = pos;
  }

  @Override
  public  boolean   equals        (Object obj)
  {
    return ( obj instanceof Finger && ((Finger) obj).getId() == id );
  }
  public  Finger    update        (MotionEvent e)
  {
    if ( fingerPosition(e) != pos )
    {
      prevPos = pos;
      pos     = fingerPosition(e);

      lastUpdateDate  = updateDate;
      updateDate      = System.nanoTime();
    }

    return this;
  }
  public int        getId         ()
  {
      return id;
  }
  public  Vector2f  getPos        ()
  {
    return pos;
  }
  Vector2f  getPrevPos    ()
  {
    return prevPos;
  }

  public  float     distanceTo    (Finger f)
  {
    return pos.distance(f.getPos());
  }
  public  Vector2f  shift         ()
  {
    return new Vector2f(pos).sub(prevPos);
  }
  public  Vector2f  shift         (Finger f)
  {
    return new Vector2f(pos).sub( f.getPos() );
  }
  public  boolean   isMoving      ()
  {
    float shiftTime     = updateDate - lastUpdateDate
        , velocity      = shift().length() / shiftTime
        , velocityOrder = Math.getExponent(velocity)
        , moveThreshold = -27;

    return velocityOrder > moveThreshold;
  }
  public  boolean   movesCollinearlyWith (Finger f)
  {
    Vector2f  sv = new Vector2f( f.getPos()  ).sub(prevPos)
            , n  = new Vector2f( shift()     ).perpendicular().normalize();

    float distance  = new Vector2f(sv).dot(n)
        , threshold = 300;

    return Math.abs(distance) < threshold;
  }

  private int       fingerId      (MotionEvent e)
  {
      return e.getPointerId( e.getActionIndex() );
  }
  private Vector2f  fingerPosition(MotionEvent e)
  {
      float x = e.getX( e.findPointerIndex(id) );
      float y = e.getY( e.findPointerIndex(id) );

      return new Vector2f(x, y);
  }

  private int       id              = 0;
  private Vector2f  pos             = new Vector2f(0, 0)
                  , prevPos         = new Vector2f(0, 0);
  private long      updateDate      = System.nanoTime()
                  , lastUpdateDate  = updateDate;
}
//-- TODO: check and fix access modifiers