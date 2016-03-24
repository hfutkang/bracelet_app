package sctek.cn.ysbracelet.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.FamilySleepLvAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FamilySleepFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FamilySleepFragment extends Fragment {

    private static final String TAG = FamilySleepFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ListView familySleepLv;
    private TextView emptyTv;

    public FamilySleepFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FamilySportsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FamilySleepFragment newInstance(String param1, String param2) {
        FamilySleepFragment fragment = new FamilySleepFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_family_sleep, container, false);
        initViewElement(view);
        return view;
    }

    private void initViewElement(View view) {

        familySleepLv = (ListView)view.findViewById(R.id.family_sleep_lv);
        FamilySleepLvAdapter adapter = new FamilySleepLvAdapter(getContext(), null);
        familySleepLv.setAdapter(adapter);

        familySleepLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FamilySleepLvAdapter.ViewHolder viewHolder = (FamilySleepLvAdapter.ViewHolder)view.getTag();
                if(viewHolder.detailV.getVisibility() == View.GONE) {
                    viewHolder.detailV.setVisibility(View.VISIBLE);
                }
                else {
                    viewHolder.detailV.setVisibility(View.GONE);
                }
            }
        });

        emptyTv = (TextView)view.findViewById(R.id.empty_tv);
        emptyTv.setVisibility(View.GONE);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
