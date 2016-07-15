package sctek.cn.ysbracelet.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.MessagesAdapter;
import sctek.cn.ysbracelet.devicedata.Message;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;
import sctek.cn.ysbracelet.user.YsUser;

public class MessageFragment extends Fragment {

    private static final String TAG = MessageFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    private List<YsData> messages;
    private ListView msgLv;
    private MessagesAdapter adapter;
    private TextView emptyTv;

    public MessageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messages = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        msgLv = (ListView)view.findViewById(R.id.messages_lv);
        emptyTv = (TextView)view.findViewById(R.id.empty_tv);
        adapter = new MessagesAdapter(getContext(), messages);
        msgLv.setAdapter(adapter);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        loadData();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        Log.e(TAG, "onPostExecute");
                        if (messages.size() == 0) {
                            emptyTv.setVisibility(View.VISIBLE);
                            msgLv.setVisibility(View.GONE);
                        }
                        else {
                            emptyTv.setVisibility(View.GONE);
                            msgLv.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }.execute();
            }
        });
        return view;
    }

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

    protected void loadData() {
        Log.e(TAG, "loadData");
        String[] projection = new String[]{LocalDataContract.Message.COLUMNS_NAME_DEVICE
                , LocalDataContract.Message.COLUMNS_NAME_TIME
                , LocalDataContract.Message.COLUMNS_NAME_TYPE
                , LocalDataContract.Message.COLUMNS_NAME_MESSAGE };

        ContentResolver cr = getContext().getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.Message.CONTENT_URI
                , projection
                , null
                , null
                , LocalDataContract.Message.COLUMNS_NAME_TIME + " desc");

        try {
            while (cursor.moveToNext()) {
                Message msg = new Message();
                msg.deviceId = cursor.getString(0);
                msg.time = cursor.getString(1);
                msg.type = cursor.getString(2);
                msg.message = cursor.getString(3);
                msg.name = YsUser.getInstance().getDevice(msg.deviceId).name;
                messages.add(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();
    }
}
