package sctek.cn.ysbracelet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.igexin.sdk.PushManager;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.activitys.LoginActivity;
import sctek.cn.ysbracelet.user.YsUser;

public class SettingsFragment extends Fragment {

    private final static String TAG = SettingsFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ListView listV = (ListView)view.findViewById(R.id.settings_lv);
        listV.setAdapter(settingsAdapter);
        listV.setOnItemClickListener(onItemClickListener);
        return view;
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position == 6) {
                YsUser.getInstance().logout(getContext());
                PushManager.getInstance().stopService(getContext().getApplicationContext());
                PushManager.getInstance().unBindAlias(getContext(), YsUser.getInstance().getName(), true);
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        }
    };

    private BaseAdapter settingsAdapter = new BaseAdapter() {

        private int[] titlesId = new int[]{R.string.app_version, R.string.about_device, R.string.support
                ,  R.string.how_to_buy, R.string.way_to_charge,R.string.help, R.string.quit};
        @Override
        public int getCount() {
            return titlesId.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return titlesId[position];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.settings_item_view, null);
                TextView titleTv = (TextView)convertView.findViewById(R.id.title_tv);
                titleTv.setText(titlesId[position]);
                convertView.setTag(titleTv);
            }
            TextView titleTv = (TextView)convertView.getTag();
            titleTv.setText(titlesId[position]);
            return convertView;
        }
    };

}
