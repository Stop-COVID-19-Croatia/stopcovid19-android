package hr.miz.evidencijakontakata.Adapters.ExposureEntityAdapter.Holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import hr.miz.evidencijakontakata.databinding.ItemExposureEntityBinding;


public class ExposureEntityHolder extends RecyclerView.ViewHolder {
   public ItemExposureEntityBinding binding;

    public ExposureEntityHolder(@NonNull View itemView) {
        super(itemView);
        binding = ItemExposureEntityBinding.bind(itemView);

    }
}
