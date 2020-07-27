package hr.miz.evidencijakontakata.Adapters.ExposureEntityAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hr.miz.evidencijakontakata.Adapters.ExposureEntityAdapter.Holder.ExposureEntityHolder;
import hr.miz.evidencijakontakata.Models.TemporaryExposureKey;
import hr.miz.evidencijakontakata.R;
import hr.miz.evidencijakontakata.Utilities.Enums.TransmissionRiskEnum;

public class ExposureEntityAdapter extends RecyclerView.Adapter<ExposureEntityHolder> {
    private ArrayList<TemporaryExposureKey> temporaryExposureKeys;
    private Context context;

    public ExposureEntityAdapter(Context context) {
        this.context = context;
        temporaryExposureKeys = new ArrayList<>();


    }

    @NonNull
    @Override
    public ExposureEntityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exposure_entity, parent, false);
        return new ExposureEntityHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExposureEntityHolder holder, int position) {
        final TemporaryExposureKey temporaryExposureKey = temporaryExposureKeys.get(position);
        //  holder.binding.exposureItemTimestamp.setText(String.valueOf(temporaryExposureKey.rollingPeriod));
        if (TransmissionRiskEnum.isHighRisk(temporaryExposureKey.transmissionRisk)) {
            holder.binding.exposureTitle.setText(R.string.high_risk_tem);
            holder.binding.ivExposeRisk.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_rizik_visoki));
            holder.binding.llExposureItem.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_pink));
            holder.binding.exposureTitle.setTextColor(ContextCompat.getColor(context, R.color.red_color));

        } else {
            holder.binding.exposureTitle.setText(R.string.low_risk_item );
            holder.binding.ivExposeRisk.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_rizik));
            holder.binding.llExposureItem.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_yellow));
            holder.binding.exposureTitle.setTextColor(ContextCompat.getColor(context, R.color.dark_blue_color));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO ExposureInfoDialog.start(context,temporaryExposureKey);
            }
        });
    }

    @Override
    public int getItemCount() {
        return temporaryExposureKeys.size();
    }

    public void addTemporaryExposureKeys(ArrayList<TemporaryExposureKey> temporaryExposureKeys) {
        this.temporaryExposureKeys.addAll(temporaryExposureKeys);
        notifyDataSetChanged();
    }
}
