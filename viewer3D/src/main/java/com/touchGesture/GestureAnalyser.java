package com.touchGesture;

import android.support.annotation.NonNull;
import android.util.Log;

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
      //-- fingers changed mean that one of fingers
      //-- suddenly is in completely different place

      prevFingerToFingerVector  = new Vector2f(0, 0);
      previousPinchDistance     = currentPinchDistance;

      return;
    }

    switch (gesture)
    {
      case SCALE:
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
        break;
      case Z_ROTATE:

        listener.onZRotate( fingerToFingerVector.angle(prevFingerToFingerVector) );
        break;
      default:
        Log.i("Gesture analyser: ","onTwoFingersMove: Unknown move type");
    }

    previousTwoFingersGesture = gesture;
  }

  private TwoFingersGestureType recognizeGesture (Finger a, Finger b)
  {
    boolean bothFingersAreMoving    = a.isMoving() && b.isMoving()
          , fingersMovesCollinearly = a.movesCollinearlyWith(b);

    if ( bothFingersAreMoving && fingersMovesCollinearly )
    {
      return TwoFingersGestureType.SCALE;
    }
    else if ( bothFingersAreMoving )
    {
      return TwoFingersGestureType.Z_ROTATE;
    }
    else
    {
      return TwoFingersGestureType.UNKNOWN;
    }
  }

  private GestureAnalyserListener listener = null;
  private TwoFingersGestureType previousTwoFingersGesture = TwoFingersGestureType.UNDEFINED;

  private float currentPinchDistance  = 0
              , previousPinchDistance = 0;

  private Vector2f  initFingerToFingerVector  = new Vector2f(0,0)
                  , fingerToFingerVector      = new Vector2f(0,0)
                  , prevFingerToFingerVector  = new Vector2f(0,0);
}
//-- TODO: improve