package com.documents;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bruce.bvs.R;
import com.example.bruce.bvs.databinding.DocumentViewerBinding;
import com.example.mikotools.MikoError;

public class DocumentViewer extends Fragment
{
  public DocumentViewer()
  {
      // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
      super.onCreate(savedInstanceState);
  }
  @Override
  public View onCreateView(LayoutInflater inflater
          , ViewGroup container
          , Bundle savedInstanceState)
  {
      DocumentViewerBinding layout
              = DataBindingUtil.inflate(inflater
                                      , R.layout.document_viewer
                                      , container
                                      , false );

      closeFab  = layout.fab;
      viewPager = layout.viewPager;

      setViewPager();
      setFabOnClickListener();

      return layout.getRoot();
  }

  @Override
  public void onAttach(Context context)
  {
    super.onAttach(context);

    if (context instanceof DocumentViewerListener)
        listener = (DocumentViewerListener) context;
    else
        throw new MikoError(this, "onAttach", "parent object does not implement needed interface");
  }
  @Override
  public void onDetach()
  {
    super.onDetach();
    listener = null;
  }

  private void setViewPager           ()
  {
    DocumentViewerAdapter adapter = new DocumentViewerAdapter( getChildFragmentManager() );
    viewPager.setAdapter(adapter);
  }
  private void setFabOnClickListener  ()
  {
    closeFab.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            close();
        }
    });
  }
  private void close                  ()
  {
    listener.onDocumentsViewed();
  }

  private DocumentViewerListener  listener;
  private FloatingActionButton    closeFab;
  private ViewPager               viewPager;
}
