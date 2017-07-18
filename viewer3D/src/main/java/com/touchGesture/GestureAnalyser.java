package com.touchGesture;

import android.support.annotation.NonNull;

import com.example.mikotools.MikoLogger;

import org.joml.Vector2f;

public class GestureAnalyser
        implements TouchAnalyserListener
{
  public GestureAnalyser(@NonNull GestureAnalyserListener listener)
  {
    this.listener = listener;
  }

  @Override
  public void onOneFingerMove   ( Finger finger )
  {
    finger.isMoving();
    Vector2f dragVector = finger.shift();
    listener.onDrag(dragVector.x, dragVector.y);
  }
  @Override
  public void onTwoFingersMove  ( Finger  firstFinger
                                , Finger  secondFinger
                                , boolean fingersChanged )
  {
    TwoFingersGestureType gesture = recognizeGesture(firstFinger, secondFinger);

    prevFingerToFingerVector = fingerToFingerVector;
    fingerToFingerVector     = firstFinger.shift(secondFinger);

    if (fingersChanged)
    {
      //-- fingers changed mean that one of them
      //-- suddenly is in completely different place

      prevFingerToFingerVector  = new Vector2f(0, 0);
      previousPinchDistance     = currentPinchDistance;

      return;
    }

    switch (gesture)
    {
      case SCALE:
        scaleHandle();
        break;
      case Z_ROTATE:
        zRotateHandle();
        break;
      case ORTHOGONAL_MOVE:
        orthogonalMoveHandle(firstFinger, secondFinger);
        break;
      default:
        MikoLogger.log("Unknown move type");
    }

    previousTwoFingersGesture = gesture;
  }

  private TwoFingersGestureType recognizeGesture (Finger a, Finger b)
  {
    boolean bothFingersAreMoving    = a.isMoving() && b.isMoving()
          , onlyOneFingeIsMoving    = a.isMoving() ^ b.isMoving()
          , fingersMovesCollinearly = a.movesCollinearlyWith(b);

    if ( bothFingersAreMoving && fingersMovesCollinearly )
    {
      return TwoFingersGestureType.SCALE;
    }
    else if ( bothFingersAreMoving )
    {
      return TwoFingersGestureType.Z_ROTATE;
    }
    else if ( onlyOneFingeIsMoving )
    {
      return TwoFingersGestureType.ORTHOGONAL_MOVE;
    }
    else
    {
      return TwoFingersGestureType.UNKNOWN;
    }
  }
  private void scaleHandle          ()
  {
    if ( previousTwoFingersGesture != TwoFingersGestureType.SCALE )
    {
      previousPinchDistance     = currentPinchDistance;
      initFingerToFingerVector  = fingerToFingerVector;
    }

    currentPinchDistance = previousPinchDistance
            + initFingerToFingerVector.length()
            - fingerToFingerVector.length();

    final float maxPinchDistance = 600f;
    if (currentPinchDistance > maxPinchDistance)
    {
      currentPinchDistance = maxPinchDistance;
    }

    listener.onScale(currentPinchDistance);
  }
  private void zRotateHandle        ()
  {
    listener.onZRotate( fingerToFingerVector.angle(prevFingerToFingerVector) );
  }
  private void orthogonalMoveHandle (Finger ff, Finger sf)
  {
    Finger movingFinger = ff.isMoving() ? ff : sf;

    Vector2f shift  = movingFinger.shift();
    float thresholdFactor = 3.0f
        , xShiftLength    = Math.abs(shift.x)
        , yShiftLength    = Math.abs(shift.y);

    //-- finger is moving in X direction if shift in this direction
    //-- is > than shift in Y direction * factor
    boolean isMovingInXDirection = xShiftLength > yShiftLength * thresholdFactor
          , isMovingInYDirection = yShiftLength > xShiftLength * thresholdFactor;

    if (isMovingInXDirection)
    {
      listener.onDrag(movingFinger.shift().x, 0);
    }
    else if (isMovingInYDirection)
    {
      listener.onDrag(0, movingFinger.shift().y);
    }
  }

  private GestureAnalyserListener listener = null;
  private TwoFingersGestureType   previousTwoFingersGesture = TwoFingersGestureType.UNDEFINED;

  private float currentPinchDistance  = 0
              , previousPinchDistance = 0;

  private Vector2f  initFingerToFingerVector  = new Vector2f(0,0)
                  , fingerToFingerVector      = new Vector2f(0,0)
                  , prevFingerToFingerVector  = new Vector2f(0,0);
}