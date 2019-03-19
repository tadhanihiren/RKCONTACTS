package ml.app.rkcontacts.helpers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ml.app.rkcontacts.FacultyDetail;
import ml.app.rkcontacts.R;

public class ListViewAdapter extends BaseAdapter {

    //variables
    Context mContext;
    LayoutInflater inflater;
    List<Model> modellist;
    ArrayList<Model> arrayList;

    //constructor
    public ListViewAdapter(Context context, List<Model> modellist) {
        mContext = context;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Model>();
        this.arrayList.addAll(modellist);
    }

    public class ViewHolder {
        TextView mTitleTv, mDescTv;
        ImageView mIconIv;
    }

    @Override
    public int getCount() {
        return modellist.size();
    }

    @Override
    public Object getItem(int i) {
        return modellist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int postition, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.contacts_row, null);

            //locate the views in row.xml
            holder.mTitleTv = view.findViewById(R.id.mainTitle);
//            holder.mDescTv = view.findViewById(R.id.mainDesc);
            holder.mIconIv = view.findViewById(R.id.mainIcon);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        //set the results into textviews
        holder.mTitleTv.setText(modellist.get(postition).getName());
//        holder.mDescTv.setText(modellist.get(postition).getEmail());
        //set the result in imageview
        if (!modellist.get(postition).getIcon().equals(""))
            Picasso.get().load(modellist.get(postition).getIcon()).into(holder.mIconIv);
        else
            holder.mIconIv.setImageResource(R.drawable.profile);

        //listview item clicks
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),
                        FacultyDetail.class);

                //PACK DATA
                i.putExtra("name", modellist.get(postition).getName());
                i.putExtra("email", modellist.get(postition).getEmail());
                i.putExtra("icon", modellist.get(postition).getIcon());
                i.putExtra("mobile", modellist.get(postition).getMobile());
                i.putExtra("ext", modellist.get(postition).getExt());
                i.putExtra("gender", modellist.get(postition).getGender());
                i.putExtra("school", modellist.get(postition).getSchool());
                i.putExtra("branch", modellist.get(postition).getBranch());

                mContext.startActivity(i);
            }
        });


        return view;
    }

    //filter
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        modellist.clear();
        if (charText.length() == 0) {
            modellist.addAll(arrayList);
        } else {
            for (Model model : arrayList) {
                if (model.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    modellist.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }
}
