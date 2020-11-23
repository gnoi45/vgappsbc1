package com.blindchat.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.blindchat.R;
import com.blindchat.Utility.ApiConsumer;
import com.blindchat.Utility.ApiResponse;
import com.blindchat.Utility.AppPref;
import com.blindchat.Utility.AppUrl;
import com.blindchat.adapter.SingleAdapter;
import com.blindchat.model.TaskModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EarnPoint extends BaseActivity implements SingleAdapter.ReturnView
{
    private ArrayList<TaskModel> taskModelArrayList=new ArrayList<>();
    private ListView listView;


    private RewardedVideoAd rewardedVideoAd;
    private String TAG="TaskActivity";



    @Override
    public void initialize(Bundle save) {
        setTitle("Earn Points");

        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        listView=(ListView) findViewById(R.id.listview);
        adddummy();
        listView.setAdapter(new SingleAdapter(this,R.layout.layout_task_single_item,taskModelArrayList,this,0));

    }

    private void adddummy()
    {
        taskModelArrayList.add(new TaskModel("1","Watch Video","Watch a video and earn points to chat with stragners","10","1"));
    }





    @Override
    public int getActivityLayout() {
        return R.layout.activity_task;
    }

    @Override
    public void getAdapterView(View view, List objects, final int position, int from) {
        TextView task=(TextView) view.findViewById(R.id.task_id);
        TextView title=(TextView) view.findViewById(R.id.title);
        TextView description=(TextView) view.findViewById(R.id.description);
        TextView points=(TextView) view.findViewById(R.id.points);
        Button click=(Button) view.findViewById(R.id.click);



        final TaskModel taskModel=taskModelArrayList.get(position);

        task.setText("Task #"+taskModel.getId());
        title.setText(taskModel.getTitle());
        description.setText(taskModel.getDescription().trim());
        if(!taskModel.getPoints().equalsIgnoreCase("")) {
            points.setText("+" + taskModel.getPoints());
        }



        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAds();
            }
        });
    }


    private void openAds()
    {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading ...");
        progressDialog.setMessage("Please Wait ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        MobileAds.initialize(this,
                "ca-app-pub-4830486419815879~4598319782");

        final RewardedVideoAd mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);

        mRewardedVideoAd.loadAd(getString(R.string.testr),
                new AdRequest.Builder()
                        .build());
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                if (mRewardedVideoAd.isLoaded()) {
                    progressDialog.dismiss();
                    mRewardedVideoAd.show();
                }

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                videoearning();
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });

    }




    private void videoearning()
    {
        int points=Integer.parseInt(AppPref.getInstance().getPOINTS());
        points+=10;
        final int pts=points;
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("id=").append(AppPref.getInstance().getUSERID());
        stringBuilder.append("&points=").append(pts+"");


        String content=stringBuilder.toString();

        ApiConsumer apiConsumer = new ApiConsumer(this, AppUrl.EARN_POINT, 0, content, true, "loading ...", new ApiResponse() {
            @Override
            public void getApiResponse(String responseData, int serviceCounter) {

                try {
                    Log.d("responseData", responseData);
                    JSONObject jsonObject=new JSONObject(responseData);
                    String msg=jsonObject.getString("message");
                    if(msg.equalsIgnoreCase("Points Updated"))
                    {
                          AppPref.getInstance().setPOINTS(""+pts);
                          toastMessage("You have earn 10 BC Credits");
                          finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        apiConsumer.execute();

    }

//    private void rewardDialog()
//    {
//        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        LayoutInflater inf = LayoutInflater.from(this);
//        View v1 = inf.inflate(R.layout.dialog_popup, null);
//
//        ImageView back = (ImageView) v1.findViewById(R.id.back);
//
//        try {
//            GifImageView gifview = (GifImageView) v1.findViewById(R.id.gifview);
//
//            final GifDrawable gifFromAssets = new GifDrawable(getAssets(), "success_gif.gif");
//
//            gifFromAssets.addAnimationListener(new AnimationListener() {
//                @Override
//                public void onAnimationCompleted(int loopNumber) {
//                    gifFromAssets.stop();
//                }
//            });
//
//            gifview.setImageDrawable(gifFromAssets);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//
//        dialog.setView(v1);
//        final Dialog d1 = dialog.create();
//        d1.show();
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                d1.dismiss();
//            }
//        });
//    }

//    private void openAlert()
//    {
//        final ArrayList<SitesModel> sitesModels=new ArrayList<>();
//        ApiConsumer apiConsumer = new ApiConsumer(this, AppUrl.SITE_LIST, 0, "", true, "loading ...", new ApiResponse() {
//            @Override
//            public void getApiResponse(String responseData, int serviceCounter) {
//
//                try {
//                    Log.d("responseData", responseData);
//                    JSONArray jsonArray = new JSONArray(responseData);
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        String title=jsonObject.getString("title");
//                        String link=jsonObject.getString("link");
//                        String id=jsonObject.getString("id");
//
//                        sitesModels.add(new SitesModel(id,title,link));
//                    }
//
//                    alerts(sitesModels);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });
//
//        apiConsumer.execute();
//    }

}
