package com.mis.relife.pages.sleep.viewPager;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mis.relife.R;
import com.mis.relife.databinding.SleepDayFragmentBinding;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class DayFragment extends Fragment {

    private DayModel vm;
    private ImageView iv_talk_pet,iv_talk_place;
    private TextView tvTalk;
    public AnimationDrawable anim;
    private int old = 27;
    private float sleeTime = 0;
    private String[] teenSleepAdvice = {"每天要睡8小時左右喔~","睡覺時間過長，會打亂生理時鐘，導致精神不振，影響記憶力",
    "晚上十點至半夜兩點是生長激素分泌最旺盛的黃金時段喔!!","充足睡眠可以消除疲勞，修補受傷的神經細胞","充足睡眠可以固定記憶喔"};
    private String[] oldSleepAdvice = {"成年男子需要6.49小時睡眠時間,婦女需要7.5小時左右",
    "晚上10點到早晨5點是「優質睡眠時間」,人在此時易達到深睡眠狀態，有助於緩解疲勞",
            "充足睡眠可以消除疲勞，修補受傷的神經細胞","充足睡眠可以固定記憶喔"};


    public DayFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SleepDayFragmentBinding binding = (SleepDayFragmentBinding)DataBindingUtil.inflate(inflater, R.layout.sleep_day_fragment, container, false);
        vm = new DayModel(getContext(),binding);
        binding.setDayModel(vm);
        View view = binding.getRoot();
        iv_talk_pet = view.findViewById(R.id.iv_talk_pet);
        iv_talk_place = view.findViewById(R.id.iv_talk_place);
        tvTalk = view.findViewById(R.id.tv_talk);

        iv_talk_pet.setOnClickListener(petClick);
        iv_talk_pet.setImageResource(R.drawable.anim_teach);
        anim = (AnimationDrawable) iv_talk_pet.getDrawable();
        Picasso
                .with(getContext())
                .load(R.drawable.blackboard)
                .into(iv_talk_place);
//        if(vm.sleepPercent.get() != null) {
//            sleeTime = vm.sleepPercent.get() / 100 * 24;
//        }
//        initText();
        return view;
    }

    private Button.OnClickListener petClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Random ran = new Random();
            if (anim.isRunning()){
                setTalkText(ran);
            }
            else {
                setTalkText(ran);
                anim.start();
            }
        }
    };

//    private void initText(){
//        if(old > 13 && old < 29) {
//            if (sleeTime == 0) {
//                tvTalk.setText("嗨~~~");
//            } else if (sleeTime > 8 && sleeTime < 9) {
//                tvTalk.setText("睡眠充足~讚讚");
//            } else if(sleeTime < 8){
//                tvTalk.setText("要睡飽一點喔~我會擔心~");
//            }
//        }
//        else if(old >= 29 && old < 60){
//            if (sleeTime == 0) {
//                tvTalk.setText("嗨~~~");
//            } else if (sleeTime > 7 && sleeTime < 8) {
//                tvTalk.setText("睡眠充足~讚讚");
//            } else if(sleeTime < 7){
//                tvTalk.setText("要睡飽一點喔~我會擔心~");
//            }
//        }
//    }

    //設定建議的文字 已年齡做區分
    private void setTalkText(Random ran){
        if(old == 0) {

        }
        else if(old > 13 && old < 29){
            teen(ran);
        }
        else  if(old >= 29 && old < 60){
            old(ran);
        }
    }

    private void teen(Random ran) {
        int choose = (ran.nextInt(9) + 1) % 5;
        switch (choose){
            case 0:
                tvTalk.setText(teenSleepAdvice[0]);
                break;
            case 1:
                tvTalk.setText(teenSleepAdvice[1]);
                break;
            case 2:
                tvTalk.setText(teenSleepAdvice[2]);
                break;
            case 3:
                tvTalk.setText(teenSleepAdvice[3]);
                break;
            case 4:
                tvTalk.setText(teenSleepAdvice[4]);
                break;
        }
    }

    private void old(Random ran) {
        int choose = (ran.nextInt(9) + 1) % 5;
        switch (choose){
            case 0:
                tvTalk.setText(oldSleepAdvice[0]);
                break;
            case 1:
                tvTalk.setText(oldSleepAdvice[1]);
                break;
            case 2:
                tvTalk.setText(oldSleepAdvice[2]);
                break;
            case 3:
                tvTalk.setText(oldSleepAdvice[3]);
                break;
        }
    }






}
