package com.documents;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.bruce.bvs.R;
import com.example.bruce.bvs.databinding.DocumentBinding;

public class Document extends Fragment
{
  public Document()
  {

  }

  public static Document newInstance(int sectionNumber)
  {
      Document fragment = new Document();
      Bundle   args     = new Bundle();
      args.putInt(BUNDLE_PARAM_DOCUMENT_NUMBER, sectionNumber);
      fragment.setArguments(args);
      return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater
                          ,ViewGroup      container
                          ,Bundle         savedInstanceState)
  {
      DocumentBinding document = DataBindingUtil.inflate( inflater
                                                        , R.layout.document
                                                        , container
                                                        , false);
    if (getArguments() != null)
    {

      WebView webView = document.webView;
      webView.setHorizontalScrollBarEnabled(false);
      webView.setVerticalScrollBarEnabled(false);

      int pageNumber      = this.getArguments().getInt(BUNDLE_PARAM_DOCUMENT_NUMBER);
      String documentUrl  = getDocumentUrl(pageNumber);

      webView.loadUrl(documentUrl);
    }

    return document.getRoot();
  }
  @Override
  public void onAttach    (Context context)
  {
      super.onAttach(context);
  }
  @Override
  public void onDetach    ()
  {
      super.onDetach();
  }

  private String getDocumentUrl(int pageNumber)
  {
    System.out.println("getDocumentUrl: " + pageNumber);
    String documentUrl;

    switch (pageNumber)
    {
      case 0:
        documentUrl = "file:///android_asset/about.html";
        break;
      case 1:
        documentUrl = "file:///android_asset/privacy_policy.html";
        break;
      default:
      {
        Log.e("Dooument:getDocumentUrl"," : can not load document");
        documentUrl = "file:///android_asset/default.html";
      }
    }

    return documentUrl;
  }

  private static final String BUNDLE_PARAM_DOCUMENT_NUMBER = "DOCUMENT_NUMBER";
}
