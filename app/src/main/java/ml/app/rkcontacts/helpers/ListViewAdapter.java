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
    String desc = "";
    Context mContext;
    LayoutInflater inflater;
    List<Model> modellist;
    ArrayList<Model> arrayList;
    GlobalFunctions gf;

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
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.contacts_row, null);

            //locate the views in row.xml
            holder.mTitleTv = view.findViewById(R.id.mainTitle);
            holder.mDescTv = view.findViewById(R.id.setpost);
//            holder.mDescTv = view.findViewById(R.id.mainDesc);
            holder.mIconIv = view.findViewById(R.id.mainIcon);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        //set the results into textviews
        holder.mTitleTv.setText(modellist.get(position).getName());

        if (!modellist.get(position).getRole().equals("NS")) {
            desc += modellist.get(position).getRole();
        }

        if (!modellist.get(position).getBranch().equals("NS") && !modellist.get(position).getRole().equals("NS")) {
            desc += " - ";
        }

        if (!modellist.get(position).getBranch().equals("NS"))
            desc += modellist.get(position).getBranch();

        if (!modellist.get(position).getSchool().equals("NS") && !modellist.get(position).getBranch().equals("NS")) {
            desc += " - ";
        }

        if (!modellist.get(position).getSchool().equals("NS"))
            desc += modellist.get(position).getSchool();

        if (!modellist.get(position).getExt().equalsIgnoreCase("null") && (!modellist.get(position).getSchool().equals("NS")) || !modellist.get(position).getRole().equals("NS")) {
            desc += " - ";
        }
        if (!modellist.get(position).getExt().equalsIgnoreCase("null"))
            desc += modellist.get(position).getExt();


        holder.mDescTv.setText(desc);
        desc = "";
        //        holder.mDescTv.setText(modellist.get(position).getEmail());
        //set the result in imageview
        if (!modellist.get(position).getIcon().equals(""))
            Picasso.get().load(modellist.get(position).getIcon()).into(holder.mIconIv);
        else
            holder.mIconIv.setImageResource(R.drawable.profile);

        //listview item clicks
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),
                        FacultyDetail.class);

                //PACK DATA
                i.putExtra("name", modellist.get(position).getName());
                i.putExtra("email", modellist.get(position).getEmail());
                i.putExtra("icon", modellist.get(position).getIcon());
                i.putExtra("mobile", modellist.get(position).getMobile());
                i.putExtra("ext", modellist.get(position).getExt());
                i.putExtra("gender", modellist.get(position).getGender());
                i.putExtra("school", modellist.get(position).getSchool());
                i.putExtra("branch", modellist.get(position).getBranch());
                i.putExtra("role", modellist.get(position).getRole());

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
