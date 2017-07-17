package com.documents;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.bruce.bvs.R;
import com.example.bruce.bvs.databinding.DocumentBinding;
import com.example.mikotools.MikoLogger;

public class Document extends Fragment
{
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

      int pageNumber      = getArguments().getInt(BUNDLE_PARAM_DOCUMENT_NUMBER);
      String documentUrl  = getDocumentUrl(pageNumber);

      webView.loadUrl(documentUrl);
    }

    return document.getRoot();
  }

  private String getDocumentUrl(int pageNumber)
  {
    String documentUrl;

    switch (pageNumber)
    {
      case 0:
        documentUrl = "file:///android_asset/about.html";
        break;
      case 1:
        documentUrl = "file:///android_asset/license.html";
        break;
      default:
      {
        documentUrl = "file:///android_asset/default.html";
        MikoLogger.log("unsupported page number");
      }
    }

    return documentUrl;
  }

  private static final String BUNDLE_PARAM_DOCUMENT_NUMBER = "DOCUMENT_NUMBER";
}
